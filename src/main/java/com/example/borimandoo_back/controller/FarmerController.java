package com.example.borimandoo_back.controller;

import com.example.borimandoo_back.domain.Request;
import com.example.borimandoo_back.domain.RequestImage;
import com.example.borimandoo_back.dto.*;
import com.example.borimandoo_back.global.ApiResponse;
import com.example.borimandoo_back.service.FarmerService;
import com.example.borimandoo_back.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/farmer")
public class FarmerController {
    private final FarmerService farmerService;
    private final S3Service s3Service;

    @PostMapping(value = "/request", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> request(@RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestPart("data") PostFarmerRequest request,
                                                  @RequestPart("file")MultipartFile requestImage) {
        String token = authorizationHeader.replace("Bearer ", "");
        RequestImage requestImageUrl = s3Service.uploadRequestImageFile(requestImage);
        PostFarmerResponse response = farmerService.request(request, token, requestImageUrl);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.created(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "진료 요청에 실패했습니다."));
    }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse<?>> getRequests(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        ArrayList<GetFarmerRequestResponses> responses = farmerService.getRequests(token);
        return (responses != null)?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "진료 요청 기록 조회에 실패했습니다."));
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<ApiResponse<?>> getRequest(@RequestHeader("Authorization") String authorizationHeader,
                                                     @PathVariable("requestId") Long requestId) {
        String token = authorizationHeader.replace("Bearer ", "");
        GetFarmerRequestResponse response = farmerService.getRequest(token, requestId);
        return (response != null)?
                ResponseEntity.ok(ApiResponse.success(response)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "해당 진료 요청 조회에 실패했습니다."));
    }

    @GetMapping("/requests/{requestId}/estimates")
    public ResponseEntity<ApiResponse<?>> getEstimates(@RequestHeader("Authorization") String authorizationHeader,
                                                       @PathVariable("requestId") Long requestId) {
        String token = authorizationHeader.replace("Bearer ", "");
        ArrayList<GetFarmerEstimateResponses> responses = farmerService.getEstimates(token, requestId);
        return (responses != null)?
                ResponseEntity.ok(ApiResponse.success(responses)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "해당 진료 요청 조회에 실패했습니다."));
    }

    @PostMapping("/requests/{requestId}/estimates/{estimateId}")
    public ResponseEntity<ApiResponse<?>> chooseEstimate(@RequestHeader("Authorization") String authorizationHeader,
                                                         @PathVariable("requestId") Long requestId,
                                                         @PathVariable("estimateId") Long estimateId) {
        String token = authorizationHeader.replace("Bearer ", "");
        Request changedRequest = farmerService.chooseEstimate(token, requestId, estimateId);
        return (changedRequest != null)?
                ResponseEntity.ok(ApiResponse.success(null)):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(500, "견적서 선택에 실패했습니다."));
    }
}
