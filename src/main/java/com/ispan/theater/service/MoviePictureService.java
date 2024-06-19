package com.ispan.theater.service;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MoviePicture;

import com.ispan.theater.repository.MoviePictureRepository;
import com.ispan.theater.repository.MovieRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class MoviePictureService {
    @Autowired
    private MoviePictureRepository moviePictureRepository;
    @Autowired
    private MovieRepository movieRepository;
    public List<MoviePicture>  getMoviePictures(Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        return moviePictureRepository.findByMovieId(movie);
    }
    public boolean insertMoviePicture(List<MultipartFile> files, Integer movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if( movie !=null){
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                byte[] bytes = file.getBytes();
                MoviePicture moviePicture = new MoviePicture();
                moviePicture.setMovie(movie);
                moviePicture.setPicture(bytes);
                moviePicture.setFilename(fileName);
                moviePictureRepository.save(moviePicture);
            }
            return true;
        }
        return false;
    }
    public void deleteMoviePicture(List<Integer> mpIds) {
        for(Integer mpId : mpIds){
            moviePictureRepository.deleteById(mpId);
        }
    }
}
