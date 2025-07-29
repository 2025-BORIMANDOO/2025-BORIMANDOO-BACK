package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "medication_record")
public class MedicationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "medication_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "treatment_id", nullable = false)
    private TreatmentRecord treatmentRecord;

    @Column(name = "medicine_name", nullable = false)
    private String medicineName;

    @Column(name = "dose")
    private String dose;

    @Enumerated(EnumType.STRING)
    @Column(name = "route")
    private Route route;

    @Column(name = "administration_date", nullable = false)
    private String administrationDate;

    public enum Route {
        ORAL, INJECTION, TOPICAL
    }
}
