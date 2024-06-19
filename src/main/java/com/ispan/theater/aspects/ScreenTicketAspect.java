
package com.ispan.theater.aspects;

import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.dto.SchduleDto;
import com.ispan.theater.repository.TicketRepository;
import com.ispan.theater.service.TicketService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class ScreenTicketAspect {
    private static final Logger log = LoggerFactory.getLogger(ScreenTicketAspect.class);
    @Autowired
    private TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @Pointcut("execution(* com.ispan.theater.service.ScreeningService.createScreening(com.ispan.theater.domain.Movie, org.json.JSONObject)) && args(movie, jsonObject)")
    public void screeningCreation(Movie movie, JSONObject jsonObject) {}

    @Pointcut("execution(* com.ispan.theater.service.ScreeningService.jsonToScreen(org.json.JSONObject)) && args(jsonScreen)")
    public void jsonScreening(JSONObject jsonScreen) {}

    @Pointcut("execution(* com.ispan.theater.service.BatchScreenService.batchScreenInsert(com.ispan.theater.dto.SchduleDto)) && args(request)")
    public void batchScreen(SchduleDto request) {}

    @AfterReturning(pointcut = "screeningCreation(movie, jsonObject)", returning = "screening")
    @Transactional
    public void afterReturningAdvice(Movie movie, JSONObject jsonObject, Screening screening) {
        if(!ticketRepository.findByScreening(screening.getId()).isEmpty()){
            return;
        }
        JSONObject screeningJson = new JSONObject();
        screeningJson.put("screeningId", screening.getId());
        ticketService.insertTicket2(screeningJson);
        System.out.println("HAHAHASuccessfully created screening for movie: " + screening.getMovie().getName());
    }

    @AfterReturning(pointcut = "jsonScreening(jsonScreen)", returning = "screeningid")
    @Transactional
    public void afterJsonScreening(JSONObject jsonScreen, Integer screeningid) {
        if(!ticketRepository.findByScreening(screeningid).isEmpty()){
            return;
        }
        JSONObject screeningJson = new JSONObject();
        screeningJson.put("screeningId", screeningid);
        ticketService.insertTicket2(screeningJson);
        System.out.println("HAHAHASuccessfully created screening for movie: " + screeningid);
    }
    @AfterReturning(pointcut = "batchScreen(request)",returning = "allScreenings")
    @Transactional
    public void afterBatchScreen(SchduleDto request, List<Screening> allScreenings) {
        for (Screening screening : allScreenings) {
            System.out.println("screening.getMovie().getName()");
            Integer screeningId = screening.getId();
            if(!ticketRepository.findByScreening(screeningId).isEmpty()){
                System.out.println(screening.getMovie().getName());
                continue;
            }
            JSONObject screeningJson = new JSONObject();
            screeningJson.put("screeningId", screeningId);
            ticketService.insertTicket2(screeningJson);
            log.info("Successfully created screening for movie: " + screeningId);
        }
    }

    @AfterThrowing(pointcut = "screeningCreation(movie, jsonObject)", throwing = "exception")
    public void afterThrowingAdvice(Movie movie, JSONObject jsonObject, Exception exception) {
        System.out.println("Error occurred while creating screening for movie: " + movie.getName());
        System.out.println("Error message: " + exception.getMessage());
    }

}
