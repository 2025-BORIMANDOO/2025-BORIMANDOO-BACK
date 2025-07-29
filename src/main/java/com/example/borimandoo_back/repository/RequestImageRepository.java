package com.example.borimandoo_back.repository;

import com.example.borimandoo_back.domain.RequestImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestImageRepository extends JpaRepository<RequestImage, Long> {
}
