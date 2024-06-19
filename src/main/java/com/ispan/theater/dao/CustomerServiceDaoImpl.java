package com.ispan.theater.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.CustomerService;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.UserRepository;
import com.ispan.theater.util.DatetimeConverter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class CustomerServiceDaoImpl implements CustomerServiceDao{
	@PersistenceContext
	private Session session;

	public Session getSession() {
		return this.session;
	}
	@Autowired
	public UserRepository ur;
	
	//避免多執行序執行時拿到同一個EntityManager autowired不是
//	private EntityManager entityManager;

	@Override
	public long count(JSONObject json) {
		Integer id =json.isNull("id") ? null : json.getInt("id");
		//使用者名字
		String user = json.isNull("user") ? null : json.getString("user");

		String text = json.isNull("text") ? null : json.getString("text");

		String category = json.isNull("category") ? null : json.getString("category");
		String email = json.isNull("userEmail") ? null : json.getString("userEmail");

		Boolean status = json.isNull("status") ? null : json.getBoolean("status");
		String createDateStart=json.isNull("createDateStart")?null:json.getString("createDateStart");
		String createDateEnd=json.isNull("createDateEnd")?null:json.getString("createDateEnd");

		
		CriteriaBuilder criteriaBuilder=this.session.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
		
		
//		from CustomerService
		Root<CustomerService> table = criteriaQuery.from(CustomerService.class);
		
		
//		select count(*)		
		criteriaQuery = criteriaQuery.select(criteriaBuilder.count(table));

		
		// Add join
				Join<CustomerService, User> userJoin = table.join("user");
//				where start
				List<Predicate> predicates = new ArrayList<>();

				if(id!=null) {
					predicates.add(criteriaBuilder.equal(table.get("id"), id));
					
				}
				
				if(text!=null && text.length()!=0) {
					predicates.add(criteriaBuilder.like(table.get("text"),"%"+text+"%"));	
				}
					
				if(category!=null && category.length()!=0) {
					predicates.add(criteriaBuilder.equal(table.get("category"), category));
				}

				if(email!=null && email.length()!=0) {
					predicates.add(criteriaBuilder.like(table.get("userEmail"),"%"+email+"%"));	
				}
				
				if(status!=null ) {
					predicates.add(criteriaBuilder.equal(table.get("status"), status));
				}
				
				if (createDateStart != null && createDateStart.length() != 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					try {
						predicates.add(criteriaBuilder.greaterThanOrEqualTo(table.get("createDate"), formatter.parse(createDateStart)));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (createDateEnd != null && createDateEnd.length() != 0) {
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					try {
						predicates.add(criteriaBuilder.lessThanOrEqualTo(table.get("createDate"), formatter.parse(createDateEnd)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// Compare jsonUser with user.user
		        if (user != null && user.length() != 0) {
		            predicates.add(criteriaBuilder.equal(userJoin.get("user"), user));
		        }
//		where end
		if (predicates != null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}
//		Order By
		
		
		TypedQuery<Long> typedQuery = this.session.createQuery(criteriaQuery);
		Long result=typedQuery.getSingleResult();
		if(result!=null) {
			return result.longValue();
		}
		return 0;
	}
	
	@Override
	public List<CustomerService> find(JSONObject json) {
		//客訴分類搜尋 狀態搜尋
		Integer id =json.isNull("id") ? null : json.getInt("id");
//使用者名字
		String user = json.isNull("user") ? null : json.getString("user");

		String text = json.isNull("text") ? null : json.getString("text");

		String category = json.isNull("category") ? null : json.getString("category");
		String email = json.isNull("userEmail") ? null : json.getString("userEmail");

		Boolean status = json.isNull("status") ? null : json.getBoolean("status");
		String createDateStart=json.isNull("createDateStart")?null:json.getString("createDateStart");
		String createDateEnd=json.isNull("createDateEnd")?null:json.getString("createDateEnd");
		//分頁排序
		int start = json.isNull("start") ? 0 : json.getInt("start");
		int rows = json.isNull("rows") ? 10 : json.getInt("rows");
		String order = json.isNull("order") ? "id" : json.getString("order");
		boolean dir = json.isNull("dir") ? true : json.getBoolean("dir");
		
		CriteriaBuilder criteriaBuilder=this.session.getCriteriaBuilder();
		CriteriaQuery<CustomerService> criteriaQuery = criteriaBuilder.createQuery(CustomerService.class);
		
		Root<CustomerService> table = criteriaQuery.from(CustomerService.class);
		
		// Add join
		Join<CustomerService, User> userJoin = table.join("user");
//		where start
		List<Predicate> predicates = new ArrayList<>();

		if(id!=null) {
			predicates.add(criteriaBuilder.equal(table.get("id"), id));
			
		}
		
		if(text!=null && text.length()!=0) {
			predicates.add(criteriaBuilder.like(table.get("text"),"%"+text+"%"));	
		}
			
		if(category!=null && category.length()!=0) {
			predicates.add(criteriaBuilder.equal(table.get("category"), category));
		}

		if(email!=null && email.length()!=0) {
			predicates.add(criteriaBuilder.like(table.get("userEmail"),"%"+email+"%"));	
		}
		
		if(status!=null ) {
			predicates.add(criteriaBuilder.equal(table.get("status"), status));
		}
		
		if (createDateStart != null && createDateStart.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(table.get("createDate"), formatter.parse(createDateStart)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (createDateEnd != null && createDateEnd.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(table.get("createDate"), formatter.parse(createDateEnd)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Compare jsonUser with user.user
        if (user != null && user.length() != 0) {
            predicates.add(criteriaBuilder.equal(userJoin.get("user"), user));
        }
//		where end
		
		
		if (predicates != null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}
		
//		Order By
		if(dir) {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(table.get(order)));
		} else {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(table.get(order)));
		}
		
		TypedQuery<CustomerService> typedQuery = this.session.createQuery(criteriaQuery)
				.setFirstResult(start)
				.setMaxResults(rows);
		
		List<CustomerService> result = typedQuery.getResultList();
		if(result!=null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}
	}
	
	
	
	

}
