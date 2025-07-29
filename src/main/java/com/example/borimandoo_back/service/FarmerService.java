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

    public PostFarmerResponse request(PostFarmerRequest frontRequest, String token, RequestImage requestImageUrl) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Farmer farmer = farmerRepository.findByUser(user);
        Request request = new Request(
                null,
                LocalDateTime.now(),
                Request.RequestStatus.WAITING,
                null,
                farmer,
                frontRequest.getAnimalType(),
                frontRequest.getUrgency(),
                frontRequest.getSymptomText(),
                requestImageUrl,
                farmer.getFarmLocation()
        );
        Request saved = requestRepository.save(request);
        requestImageUrl.setRequest(request);
        requestImageRepository.save(requestImageUrl);

        return new PostFarmerResponse(
                saved.getId()
        );
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
