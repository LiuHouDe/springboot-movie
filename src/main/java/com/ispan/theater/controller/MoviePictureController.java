package com.ispan.theater.controller;

import com.ispan.theater.service.MoviePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
public class MoviePictureController {
    @Autowired
    private MoviePictureService moviePictureService;

    @GetMapping("/moviePicture/{id}")
    public ResponseEntity<?> getMoviePicture(@PathVariable Integer id) {
        return ResponseEntity.ok(moviePictureService.getMoviePictures(id));
    }

    @PostMapping("/admin/moviePicture/{id}")
    public ResponseEntity<?> addMoviePicture(@PathVariable Integer id, @RequestParam("files") List<MultipartFile> files) throws IOException {
        boolean insert = moviePictureService.insertMoviePicture(files, id);
        if (insert) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/admin/moviePicture")
    public void deleteMoviePicture(@RequestParam("lists") List<Integer> lists) {
        moviePictureService.deleteMoviePicture(lists);
    }
}
