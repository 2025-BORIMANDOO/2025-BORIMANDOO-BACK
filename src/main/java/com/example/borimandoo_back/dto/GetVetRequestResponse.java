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
public class GetVetRequestResponse {
    private LocalDateTime time;
    private String farmLocation;
    private Boolean urgency;
    private String animalType;
    private String symptomText;
    private String requestImageUrl;
}
