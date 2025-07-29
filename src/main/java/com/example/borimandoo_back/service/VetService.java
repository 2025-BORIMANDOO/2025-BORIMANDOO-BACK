package com.example.borimandoo_back.service;

import com.example.borimandoo_back.domain.Request;
import com.example.borimandoo_back.dto.GetVetRequestResponses;
import com.example.borimandoo_back.repository.RequestImageRepository;
import com.example.borimandoo_back.repository.RequestRepository;
import com.example.borimandoo_back.repository.UserRepository;
import com.example.borimandoo_back.repository.VetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class VetService {
    private final UserRepository userRepository;
    private final VetRepository vetRepository;
    private final RequestRepository requestRepository;
    private final RequestImageRepository requestImageRepository;

    public ArrayList<GetVetRequestResponses> getUrgentRequests() {
        ArrayList<Request> requests = requestRepository.findAllByUrgencyTrue();
        ArrayList<GetVetRequestResponses> responses = new ArrayList<>();
        for (Request request : requests) {
            responses.add(new GetVetRequestResponses(
                    request.getCreatedAt(),
                    request.getFarmLocation(),
                    request.getAnimalType()
            ));
        }
        return responses;
    }

    public ArrayList<GetVetRequestResponses> getGeneralRequests() {
        ArrayList<Request> requests = requestRepository.findAllByUrgencyFalse();
        ArrayList<GetVetRequestResponses> responses = new ArrayList<>();
        for (Request request : requests) {
            responses.add(new GetVetRequestResponses(
                    request.getReservedAt(),
                    request.getFarmLocation(),
                    request.getAnimalType()
            ));
        }
        return responses;
    }
}
