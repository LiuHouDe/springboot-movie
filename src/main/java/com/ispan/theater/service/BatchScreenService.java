package com.ispan.theater.service;

import com.ispan.theater.dto.SchduleDto;
import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.repository.AuditoriumRepository;
import com.ispan.theater.repository.CinemaRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.util.DatetimeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BatchScreenService {
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private CinemaRepository cinemaRepository;


    @Transactional
    public List<Screening> batchScreenInsert(SchduleDto request) throws Exception {
        Movie movie = movieRepository.findById(request.getMovieId()).orElseThrow(() -> new RuntimeException("Movie not found"));
        Auditorium auditorium = auditoriumRepository.findById(request.getAudotoriumId()).orElseThrow(() -> new RuntimeException("Auditorium not found"));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DatetimeConverter.parse(request.getStartDate(), "yyyy-MM-dd"));
        Date endDate = DatetimeConverter.parse(request.getEndDate(), "yyyy-MM-dd");
        List<Screening> allScreenings = new ArrayList<>();
        Calendar movieEndCalendar = Calendar.getInstance();
        movieEndCalendar.setTime(movie.getEndDate());
        movieEndCalendar.add(Calendar.DATE, 1);
        Date movieEndDatePlusOne = movieEndCalendar.getTime();
        while (!calendar.getTime().after(endDate)) {
            List<Screening> dailyScreenings = setupDailyScreenings(calendar, movie, auditorium, request.getFrequency(), request.getDailySessions(), movieEndDatePlusOne);
            allScreenings.addAll(dailyScreenings);
            calendar.add(Calendar.DATE, 1);
        }
        return allScreenings;
    }

    private List<Screening> setupDailyScreenings(Calendar date, Movie movie, Auditorium auditorium, String frequency, int sessionsPerDay, Date movieEndDatePlusOne) throws Exception {
        Calendar sessionTime = (Calendar) date.clone();
        List<Screening> screenings = new ArrayList<>();
        sessionTime.set(Calendar.HOUR_OF_DAY, 9);
        sessionTime.set(Calendar.MINUTE, 0);
        sessionTime.set(Calendar.SECOND, 0);
        int endHour = 23;
        int endHourmin = 1;
        int currentDay = sessionTime.get(Calendar.DAY_OF_YEAR);
        if (frequency.equals("僅熱門時段")) {
            if (sessionTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || sessionTime.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                // 周末全天
                sessionTime.set(Calendar.HOUR_OF_DAY, 9);
            } else {
                // 平日下午四點到晚上九點
                if (sessionTime.get(Calendar.HOUR_OF_DAY) < 16) {
                    sessionTime.set(Calendar.HOUR_OF_DAY, 16);
                }
                endHour = 21;
            }
        }
        for (int i = 0; i < sessionsPerDay; i++) {
            int hourOfDay = sessionTime.get(Calendar.HOUR_OF_DAY);
            if (hourOfDay > endHour || hourOfDay < endHourmin) {
                break; // 確保不超過指定的結束時間
            }

            if (frequency.equals("僅早午夜場")) {
                // 早上十點前 晚上九點後
                if (sessionTime.get(Calendar.HOUR_OF_DAY) < 21 && sessionTime.get(Calendar.HOUR_OF_DAY) > 9) {
                    sessionTime.set(Calendar.HOUR_OF_DAY, 21);
                }
            }
            Screening screening = new Screening();
            screening.setStartTime(sessionTime.getTime());
            sessionTime.add(Calendar.MINUTE, movie.getDuration());  // 增加電影時長

            screening.setEndTime(sessionTime.getTime());
            screening.setMovie(movie);
            screening.setAuditorium(auditorium);
            screening.setCreateDate(new Date());
            screening.setModifyDate(new Date());

            if (sessionTime.getTime().after(movieEndDatePlusOne)||(sessionTime.getTime().before(movie.getReleaseDate()))) {
                throw new Exception("場次日期超出上映範圍");
            }
            List<Integer> overlappingScreenings = screeningRepository.findOverlapScreenings(auditorium.getId(), screening.getStartTime(), screening.getEndTime());
            if (!overlappingScreenings.isEmpty()) {
                throw new Exception("場次時間地點發生重疊");
            }


            screeningRepository.save(screening);
            screenings.add(screening);
            sessionTime.add(Calendar.MINUTE, 30);  // 增加30分鐘緩衝

            if (hourOfDay > endHour || hourOfDay < endHourmin || sessionTime.get(Calendar.DAY_OF_YEAR) != currentDay) {
                break; // 確保不超過指定的結束時間，且不跨越到新的一天
            }
        }
        return screenings;
    }


}
