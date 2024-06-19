package com.ispan.theater.dao;

import java.util.List;

import org.json.JSONObject;

import com.ispan.theater.domain.User;

public interface UserDao {
	
	 List<User> find (JSONObject json);
	 
	 long count(JSONObject obj);

}
