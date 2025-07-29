package com.example.borimandoo_back.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostTreatmentRequest {
    private Long animalId;
    private String treatmentDate;
    private String diagnosis;
    private String notes;
    private List<MedicationDto> medications;
}
