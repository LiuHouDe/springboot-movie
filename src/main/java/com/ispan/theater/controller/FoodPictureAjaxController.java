package com.ispan.theater.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.theater.domain.FoodPicture;
import com.ispan.theater.service.FoodPictureService;

import jakarta.annotation.PostConstruct;

@RestController
@CrossOrigin
public class FoodPictureAjaxController {
	@Autowired
	private FoodPictureService foodPictureService;
	
	
//	@GetMapping("/backstage/food/photo/{id}")
//    public ResponseEntity<?> getFoodPicture(@PathVariable Integer id) {
//        return ResponseEntity.ok(foodPictureService.getFoodPicture(id));
//    }
	@GetMapping("/foodPicture/{id}")
    public ResponseEntity<?> getFoodPicture(@PathVariable Integer id) {
        return ResponseEntity.ok(foodPictureService.getFoodPicture(id));
    }

	@PostMapping("/backstage/food/uploadPhoto/{id}")
	public ResponseEntity<?> addFoodPicture(@PathVariable Integer id, @RequestParam("files") List<MultipartFile> files) throws IOException {
	    boolean insert = foodPictureService.insertFoodPicture(files, id);        
	    if (insert) {
	            return ResponseEntity.ok().build();
	    }
	        return ResponseEntity.badRequest().build();
	}

    @DeleteMapping("/backstage/foodpicture/{pk}")//沒用到
    public String remove(@PathVariable(name = "pk") Integer id) {
        JSONObject responseJson = new JSONObject();
        System.out.println("responseJson"+responseJson);
            if(foodPictureService.deleteByFoodId(id)) {
                responseJson.put("success", true);
                responseJson.put("message", "刪除成功");
            } else {                
                responseJson.put("success", false);
                responseJson.put("message", "刪除失敗");
            }
        return responseJson.toString();
    }
   
	
	private byte[] picture = null;	
	@PostConstruct
	public void initialize() throws IOException {
		byte[] buffer = new byte[8192];

		ClassLoader classLoader = getClass().getClassLoader();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		BufferedInputStream is = new BufferedInputStream(
				classLoader.getResourceAsStream("static/no-image.jpg"));
		int len = is.read(buffer);
		while(len!=-1) {
			os.write(buffer, 0, len);
			len = is.read(buffer);
		}
		is.close();
		this.picture = os.toByteArray();
    }
	
	@GetMapping(
			path = "/backstage/food/photo/{food_pictureid}",
			produces = {MediaType.IMAGE_JPEG_VALUE})
	public @ResponseBody byte[] findPhotoByPhotoId(@PathVariable(name="food_pictureid") Integer id) {

        List<FoodPicture> foodPicture = foodPictureService.getFoodPicture(id);
		byte[] result = this.picture;
		if(foodPicture!=null) {
			result =  foodPicture.get(foodPicture.size()-1).getPicture();
        }
        return result;
	}
	
	 //origin
//  @DeleteMapping("/backstage/foodpicture/{pk}")
//  public String remove(@PathVariable(name = "pk") Integer id) {
//      JSONObject responseJson = new JSONObject();
//      if(id==null) {
//          responseJson.put("success", false);
//          responseJson.put("message", "id是必要欄位");
//      } else if(!foodPictureService.existById(id)) {
//          responseJson.put("success", false);
//          responseJson.put("message", "id不存在");
//      } else {
//          if(foodPictureService.delete(id)) {
//              responseJson.put("success", true);
//              responseJson.put("message", "刪除成功");
//          } else {                
//              responseJson.put("success", false);
//              responseJson.put("message", "刪除失敗");
//          }
//      }
//      return responseJson.toString();
//  }
	
}
