package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
