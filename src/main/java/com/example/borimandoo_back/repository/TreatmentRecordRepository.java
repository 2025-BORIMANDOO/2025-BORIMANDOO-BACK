package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.Animal;
import com.example.borimandoo_back.domain.TreatmentRecord;
import com.example.borimandoo_back.domain.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;

@Repository
public interface TreatmentRecordRepository extends JpaRepository<TreatmentRecord, Long> {
    ArrayList<TreatmentRecord> findAllByVet(Vet vet);

    ArrayList<TreatmentRecord> findAllByAnimal(Animal animal);
}
