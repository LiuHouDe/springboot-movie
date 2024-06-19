package com.ispan.theater.service;

import com.ispan.theater.domain.Rated;
import com.ispan.theater.repository.RatedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatedService {
    @Autowired
    private RatedRepository ratedRepository;

    public List<Rated> getRateds() {
        return ratedRepository.findAll();
    }
}
