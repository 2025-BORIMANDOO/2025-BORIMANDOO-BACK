package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "vet")
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vet_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "license_number")
    private String licenseNumber;

    @OneToOne(mappedBy = "vet", cascade = CascadeType.ALL)
    private LicenseImage licenseImage;

    @Column(name = "vet_area")
    private VetArea vetArea; // 예: 02, 031 등 지역 코드
}
