package com.ispan.theater.dao;

import java.util.List;

import org.json.JSONObject;

import com.ispan.theater.domain.Food;

public interface FoodDao {
	
	List<Food> find(JSONObject obj);
	
	long count(JSONObject obj);

}
