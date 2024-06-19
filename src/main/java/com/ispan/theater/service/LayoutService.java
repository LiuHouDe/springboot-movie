package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Layout;
import com.ispan.theater.domain.LayoutId;
import com.ispan.theater.domain.Seat;
import com.ispan.theater.repository.LayoutRepository;
import com.ispan.theater.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LayoutService {
    @Autowired
    private LayoutRepository layoutRepository;
    @Autowired
    private SeatRepository seatRepository;
    private final static int MAX_COL = 24;
    public void insertLayout(Auditorium auditorium) {//測試版 還未加上版型


        for(int i=1;i<=12;i++){
            for(int j=1;j<=19;j++){
                Integer seatid = (i-1)*24+j;
                Optional<Seat> optionalSeat = seatRepository.findById(seatid);
                if(optionalSeat.isPresent()){
                    Layout layout = new Layout();
                    LayoutId layoutId = new LayoutId();
                    Seat seat = optionalSeat.get();
                    layoutId.setAuditoriumId(auditorium.getId());
                    layoutId.setSeatId(seat.getId());
                    layout.setId(layoutId);
                    layout.setAuditorium(auditorium);
                    layout.setSeat(seat);
                    layoutRepository.save(layout);
                }
            }
        }
    }

    public List<Layout> getLayout(Auditorium auditorium){
        return layoutRepository.findByAuditorium(auditorium);
    }
}
