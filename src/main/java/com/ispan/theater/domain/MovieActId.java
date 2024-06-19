package com.ispan.theater.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class MovieActId implements Serializable {
    private static final long serialVersionUID = 2025129868047106341L;
    @Column(name = "actor_id", nullable = false)
    private Integer actorId;

    @Column(name = "movie_id", nullable = false)
    private Integer movieId;

    public MovieActId(Integer actorId, Integer movieId) {
        this.actorId = actorId;
        this.movieId = movieId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MovieActId entity = (MovieActId) o;
        return Objects.equals(this.actorId, entity.actorId) &&
                Objects.equals(this.movieId, entity.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, movieId);
    }

}