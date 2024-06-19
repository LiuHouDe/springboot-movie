package com.ispan.theater.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.theater.domain.Food;
import com.ispan.theater.domain.FoodPicture;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.repository.FoodPictureRepository;
import com.ispan.theater.repository.FoodRepository;
import com.ispan.theater.util.DatetimeConverter;

@Service
@Transactional
public class FoodPictureService {
	@Autowired
	private FoodPictureRepository foodPictureRepository;
	@Autowired
	private FoodRepository foodRepository;
	
	public List<FoodPicture> getFoodPicture(Integer fid) {
		Food food = foodRepository.findById(fid).orElse(null);
		return foodPictureRepository.findFoodPictureByFoodId(food);
	}
	
	public boolean insertFoodPicture(List<MultipartFile> files, Integer foodId) throws IOException {
		Food food = foodRepository.findById(foodId).orElse(null);

        if( food !=null){
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                FoodPicture foodPicture = new FoodPicture();
                foodPicture.setFood(food);
                foodPicture.setPicture(bytes);
                foodPicture.setFilename(fileName);
                foodPictureRepository.save(foodPicture);
            }
            return true;
        }
        return false;
    }
	
	//find picture      origin
    public FoodPicture findFoodPictureById(Integer id) {
		if(id!=null) {
			Optional<FoodPicture> optional = foodPictureRepository.findById(id);
			if(optional.isPresent()) {
				return optional.get();				
			}
		}
		return null;
	}
    
    
    //plus 沒用到
    public boolean existsByFoodId(Integer foodId) {
        return foodPictureRepository.existsByFoodId(foodId);
    }
    //plus 沒用到
    public boolean deleteByFoodId(Integer fid) {
		if (fid != null && existsByFoodId(fid)) {
			System.out.println("fid" + fid);
			Food food = foodRepository.findById(fid).orElse(null);
			System.out.println("food" + food);
			if (food!=null) {
				foodPictureRepository.deleteByFoodId(food);
				System.out.println("food" + food);
				return true;
			}
		}		
		return false;
	}
    
    public boolean existById(Integer id) {
		if(id!=null) {
			return foodPictureRepository.existsById(id);
		}
		return false;
	}

  //origin
//  public boolean delete(Integer id) {
//		if(id!=null) {
//			Optional<FoodPicture> optional = foodPictureRepository.findById(id);
//			if(optional.isPresent()) {
//				foodPictureRepository.deleteById(id);
//				return true;
//			}
//		}
//		return false;
//	}
}
