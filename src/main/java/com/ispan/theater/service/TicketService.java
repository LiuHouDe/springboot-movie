package com.ispan.theater.service;

import com.ispan.theater.dao.TicketMapper;
import com.ispan.theater.domain.*;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private LayoutService layoutService;
    @Autowired
    private TicketMapper ticketMapper ;

    @Async
    public  void insertTicket(JSONObject jsonObject){
        Integer screeningId = jsonObject.getInt("screeningId");
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
        if(optionalScreening.isPresent()){
            Screening screening = optionalScreening.get();
            Movie movie = screening.getMovie();
            Auditorium auditorium = screening.getAuditorium();
            List<Layout> layoutList = layoutService.getLayout(auditorium);
            for(Layout layout : layoutList){
                Seat seat = layout.getSeat();
                Ticket ticket = new Ticket();
                ticket.setMovie(movie);
                ticket.setAuditorium(auditorium);
                ticket.setSeat(seat);
                ticket.setIsAvailable("未售出");
                ticket.setCreateDate(new Date());
                ticket.setModifyDate(new Date());
                ticket.setScreening(screening);
                ticketRepository.save(ticket);
            }
        }
    }
    @Async
    @Transactional
    public  void insertTicket2(JSONObject jsonObject){
        Integer screeningId = jsonObject.getInt("screeningId");
        Optional<Screening> optionalScreening = screeningRepository.findById(screeningId);
        if(optionalScreening.isPresent()){
            Screening screening = optionalScreening.get();
            Movie movie = screening.getMovie();
            Auditorium auditorium = screening.getAuditorium();
            List<Layout> layoutList = layoutService.getLayout(auditorium);
            List<Ticket> ticketList = new ArrayList<>();

            for(Layout layout : layoutList){
                Seat seat = layout.getSeat();
                Ticket ticket = new Ticket();
                ticket.setMovie(movie);
                ticket.setAuditorium(auditorium);
                ticket.setSeat(seat);
                ticket.setIsAvailable("未售出");
                ticket.setCreateDate(new Date());
                ticket.setModifyDate(new Date());
                ticket.setScreening(screening);
                ticketList.add(ticket);
            }
            ticketMapper.insertBatch(ticketList);
        }

    }
    @Transactional
    public void setTicketStatuaToNotSale(List<Integer> ticketIds){
        for(Integer ticketId : ticketIds){
            Ticket ticket = ticketRepository.findById(ticketId).get();
            ticket.setIsAvailable("未售出");
            ticket.setModifyDate(new Date());
            ticketRepository.save(ticket);
        }
    }

}
