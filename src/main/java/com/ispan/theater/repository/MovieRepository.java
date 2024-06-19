package com.ispan.theater.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.dao.MovieDao;
import com.ispan.theater.domain.Movie;
@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> , JpaSpecificationExecutor<Movie>,MovieDao {
    @Query("select c from Movie c where c.name = :name")
    public Movie findByName(@Param("name") String name);
    @Query("select c from Movie c where c.name like concat('%', :name ,'%')")
    public List<Movie> fineMovieByNameLike(@Param("name") String name);
    @Query("select count(*) from Movie c where c.name like concat('%', :name ,'%')")
    public long countByNameLike(String name);
    
    
    @Query(value="select distinct m.movie_id,m.name,a.cinema_id from movie as m join Screening as s on m.movie_id=s.Movie_id join auditorium as a on s.auditorium_id=a.auditorium_id where a.cinema_id= :cinemaId and substring(convert(nvarchar,s.Start_time),1,10)>=CONVERT(nvarchar,GETDATE(),23)",nativeQuery = true)
    public List<Map<String,Object>> findMovieByScreening(@Param(value="cinemaId")Integer cinemaId);
    	
}