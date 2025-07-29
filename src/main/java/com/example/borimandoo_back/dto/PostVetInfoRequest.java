package com.example.borimandoo_back.dto;

import com.example.borimandoo_back.domain.VetArea;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostVetInfoRequest {
    private String phoneNumber;
    private String licenseNumber;
    private VetArea vetArea;
}
