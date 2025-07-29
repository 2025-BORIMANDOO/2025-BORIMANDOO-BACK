package com.example.borimandoo_back.service;

import com.example.borimandoo_back.domain.*;
import com.example.borimandoo_back.dto.*;
import com.example.borimandoo_back.repository.*;
import com.example.borimandoo_back.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VetService {
    private final UserRepository userRepository;
    private final VetRepository vetRepository;
    private final RequestRepository requestRepository;
    private final RequestImageRepository requestImageRepository;
    private final EstimateRepository estimateRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final AnimalRepository animalRepository;
    private final TreatmentRecordRepository treatmentRecordRepository;
    private final MedicationRecordRepository medicationRecordRepository;

    public ArrayList<GetVetRequestResponses> getUrgentRequests() {
        ArrayList<Request> requests = requestRepository.findAllByUrgencyTrueAndRequestStatus(Request.RequestStatus.WAITING);
        ArrayList<GetVetRequestResponses> responses = new ArrayList<>();
        for (Request request : requests) {
            responses.add(new GetVetRequestResponses(
                    request.getId(),
                    request.getCreatedAt(),
                    request.getFarmLocation(),
                    request.getAnimalType()
            ));
        }
        return responses;
    }

    public ArrayList<GetVetRequestResponses> getGeneralRequests() {
        ArrayList<Request> requests = requestRepository.findAllByUrgencyFalseAndRequestStatus(Request.RequestStatus.WAITING);
        ArrayList<GetVetRequestResponses> responses = new ArrayList<>();
        for (Request request : requests) {
            responses.add(new GetVetRequestResponses(
                    request.getId(),
                    request.getReservedAt(),
                    request.getFarmLocation(),
                    request.getAnimalType()
            ));
        }
        return responses;
    }

    public GetVetRequestResponse getRequest(Long requestId) {
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) return null;

        return new GetVetRequestResponse(
                (request.getUrgency())? request.getCreatedAt() : request.getReservedAt(),
                request.getFarmLocation(),
                request.getUrgency(),
                request.getAnimalType(),
                request.getSymptomText(),
                request.getRequestImage().getImageUrl()
        );
    }

    public Estimate estimate(Long requestId, String token, PostVetEstimateRequest frontRequest) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Vet vet = vetRepository.findByUser(user);
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null) return null;

        Estimate estimate = new Estimate(
                null,
                frontRequest.getPrice(),
                frontRequest.getAction(),
                request,
                vet
        );
        Estimate saved = estimateRepository.save(estimate);
        return saved;
    }

    public void writeTreatment(PostTreatmentRequest request, String token) {
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        Vet vet = vetRepository.findByUser_UserId(userId);
        Animal animal = animalRepository.findById(request.getAnimalId())
                .orElseThrow(() -> new IllegalArgumentException("가축 정보를 찾을 수 없습니다."));

        TreatmentRecord treatment = treatmentRecordRepository.save(
                TreatmentRecord.builder()
                        .animal(animal)
                        .vet(vet)
                        .treatmentDate(request.getTreatmentDate())
                        .diagnosis(request.getDiagnosis())
                        .notes(request.getNotes())
                        .build()
        );

        for (MedicationDto dto : request.getMedications()) {
            MedicationRecord medication = MedicationRecord.builder()
                    .treatmentRecord(treatment)
                    .medicineName(dto.getMedicineName())
                    .dose(dto.getDose())
                    .route(dto.getRoute())
                    .administrationDate(dto.getAdministrationDate())
                    .build();
            medicationRecordRepository.save(medication);
        }
    }

    public List<TreatmentResponse> viewMyTreatments(String token) {
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        Vet vet = vetRepository.findByUser_UserId(userId);
        return treatmentRecordRepository.findAllByVet(vet).stream()
                .map(TreatmentResponse::from)
                .collect(Collectors.toList());
    }

    public List<TreatmentResponse> viewAnimalTreatments(Long animalId, String token) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new IllegalArgumentException("가축 정보를 찾을 수 없습니다."));

        return treatmentRecordRepository.findAllByAnimal(animal).stream()
                .map(TreatmentResponse::from)
                .collect(Collectors.toList());
    }

    public TreatmentResponse viewMyTreatment(Long treatmentId, String token) {
        UUID userId = jwtTokenProvider.getUserIdFromToken(token);
        Vet vet = vetRepository.findByUser_UserId(userId);

        TreatmentRecord treatment = treatmentRecordRepository.findById(treatmentId)
                .filter(t -> t.getVet().equals(vet))
                .orElseThrow(() -> new IllegalArgumentException("해당 진료 기록에 접근할 수 없습니다."));

        return TreatmentResponse.from(treatment);
    }
}
