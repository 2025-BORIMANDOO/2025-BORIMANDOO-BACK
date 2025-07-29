package com.example.borimandoo_back.dto;

import com.example.borimandoo_back.domain.MedicationRecord;
import com.example.borimandoo_back.domain.TreatmentRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreatmentResponse {

    private Long treatmentId;
    private Long animalId;
    private String treatmentDate;
    private String diagnosis;
    private String notes;
    private List<MedicationResponse> medications;

    public static TreatmentResponse from(TreatmentRecord entity) {
        return TreatmentResponse.builder()
                .treatmentId(entity.getId())
                .animalId(entity.getAnimal().getId())
                .treatmentDate(entity.getTreatmentDate())
                .diagnosis(entity.getDiagnosis())
                .notes(entity.getNotes())
                .medications(entity.getMedicationRecords() != null
                        ? entity.getMedicationRecords().stream()
                        .map(MedicationResponse::from)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MedicationResponse {
        private String medicineName;
        private String dose;
        private String route;
        private String administrationDate;

        public static MedicationResponse from(MedicationRecord record) {
            return MedicationResponse.builder()
                    .medicineName(record.getMedicineName())
                    .dose(record.getDose())
                    .route(record.getRoute().name())
                    .administrationDate(record.getAdministrationDate())
                    .build();
        }
    }
}
