package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "request_image")
public class RequestImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "license_image_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "request_id", nullable = false)
    private Request request;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;
}
