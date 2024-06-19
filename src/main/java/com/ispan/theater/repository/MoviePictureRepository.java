package com.ispan.theater.repository;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MoviePicture;
import com.ispan.theater.dto.MoviePicDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoviePictureRepository extends JpaRepository<MoviePicture, Integer> {
    @Query("select c from MoviePicture c where c.movie = :movie")
    List<MoviePicture> findByMovieId(@Param("movie") Movie movie);
}