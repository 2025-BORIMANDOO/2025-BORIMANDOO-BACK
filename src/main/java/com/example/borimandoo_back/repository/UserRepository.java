package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderId(String providerId);

    User findByUserId(UUID userId);
}
