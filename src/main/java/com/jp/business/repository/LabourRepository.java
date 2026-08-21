package com.jp.business.repository;

import com.jp.business.entity.Labour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabourRepository
        extends JpaRepository<Labour, Long> {

    Optional<Labour> findByLabourNo(String labourNo);

    List<Labour> findByActiveTrue();

    List<Labour> findByActiveFalse();

    boolean existsByPhone(String phone);
}