package com.ispan.theater.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Cinema;
import com.ispan.theater.repository.CinemaRepository;

@Service
@Transactional
public class CinemaService {
    @Autowired
    private CinemaRepository cinemaRepository;

    public List<Cinema> getAllCinema() {
        return cinemaRepository.findAll();
    }
    public Cinema getCinemaById(Integer id) {
        if(cinemaRepository.findById(id).isPresent()) {
            return cinemaRepository.findById(id).get();
        }
        else{
            return null;
        }
    }
    public Cinema saveCinemaJson(JSONObject cinema) {//test passed
        Cinema cinemaObj = new Cinema();
        cinemaObj.setName(cinema.getString("name"));
        cinemaObj.setAddress(cinema.getString("address"));
        cinemaObj.setLocationCategory(cinema.getString("location_category"));
        cinemaObj.setPhone(cinema.getString("phone"));
        return cinemaRepository.save(cinemaObj);
    }
    public Cinema updateCinemaJson(JSONObject cinema) {//test passed
        Optional<Cinema> optiona = cinemaRepository.findById(cinema.getInt("id"));
        if(optiona.isPresent()) {
            Cinema cinemaObj = optiona.get();
            cinemaObj.setName(cinema.getString("name"));
            cinemaObj.setAddress(cinema.getString("address"));
            cinemaObj.setLocationCategory(cinema.getString("location_category"));
            cinemaObj.setPhone(cinema.getString("phone"));
            return cinemaRepository.save(cinemaObj);
        }
        else{
            return null;
        }
    }

    public List<Map<String,Object>> findAllCinemaName() {
    	List<Map<String,Object>> list=cinemaRepository.findAllCinemaName();
    	for(Map<String,Object> map:list) {
    		System.out.println(map.entrySet());
    	}
    	return cinemaRepository.findAllCinemaName();
    }
    public List<Map<String, Object>> findAllCinema() {
//        List<Map<String, Object>> list = cinemaRepository.findAllCinema();
//        for (Map<String, Object> map : list) {
//            System.out.println(map.entrySet());
//        }
        return cinemaRepository.findAllCinema();
    }
    public void saveCinema(Cinema cinema){
        cinemaRepository.save(cinema);
    }
    
    
}
