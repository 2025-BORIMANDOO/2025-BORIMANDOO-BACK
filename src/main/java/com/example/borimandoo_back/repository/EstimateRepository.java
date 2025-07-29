package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    ArrayList<Estimate> findAllByRequest();
}
