package com.jp.business.repository;

import com.jp.business.entity.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteRepository
        extends JpaRepository<Site, Long> {

    Optional<Site> findBySiteName(String siteName);

    List<Site> findByActiveTrue();

    List<Site> findByActiveFalse();

    boolean existsBySiteName(String siteName);
}