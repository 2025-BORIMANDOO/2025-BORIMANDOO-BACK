package com.example.borimandoo_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostFarmerRequest {
    private String animalType;
    private Boolean urgency;
    private String symptomText;
}
