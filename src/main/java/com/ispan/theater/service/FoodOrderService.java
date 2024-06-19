package com.ispan.theater.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.FoodOrder;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.FoodOrderRepository;
import com.ispan.theater.repository.UserRepository;
import com.ispan.theater.util.DatetimeConverter;

@Service
@Transactional
public class FoodOrderService {
	@Autowired
	private FoodOrderRepository foodOrderRepository;
	
	private UserRepository userRepository;
	
	// controller modifyFoodOrder
		public boolean existFoodOrderById(Integer id) {
			if (id!=null) {
				return foodOrderRepository.existsById(id);
			}
			return false;
		}
	
	public FoodOrder createFoodOrder(JSONObject obj) {
		
		try {
			Integer userId = obj.isNull("userId")? null : obj.getInt("userId");
			User user = userRepository.findById(userId).orElse(null);
			
			FoodOrder insertOrder = new FoodOrder();
			insertOrder.setUser(user);
			insertOrder.setCreateDate(new Date());
			insertOrder.setModifyDate(new Date());
			insertOrder.setOrderDate(new Date());
			
			return foodOrderRepository.save(insertOrder);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public FoodOrder modifyFoodOrder(JSONObject obj) {
		
		Integer foodOrderId = obj.isNull("id")? null : obj.getInt("id");
		Integer userId = obj.isNull("userId")? null : obj.getInt("userId");
		User user = userRepository.findById(userId).orElse(null);
		String createDate = obj.isNull("createDate")? null : obj.getString("createDate");
		String modifyDate = obj.isNull("modifyDate")? null : obj.getString("modifyDate");
		String orderDate = obj.isNull("orderDate")? null : obj.getString("orderDate");
		
		if (foodOrderId!=null) {
			Optional<FoodOrder> optional = foodOrderRepository.findById(foodOrderId);
			if (optional.isPresent()) {
				FoodOrder updateOrder = optional.get();
				
				updateOrder.setId(foodOrderId);
				updateOrder.setUser(user);
				if (createDate != null && createDate.length() > 0) {
					java.util.Date temp = DatetimeConverter.parse(createDate, "yyyy-MM-dd");
					updateOrder.setCreateDate(temp);
				}
				if (modifyDate != null && modifyDate.length() > 0) {
					java.util.Date temp = DatetimeConverter.parse(modifyDate, "yyyy-MM-dd");
					updateOrder.setModifyDate(temp);
				}
				if (orderDate != null && orderDate.length() > 0) {
					java.util.Date temp = DatetimeConverter.parse(orderDate, "yyyy-MM-dd");
					updateOrder.setOrderDate(temp);
				}
			return foodOrderRepository.save(updateOrder);
			}
		}
		return null;
	}
	
	public FoodOrder findByFoodOrderId(Integer foodOrderId) {
		if (foodOrderId!=null) {
			Optional<FoodOrder> optional = foodOrderRepository.findById(foodOrderId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	// controller deleteFoodOrder
	public boolean deleteByFoodOrderId(Integer foodOrderId) {
		if(foodOrderId!=null) {
			Optional<FoodOrder> optional = foodOrderRepository.findById(foodOrderId);
			if(optional.isPresent()) {
				foodOrderRepository.deleteById(foodOrderId);
				return true;
			}
		}
		return false;
	}
	
	// controller deleteFoodOrder
	public void deleteFoodOrder(FoodOrder foodOrder) {
        foodOrderRepository.delete(foodOrder);
    }
	
	//controller find
		public List<FoodOrder> find(String json){
			try {
				JSONObject obj = new JSONObject(json);
//				 return foodOrderRepository.find(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	
	//controller findFoodOrder
	public JSONObject foodOrderToJson(FoodOrder foodOrder) {
//		String photoUrl = "/backstage/movie/photo/" + movie.getId();
		JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", foodOrder.getId());
        jsonObject.put("userId", foodOrder.getUser().getId());
        jsonObject.put("createDate", foodOrder.getCreateDate());
        jsonObject.put("modifyDate", foodOrder.getModifyDate());
        jsonObject.put("orderDate", foodOrder.getOrderDate());
        return jsonObject;
	}

}
