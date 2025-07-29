package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.Farmer;
import com.example.borimandoo_back.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    ArrayList<Request> findAllByFarmer(Farmer farmer);
}
