package com.ispan.theater.service;

import com.ispan.theater.domain.Auditorium;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Rated;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.repository.AuditoriumRepository;
import com.ispan.theater.repository.MovieRepository;
import com.ispan.theater.repository.ScreeningRepository;
import com.ispan.theater.util.DatetimeConverter;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ScreeningService {
    @Autowired
    private ScreeningRepository screeningRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;

    public JSONObject screenToJSON(Screening screening) {
        JSONObject obj = new JSONObject();
        obj.put("Screening_id", screening.getId());
        obj.put("Movie_id", screening.getMovie());
        obj.put("Auditorium_id", screening.getAuditorium());
        obj.put("Start_time", screening.getStartTime());
        obj.put("End_time", screening.getEndTime());
        return obj;
    }

    public Screening createScreening(Movie movie, JSONObject jsonObject) throws Exception {
        String startTime = jsonObject.getString("startTime");
        String endTime = jsonObject.getString("endTime");
        Integer auditoriumId = jsonObject.getInt("auditoriumId");
        Auditorium auditorium = auditoriumRepository.findById(auditoriumId).orElse(null);
        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setCreateDate(new Date());
        screening.setModifyDate(new Date());
        screening.setStartTime(DatetimeConverter.parse(startTime, "yyyy-MM-dd HH:mm"));
        screening.setEndTime(DatetimeConverter.parse(endTime, "yyyy-MM-dd HH:mm"));
        screening.setAuditorium(auditorium);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movie.getEndDate());
        calendar.add(Calendar.DATE, 1);
        Date movieEndDatePlusOne = calendar.getTime();
        if (screening.getStartTime().before(movie.getReleaseDate()) || screening.getEndTime().after(movieEndDatePlusOne)) {
            throw new Exception("場次時間不在上映範圍內");
        }

        if(!screeningRepository.findOverlapScreenings(auditoriumId, screening.getStartTime(), screening.getEndTime()).isEmpty()) {
            throw new Exception("該時段此影廳已有場次存在");
        }
        return screeningRepository.save(screening);
    }

    public Screening findByScreening(Integer screeningId) {
        return screeningRepository.findById(screeningId).orElse(null);
    }

    public Page<Screening> findScreenings(JSONObject jsonObject) {
        Integer movieId = jsonObject.isNull("movieid") ? null : jsonObject.getInt("movieid");
        String name = jsonObject.isNull("name") ? null : jsonObject.getString("name");
        String startTimeMin = jsonObject.isNull("startTimeMin") ? null : jsonObject.getString("startTimeMin");
        String endTimeMin = jsonObject.isNull("endTimeMin") ? null : jsonObject.getString("endTimeMin");
        String startTimeMax = jsonObject.isNull("startTimeMax") ? null : jsonObject.getString("startTimeMax");
        String endTimeMax = jsonObject.isNull("endTimeMax") ? null : jsonObject.getString("endTimeMax");
        Integer auditorium_id = jsonObject.isNull("auditoriumId") ? null : jsonObject.getInt("auditoriumId");
        int start = jsonObject.isNull("start") ? 0 : jsonObject.getInt("start");
        int rows = jsonObject.isNull("rows") ? 10 : jsonObject.getInt("rows");
        String order = jsonObject.isNull("order") ? "startTime" : jsonObject.getString("order");
        boolean dir = !jsonObject.isNull("dir") && jsonObject.getBoolean("dir");
        Pageable pageable;

        //order sort
        if (dir) {
            pageable = PageRequest.of(start, rows, Sort.Direction.DESC, order);
        } else {
            pageable = PageRequest.of(start, rows, Sort.Direction.ASC, order);
        }
        Specification<Screening> spec = (root, query, builder) -> {
            //where
            List<Predicate> predicates = new ArrayList<>();
            Join<Screening, Movie> movieJoin = root.join("movie");
            if (movieId == null) {
                if (name != null && !name.isEmpty()) {
                    Pattern pattern = Pattern.compile("[\\u4E00-\\u9FA5]+");
                    Matcher matcher = pattern.matcher(name);
                    if (matcher.find()) {
                        System.out.println("中文");
                        predicates.add(builder.like(movieJoin.get("name"), "%" + name + "%"));
                    } else {
                        System.out.println("英文");
                        predicates.add(builder.like(movieJoin.get("name_eng"), "%" + name + "%"));
                    }
                }
            }
            else {
                Movie movie = movieRepository.findById(movieId).orElse(null);
                if (movie != null) {
                    predicates.add(builder.equal(root.get("movie"), movie));
                } else {
                    return null;
                }
            }
            if (startTimeMin != null && !startTimeMin.isEmpty()) {
                Date releaseParse = DatetimeConverter.parse(startTimeMin, "yyyy-MM-dd HH:mm");
                predicates.add(builder.greaterThanOrEqualTo(root.get("startTime"), releaseParse));
            }
            if (startTimeMax != null && !startTimeMax.isEmpty()) {
                Date endParse = DatetimeConverter.parse(startTimeMax, "yyyy-MM-dd HH:mm");
                predicates.add(builder.lessThanOrEqualTo(root.get("startTime"), endParse));
            }
            if (endTimeMin != null && !endTimeMin.isEmpty()) {
                Date releaseParse = DatetimeConverter.parse(endTimeMin, "yyyy-MM-dd HH:mm");
                predicates.add(builder.greaterThanOrEqualTo(root.get("endTime"), releaseParse));
            }
            if (endTimeMax != null && !endTimeMax.isEmpty()) {
                Date endParse = DatetimeConverter.parse(endTimeMax, "yyyy-MM-dd HH:mm");
                predicates.add(builder.lessThanOrEqualTo(root.get("endTime"), endParse));
            }

            if (auditorium_id != null) {
                Optional<Auditorium> item = auditoriumRepository.findById(auditorium_id);
                item.ifPresent(auditorium -> predicates.add(builder.equal(root.get("auditorium"), auditorium)));

            }
            return builder.and(predicates.toArray(new Predicate[0]));
        };
        return screeningRepository.findAll(spec, pageable);
    }
    public List<Screening> findScreeningsByMovieId(Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        List<Screening> screenings = new ArrayList<>();
        if (movie != null) {
            screenings = screeningRepository.findByMovie(movie);
        }
        return screenings;
    }

    public void deleteScreening(Integer screeningId) {
        Screening screening = screeningRepository.findById(screeningId).orElse(null);
        if (screening != null) {
            screeningRepository.delete(screening);
        }
    }
    public List<Map<String,Object>> findScreeningByMovieCinema(Integer cinemaId, Integer movieId){
        return screeningRepository.findScreeningByMovieCinema(cinemaId,movieId);
    }
    public List<Map<String,Object>> findScreeningByCinema(Integer cinemaId){
        return screeningRepository.findScreeningByCinema(cinemaId);
    }
    public List<Map<String, Object>> getScreeningsByMovieIdAuditoriumId(Integer movieId, Integer auditoriumId) {
        return screeningRepository.findScreeningsByMovieAuditoium(movieId, auditoriumId);
    }

    public Integer jsonToScreen(JSONObject jsonScreen) throws Exception {
        Integer screeningId = jsonScreen.isNull("screeningId") ? 0 : jsonScreen.getInt("screeningId");
        Integer movieId = jsonScreen.isNull("movieid") ? null : jsonScreen.getInt("movieid");
        Integer auditoriumId = jsonScreen.isNull("auditoriumId") ? null : jsonScreen.getInt("auditoriumId");
        String startTime = jsonScreen.isNull("start") ? null : jsonScreen.getString("start");
        String endTime = jsonScreen.isNull("end") ? null : jsonScreen.getString("end");
        System.out.println("AUD"+auditoriumId);
        if (movieId == null || auditoriumId == null || startTime == null || endTime == null) {
            return null;
        }
        if (screeningId!=0) {
            Screening screening = screeningRepository.findById(screeningId).get();
            screening.setStartTime(DatetimeConverter.parse(startTime, "yyyy-MM-dd HH:mm"));
            screening.setEndTime(DatetimeConverter.parse(endTime, "yyyy-MM-dd HH:mm"));
            screening.setAuditorium(auditoriumRepository.findById(auditoriumId).get());
            screening.setModifyDate(new Date());
            Screening insert = screeningRepository.save(screening);
            return insert.getId();
        } else {
            Screening screening = new Screening();
            Movie movie = movieRepository.findById(movieId).orElse(null);
            screening.setStartTime(DatetimeConverter.parse(startTime, "yyyy-MM-dd HH:mm"));
            screening.setEndTime(DatetimeConverter.parse(endTime, "yyyy-MM-dd HH:mm"));
            screening.setMovie(movie);
            screening.setAuditorium(auditoriumRepository.findById(auditoriumId).orElse(null));
            screening.setCreateDate(new Date());
            screening.setModifyDate(new Date());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(movie.getEndDate());
            calendar.add(Calendar.DATE, 1);
            Date movieEndDatePlusOne = calendar.getTime();
            if (screening.getStartTime().before(movie.getReleaseDate()) || screening.getEndTime().after(movieEndDatePlusOne)) {
                throw new Exception("場次時間不在上映範圍內");
            }

            if(!screeningRepository.findOverlapScreenings(auditoriumId, screening.getStartTime(), screening.getEndTime()).isEmpty()) {
                throw new Exception("該時段此影廳已有場次存在");
            }
            Screening insert = screeningRepository.save(screening);
            return insert.getId();
        }
    }
    public List<Map<String, Object>> getScreeningsByAuditoriumId(Integer auditoriumId) {
        return screeningRepository.findScreeningsByAuditorium(auditoriumId);
    }
}
