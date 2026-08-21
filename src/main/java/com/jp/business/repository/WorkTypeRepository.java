package com.jp.business.repository;

import com.jp.business.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkTypeRepository
        extends JpaRepository<WorkType, Long> {

    Optional<WorkType> findByName(String name);

    List<WorkType> findByActiveTrue();

    List<WorkType> findByActiveFalse();

    boolean existsByName(String name);
}