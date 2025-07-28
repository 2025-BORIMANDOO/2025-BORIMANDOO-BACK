package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "license_image")
public class LicenseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_image_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
