package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.MedicationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRecordRepository extends JpaRepository<MedicationRecord, Long> {
}
