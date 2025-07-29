package com.example.borimandoo_back.controller;

import com.example.borimandoo_back.domain.Estimate;
import com.example.borimandoo_back.dto.GetVetRequestResponse;
import com.example.borimandoo_back.dto.GetVetRequestResponses;
import com.example.borimandoo_back.dto.PostVetEstimateRequest;
import com.example.borimandoo_back.global.ApiResponse;
import com.example.borimandoo_back.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vet")
public class VetController {
    private final VetService vetService;

    @GetMapping("/requests/urgent")
    public ResponseEntity<ApiResponse<?>> getUrgentRequests() {
        ArrayList<GetVetRequestResponses> responses = vetService.getUrgentRequests();
        return (responses != null)?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "긴급 요청을 불러오는 데 실패했습니다."));
    }

    @GetMapping("/requests/general")
    public ResponseEntity<ApiResponse<?>> getGeneralRequests() {
        ArrayList<GetVetRequestResponses> responses = vetService.getGeneralRequests();
        return (responses != null)?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "일반 요청을 불러오는 데 실패했습니다."));
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<ApiResponse<?>> getRequest(@PathVariable("requestId") Long requestId) {
        GetVetRequestResponse response = vetService.getRequest(requestId);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "진료 요청을 불러오는 데 실패했습니다."));
    }

    @PostMapping("/requests/{requestId}/estimate")
    public ResponseEntity<ApiResponse<?>> estimate(@PathVariable("requestId") Long requestId,
                                                   @RequestBody PostVetEstimateRequest frontRequest) {
        Estimate estimate = vetService.estimate(requestId, frontRequest);
        return (estimate != null)?
                ResponseEntity.ok(ApiResponse.created(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "견적을 전송하는 데 실패했습니다."));
    }
}
