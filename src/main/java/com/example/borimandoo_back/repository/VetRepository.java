package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.User;
import com.example.borimandoo_back.domain.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VetRepository extends JpaRepository<Vet, Long> {
    Vet findByUser(User user);

    Vet findByUser_UserId(UUID userId);
}
