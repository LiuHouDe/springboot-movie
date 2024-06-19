package com.ispan.theater.service;

import com.ispan.theater.domain.AuditoriumLevel;
import com.ispan.theater.repository.AuditoriumLevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AudoriumLevelService {
    @Autowired
    private AuditoriumLevelRepository auditoriumLevelRepository;

    public  void insertLevel (AuditoriumLevel auditoriumLevel) {
        auditoriumLevelRepository.save(auditoriumLevel);
    }
}
