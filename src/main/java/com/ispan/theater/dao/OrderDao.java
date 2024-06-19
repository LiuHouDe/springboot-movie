package com.ispan.theater.dao;

import java.util.Map;

public interface OrderDao {

    String multiConditionFindMovie(Map<String, String> requestParams);

}
