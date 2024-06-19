package com.ispan.theater.repository;

import com.ispan.theater.domain.AuditoriumLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriumLevelRepository extends JpaRepository<AuditoriumLevel, Integer> {
}