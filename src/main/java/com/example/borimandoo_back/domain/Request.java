package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status", nullable = false)
    private RequestStatus requestStatus;

    @ManyToOne
    @JoinColumn(name = "vet_id")
    private Vet vet; // 담당 수의사 (nullable 허용 가능)

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer; // 요청자

    @Column(name = "animal_type", nullable = false)
    private String animalType; // 소/말/돼지/기타

    @Column(name = "urgency", nullable = false)
    private Boolean urgency;

    @Column(name = "symptom_text", columnDefinition = "TEXT")
    private String symptomText;

    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private RequestImage requestImage;

    @Column(name = "farm_location", nullable = false)
    private String farmLocation;

    public enum RequestStatus {
        WAITING, ACCEPTED, DONE
    }
}
