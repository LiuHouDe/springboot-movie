package com.ispan.theater.repository;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
    @Query(value="select a.auditorium_id, a.auditorium_number from auditorium as a where a.cinema_id = :cinemaId ",nativeQuery=true)
    List<Map<String,Object>> findByCinema(@Param(value="cinemaId")Integer cinemaId);

}