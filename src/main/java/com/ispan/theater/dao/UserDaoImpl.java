package com.ispan.theater.dao;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.ispan.theater.domain.User;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UserDaoImpl implements UserDao {
	@PersistenceContext
	private Session session;

	public Session getSession() {
		return this.session;
	}

	
	@Override
	public long count(JSONObject obj) {

		Integer id =obj.isNull("id") ? null : obj.getInt("id");
		String userFirstname = obj.isNull("userFirstname") ? null : obj.getString("userFirstname");
		String userLastname = obj.isNull("userLastname") ? null : obj.getString("userLastname");
		String email = obj.isNull("email") ? null : obj.getString("email");
		String phone = obj.isNull("phone") ? null : obj.getString("phone");
		String registrationDateStart = obj.isNull("registrationDateStart") ? null: obj.getString("registrationDateStart");
		String registrationDateEnd = obj.isNull("registrationDateEnd") ? null : obj.getString("registrationDateEnd");
		Double consumptionStart = obj.isNull("consumptionStart") ? null : obj.getDouble("consumptionStart");
		Double consumptionEnd = obj.isNull("consumptionEnd") ? null : obj.getDouble("consumptionEnd");

		Integer userlevel = obj.isNull("userlevel") ? null : obj.getInt("userlevel");

		Integer birthMonth = obj.isNull("birthMonth") ? null : obj.getInt("birthMonth");
		String gender = obj.isNull("gender") ? null : obj.getString("gender");
		Boolean isverified = obj.isNull("isverified") ? null : obj.getBoolean("isverified");

		CriteriaBuilder criteriaBuilder = this.session.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);

//		from User
		Root<User> table = criteriaQuery.from(User.class);

//		select count(*)		
		criteriaQuery = criteriaQuery.select(criteriaBuilder.count(table));

//		where start
		List<Predicate> predicates = new ArrayList<>();
		if (id != null) {
			predicates.add(criteriaBuilder.equal(table.get("id"), id));
		}
		if (userFirstname != null && userFirstname.length() != 0) {
			predicates.add(criteriaBuilder.equal(table.get("userFirstname"), userFirstname));
		}
		if (userLastname != null && userLastname.length() != 0) {
			predicates.add(criteriaBuilder.equal(table.get("userLastname"), userLastname));
		}
		if (email != null && email.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("email"), "%" + email + "%"));
		}
		if (phone != null && phone.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("phone"), "%" + phone + "%"));
		}
		if (registrationDateStart != null && registrationDateStart.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				predicates.add(criteriaBuilder.greaterThan(table.get("registrationDate"), formatter.parse(registrationDateStart)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (registrationDateEnd != null && registrationDateEnd.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				predicates.add(criteriaBuilder.lessThan(table.get("registrationDate"), formatter.parse(registrationDateEnd)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (consumptionStart != null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("consumption"), consumptionStart));
		}
		if (consumptionEnd != null) {
			predicates.add(criteriaBuilder.lessThan(table.get("consumption"), consumptionEnd));
		}

		if (userlevel != null) {
			predicates.add(criteriaBuilder.equal(table.get("userlevel"), userlevel));
		}

		if (birthMonth != null) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, table.get("birth")),
					birthMonth));
		}

		if (gender != null && gender.length() != 0) {
			predicates.add(criteriaBuilder.equal(table.get("gender"), gender));
		}

		if (isverified != null) {
			predicates.add(criteriaBuilder.equal(table.get("isverified"), isverified));
		}

		if (predicates != null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}

		TypedQuery<Long> typedQuery = this.getSession().createQuery(criteriaQuery);

		Long result = typedQuery.getSingleResult();
		if (result != null ) {
			return result;
		} else {
			return 0;
		}

	}
	
	
	
	@Override
	public List<User> find(JSONObject obj) {

		Integer id =obj.isNull("id") ? null : obj.getInt("id");
		String userFirstname = obj.isNull("userFirstname") ? null : obj.getString("userFirstname");
		String userLastname = obj.isNull("userLastname") ? null : obj.getString("userLastname");
		String email = obj.isNull("email") ? null : obj.getString("email");
		String phone = obj.isNull("phone") ? null : obj.getString("phone");
		String registrationDateStart = obj.isNull("registrationDateStart") ? null: obj.getString("registrationDateStart");
		String registrationDateEnd = obj.isNull("registrationDateEnd") ? null : obj.getString("registrationDateEnd");
		Double consumptionStart = obj.isNull("consumptionStart") ? null : obj.getDouble("consumptionStart");
		Double consumptionEnd = obj.isNull("consumptionEnd") ? null : obj.getDouble("consumptionEnd");

		Integer userlevel = obj.isNull("userlevel") ? null : obj.getInt("userlevel");

		Integer birthMonth = obj.isNull("birthMonth") ? null : obj.getInt("birthMonth");
		String gender = obj.isNull("gender") ? null : obj.getString("gender");
		Boolean isverified = obj.isNull("isverified") ? null : obj.getBoolean("isverified");

		int start = obj.isNull("start") ? 0 : obj.getInt("start");
		int rows = obj.isNull("rows") ? 10 : obj.getInt("rows");
		String order = obj.isNull("order") ? "id" : obj.getString("order");
		boolean dir = obj.isNull("dir") ? false : obj.getBoolean("dir");

		CriteriaBuilder criteriaBuilder = this.session.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

//		from User
		Root<User> table = criteriaQuery.from(User.class);

//		where start
		List<Predicate> predicates = new ArrayList<>();
		if (id != null) {
			predicates.add(criteriaBuilder.equal(table.get("id"), id));
		}
		if (userFirstname != null && userFirstname.length() != 0) {
			predicates.add(criteriaBuilder.equal(table.get("userFirstname"), userFirstname));
		}
		if (userLastname != null && userLastname.length() != 0) {
			predicates.add(criteriaBuilder.equal(table.get("userLastname"), userLastname));
		}
		if (email != null && email.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("email"), "%" + email + "%"));
		}
		if (phone != null && phone.length() != 0) {
			predicates.add(criteriaBuilder.like(table.get("phone"), "%" + phone + "%"));
		}
		if (registrationDateStart != null && registrationDateStart.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				predicates.add(criteriaBuilder.greaterThan(table.get("registrationDate"), formatter.parse(registrationDateStart)));
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		if (registrationDateEnd != null && registrationDateEnd.length() != 0) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			try {
				predicates.add(criteriaBuilder.lessThan(table.get("registrationDate"), formatter.parse(registrationDateEnd)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (consumptionStart != null) {
			predicates.add(criteriaBuilder.greaterThan(table.get("consumption"), consumptionStart));
		}
		if (consumptionEnd != null) {
			predicates.add(criteriaBuilder.lessThan(table.get("consumption"), consumptionEnd));
		}

		if (userlevel != null) {
			predicates.add(criteriaBuilder.equal(table.get("userlevel"), userlevel));
		}

		if (birthMonth != null) {
			predicates.add(criteriaBuilder.equal(criteriaBuilder.function("MONTH", Integer.class, table.get("birth")),
					birthMonth));
		}

		if (gender != null && gender.length() != 0) {
			predicates.add(criteriaBuilder.equal(table.get("gender"), gender));
		}

		if (isverified != null) {
			predicates.add(criteriaBuilder.equal(table.get("isverified"), isverified));
		}

		if (predicates != null && !predicates.isEmpty()) {
			Predicate[] array = predicates.toArray(new Predicate[0]);
			criteriaQuery = criteriaQuery.where(array);
		}

		if (dir) {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.desc(table.get(order)));
		} else {
			criteriaQuery = criteriaQuery.orderBy(criteriaBuilder.asc(table.get(order)));
		}

		TypedQuery<User> typedQuery = this.getSession().createQuery(criteriaQuery).setFirstResult(start)
				.setMaxResults(rows);

		List<User> result = typedQuery.getResultList();
		if (result != null && !result.isEmpty()) {
			return result;
		} else {
			return null;
		}

	}
}
