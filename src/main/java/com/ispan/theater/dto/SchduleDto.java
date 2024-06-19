package com.ispan.theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SchduleDto {
        private Integer movieId;
        private Integer dailySessions;
        private String startDate;
        private String endDate;
        private Integer audotoriumId;
        private String frequency;
}
