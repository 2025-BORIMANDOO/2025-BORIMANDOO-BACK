package com.example.borimandoo_back.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "farmer")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farmer_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "farm_location", nullable = false)
    private String farmLocation;
}
