package com.ispan.theater.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Food;
import com.ispan.theater.domain.FoodPicture;
@Repository
public interface FoodPictureRepository extends JpaRepository<FoodPicture, Integer> {

	@Query("select fp from FoodPicture fp where fp.food = :food")
	public List<FoodPicture> findFoodPictureByFoodId(@Param("food") Food food);

	
	@Transactional
    @Modifying
    @Query("DELETE FROM FoodPicture fp WHERE fp.food = :food")
    void deleteByFoodId(@Param("food")Food food);
	
	boolean existsByFoodId(Integer foodId);
}
