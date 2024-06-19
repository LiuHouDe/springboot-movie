package com.ispan.theater.service;

import com.ispan.theater.domain.Seat;
import com.ispan.theater.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    @Autowired
    private SeatRepository seatRepository;

    public void insertSeat(Integer row,Integer col) {
        for(int i=0;i<row;i++) {
            for(int j=0;j<col;j++) {
                Seat seat = new Seat();
                seat.setSeatRow(i+1);
                seat.setSeatColumn(j+1);
                seatRepository.save(seat);
            }
        }
    }
}
