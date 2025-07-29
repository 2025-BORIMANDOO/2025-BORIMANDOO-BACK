package com.example.borimandoo_back.controller;

import com.example.borimandoo_back.domain.Estimate;
import com.example.borimandoo_back.dto.GetVetRequestResponse;
import com.example.borimandoo_back.dto.GetVetRequestResponses;
import com.example.borimandoo_back.dto.PostTreatmentRequest;
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
                                                   @RequestHeader("Authorization") String authorizationHeader,
                                                   @RequestBody PostVetEstimateRequest frontRequest) {
        String token = authorizationHeader.replace("Bearer ", "");
        Estimate estimate = vetService.estimate(requestId, token, frontRequest);
        return (estimate != null)?
                ResponseEntity.ok(ApiResponse.created(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "견적을 전송하는 데 실패했습니다."));
    }

    @PostMapping("/treatment")
    public ResponseEntity<ApiResponse<?>> writeTreatment(@RequestBody PostTreatmentRequest request,
                                                         @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        vetService.writeTreatment(request, token);
        return ResponseEntity.ok(ApiResponse.success("진료 기록 작성 완료"));
    }

    @GetMapping("/treatments")
    public ResponseEntity<ApiResponse<?>> viewMyTreatments(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(ApiResponse.success(vetService.viewMyTreatments(token)));
    }

    @GetMapping("/animal/{animalId}/treatments")
    public ResponseEntity<ApiResponse<?>> viewCertainAnimalTreatment(@PathVariable("animalId") Long animalId,
                                                                     @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(ApiResponse.success(vetService.viewAnimalTreatments(animalId, token)));
    }

    @GetMapping("/treatment/{treatmentId}")
    public ResponseEntity<ApiResponse<?>> viewMyTreatment(@PathVariable("treatmentId") Long treatmentId,
                                                          @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(ApiResponse.success(vetService.viewMyTreatment(treatmentId, token)));
    }
}
