package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Comment;
import com.ispan.theater.domain.Movie;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	
	Page<Comment> findAllByMovieIdAndPidIsNull(Movie movieId, Pageable pageable);
	List<Comment> findAllByMovieId(Movie movieId);
    

	    @Query(value = "SELECT c.*, u.user_id AS userId, u.email, u.user_photo AS userPhoto, u.user_lastname AS userLastname, " +
	                   "m.movie_id AS movieId, m.category_code AS categoryCode, m.description, m.director, m.image, " +
	                   "m.modify_date AS modifyDate, m.name AS movieName, m.name_eng AS movieNameEng, m.rated_code AS ratedCode " +
	                   "FROM comment c " +
	                   "JOIN \"user\" u ON c.user_id = u.user_id " +
	                   "JOIN movie m ON c.movie_id = m.movie_id " +
	                   "WHERE c.movie_id = :movieId " +
	                   "ORDER BY c.rate DESC", nativeQuery = true)
	    List<Comment> findCommentsByMovieIdByRate(@Param("movieId") Integer movieId);
	    

	    @Query(value = "SELECT c.*, u.user_id AS userId, u.email, u.user_photo AS userPhoto, u.user_lastname AS userLastname, " +
	                   "m.movie_id AS movieId, m.category_code AS categoryCode, m.description, m.director, m.image, " +
	                   "m.modify_date AS modifyDate, m.name AS movieName, m.name_eng AS movieNameEng, m.rated_code AS ratedCode " +
	                   "FROM comment c " +
	                   "JOIN \"user\" u ON c.user_id = u.user_id " +
	                   "JOIN movie m ON c.movie_id = m.movie_id " +
	                   "WHERE c.movie_id = :movieId " +
	                   "ORDER BY c.createtime DESC", nativeQuery = true)
	    List<Comment> findCommentsByMovieIdByTime(@Param("movieId") Integer movieId);
	    
	    
	    @Query("SELECT c FROM Comment c WHERE c.content LIKE %:keyword% ")
	    Page<Comment> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
	    
	    @Query("SELECT c FROM Comment c WHERE c.userId.email LIKE %:keyword% ")
	    Page<Comment> searchByName(@Param("keyword") String keyword, Pageable pageable);
	    @Query("SELECT c FROM Comment c WHERE c.movieId.name LIKE %:keyword% ")
	    Page<Comment> searchByMovie(@Param("keyword") String keyword, Pageable pageable);
	    
	}
//    @Query(value = "SELECT c.*, u.userPhoto, u.userLastname, u.userId, m.movieId, m.categoryCode, m.description, m.director, m.image, m.modifyDate, m.name, m.nameEng FROM [User] u JOIN Comment c ON c.userId = u.userId JOIN Movie m ON m.movieId = c.movieId WHERE c.movieId = 2 ORDER BY c.rate DESC", nativeQuery = true)
//    List<Comment> findCommentsByMovieIdByRate(@Param("movieId") Integer movieId);
    

	
	



