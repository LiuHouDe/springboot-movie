package com.ispan.theater.controller;

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

import com.ispan.theater.domain.FoodOrder;
import com.ispan.theater.domain.FoodOrderDetail;
import com.ispan.theater.service.FoodOrderDetailService;

@RestController
@CrossOrigin
public class FoodOrderDetailController {
	@Autowired
	private FoodOrderDetailService foodOrderDetailService;
	
	@PostMapping("/backstage/foodorderDetail")
	public String createFoodOrder(@RequestBody String foodorderjson) {
		JSONObject response = new JSONObject();
		JSONObject jsonObject = new JSONObject(foodorderjson);
		Integer id = jsonObject.isNull("id") ? null : jsonObject.getInt("id");
		if(foodOrderDetailService.existFoodOrderDetailById(id)) {
			response.put("success", false);
			response.put("message", "id已存在");
		}else {
			FoodOrderDetail foodOrderDetail = foodOrderDetailService.createFoodOrderDetail(jsonObject);
			if (foodOrderDetail==null) {
				response.put("success", false);
				response.put("message", "新增失敗");
			}else {
				response.put("succcess", true);
				response.put("message", "新增成功");
			}
		}
		return response.toString();
	}
	
	@GetMapping("/backstage/foodorderDetail/{id}")
    public String findFoodOrder(@PathVariable Integer id) {
		FoodOrderDetail foodOrderDetail = foodOrderDetailService.findByFoodOrderDetailId(id);
        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        if (foodOrderDetail!=null) {
            JSONObject foodOrderJson = foodOrderDetailService.foodOrderDetailToJson(foodOrderDetail);
            array.put(foodOrderJson);
            response.put("list", array);
            System.out.println(foodOrderDetail);
            response.put("success","success");
        }
        else{
            response.put("fail","foodOrder not found");
        }
        return response.toString();
    }
	
	@PutMapping("/admin/backstage/foodorderDetail/{id}")
    public String updateFoodOrder(@RequestBody String str,@PathVariable Integer id) {
        JSONObject jsonObject = new JSONObject(str);
        JSONObject response = new JSONObject();
        if (foodOrderDetailService.findByFoodOrderDetailId(id) != null) {
        	FoodOrderDetail foodOrderDetail = foodOrderDetailService.modifyFoodOrderDetail(jsonObject);
            response.put("message", "更新成功");
            response.put("success", "success");
        } else {
            response.put("message", "更新失敗");
            response.put("fail", "fail");
        }
        return response.toString();

    }

    @DeleteMapping("/admin/backstage/foodorderDetail/{id}")
    public String deleteFoodOrder(@PathVariable("id") int id) {
    	FoodOrderDetail foodOrderDetail = foodOrderDetailService.findByFoodOrderDetailId(id);
        JSONObject response = new JSONObject(foodOrderDetail);
        if (foodOrderDetail != null)
        	foodOrderDetailService.deleteFoodOrderDetail(foodOrderDetail);
        response.put("msg", "刪除成功");
        response.put("succeed", "succeed");
        return response.toString();
    }

}
