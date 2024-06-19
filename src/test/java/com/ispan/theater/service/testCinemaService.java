package com.ispan.theater.service;

import com.ispan.theater.domain.Cinema;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testCinemaService {
    @Autowired
    private CinemaService cinemaService;
    @Test
    public void testCinemaService() {
        JSONObject json = new JSONObject();
        json.put("id", 1);
        json.put("name","大安店");
        json.put("address","台北市大安區和平東路二段162號");
        json.put("phone","02-20202033");
        json.put("location_category","北北基");
        //Cinema cinema = cinemaService.saveCinemaJson(json);
        Cinema cinema = cinemaService.updateCinemaJson(json);
        System.out.println(cinema);
    }
}
