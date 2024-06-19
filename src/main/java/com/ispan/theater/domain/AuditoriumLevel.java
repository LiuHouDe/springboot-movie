package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Auditorium_Level")
public class AuditoriumLevel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id", nullable = false)
    private Integer id;

    @Column(name = "context", nullable = false, length = 20)
    private String context;


}
