package com.ispan.theater.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ispan.theater.domain.CustomerService;
import com.ispan.theater.domain.Screening;
import com.ispan.theater.repository.CustomerServiceRepository;
import com.ispan.theater.service.CustomerServiceService;
import com.ispan.theater.util.EmailSenderComponent;
import com.ispan.theater.util.JsonWebTokenUtility;

@RestController
@CrossOrigin
public class CustomerServiceAjaxController {
    @Autowired
    private CustomerServiceService customerServiceService;
    @Autowired
    CustomerServiceRepository csr;

    @Autowired
    private JsonWebTokenUtility jwtu;
    
    @Autowired
	EmailSenderComponent emailSenderComponent;

//    客服系統回復信
    @GetMapping("/back/custService/{email}")
	public  ResponseEntity<?> sendVeriftEmail (@PathVariable(name = "email") String email){
		if(email!=null && !email.isEmpty()) {
			emailSenderComponent.sendCSEmail(email);
			return ResponseEntity.ok().body(null);
			
		}
		
		return ResponseEntity.notFound().build();
	}
    
    
    
    
//    @Value("${site.path}")
//     private String path;

    // String
    @PostMapping("/customerServices")
    public ResponseEntity<?> createCustomerService(@RequestBody String jsonObject,
    		@RequestHeader("Authorization") String token) {

    	
    	if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 移除 "Bearer " 前綴
            System.out.println(token);
            
        // 取得USERID
    
            // 解碼TOKEN
            String authToken = jwtu.validateToken(token);
            System.out.println(authToken);
            if (authToken != null) {
                // 解碼TOKEN
                JSONObject obj = new JSONObject(authToken);
                Integer userId = obj.getInt("userid");
                System.out.println(userId);

                // 新增

                JSONObject json = new JSONObject(jsonObject);
                json.put("userid", userId);

                if (userId != null) {

                    CustomerService createdCustomerService = customerServiceService.insertCustomerService(json);
                    if (createdCustomerService != null) {
                        // endpoint
                        // @Value("${site.path}")
                        // private String path;

                        String uri = "http://localhost:8080/customerServices/" + createdCustomerService.getId();
                        return ResponseEntity.created(URI.create(uri))
                        		.contentType(MediaType.APPLICATION_JSON)
                                .body(createdCustomerService);

                    }
                }
            
                     
            }else {
            	// 錯誤的TOKEN
	            System.out.println("Ttoken遺失");
			        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ttoken遺失");

            }
            	
            

        }

        return ResponseEntity.noContent().build();

    }

    @PostMapping("/back/custService/find")
    public String findCustomerService2(@RequestBody String json) {
    	 JSONObject jsonObject = new JSONObject(json);
 		System.out.println("要查詢的屬性: " + jsonObject.toString());

         JSONArray array = new JSONArray();
         Page<CustomerService> result = customerServiceService.findBySearch(jsonObject);
         List<CustomerService> customerServices = result.getContent();
         if (!customerServices.isEmpty()) {
             for (CustomerService customerService : customerServices) {
                 JSONObject csobj = customerServiceService.csToJSON(customerService);
                 array.put(csobj);
             }
         }
         jsonObject.put("list", array);
         jsonObject.put("count", result.getTotalElements());
         return jsonObject.toString();
     }
    
    
    
    
    

//    查詢多筆
    @GetMapping("/back/custService")
    public String findCustomerService(@RequestParam Map<String ,String> param) {
		JSONObject obj= new JSONObject(param);
		
		
		System.out.println("要查詢的屬性: " + obj.toString());
		List<CustomerService> custServices = customerServiceService.find(obj);
		long total =customerServiceService.countCustService(obj);
		JSONArray array =new JSONArray();

		 JSONObject result = new JSONObject();

		if(custServices!=null && !custServices.isEmpty()) {
			for(CustomerService custService :custServices) {
						
				JSONObject item=new JSONObject()
					.put("id", custService.getId())
					.put("user",custService.getUser().getUserFirstname()+custService.getUser().getUserLastname())
					.put("text", custService.getText())
					.put("category",custService.getCategory())
					.put("userEmail",custService.getUserEmail())
					.put("status",custService.getStatus())
					.put("createDate", custService.getCreateDate())
					.put("HandleDate",custService.getHandleDate());
				array.put(item);
			}
			result.put("list", array);
		}
		result.put("count",total );
		
		return result.toString();

		
    }
    //改設定 不用船json
    @PutMapping("/back/custService/update/{csid}")
    public ResponseEntity<?>  putMethodName(@PathVariable(name="csid") Integer custserviceId) {
    	JSONObject obj= new JSONObject()
    			.put("custserviceId", custserviceId);
		CustomerService customerService = customerServiceService.updateCustomerServiceByEmp(obj);
		if(customerService!=null) {		
			System.out.println("test=200");
			return ResponseEntity.ok(customerService);
		}	
		System.out.println("test=400");

    	return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/back/custService/delete/{id}")
    public ResponseEntity<?>  deleteCS (@PathVariable(name = "id") Integer id){
		 if (customerServiceService.deleteCSById(id)) {
			 return ResponseEntity.ok().build();
		}
		 return ResponseEntity.notFound().build();
	}

    
//  @PutMapping("/customerServices/back/{id}")
//  public ResponseEntity<?> updateCustomerService(@PathVariable Integer csId,
//          @RequestBody JSONObject jsonObject) {
//      // id錯誤
//      if (csId == null || !customerServiceService.existById(csId)) {
//          return ResponseEntity.notFound().build();
//      } else {
//          // id正確
//          CustomerService updatedCustomerService = customerServiceService.updateCustomerServiceByEmp(jsonObject);
//          if (updatedCustomerService == null) {
//              return ResponseEntity.notFound().build();
//          } else {
//              return ResponseEntity.ok().body(updatedCustomerService);
//          }
//      }
//  }
}
