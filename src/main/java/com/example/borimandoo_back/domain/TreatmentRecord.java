package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "treatment_record")
public class TreatmentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "treatment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Column(name = "treatment_date", nullable = false)
    private String treatmentDate;

    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "treatmentRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationRecord> medicationRecords = new ArrayList<>();
}
