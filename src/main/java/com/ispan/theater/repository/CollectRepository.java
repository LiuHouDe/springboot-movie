package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ispan.theater.domain.Collect;
import com.ispan.theater.domain.CollectId;

public interface CollectRepository extends JpaRepository<Collect, CollectId>{
	
	@Query("from Collect c where c.collectId.userId= :id")
	List<Collect> findByUserId(@Param("id") Integer users);
	
	
	@Query("select c from Collect c where c.collectId.userId = :userId and c.collectId.movieId = :movieId")
	public Collect findByUsersAndPhoto(@Param("userId") Integer userId, @Param("movieId") Integer movieId);
	
}
