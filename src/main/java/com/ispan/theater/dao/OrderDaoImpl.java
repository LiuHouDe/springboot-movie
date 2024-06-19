package com.ispan.theater.dao;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Repository
@Transactional(readOnly = true)
public class OrderDaoImpl implements OrderDao {

    @PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public String multiConditionFindMovie(Map<String, String> requestParams) {
		AtomicReference<String> queryString = new AtomicReference<>("select ROW_NUMBER() over(order by o.create_date desc) as 'no',o.order_id,u.email,m.name,SUBSTRING(convert(varchar(19),o.create_date),1,19) as create_date,o.order_amount,o.supplier,o.order_status FROM \"Order\" as o join movie as m on o.movie_id=m.movie_id join \"user\" as u on u.user_id=o.user_id WHERE 1=1");
		AtomicReference<String> queryCountString =new AtomicReference<>("select count(o.order_id) FROM \"Order\" as o join movie as m on o.movie_id=m.movie_id join \"user\" as u on u.user_id=o.user_id WHERE 1=1");
        requestParams.entrySet().forEach(entry->{
        	if(!"".equals(entry.getValue())){
            	switch(entry.getKey()) {
            	case "email":
            		queryString.set(queryString.get()+" and "+"u.email"+" like :"+entry.getKey());
            		queryCountString.set(queryCountString.get()+" and "+"u.email"+" like :"+entry.getKey());
            		break;
            	case "supplier":
            		queryString.set(queryString.get()+" and "+"o.supplier"+" = :"+entry.getKey());
            		queryCountString.set(queryCountString.get()+" and "+"o.supplier"+" = :"+entry.getKey());
            		break;           	
            	case "dateStart":
            		queryString.set(queryString.get()+" and "+"substring(convert(varchar,o.create_date),1,10) >= :"+entry.getKey());
            		queryCountString.set(queryCountString.get()+" and "+"substring(convert(varchar,o.create_date),1,10) >= :"+entry.getKey());
            		break;
            	case "dateEnd":
            		queryString.set(queryString.get()+" and "+"substring(convert(varchar,o.create_date),1,10) <= :"+entry.getKey());
            		queryCountString.set(queryCountString.get()+" and "+"substring(convert(varchar,o.create_date),1,10) <= :"+entry.getKey());
            		break;  
            	}
        	}
        });
        queryString.set(queryString.get()+" order by o.create_date desc");
		Query query = entityManager.createNativeQuery(queryString.get(), Map.class);
		Query queryCount = entityManager.createNativeQuery(queryCountString.get());
        requestParams.entrySet().forEach(entry->{
        	if(!"".equals(entry.getValue())&&!"page".equals(entry.getKey())){
        		if("email".equals(entry.getKey())) {
        			query.setParameter(entry.getKey(),"%"+entry.getValue()+"%");
                	queryCount.setParameter(entry.getKey(),"%"+entry.getValue()+"%");
        		}else {
                	query.setParameter(entry.getKey(), entry.getValue());
                	queryCount.setParameter(entry.getKey(), entry.getValue());
        		}
        	}
        });
        Long total=((Number)queryCount.getSingleResult()).longValue();
		Long pages = 1L;
		if (total % 10 == 0 && total / 10 != 0) {
			pages = total / 10;
		} else if (total % 10 != 0 && total / 10 != 0) {
			pages = (total / 10) + 1;
		}
        query.setFirstResult((Integer.parseInt(requestParams.get("page"))-1)*10);
        query.setMaxResults(10);
		return new JSONObject().put("orders", query.getResultList()).put("pages",pages).toString();
	}

}
