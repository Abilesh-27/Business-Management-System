package com.jp.business.repository;

import com.jp.business.entity.LabourWorkEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LabourWorkEntryRepository
        extends JpaRepository<LabourWorkEntry, Long> {
    List<LabourWorkEntry> findByWorkDateOrderByWorkDateAsc(
        LocalDate workDate
);
    List<LabourWorkEntry> findByLabourIdOrderByWorkDateDesc(
            Long labourId
    );

    List<LabourWorkEntry> findByWorkDateOrderByLabourId(
            LocalDate workDate
    );

    List<LabourWorkEntry>
    findByLabourIdAndWorkDateBetweenOrderByWorkDateAsc(
            Long labourId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<LabourWorkEntry>
    findBySiteIdAndWorkDateBetweenOrderByWorkDateAsc(
            Long siteId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<LabourWorkEntry>
    findByWorkTypeIdAndWorkDateBetweenOrderByWorkDateAsc(
            Long workTypeId,
            LocalDate startDate,
            LocalDate endDate
    );
}