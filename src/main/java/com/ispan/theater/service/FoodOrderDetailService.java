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
import com.ispan.theater.domain.FoodOrder;
import com.ispan.theater.domain.FoodOrderDetail;
import com.ispan.theater.repository.FoodOrderDetailRepository;
import com.ispan.theater.repository.FoodOrderRepository;
import com.ispan.theater.repository.FoodRepository;
import com.ispan.theater.util.DatetimeConverter;

@Service
@Transactional
public class FoodOrderDetailService {
	
	@Autowired
	private FoodOrderDetailRepository foodOrderDetailRepository;
	@Autowired
	private FoodOrderRepository foodOrderRepository;
	@Autowired
	private FoodRepository foodRepository;
	
	// controller modifyFoodOrderDetail
		public boolean existFoodOrderDetailById(Integer id) {
			if (id!=null) {
				return foodOrderDetailRepository.existsById(id);
			}
			return false;
		}
	
	public FoodOrderDetail createFoodOrderDetail(JSONObject obj) {

		try {
			Integer foodOrderId = obj.isNull("foodOrderId") ? null : obj.getInt("foodOrderId");
			FoodOrder foodOrder = foodOrderRepository.findById(foodOrderId).orElse(null);
			Integer foodId = obj.isNull("foodId") ? null : obj.getInt("foodId");
			Food food = foodRepository.findById(foodId).orElse(null);
			Integer buyNumber = obj.isNull("buyNumber") ? null : obj.getInt("buyNumber");
			Integer totalPrice = obj.isNull("totalPrice") ? null : obj.getInt("totalPrice");
			
			FoodOrderDetail insertFoodOrderDetail = new FoodOrderDetail();
			insertFoodOrderDetail.setOrder(foodOrder);
			insertFoodOrderDetail.setFood(food);
			insertFoodOrderDetail.setBuyNumber(buyNumber);
			insertFoodOrderDetail.setTotalPrice(totalPrice);
			insertFoodOrderDetail.setCreateDate(new Date());
			insertFoodOrderDetail.setModifyDate(new Date());
			
			return foodOrderDetailRepository.save(insertFoodOrderDetail);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public FoodOrderDetail modifyFoodOrderDetail(JSONObject obj) {
		
		Integer foodOrderDetailId = obj.isNull("foodOrderDetailId") ? null : obj.getInt("foodOrderDetailId");
		Integer foodOrderId = obj.isNull("foodOrderId") ? null : obj.getInt("foodOrderId");
		FoodOrder foodOrder = foodOrderRepository.findById(foodOrderId).orElse(null);
		Integer foodId = obj.isNull("foodId") ? null : obj.getInt("foodId");
		Food food = foodRepository.findById(foodId).orElse(null);
		Integer buyNumber = obj.isNull("buyNumber") ? null : obj.getInt("buyNumber");
		Integer totalPrice = obj.isNull("totalPrice") ? null : obj.getInt("totalPrice");
		String createDate = obj.isNull("createDate")? null : obj.getString("createDate");
		String modifyDate = obj.isNull("modifyDate")? null : obj.getString("modifyDate");
		
		if (foodOrderDetailId!=null) {
			Optional<FoodOrderDetail> optional = foodOrderDetailRepository.findById(foodOrderDetailId);
			if (optional.isPresent()) {
				FoodOrderDetail updateOrderDetail = optional.get();
				
				updateOrderDetail.setId(foodOrderDetailId);
				updateOrderDetail.setOrder(foodOrder);
				updateOrderDetail.setFood(food);
				updateOrderDetail.setBuyNumber(buyNumber);
				updateOrderDetail.setTotalPrice(totalPrice);
				if (createDate != null && createDate.length() > 0) {
					java.util.Date temp = DatetimeConverter.parse(createDate, "yyyy-MM-dd");
					updateOrderDetail.setCreateDate(temp);
				}
				if (modifyDate != null && modifyDate.length() > 0) {
					java.util.Date temp = DatetimeConverter.parse(modifyDate, "yyyy-MM-dd");
					updateOrderDetail.setModifyDate(temp);
				}
				
				return foodOrderDetailRepository.save(updateOrderDetail);
			}
		}
		return null;
	}
	
	public FoodOrderDetail findByFoodOrderDetailId(Integer FoodOrderDetailId) {
		if (FoodOrderDetailId!=null) {
			Optional<FoodOrderDetail> optional = foodOrderDetailRepository.findById(FoodOrderDetailId);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	public boolean deleteFoodOrderDetailId(Integer foodOrderDetailId) {
		if (foodOrderDetailId!=null) {
			Optional<FoodOrderDetail> op = foodOrderDetailRepository.findById(foodOrderDetailId);
			if (op.isPresent()) {
				foodOrderDetailRepository.deleteById(foodOrderDetailId);
				return true;
			}
		}
		return false;
	}
	
	public void deleteFoodOrderDetail(FoodOrderDetail foodOrderDetail) {
        foodOrderDetailRepository.delete(foodOrderDetail);
    }
	
	//controller find
			public List<FoodOrder> find(String json){
				try {
					JSONObject obj = new JSONObject(json);
//					 return foodOrderRepository.find(obj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
			
	//controller findFoodOrder
	public JSONObject foodOrderDetailToJson(FoodOrderDetail foodOrderDetail) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", foodOrderDetail.getId());
		jsonObject.put("foodOrderId", foodOrderDetail.getOrder());
		jsonObject.put("foodId", foodOrderDetail.getFood());
		jsonObject.put("buyNumber", foodOrderDetail.getBuyNumber());
		jsonObject.put("totalPrice", foodOrderDetail.getTotalPrice());
		jsonObject.put("createDate", foodOrderDetail.getCreateDate());
        jsonObject.put("modifyDate", foodOrderDetail.getModifyDate());
        
        return jsonObject;
	}

}
