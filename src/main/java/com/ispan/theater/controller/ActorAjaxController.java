package com.ispan.theater.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.Actor;
import com.ispan.theater.domain.MovieAct;
import com.ispan.theater.service.ActorService;

@RestController
@RequestMapping
@CrossOrigin
public class ActorAjaxController {
    @Autowired
    private ActorService actorService;

    @GetMapping("/backstage/act")
    public String actList() {
        System.out.println("TESTTESTTEST");
        List<Actor> actors = actorService.findAll();
        JSONArray jsonArray = new JSONArray();
        for (Actor actor : actors) {
            jsonArray.put(actor);
        }
        return jsonArray.toString();
    }
    @PostMapping("/backstage/act")
    public String actInsert(@RequestBody String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject response = new JSONObject();
        Actor actor = actorService.findByName(jsonObject.getString("name"));
        if (actor == null) {
            actorService.insert(jsonString);
            response.put("msg", "更新成功");
            response.put("succeed","succeed");
        }
        else{
            response.put("msg", "更新失敗");
            response.put("fail","fail");
        }
        return response.toString();
    }
    @PostMapping("backstage/actor")
    public ResponseEntity<?> backstageActor(@RequestBody Actor actor) {
        if(actor != null) {
            boolean exists = actorService.findByName(actor.getName()) != null;
            if (!exists) {
                Actor actor1 = actorService.insertActor(actor);
                if(actor1 != null) {
                    String uri = "http://localhost:8082/backstage/actor/" + actor1.getId();
                    return ResponseEntity.created(URI.create(uri))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(actor1);
                }
            }
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/backstage/actor")
        public String actorListLike(@RequestParam(name = "namelike")String namelike) {
        List<Map<String,Object>> result = actorService.findByNameLike(namelike);
        JSONArray jsonArray = new JSONArray();
        JSONObject response = new JSONObject();
        for (Map<String,Object> actorid : result) {
            jsonArray.put(actorid);
        }
        response.put("idlist", jsonArray);
        return response.toString();
    }
    @PostMapping("/backstage/movieact/{movieid}")
    public String movieAct(@PathVariable("movieid") String movieid, @RequestBody String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray array = jsonObject.getJSONArray("actorlist");
        JSONObject response = new JSONObject();
        List<Integer> actorids = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject item = array.getJSONObject(i);
            int actorId = item.getInt("actor_id");
            actorids.add(actorId);
        }
        List<MovieAct> list = actorService.insertActIntoMovie(Integer.parseInt(movieid), actorids);
        if(list != null) {
            response.put("message", "新增成功");
            response.put("success","success");
        }
        else{
            response.put("message","新增失敗");
            response.put("fail","fail");
        }
        return response.toString();
    }
}
