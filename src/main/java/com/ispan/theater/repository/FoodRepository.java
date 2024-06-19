package com.ispan.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ispan.theater.dao.FoodDao;
import com.ispan.theater.domain.Food;
@Repository
public interface FoodRepository extends JpaRepository<Food, Integer>,FoodDao {
	
	@Query("select f from Food f where f.name = :name")
	public Food findByName(@Param("name") String name);	
	
	@Query("select count(*) from Food where name = :name")
	public long countByName(String name);
		
	
	
}
