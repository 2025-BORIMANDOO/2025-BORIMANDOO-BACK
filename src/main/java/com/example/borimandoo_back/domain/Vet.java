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

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "license_image_url")
    private String licenseImageUrl;

    @Column(name = "vet_area", nullable = false)
    private Integer vetArea; // 예: 02, 031 등 지역 코드
}
