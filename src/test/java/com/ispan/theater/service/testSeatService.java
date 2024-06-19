package com.ispan.theater.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testSeatService {
    @Autowired
    private SeatService seatService;

    @Test
    public void testSeatService() {
        Integer row = 15;
        Integer col = 24;
        seatService.insertSeat(row, col);

    }
}
