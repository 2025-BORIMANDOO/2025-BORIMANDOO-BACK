package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderId(String providerId);
}
