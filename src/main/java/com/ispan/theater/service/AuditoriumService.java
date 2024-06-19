package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.AuditoriumLevel;
import com.ispan.theater.domain.Cinema;
import com.ispan.theater.repository.AuditoriumLevelRepository;
import com.ispan.theater.repository.AuditoriumRepository;
import com.ispan.theater.repository.CinemaRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuditoriumService {
    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private AuditoriumLevelRepository levelRepository;
    @Autowired
    private AuditoriumLevelRepository auditoriumLevelRepository;

    public List<Map<String, Object>> getCinemaAuditoriums(Integer cinemaId) {
        List<Map<String, Object>> result = new ArrayList<>();


        result = auditoriumRepository.findByCinema(cinemaId);
        return result;

    }

    public Auditorium getAuditorium(Integer auditoriumId) {
        Optional<Auditorium> auditoriumOptional = auditoriumRepository.findById(auditoriumId);
        if (auditoriumOptional.isPresent()) {
            Auditorium auditorium = auditoriumOptional.get();
            return auditorium;
        } else {
            return null;
        }
    }

    public Auditorium saveAuditorium(JSONObject json) {
        Auditorium auditorium = null;
        auditorium = new Auditorium();
        auditorium.setLayoutId(json.getInt("layoutId"));
        auditorium.setAuditoriumNumber(json.getInt("auditoriumNumber"));
        if (auditoriumLevelRepository.findById(json.getInt("levelid")).isPresent()) {
            AuditoriumLevel auditoriumLevel = auditoriumLevelRepository.findById(json.getInt("levelid")).get();
            auditorium.setLevelId(auditoriumLevel);
        } else {
            return null;
        }
        if (cinemaRepository.findById(json.getInt("cinemaid")).isPresent()) {
            Cinema cinema = cinemaRepository.findById(json.getInt("cinemaid")).get();
            auditorium.setCinema(cinema);
        } else {
            return null;
        }
        return auditoriumRepository.save(auditorium);
    }

}
