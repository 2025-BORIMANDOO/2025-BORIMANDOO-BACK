package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "animal")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "animal_id")
    private Long id;

    @Column(name = "ear_tag", nullable = false, unique = true)
    private String earTag;  // 귀표번호

    @Enumerated(EnumType.STRING)
    @Column(name = "species", nullable = false)
    private Species species;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "birth_date", nullable = false)
    private String birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    public enum Species {
        HANWOO, DAIRY_COW, BEEF_COW, PIG, CHICKEN, OTHER
    }

    public enum Gender {
        FEMALE, MALE, CASTRATED
    }
}
