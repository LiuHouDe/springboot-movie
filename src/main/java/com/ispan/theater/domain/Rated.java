package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Rated")
public class Rated {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rated_id", nullable = false)
    private Integer id;

    @Column(name = "context", nullable = false, length = 50)
    private String context;

    @Column(name = "code", nullable = false, length = 20 , unique = true)
    private String code;

    @Override
    public String toString() {
        return "Rated{" +
                "context='" + context + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}