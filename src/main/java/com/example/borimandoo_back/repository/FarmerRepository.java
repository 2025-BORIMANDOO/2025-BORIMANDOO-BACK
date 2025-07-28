package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.Farmer;
import com.example.borimandoo_back.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, Long> {
    Farmer findByUser(User user);
}
