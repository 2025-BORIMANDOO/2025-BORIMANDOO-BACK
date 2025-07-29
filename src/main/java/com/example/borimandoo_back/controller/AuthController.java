package com.example.borimandoo_back.controller;

import com.example.borimandoo_back.domain.LicenseImage;
import com.example.borimandoo_back.domain.User;
import com.example.borimandoo_back.dto.PostFarmerInfoRequest;
import com.example.borimandoo_back.dto.PostRoleRequest;
import com.example.borimandoo_back.dto.PostRoleResponse;
import com.example.borimandoo_back.dto.PostVetInfoRequest;
import com.example.borimandoo_back.global.ApiResponse;
import com.example.borimandoo_back.security.jwt.JwtTokenProvider;
import com.example.borimandoo_back.service.AuthService;
import com.example.borimandoo_back.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final S3Service s3Service;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/role")
    public ResponseEntity<ApiResponse<?>> setRole(@RequestHeader("Authorization") String authorizationHeader,
                                                  @RequestBody PostRoleRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");
        PostRoleResponse response = authService.setRole(request, token);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping(value = "/vet-info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> setVetInfo(@RequestHeader("Authorization") String authorizationHeader,
                                                     @RequestPart("data") PostVetInfoRequest request,
                                                     @RequestPart("file") MultipartFile licenseImage) {
        String token = authorizationHeader.replace("Bearer ", "");
        LicenseImage licenseImageUrl = s3Service.uploadFile(licenseImage);
        authService.setVetInfo(request, token, licenseImageUrl);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/farmer-info")
    public ResponseEntity<ApiResponse<?>> setFarmerInfo(@RequestHeader("Authorization") String authorizationHeader,
                                                        @RequestBody PostFarmerInfoRequest request) {
        String token = authorizationHeader.replace("Bearer ", "");
        authService.setFarmerInfo(request, token);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
