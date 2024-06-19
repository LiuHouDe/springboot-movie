package com.ispan.theater.service;

import com.ispan.theater.domain.Actor;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.MovieAct;
import com.ispan.theater.domain.MovieActId;
import com.ispan.theater.repository.ActorRepository;
import com.ispan.theater.repository.MovieActRepository;
import com.ispan.theater.repository.MovieRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ActorService {
    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private MovieActRepository movieActRepository;
    @Autowired
    private MovieRepository movieRepository;
    public Actor findById(Integer id) {
        return actorRepository.findById(id).get();
    }
    public void insert(String json) {
        Actor actor = new Actor();
        JSONObject obj = new JSONObject(json);
        actor.setName(obj.getString("name"));
        actor.setPhoto(obj.getString("photo"));
        actorRepository.save(actor);
    }
    public Actor insertActor (Actor actor) {
        return actorRepository.save(actor);
    }
    public void delete(Integer id) {
        actorRepository.deleteById(id);
    }
    public List<Actor> findAll() {
        return actorRepository.findAll();
    }
    public List<Map<String,Object>> findByNameLike(String name) {
        return actorRepository.searchByNameUsingFullText(name);
    }
    public Actor findByName(String name) { return actorRepository.findByName(name);};
    public Actor updateActorName(Integer id){
        Actor actor = actorRepository.findById(id).get();
        actor.setName(actor.getName());
        return actor;
    }
    public void insertPhoto(Integer id,Byte[] photo){
        Actor actor = actorRepository.findById(id).get();

    }
    public List<MovieAct> insertActIntoMovie(Integer movieId,List<Integer> actorIds){
        Movie movie = movieRepository.findById(movieId).get();
        List<MovieAct> result = new ArrayList<MovieAct>();
        for(Integer actorId : actorIds){
            Actor actor = actorRepository.findById(actorId).orElse(null);
            if(actor != null){
                MovieActId movieActId = new MovieActId(movieId,actorId);
                MovieAct movieAct = new MovieAct(movieActId,actor,movie);
                movieActRepository.save(movieAct);
                result.add(movieAct);
            }
        }
        return result;
    }

}
