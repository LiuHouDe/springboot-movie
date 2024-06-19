package com.ispan.theater.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Food;
import com.ispan.theater.repository.FoodRepository;
import com.ispan.theater.util.DatetimeConverter;

@Service
@Transactional
public class FoodService {
	@Autowired
	private FoodRepository foodRepository;
	
	// controller existsByName
	public boolean existByName(String name) {
		if (name!=null && name.length()!=0) {
			long result = foodRepository.countByName(name);
			if (result!=0) {
				return true;
			}
		}
		return false;
	}
	// controller find
	public long count(String json) {
		try {
			JSONObject responseJson = new JSONObject(json);
			return foodRepository.count(responseJson);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return 0;
	}
	// controller modifyFood
	public boolean existFoodById(Integer id) {
		if (id!=null) {
			return foodRepository.existsById(id);
		}
		return false;
	}
	
	//controller createFood
	public boolean existsFoodByName(String name) {
		Food food = foodRepository.findByName(name);
		return food != null;
	}
	
	// controller createFood
	public Food createFood(JSONObject obj) {
		String name = obj.isNull("name") ? null : obj.getString("name");
		Double price = obj.isNull("price") ? null : obj.getDouble("price");
		Integer count = obj.isNull("count") ? null : obj.getInt("count");
		String c_date = obj.isNull("createDate") ? null : obj.getString("createDate");
		String m_date = obj.isNull("modifyDate") ? null : obj.getString("modifyDate");
		Food food = new Food();
		if (name != null && !name.isEmpty()) {
			food.setName(name);

        }
		if (price != null && price.doubleValue() > 0) {
			food.setPrice(price);

        }
		if (count != null && count > 0) {
			food.setCount(count);

        }
		food.setCreateDate(new Date());
		food.setModifyDate(new Date());
		System.out.println("food:" + food.toString());
		return foodRepository.save(food);
	}
//	public Food createFood(String json) {
//		try {
//			JSONObject obj = new JSONObject(json);
//			Integer id = obj.isNull("id") ? null : obj.getInt("id");
//			String name = obj.isNull("name") ? null : obj.getString("name");
//			Double price = obj.isNull("price") ? null : obj.getDouble("price");
//			Integer count = obj.isNull("count") ? null : obj.getInt("count");
//			String c_date = obj.isNull("createDate") ? null : obj.getString("createDate");
//			String m_date = obj.isNull("modifyDate") ? null : obj.getString("modifyDate");
//
//			if(id!=null) {
//				Optional<Food> optional = foodRepository.findById(id);
//				if(optional.isEmpty()) {
//					Food insert = new Food();
//					insert.setId(id);
//					insert.setName(name);
//					insert.setPrice(price);
//					insert.setCount(count);					
//					if(c_date!=null && c_date.length()!=0) {
//						java.util.Date temp = DatetimeConverter.parse(c_date, "yyyy-MM-dd");
//						insert.setCreateDate(temp);
//					} else {
//						insert.setCreateDate(null);
//					}
//					
//					if(m_date!=null && m_date.length()!=0) {
//						java.util.Date temp = DatetimeConverter.parse(m_date, "yyyy-MM-dd");
//						insert.setModifyDate(temp);
//					} else {
//						insert.setModifyDate(null);
//					}
//
//					return foodRepository.save(insert);
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	// controller modifyFood
	public Food modifyFood(String json) {
		
		try {
			JSONObject obj = new JSONObject(json);
			Integer id = obj.isNull("id") ? null : obj.getInt("id");
			String name = obj.isNull("name") ? null : obj.getString("name");
			Double price = obj.isNull("price") ? null : obj.getDouble("price") ;
			Integer count = obj.isNull("count")? null : obj.getInt("count");
			String c_date = obj.isNull("createDate") ? null : obj.getString("createDate");
			String m_date = obj.isNull("modifyDate") ? null : obj.getString("modifyDate");
			
			if (id!=null) {
				Optional<Food> optional = foodRepository.findById(id);
				if (optional.isPresent()) {
					Food update = optional.get();
					update.setId(id);
					update.setName(name);
					update.setPrice(price);
					update.setCount(count);			
					if(c_date!=null && c_date.length()!=0) {
						java.util.Date temp = DatetimeConverter.parse(c_date, "yyyy-MM-dd");
						update.setCreateDate(temp);
					} else {
						update.setCreateDate(null);
					}
					
					if(m_date!=null && m_date.length()!=0) {
						java.util.Date temp = DatetimeConverter.parse(m_date, "yyyy-MM-dd");
						update.setModifyDate(temp);
					} else {
						update.setModifyDate(null);
					}

					return foodRepository.save(update);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	//	controller findByFoodId & deleteFood
	public Food findByFoodId(Integer id) {
		if (id!=null) {
			Optional<Food> optional = foodRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	//controller find
	public List<Food> find(String json){
		try {
			JSONObject obj = new JSONObject(json);
			 return foodRepository.find(obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// controller deleteFood
	public boolean deleteByFoodId(Integer id) {
		if(id!=null) {
			Optional<Food> optional = foodRepository.findById(id);
			if(optional.isPresent()) {
				foodRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	
	public Food insertFood(Food food) {
		if (food != null && food.getId() != null ) {
			Optional<Food> optional = foodRepository.findById(food.getId());
			if (optional.isEmpty()) {
				return foodRepository.save(food);
			}
		}
		return null;
	}
	
	public Food jsonToFood(JSONObject jsonObject) {
		Food food = null;
		if (foodRepository.findByName(jsonObject.getString("name")) == null) {
			food = new Food();
			food.setName(jsonObject.getString("name"));
			food.setPrice(jsonObject.getDouble("price"));
			food.setCount(jsonObject.getInt("count"));
			food.setCreateDate(new Date());
			food.setModifyDate(new Date());
			
			foodRepository.save(food);
						
			return food;
		}else {
			return null;
		}
		
	}
	
	

}
