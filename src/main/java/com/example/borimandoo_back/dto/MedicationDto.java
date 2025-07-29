package com.example.borimandoo_back.dto;

import com.example.borimandoo_back.domain.MedicationRecord;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationDto {
    private String medicineName;
    private String dose;
    private MedicationRecord.Route route;
    private String administrationDate;
}
