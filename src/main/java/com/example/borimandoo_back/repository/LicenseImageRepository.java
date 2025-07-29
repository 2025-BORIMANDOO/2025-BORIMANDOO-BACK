package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.LicenseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseImageRepository extends JpaRepository<LicenseImage, Long> {
}
