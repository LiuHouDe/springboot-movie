package com.ispan.theater.repository;

import com.ispan.theater.domain.Rated;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatedRepository extends JpaRepository<Rated, Integer> {
    @Query("select c from Rated c where c.code = :code")
    public Rated findByCode(String code);


}