package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "animal_status")
public class AnimalStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(name = "is_pregnant")
    private Boolean isPregnant;

    @Column(name = "birth_count")
    private Integer birthCount;

    @Column(name = "last_birth_date")
    private String lastBirthDate;

    @Column(name = "last_estrus_date")
    private String lastEstrusDate;

    @Column(name = "estrus_cycle_days")
    private Integer estrusCycleDays;

    @Column(name = "is_castrated")
    private Boolean isCastrated;
}
