package com.example.borimandoo_back.service;

import com.example.borimandoo_back.domain.Farmer;
import com.example.borimandoo_back.domain.Request;
import com.example.borimandoo_back.domain.RequestImage;
import com.example.borimandoo_back.domain.User;
import com.example.borimandoo_back.dto.GetFarmerRequestResponse;
import com.example.borimandoo_back.dto.GetFarmerRequestResponses;
import com.example.borimandoo_back.dto.PostFarmerRequest;
import com.example.borimandoo_back.dto.PostFarmerResponse;
import com.example.borimandoo_back.repository.FarmerRepository;
import com.example.borimandoo_back.repository.RequestImageRepository;
import com.example.borimandoo_back.repository.RequestRepository;
import com.example.borimandoo_back.repository.UserRepository;
import com.example.borimandoo_back.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class FarmerService {
    private final UserRepository userRepository;
    private final FarmerRepository farmerRepository;
    private final RequestRepository requestRepository;
    private final RequestImageRepository requestImageRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public PostFarmerResponse request(PostFarmerRequest frontRequest, String token, RequestImage requestImage) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Farmer farmer = farmerRepository.findByUser(user);

        // 1. 먼저 Request 생성 (이미지 없이)
        Request request = new Request(
                null,
                LocalDateTime.now(),
                (frontRequest.getUrgency())? null : frontRequest.getReservedAt(),
                Request.RequestStatus.WAITING,
                null,
                farmer,
                frontRequest.getAnimalType(),
                frontRequest.getUrgency(),
                frontRequest.getSymptomText(),
                null, // 일단 이미지 없이
                farmer.getFarmLocation()
        );
        Request saved = requestRepository.save(request);

        // 2. 이미지에 request 연결
        requestImage.setRequest(saved);
        requestImageRepository.save(requestImage);

        // 3. 다시 연결 (양방향 유지하고 싶다면)
        saved.setRequestImage(requestImage);

        return new PostFarmerResponse(saved.getId());
    }

    public ArrayList<GetFarmerRequestResponses> getRequests(String token) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Farmer farmer = farmerRepository.findByUser(user);
        ArrayList<Request> requests = requestRepository.findAllByFarmer(farmer);
        ArrayList<GetFarmerRequestResponses> responses = new ArrayList<>();

        for (Request request : requests) {
            responses.add(new GetFarmerRequestResponses(
                    request.getId(),
                    request.getCreatedAt(),
                    request.getRequestStatus(),
                    request.getVet()
            ));
        }
        return responses;
    }

    public GetFarmerRequestResponse getRequest(String token, Long requestId) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Farmer farmer = farmerRepository.findByUser(user);
        Request request = requestRepository.findById(requestId).orElse(null);
        if (request == null || request.getFarmer() != farmer)
            return null;

        GetFarmerRequestResponse response = new GetFarmerRequestResponse(
                request.getUrgency(),
                request.getCreatedAt(),
                request.getRequestStatus(),
                request.getVet(),
                request.getAnimalType(),
                request.getSymptomText(),
                request.getRequestImage().getImageUrl()
        );
        return response;
    }
}
