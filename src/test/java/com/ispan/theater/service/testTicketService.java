package com.ispan.theater.service;

import com.ispan.theater.domain.Screening;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class testTicketService {
    @Autowired
    private TicketService ticketService;
    @Autowired
    private ScreeningService screeningService;
    @Test
    public void testTicketServiceInsert() {
        Screening s = screeningService.findByScreening(2);
        JSONObject jo = new JSONObject().put("screeningId", s.getId());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            ticketService.insertTicket(jo);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println(elapsedTime);

    }
    @Test
    public void testTicketServiceInsert2() {
        Screening s = screeningService.findByScreening(3);
        JSONObject jo = new JSONObject().put("screeningId", s.getId());
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            ticketService.insertTicket2(jo);
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println(elapsedTime);
    }
}
