package com.example.borimandoo_back.service;

import com.example.borimandoo_back.domain.Farmer;
import com.example.borimandoo_back.domain.LicenseImage;
import com.example.borimandoo_back.domain.User;
import com.example.borimandoo_back.domain.Vet;
import com.example.borimandoo_back.dto.PostFarmerInfoRequest;
import com.example.borimandoo_back.dto.PostRoleRequest;
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

    public void setRole(PostRoleRequest request, String token) {
        User user = userRepository.findByUserId(jwtTokenProvider.getUserIdFromToken(token));
        if (request.getRole() == User.Role.VET) {
            user.setRole(User.Role.VET);
            Vet vet = new Vet(
                    null,
                    user,
                    null,
                    null,
                    null
            );
        }
        else {
            user.setRole(User.Role.FARMER);
            Farmer farmer = new Farmer(
                    null,
                    user,
                    null
            );
        }
        userRepository.save(user);
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
