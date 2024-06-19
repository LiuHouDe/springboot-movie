package com.ispan.theater.dao;

import java.util.List;

import org.json.JSONObject;

import com.ispan.theater.domain.CustomerService;

public interface CustomerServiceDao {
	public List<CustomerService> find(JSONObject json);
	public long count(JSONObject json) ;

}
