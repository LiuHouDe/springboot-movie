package com.ispan.theater.service;

import com.ispan.theater.domain.AuditoriumLevel;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.Auditorium;

@SpringBootTest
public class testAuditoriumService {
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private AudoriumLevelService audoriumLevelService;
    @Autowired
    private LayoutService layoutService;

//    @Test
//    public void testAuditoriumService() {
//        AuditoriumLevel auditoriumLevel = new AuditoriumLevel();
//        auditoriumLevel.setContext("數位");
//        audoriumLevelService.insertLevel(auditoriumLevel);
//        for(int i=1;i<=3;i++){
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("layoutId", 1);
//            jsonObject.put("levelid",1);
//            jsonObject.put("auditoriumNumber",i);
//            jsonObject.put("cinemaid",3);
//            Auditorium auditorium =  auditoriumService.saveAuditorium(jsonObject);
//        }
//    }
    @Test
    public void testLayout() {
        Auditorium auditorium = auditoriumService.getAuditorium(9);
        layoutService.insertLayout(auditorium);
    }
}
