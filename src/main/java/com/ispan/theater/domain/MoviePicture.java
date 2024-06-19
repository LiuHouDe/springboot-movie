package com.ispan.theater.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Movie_Picture")
public class MoviePicture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "picture_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    @JsonIgnore
    private Movie movie;
    @Lob
    @Column(name = "picture", nullable = false)
    private byte[] picture;
    @Column(name = "filename", nullable = false)
    private String filename;
    
}