package com.ispan.theater.controller;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.repository.AuditoriumLevelRepository;
import com.ispan.theater.service.MovieService;


@RestController
@CrossOrigin
public class MovieAjaxController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private AuditoriumLevelRepository auditoriumLevelRepository;

    @PostMapping("/backstage/movie/find")//test passed
    public String findMovie(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        JSONObject response = new JSONObject();

        Page<Movie> result = movieService.findMulti1(jsonObject);
        System.out.println("test");
        List<Movie> movies = result.getContent();
        long count = movieService.count(jsonObject);
        JSONArray array = new JSONArray();
        if (!movies.isEmpty()) {
            for (Movie m : movies) {
                JSONObject movie = movieService.movieToJson(m);
                array.put(movie);
            }
        }
        response.put("list", array);
        response.put("count", count);
        return response.toString();
    }
    @GetMapping("/backstage/movie/find")
    public ResponseEntity<?> findMovie(@RequestParam Map<String, String> requestParams) {
        JSONObject jsonObject = new JSONObject(requestParams);
        JSONObject response = new JSONObject();
        Page<Movie> result = movieService.findMulti1(jsonObject);
        List<Movie> movies = result.getContent();
        long count = result.getTotalElements();
        JSONArray array = new JSONArray();
        if (!movies.isEmpty()) {
            for (Movie m : movies) {
                JSONObject movie = movieService.movieToJson(m);
                array.put(movie);
            }
            response.put("movies", array);
            response.put("count", count);
            System.out.println(array);
            return ResponseEntity.ok().body(movies);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("backstage/movie/{id}")
    public String findMovie(@PathVariable Integer id) {
        Movie movie = movieService.getMovieById(id);
        JSONObject response = new JSONObject();
        JSONArray array = new JSONArray();
        if (movie!=null) {
            JSONObject movieJson = movieService.movieToJson(movie);
            array.put(movieJson);
            response.put("list", array);
            System.out.println(movie);
            response.put("success","success");
        }
        else{
            response.put("fail","movie not found");
        }
        return response.toString();
    }

    @PostMapping("/admin/backstage/movie/uploadPhoto/{id}")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file ,@PathVariable Integer id) {//測試用
        Movie movie = movieService.getMovieById(id);
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("上傳的檔案為空");
            }
            byte[] imageData = file.getBytes();
            movie.setImage(imageData);
            movieService.saveMovie(movie);

            return ResponseEntity.ok("上傳成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("上傳失敗: " + e.getMessage());
        }
    }

    @PostMapping("/admin/backstage/movie")
    public String insertMovie(@RequestBody String moviestr) {
        JSONObject jsonObject = new JSONObject(moviestr);
        JSONObject response = new JSONObject();
        if (!movieService.existsMovieByName(jsonObject.getString("name"))) {
            Movie movie = movieService.insertMovie(jsonObject);
            response.put("message", "新增成功");
            response.put("success", "success");
            response.put("id",movie.getId());
        } else {
            response.put("message", "新增失敗");
            response.put("fail", "fail");
        }
        return response.toString();


    }

    @PutMapping("/admin/backstage/movie/{id}")
    public String updateMovie(@RequestBody String moviestr,@PathVariable Integer id) {
        JSONObject jsonObject = new JSONObject(moviestr);
        JSONObject response = new JSONObject();
        if (movieService.getMovieById(id) != null) {
            Movie movie = movieService.updateMovie(jsonObject);
            response.put("message", "更新成功");
            response.put("success", "success");
        } else {
            response.put("message", "更新失敗");
            response.put("fail", "fail");
        }
        return response.toString();

    }

    @DeleteMapping("/admin/backstage/movie/{id}")
    public String deleteMovie(@PathVariable("id") int id) {
        Movie movie = movieService.getMovieById(id);
        JSONObject response = new JSONObject(movie);
        if (movie != null)
            movieService.deleteMovie(movie);
        response.put("msg", "刪除成功");
        response.put("succeed", "succeed");
        return response.toString();
    }

    //    @GetMapping("/backstage/movie/photo/{id}")
//    public String getMoviePhoto(@PathVariable("id") Integer id){
//        Movie movie = movieService.getMovieById(id);
//        String photoBase64;
//        if(movie != null){
//            photoBase64 = movie.getImage();
//            return photoBase64;
//        } else {
//            return null;
//        }
//    }
    @GetMapping(path = "/backstage/movie/photo/{id}"
    ,produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getMoviePhoto(@PathVariable("id") Integer id) {
        Movie movie = movieService.getMovieById(id);
        if (movie != null) {
            return movie.getImage();
        }
        return null;
    }

}
