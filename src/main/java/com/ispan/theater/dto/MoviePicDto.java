package com.ispan.theater.dto;

public class MoviePicDto {
    public Integer picture_id;
    public byte[] picture;
    public Integer movie_id;
    public String filename;

    public MoviePicDto(Integer picture_id, byte[] picture, Integer movie_id, String filename) {
        this.picture_id = picture_id;
        this.picture = picture;
        this.movie_id = movie_id;
        this.filename = filename;
    }
}
