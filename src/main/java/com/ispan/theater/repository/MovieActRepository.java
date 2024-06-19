package com.ispan.theater.repository;

import com.ispan.theater.domain.MovieAct;
import com.ispan.theater.domain.MovieActId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieActRepository extends JpaRepository<MovieAct, MovieActId> {
}