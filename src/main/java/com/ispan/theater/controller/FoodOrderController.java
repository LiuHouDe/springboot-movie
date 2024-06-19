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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.FoodOrder;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.service.FoodOrderService;

@RestController
@CrossOrigin
public class FoodOrderController {
	@Autowired
	private FoodOrderService foodOrderService;
	
	@PostMapping("/backstage/foodorder")
	public String createFoodOrder(@RequestBody String foodorderjson) {
		JSONObject response = new JSONObject();
		JSONObject jsonObject = new JSONObject(foodorderjson);
		Integer id = jsonObject.isNull("id") ? null : jsonObject.getInt("id");
		if(foodOrderService.existFoodOrderById(id)) {
			response.put("success", false);
			response.put("message", "id已存在");
		}else {
			FoodOrder foodOrder = foodOrderService.createFoodOrder(jsonObject);
			if (foodOrder==null) {
				response.put("success", false);
				response.put("message", "新增失敗");
			}else {
				response.put("succcess", true);
				response.put("message", "新增成功");
			}
		}
		return response.toString();
	}
	
	@GetMapping("/backstage/foodorder/{id}")
    public String findFoodOrder(@PathVariable Integer id) {
        FoodOrder foodOrder = foodOrderService.findByFoodOrderId(id);
        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        if (foodOrder!=null) {
            JSONObject foodOrderJson = foodOrderService.foodOrderToJson(foodOrder);
            array.put(foodOrderJson);
            response.put("list", array);
            System.out.println(foodOrder);
            response.put("success","success");
        }
        else{
            response.put("fail","foodOrder not found");
        }
        return response.toString();
    }
	
	@PutMapping("/admin/backstage/foodorder/{id}")
    public String updateFoodOrder(@RequestBody String foodorderstr,@PathVariable Integer id) {
        JSONObject jsonObject = new JSONObject(foodorderstr);
        JSONObject response = new JSONObject();
        if (foodOrderService.findByFoodOrderId(id) != null) {
        	FoodOrder foodOrder = foodOrderService.modifyFoodOrder(jsonObject);
            response.put("message", "更新成功");
            response.put("success", "success");
        } else {
            response.put("message", "更新失敗");
            response.put("fail", "fail");
        }
        return response.toString();

    }

    @DeleteMapping("/admin/backstage/foodorder/{id}")
    public String deleteFoodOrder(@PathVariable("id") int id) {
    	FoodOrder foodOrder = foodOrderService.findByFoodOrderId(id);
        JSONObject response = new JSONObject(foodOrder);
        if (foodOrder != null)
        	foodOrderService.deleteFoodOrder(foodOrder);
        response.put("msg", "刪除成功");
        response.put("succeed", "succeed");
        return response.toString();
    }
    
    
//    @GetMapping("foodorder/food/getOrder")
//	public String getOrder(@RequestParam("userId")Integer userId,@RequestParam("page")Integer page) {
////		orderConditionPublisher.publishV2(userId);
//    	System.out.println("getOrder start");
//		return foodOrderService.getOrder(userId, page);
//	}
//	
//    @PostMapping("/backstage/movie/find")//test passed
//    public String findMovie(@RequestBody String json) {
//        JSONObject jsonObject = new JSONObject(json);
//        JSONObject response = new JSONObject();
//
//        Page<Movie> result = movieService.findMulti1(jsonObject);
//        System.out.println("test");
//        List<Movie> movies = result.getContent();
//        long count = movieService.count(jsonObject);
//        JSONArray array = new JSONArray();
//        if (!movies.isEmpty()) {
//            for (Movie m : movies) {
//                JSONObject movie = movieService.movieToJson(m);
//                array.put(movie);
//            }
//        }
//        response.put("list", array);
//        response.put("count", count);
//        return response.toString();
//    }

}
