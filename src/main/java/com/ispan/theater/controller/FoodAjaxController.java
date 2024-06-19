package com.ispan.theater.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Food;
import com.ispan.theater.service.FoodService;
import com.ispan.theater.util.DatetimeConverter;

@RestController
@CrossOrigin
public class FoodAjaxController {
	
	@Autowired
	private FoodService foodService;
	
	@GetMapping("/backstage/food/name/{name}")
	public String existsByName(@PathVariable("name") String name) {
		JSONObject responseJson = new JSONObject();
		boolean exist = foodService.existByName(name);
		if (exist) {
			responseJson.put("success", false);
			responseJson.put("message", "名稱存在");
		}else {
			responseJson.put("success", true);
			responseJson.put("message", "名稱不存在");
		}
		return responseJson.toString();
	}
	
	@PostMapping("/backstage/food")//postman test
	public String createFood(@RequestBody String foodjson) {
		JSONObject jsonObject = new JSONObject(foodjson);
		JSONObject response = new JSONObject();
		if (!foodService.existsFoodByName(jsonObject.getString("name"))) {
			Food food = foodService.createFood(jsonObject);
			response.put("message", "新增成功");
            response.put("success", "success");
            response.put("id",food.getId());
		}else {
			response.put("message", "新增失敗");
            response.put("fail", "fail");
		}
		return response.toString();
	}

	
	@PostMapping("/backstage/food/find")//postman test
	public String find(@RequestBody String json) {
		JSONObject responseJson = new JSONObject();
		JSONArray array = new JSONArray();
		List<Food> products = foodService.find(json);
		if (products!=null && !products.isEmpty()) {
			for(Food product : products) {

				String createDate = DatetimeConverter.toString(product.getCreateDate(), "yyyy-MM-dd");
				String modifyDate = DatetimeConverter.toString(product.getModifyDate(), "yyyy-MM-dd");

				JSONObject item = new JSONObject()
							.put("id", product.getId())
							.put("name", product.getName())
							.put("price", product.getPrice())
							.put("count", product.getCount())
							.put("createDate", createDate)
							.put("modifyDate", modifyDate);
				array.put(item);
			}
		}
		
		responseJson.put("list", array);
		
		long counts = foodService.count(json);
		responseJson.put("counts", counts);
		
		return responseJson.toString();
	}
	
	@GetMapping("/backstage/food/{pk}")//postman test
	public String findByFoodId(@PathVariable(name="pk") Integer id) {
		JSONObject jsonResponseObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		Food food = foodService.findByFoodId(id);
		//有找到資料才會做if內程式碼，沒找到資料則會是一個名為list空陣列，toString()回傳String
		if (food!=null) {
			String createDate = DatetimeConverter.toString(food.getCreateDate(), "yyyy-MM-dd");
			String modifyDate = DatetimeConverter.toString(food.getModifyDate(), "yyyy-MM-dd");
			JSONObject item = new JSONObject();
			item.put("id", food.getId());
			item.put("name", food.getName());
			item.put("price", food.getPrice());
			item.put("count", food.getCount());
			item.put("createDate", createDate);
			item.put("modifyDate", modifyDate);
			jsonArray.put(item);
		}
		
		jsonResponseObject.put("list", jsonArray);
		return jsonResponseObject.toString();
	}
	
	@PutMapping("/backstage/food/{pk}")//postman test
	public String modifyFood (@PathVariable(name="pk") Integer id ,@RequestBody String foodjson) {
		JSONObject responseJson = new JSONObject();
		if (id==null) {
			responseJson.put("success", false);
			responseJson.put("msg", "id為必要欄位");
		} else if (!foodService.existFoodById(id)) {
			responseJson.put("success", false);
			responseJson.put("msg", "id不存在");
		} else {
			Food food = foodService.modifyFood(foodjson);
			if (food == null) {
				responseJson.put("success", false);
				responseJson.put("msg", "修改失敗");
			} else {
				responseJson.put("success", true);
				responseJson.put("msg", "修改成功");
			}
		}
		
		
		return responseJson.toString();
	}
	
	@DeleteMapping("/backstage/food/{pk}")//postman test
	public String deleteFood (@PathVariable(name="pk") Integer id) {
		JSONObject responseJson = new JSONObject();
		System.out.println("responseJson"+responseJson);
        if(id==null) {
            responseJson.put("success", false);
            responseJson.put("message", "id是必要欄位");
        } else if(!foodService.existFoodById(id)) {
            responseJson.put("success", false);
            responseJson.put("message", "id不存在");
        } else {
            if(foodService.deleteByFoodId(id)) {
                responseJson.put("success", true);
                responseJson.put("message", "刪除成功");
            } else {                
                responseJson.put("success", false);
                responseJson.put("message", "刪除失敗");
            }
        }
        return responseJson.toString();
	}
	
	
	
}
