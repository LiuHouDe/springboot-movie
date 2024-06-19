package com.ispan.theater.dao;

import com.ispan.theater.domain.Movie;
import org.json.JSONObject;

import java.util.List;

public interface MovieDao {

    List<Movie> multiConditionFindMovie(JSONObject jsonObject);
}
