package com.ispan.theater.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.FoodOrderDetail;
@Repository
public interface FoodOrderDetailRepository extends JpaRepository<FoodOrderDetail, Integer> {

}
