package com.ispan.theater.service;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testScreeningService {
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private MovieService movieService;

//    @Test
//    public void testScreeningService() {
//        JSONObject obj = new JSONObject();
//        Movie movie = movieService.getMovieById(2);
//        obj.put("startTime","2024-03-01 9:00");
//        obj.put("endTime","2024-03-01 11:20");
//        obj.put("auditoriumId",1);
//        Screening s = screeningService.createScreening(movie, obj);
//        System.out.println(s);
//    }

//    @Test
    public void testScreeningService2() {
        screeningService.deleteScreening(8);
    }
}
