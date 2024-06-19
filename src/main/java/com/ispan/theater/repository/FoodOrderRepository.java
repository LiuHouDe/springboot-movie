package com.ispan.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.FoodOrder;
@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Integer> {

}
