package com.example.borimandoo_back.service;

import com.example.borimandoo_back.domain.Farmer;
import com.example.borimandoo_back.domain.LicenseImage;
import com.example.borimandoo_back.domain.User;
import com.example.borimandoo_back.domain.Vet;
import com.example.borimandoo_back.dto.PostFarmerInfoRequest;
import com.example.borimandoo_back.dto.PostRoleRequest;
import com.example.borimandoo_back.dto.PostRoleResponse;
import com.example.borimandoo_back.dto.PostVetInfoRequest;
import com.example.borimandoo_back.repository.FarmerRepository;
import com.example.borimandoo_back.repository.UserRepository;
import com.example.borimandoo_back.repository.VetRepository;
import com.example.borimandoo_back.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final VetRepository vetRepository;
    private final FarmerRepository farmerRepository;

    public PostRoleResponse setRole(PostRoleRequest request, String token) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        PostRoleResponse response = null;
        if (request.getRole() == User.Role.VET) {
            user.setRole(User.Role.VET);
            Vet vet = new Vet(
                    null,
                    user,
                    null,
                    null,
                    null
            );
            vetRepository.save(vet);
            String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), "VET", 3600000);
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), "VET", 1209600000);
            response = new PostRoleResponse(accessToken, refreshToken);
        }
        else {
            user.setRole(User.Role.FARMER);
            Farmer farmer = new Farmer(
                    null,
                    user,
                    null
            );
            farmerRepository.save(farmer);
            String accessToken = jwtTokenProvider.createAccessToken(user.getUserId(), "FARMER", 3600000);
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId(), "FARMER", 1209600000);
            response = new PostRoleResponse(accessToken, refreshToken);
        }
        userRepository.save(user);
        return response;
    }

    public void setVetInfo(PostVetInfoRequest request, String token, LicenseImage licenseImageUrl) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Vet vet = vetRepository.findByUser(user);
        licenseImageUrl.setVet(vet);
        vet.setLicenseNumber(request.getLicenseNumber());
        vet.setLicenseImage(licenseImageUrl);
        vet.setVetArea(request.getVetArea());
        vetRepository.save(vet);
    }

    public void setFarmerInfo(PostFarmerInfoRequest request, String token) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        Farmer farmer = farmerRepository.findByUser(user);
        farmer.setFarmLocation(request.getFarmLocation());
        farmerRepository.save(farmer);
    }
}
