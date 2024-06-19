package com.ispan.theater.dao;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.Food;
import com.ispan.theater.util.DatetimeConverter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class FoodDaoImpl implements FoodDao {
	@PersistenceContext
	private EntityManager entityManager;
//	private Session session;
//	public Session getSession() {
//		return this.session;
//	}
//	public void setSession(Session session) {
//		this.session = session;
//	}

	@Override
	public List<Food> find(JSONObject obj) {
		Integer id = obj.isNull("id") ? null : obj.getInt("id");
		String name = obj.isNull("name") ? null : obj.getString("name");
		String ename = obj.isNull("name_eng") ? null : obj.getString("name_eng");
		Double startPrice = obj.isNull("startPrice") ? null : obj.getDouble("startPrice");	
		Double endPrice = obj.isNull("endPrice") ? null : obj.getDouble("endPrice");	
		String startMake = obj.isNull("startMake") ? null : obj.getString("startMake");	
		String endMake = obj.isNull("endMake") ? null : obj.getString("endMake");	
		String startExpire = obj.isNull("startExpire") ? null : obj.getString("startExpire");	
		String endExpire = obj.isNull("endExpire") ? null : obj.getString("endExpire");	
		String startCreateDate = obj.isNull("startCreateDate") ? null : obj.getString("startCreateDate");	
		String endCreateDate = obj.isNull("endCreateDate") ? null : obj.getString("endCreateDate");	
		String startModifyDate = obj.isNull("startModifyDate") ? null : obj.getString("startModifyDate");	
		String endModifyDate = obj.isNull("endModifyDate") ? null : obj.getString("endModifyDate");	
		
		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int rows = obj.isNull("rows") ? 10 : obj.getInt("rows");
//		沒有order就取id來排，有order就已抓到的來排
		String order = obj.isNull("order") ? "id" : obj.getString("order");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");
		
//		CriteriaBuilder criteriaBuilder = this.getSession().getCriteriaBuilder();
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Food> criteriaQuery = criteriaBuilder.createQuery(Food.class);
		
		//from Food
		Root<Food> table = criteriaQuery.from(Food.class);
		
		//where start
		List<Predicate> predicates = new ArrayList<>();
		if (id!=null) {
			Predicate p = criteriaBuilder.equal(table.get("id"), id);
			predicates.add(p);
		}
		if (name!=null && name.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("name"), "%"+name+"%"));
		}
		if (ename!=null && ename.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("name_eng"), "%"+ename+"%"));
		}
		if (startPrice!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startPrice"), startPrice));
		}
		if (endPrice!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endPrice"), endPrice));
		}
		if (startMake!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startMake"), startMake));
		}
		if (endMake!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endMake"), endMake));
		}
		if (startExpire!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startExpire"), startExpire));
		}
		if (endExpire!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endExpire"), endExpire));
		}
		if (startCreateDate!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startCreateDate"), startCreateDate));
		}
		if (endCreateDate!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endCreateDate"), endCreateDate));
		}
		if (startModifyDate!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("startModifyDate"), startModifyDate));
		}
		if (endModifyDate!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("endModifyDate"), endModifyDate));
		}
		
		if(predicates!=null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}
		//where end
		
		//order by
		if (dir) {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(table.get(order)));
		}else {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(table.get(order)));
		}
		
//		TypedQuery<Food> typedQuery = this.getSession().createQuery(criteriaQuery)
		TypedQuery<Food> typedQuery = entityManager.createQuery(criteriaQuery)
				.setFirstResult(start)
				.setMaxResults(rows);
		
		List<Food> result = typedQuery.getResultList();
		if(result!=null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}
		
	}
	
	@Override
	public long count(JSONObject obj) {
		Integer id = obj.isNull("id") ? null : obj.getInt("id");
		String name = obj.isNull("name") ? null : obj.getString("name");
		String e_name = obj.isNull("name_eng") ? null : obj.getString("name_eng");
		Double startPrice = obj.isNull("startPrice") ? null : obj.getDouble("startPrice") ;
		Double endPrice = obj.isNull("endPrice") ? null : obj.getDouble("endPrice") ;
		Integer startCount = obj.isNull("startCount") ? null : obj.getInt("startCount");
		Integer endCount = obj.isNull("endCount") ? null : obj.getInt("endCount");
		String description = obj.isNull("description") ? null : obj.getString("description");
		String foodCategory_code = obj.isNull("foodCategory_code") ? null : obj.getString("foodCategory_code");
		String startMake = obj.isNull("startMake") ? null : obj.getString("startMake");
		String endMake = obj.isNull("endMake") ? null : obj.getString("endMake");
		Integer startExpire = obj.isNull("startExpire") ? null : obj.getInt("startExpire");
		Integer endExpire = obj.isNull("endExpire") ? null : obj.getInt("endExpire");
		String c_date = obj.isNull("createDate") ? null : obj.getString("createDate");
		String m_date = obj.isNull("modifyDate") ? null : obj.getString("modifyDate");

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

//		from product
		Root<Food> table = criteriaQuery.from(Food.class);

//		select count(*)
		criteriaQuery = criteriaQuery.select(criteriaBuilder.count(table));
		
//		where start
		List<Predicate> predicates = new ArrayList<>();
		if(id!=null) {
			Predicate p = criteriaBuilder.equal(table.get("id"), id);
			predicates.add(p);
		}
		if(name!=null && name.length()!=0) {
			predicates.add(criteriaBuilder.like(table.get("name"), "%"+name+"%"));
		}
		if (e_name!=null && e_name.length()!=0) {
			predicates.add(criteriaBuilder.like(table.get("name_eng"), "%"+e_name+"%"));
		}
		if(startPrice!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("price"), startPrice));
		}
		if(endPrice!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("price"), endPrice));
		}
//		Integer count = obj.isNull("count") ? null : obj.getInt("count");
		if (startCount!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("count"), startCount));
		}
		if (endCount!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("count"), endCount));
		}
//		String description = obj.isNull("description") ? null : obj.getString("description");
		if (description!=null) {
			predicates.add(criteriaBuilder.like(table.get("description"), "%"+description+"%"));
		}
//		String foodCategory_code = obj.isNull("foodCategory_code") ? null : obj.getString("foodCategory_code");
		if (foodCategory_code!=null) {
			predicates.add(criteriaBuilder.like(table.get("foodCategory_code"), "%"+foodCategory_code+"%"));
		}
		if(startMake!=null && startMake.length()!=0) {
			java.util.Date temp = DatetimeConverter.parse(startMake, "yyyy-MM-dd");
			predicates.add(criteriaBuilder.greaterThan(table.get("make"), temp));
		}
		if(endMake!=null && endMake.length()!=0) {
			java.util.Date temp = DatetimeConverter.parse(endMake, "yyyy-MM-dd");
			predicates.add(criteriaBuilder.lessThan(table.get("make"), temp));
		}
		if(startExpire!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("expired"), startExpire));
		}
		if(endExpire!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("expire"), endExpire));
		}
//		String c_date = obj.isNull("createDate") ? null : obj.getString("createDate");
		if(c_date!=null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("createDate"), c_date));
		}
		if(m_date!=null) {
			predicates.add(criteriaBuilder.lessThan(table.get("modifyDate"), m_date));
		}
//		String m_date = obj.isNull("modifyDate") ? null : obj.getString("modifyDate");
		
		if(predicates!=null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}
//		where end
		
		TypedQuery<Long> typedQuery = entityManager.createQuery(criteriaQuery);
		Long result = typedQuery.getSingleResult();
		if(result!=null) {
			
			return result.longValue();
			
		} else {
			return 0;
		}
//		return result != null ? result : 0;
	}

}
