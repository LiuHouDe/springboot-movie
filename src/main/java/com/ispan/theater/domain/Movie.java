package com.ispan.theater.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "name_eng", length = 50)
    private String name_eng;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "category_code", nullable = false, length = 50)
    private String categoryCode;

    @Column(name = "director", length = 10)
    private String director;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "release_Date", nullable = false)
    private Date releaseDate;

    @Column(name = "end_Date", nullable = false)
    private Date endDate;

    @ColumnDefault("0")
    @Column(name = "viewer")
    private Integer viewer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

    @Column(name = "price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rated_code", nullable = false, referencedColumnName = "code")
    private Rated ratedCode;

    @Lob
    @JsonIgnore
    private byte[] image;


    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", name_eng='" + name_eng + '\'' +
                ", duration=" + duration +
                ", categoryCode='" + categoryCode + '\'' +
                ", director='" + director + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", endDate=" + endDate +
                ", viewer=" + viewer +
                ", price=" + price +
                ", ratedCode=" + ratedCode +
                '}';
    }
}