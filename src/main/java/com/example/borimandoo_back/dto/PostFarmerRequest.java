package com.example.borimandoo_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFarmerRequest {
    private String animalType;
    private Boolean urgency;
    private LocalDateTime reservedAt;
    private String symptomText;
}
