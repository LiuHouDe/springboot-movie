package com.ispan.theater.controller;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Category;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.service.CategoryService;
import com.ispan.theater.service.RatedService;

@RestController
@CrossOrigin
public class OptionAjaxController {
    @Autowired
    private RatedService ratedService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/backstage/movie/ratedlist")
    public String getRatedList(){
        JSONObject response = new JSONObject();
        List<Rated> result = ratedService.getRateds();
        response.put("ratedlist", result);
        return response.toString();
    }
    @GetMapping("/backstage/movie/categorylist")
    public String getCategoryList(){
        JSONObject response = new JSONObject();
        List<Category> result = categoryService.findAll();
        response.put("categorylist", result);
        return response.toString();
    }
}
