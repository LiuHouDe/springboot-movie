package com.ispan.theater.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ispan.theater.domain.CustomerService;
import com.ispan.theater.domain.Employee;
import com.ispan.theater.domain.Movie;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.CustomerServiceRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
@Transactional
public class CustomerServiceService {

	@Autowired
	private CustomerServiceRepository customerServiceRepository;
	@Autowired
	private UserService userService;

	// @Autowired
	// private JsonWebTokenUtility jsonWebTokenUtility;

	// 新增
	// 從token取得使用者id
	
	public List<CustomerService> find(JSONObject obj) {
		return customerServiceRepository.find(obj);
	}
	public long countCustService(JSONObject obj) {
		return customerServiceRepository.count(obj);
	}
	
//	有登入
	public CustomerService insertCustomerService(JSONObject json) {
		Integer userId = json.isNull("userid") ? null : json.getInt("userid");
		
		String text = json.isNull("text") ? null : json.getString("text");
		String category = json.isNull("category") ? null : json.getString("category");
		String userEmail = json.isNull("userEmail") ? null : json.getString("userEmail");

		CustomerService customerService = new CustomerService();

		if (userId != null && userId != 0) {
			customerService.setUser(userService.getUserById(userId));

		}

		customerService.setText(text);
		customerService.setCategory(category);
		customerService.setUserEmail(userEmail);
		customerService.setStatus(false);
		// customerService.setAgentEmp(null);
		customerService.setCreateDate(new Date());
		customerService.setHandleDate(new Date());

		return customerServiceRepository.save(customerService);

	}

//	沒登入
//	public CustomerService insertCustomerServiceNoUser(JSONObject json ) {
//
//		String text = json.isNull("text") ? null : json.getString("text");
//		String category = json.isNull("category") ? null : json.getString("category");
//		String userEmail = json.isNull("userEmail") ? null : json.getString("userEmail");
//
//		CustomerService customerService = new CustomerService();
//
//		
//
//		customerService.setText(text);
//		customerService.setCategory(category);
//		customerService.setUserEmail(userEmail);
//		customerService.setStatus("false");
//		// customerService.setAgentEmp(null);
//		customerService.setCreateDate(new Date());
//		customerService.setHandleDate(new Date());
//
//		return customerServiceRepository.save(customerService);
//
//	}
	// 客人登入修改
//	public CustomerService updateCustomerService(JSONObject json) {
//
//		Integer custserviceId = json.isNull("custserviceId")?null: json.getInt("custserviceId");
//		String text = json.isNull("text") ? null : json.getString("text");
//		String category = json.isNull("category") ? null : json.getString("category");
//		String userEmail = json.isNull("userEmail") ? null : json.getString("userEmail");
//		
//		
//		
//		if (custserviceId != null) {
//			Optional<CustomerService> optional = customerServiceRepository.findById(custserviceId);
//			if (optional.isPresent()) {
//				
//				CustomerService customerService = optional.get();
//				
//				
//				customerService.setHandleDate(new Date());
//				customerService.setText(text);
//				customerService.setCategory(category);
//				customerService.setUserEmail(userEmail);
//
//				return customerServiceRepository.save(customerService);
//			}
//		}
//
//		return null;
//	}

	public boolean existById(Integer id) {
		if (id != null) {
			return customerServiceRepository.existsById(id);
		}
		return false;
	}
	
	
	

	// 管理員修改
	public CustomerService updateCustomerServiceByEmp(JSONObject json) {
//		Integer userId = json.isNull("userid") ? null : json.getInt("userid");
		Integer custserviceId = json.isNull("custserviceId") ? null : json.getInt("custserviceId");
		System.out.println("test");
		String status = json.isNull("status") ? "false" : json.getString("status");
		System.out.println("test"+status);

		if (custserviceId != null) {
			Optional<CustomerService> optional = customerServiceRepository.findById(custserviceId);
			if (optional.isPresent()) {

		
					CustomerService customerService = optional.get();
					customerService.setHandleDate(new Date());
					customerService.setStatus(true);
					return customerServiceRepository.save(customerService);
				}

			}
		
		return null;
	}
	
	
	
	public CustomerService findById(Integer id) {
		if (id != null) {
			Optional<CustomerService> optional = customerServiceRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}
	
	public boolean deleteCSById(Integer id) {
		if (id != null) {
			Optional<CustomerService> optional = customerServiceRepository.findById(id);
			if (optional.isPresent()) {
				customerServiceRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}
	
//	查詢多筆 沒用到DAO了
	
	public Page<CustomerService> findBySearch(JSONObject json) {
		Integer id =json.isNull("id") ? null : json.getInt("id");
		//使用者姓氏
		String userFirstname = json.isNull("userFirstname") ? null : json.getString("userFirstname");
		//使用者名字
		String userLastname = json.isNull("userLastname") ? null : json.getString("userLastname");


		String text = json.isNull("text") ? null : json.getString("text");

		String category = json.isNull("category") ? null : json.getString("category");
		String email = json.isNull("userEmail") ? null : json.getString("userEmail");

		Boolean status = json.isNull("status") ? null : json.getBoolean("status");
		String createDateStart=json.isNull("createDateStart")?null:json.getString("createDateStart");
		String createDateEnd=json.isNull("createDateEnd")?null:json.getString("createDateEnd");
		String handleDateStart=json.isNull("handleDateStart")?null:json.getString("handleDateStart");
		String handleDateEnd=json.isNull("handleDateEnd")?null:json.getString("handleDateEnd");
		
		
		
		//分頁排序
		int start = json.isNull("start") ? 0 : json.getInt("start");
		int rows = json.isNull("rows") ? 10 : json.getInt("rows");
		String order = json.isNull("order") ? "id" : json.getString("order");
		boolean dir = !json.isNull("dir") && json.getBoolean("dir");
		
        Pageable pageable;
        if (dir) {
            pageable = PageRequest.of(start, rows, Sort.Direction.DESC, order);
        } else {
            pageable = PageRequest.of(start, rows, Sort.Direction.ASC, order);
        }
		
		
        Specification<CustomerService> spec = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
    		Join<CustomerService, User> userJoin = root.join("user");

    		
    		if(id!=null) {
    			predicates.add(builder.equal(root.get("id"), id));
    			
    		}
    		
    		
			if(userFirstname!=null && !userFirstname.isEmpty()) {					
					System.out.println("姓");
					predicates.add(builder.like(userJoin.get("userFirstname"), "%" + userFirstname + "%"));               
			}
			
			if(userLastname!=null && !userLastname.isEmpty()) {					
				System.out.println("姓");
				predicates.add(builder.like(userJoin.get("userLastname"), "%" + userLastname + "%"));               
		}
			
			
    		
    		if(text!=null && text.length()!=0) {
    			predicates.add(builder.like(root.get("text"),"%"+text+"%"));	
    		}
    			
    		if(category!=null && category.length()!=0) {
    			predicates.add(builder.equal(root.get("category"), category));
    		}

    		if(email!=null && email.length()!=0) {
    			predicates.add(builder.like(root.get("userEmail"),"%"+email+"%"));	
    		}
    		
    		if(status!=null ) {
    			predicates.add(builder.equal(root.get("status"), status));
    		}
//    新增時間
    		if (createDateStart != null && createDateStart.length() != 0) {
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			try {
    				predicates.add(builder.greaterThanOrEqualTo(root.get("createDate"), formatter.parse(createDateStart)));
    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    		}
    		if (createDateEnd != null && createDateEnd.length() != 0) {
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			try {
    				predicates.add(builder.lessThanOrEqualTo(root.get("createDate"), formatter.parse(createDateEnd)));
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
//    修改時間
    		if (handleDateStart != null && handleDateStart.length() != 0) {
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			try {
    				predicates.add(builder.greaterThanOrEqualTo(root.get("handleDate"), formatter.parse(handleDateStart)));
    			} catch (Exception e) {
    				e.printStackTrace();
    			}

    		}
    		if (handleDateEnd != null && handleDateEnd.length() != 0) {
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			try {
    				predicates.add(builder.lessThanOrEqualTo(root.get("handleDate"), formatter.parse(handleDateEnd)));
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
            return builder.and(predicates.toArray(new Predicate[0]));   
        };
        return customerServiceRepository.findAll(spec,pageable);	
	}
	public JSONObject csToJSON(CustomerService custService) {
        JSONObject obj = new JSONObject()
        .put("id", custService.getId())
		.put("user",custService.getUser().getUserFirstname()+custService.getUser().getUserLastname())
		.put("text", custService.getText())
		.put("category",custService.getCategory())
		.put("userEmail",custService.getUserEmail())
		.put("status",custService.getStatus())
		.put("createDate", custService.getCreateDate())
		.put("HandleDate",custService.getHandleDate());
        return obj;
    }
}
