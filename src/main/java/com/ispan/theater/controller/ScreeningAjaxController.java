package com.ispan.theater.controller;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.service.AuditoriumService;
import com.ispan.theater.service.MovieService;
import com.ispan.theater.service.ScreeningService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class ScreeningAjaxController {
    @Autowired
    private ScreeningService screeningService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private AuditoriumService auditoriumService;

    @PostMapping("/backstage/screening/find")
    public String findScreening(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray array = new JSONArray();
        Page<Screening> result = screeningService.findScreenings(jsonObject);
        List<Screening> screenings = result.getContent();
        if (!screenings.isEmpty()) {
            for (Screening screening : screenings) {
                JSONObject screeningObject = screeningService.screenToJSON(screening);
                array.put(screeningObject);
            }
        }
        jsonObject.put("list", array);
        jsonObject.put("count", result.getTotalElements());
        return jsonObject.toString();
    }

    @GetMapping("/backstage/screening/{movieId}")
    public String screeningByMovieId(@PathVariable Integer movieId) {
        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        List<Screening> result = screeningService.findScreeningsByMovieId(movieId);
        if (!result.isEmpty()) {
            response.put("list", array);
            response.put("count", result.size());
            response.put("success", true);
            return response.toString();
        }
        response.put("noresult", true);
        return response.toString();
    }

    @GetMapping("/backstage/screening")
    public String findScreeningCinemaMovie(@RequestParam("cinemaId") Integer cinemaId, @RequestParam("movieId") Integer movieId) {
        JSONObject response = new JSONObject();
        List<Map<String, Object>> result = screeningService.findScreeningByMovieCinema(cinemaId, movieId);
        if (!result.isEmpty()) {
            response.put("list", result);
            response.put("count", result.size());
            response.put("success", "success");
        } else {
            response.put("fail", "fail");
            response.put("message", "Not Found");
        }
        return response.toString();
    }

    @GetMapping("/backstage/screeningbyc")
    public String findScreeningCinema(@RequestParam("cinemaId") Integer cinemaId) {
        JSONObject response = new JSONObject();
        List<Map<String, Object>> result = screeningService.findScreeningByCinema(cinemaId);
        if (!result.isEmpty()) {
            response.put("list", result);
            response.put("count", result.size());
            response.put("success", "success");
        } else {
            response.put("fail", "fail");
            response.put("message", "Not Found");
        }
        return response.toString();
    }

    @PostMapping("/backstage/screening")
    public String insertScreening(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject response = new JSONObject();
        if (jsonObject.has("movieId")) {
            Movie movie = movieService.getMovieById(jsonObject.getInt("movieId"));
            try {
                Screening screening = screeningService.createScreening(movie, jsonObject);
                response.put("success", true);
                response.put("message", screening.toString());
            }catch (Exception e) {
                response.put("fail", false);
                response.put("message", e.getMessage());
            }
        } else {
            response.put("fail", false);
            response.put("message", "電影不存在");
        }
        return response.toString();
    }

    //find by a_id m_id
    @GetMapping("/backstage/screen")
    public String screeningByMovieAuditorium(@RequestParam("mid") Integer mid, @RequestParam("aid") Integer aid) {
        JSONObject response = new JSONObject();
        List<Map<String, Object>> result = screeningService.getScreeningsByMovieIdAuditoriumId(mid, aid);
        if (!result.isEmpty()) {
            response.put("list", result);
            response.put("count", result.size());
            response.put("success", "success");
        } else {
            response.put("fail", "fail");
            response.put("message", "Not Found");
        }
        return response.toString();
    }

    @GetMapping("/backstage/screenbya")
    public String screeningByAuditorium(@RequestParam("aid") Integer aid) {
        JSONObject response = new JSONObject();
        List<Map<String, Object>> result = screeningService.getScreeningsByAuditoriumId(aid);
        if (!result.isEmpty()) {
            response.put("list", result);
            response.put("count", result.size());
            response.put("success", "success");
        } else {
            response.put("fail", "fail");
            response.put("message", "Not Found");
        }
        return response.toString();
    }

    //從日曆獲得資料CRUD
    @PostMapping("/admin/backstage/screen/calendar")
    public String modifyCalendar(@RequestBody String json) {
        JSONArray array = new JSONArray(json);
        JSONObject response = new JSONObject();
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            Integer re = null;
            try {
                re = screeningService.jsonToScreen(object);
            } catch (Exception e) {
                response.put("message",e.getMessage());
                response.put("fail","fail");
                return response.toString();
            }
            System.out.println(re);
            System.out.println(object);
        }
        response.put("success","success");
        response.put("message","場次更動完成");
        return response.toString();
    }

    @DeleteMapping("/admin/backstage/screen/calendar")
    public void deleteScreen(@RequestParam("deletelist") List<Integer> deletelist) {
        for (Integer id : deletelist) {
            screeningService.deleteScreening(id);
        }
    }

    @GetMapping("/backstage/auditorium")
    public String screeningByCinemaId(@RequestParam("cinemaId") Integer cinemaId) {
        JSONObject response = new JSONObject();
        List<Map<String, Object>> result = auditoriumService.getCinemaAuditoriums(cinemaId);
        if (!result.isEmpty()) {
            response.put("list", result);
            response.put("count", result.size());
            response.put("success", "success");
        } else {
            response.put("fail", false);
            response.put("message", "Not Found");
        }
        return response.toString();
    }
}
