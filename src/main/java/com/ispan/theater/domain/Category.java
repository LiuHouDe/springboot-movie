package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Column(name = "category_content", nullable = false, length = 50)
    private String categoryContent;

    @Override
    public String toString() {
        return "Category{" +
                "code='" + code.trim() + '\'' +
                ", categoryContent='" + categoryContent + '\'' +
                '}';
    }
}