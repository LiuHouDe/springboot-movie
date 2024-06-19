package com.ispan.theater.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import com.google.gson.JsonObject;

import com.ispan.theater.domain.User;
import com.ispan.theater.dto.UserDTO;
import com.ispan.theater.service.UserService;
import com.ispan.theater.util.EmailSenderComponent;
import com.ispan.theater.util.JsonWebTokenUtility;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
public class UserAjaxController {

	@Autowired
	EmailSenderComponent emailSenderComponent;

	@Autowired
	UserService userService;

	@Autowired
	JsonWebTokenUtility jsonWebTokenUtility;

	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@PostMapping("/pass/register") // testpage
	public String userRegister(@RequestBody String json) {
		JSONObject repJson = new JSONObject();
		JSONObject userJson = new JSONObject(json);
		System.out.println(userJson.getString("email"));
		Boolean check = userService.existByPhoneOrEmail(userJson.getString("email"), userJson.getString("phone"));
		if (check) {
			repJson.put("success", false);
			repJson.put("message", "註冊失敗，資料重複");
			return repJson.toString();
		}
		User user = userService.InsertUser(userJson);
		if (user != null) {

//			寄送驗證信
//			JSONObject inputjson = new JSONObject().put("userid", user.getId());
//			String token = jsonWebTokenUtility.createEncryptedToken(inputjson.toString(), null);
//			emailSenderComponent.sendEmail(user.getEmail(),token);

			repJson.put("success", true);

			repJson.put("message", "註冊成功");
		} else {
			repJson.put("success", false);
			repJson.put("message", "註冊失敗");
		}
		return repJson.toString();
	}

	@PostMapping("/pass/login")
	public String userLogin(@RequestBody String json) {
		JSONObject jsonobj = new JSONObject(json);
		JSONObject result = new JSONObject();
		User user = userService.checkLogin(jsonobj);
		if (user != null) {
			//----security----
			GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
	        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
					(user.getEmail(),jsonobj.getString("password"),Collections.singleton(authority)));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        //----security----
			result.put("success", true);
			result.put("message", "登入成功");
			JSONObject inputjson = new JSONObject().put("userid", user.getId()).put("email", user.getEmail());
			String token = jsonWebTokenUtility.createToken(inputjson.toString(), null);			
			result.put("token", token);
			result.put("username", user.getUserFirstname() + user.getUserLastname());
		} else {
			result.put("success", false);
			result.put("message", "登入失敗");
		}
		return result.toString();
	}

	@GetMapping("/pass/check/phone")
	public String checkPhone(@RequestParam String phone) {
		JSONObject result = new JSONObject();
		if (phone == null || phone.length() != 10) {
			result.put("success", false);
			result.put("message", "手機號格式有誤");
		} else {
			if (userService.existByPhone(phone)) {
				result.put("success", false);
				result.put("message", "手機號已存在,請使用其他手機號");
			} else {
				result.put("success", true);
				result.put("message", "手機號可以使用");
			}
		}
		return result.toString();
	}

	@GetMapping("/pass/check/email")
	public String checkEmail(@RequestParam String email) {
		JSONObject result = new JSONObject();
		if (email == null || email.length() == 0) {
			result.put("success", false);
			result.put("message", "Email格式有誤");
		} else {
			if (userService.existByEmail(email)) {
				result.put("success", false);
				result.put("message", "Email已存在,請使用其他Eamil");
			} else {
				result.put("success", true);
				result.put("message", "Eamil可以使用");
			}
		}
		return result.toString();
	}

//	要檔
	@GetMapping("/profile")
	public ResponseEntity<?> userProfile(@RequestParam String token) {
		String data = jsonWebTokenUtility.validateToken(token);
		if (data != null && data.length() != 0) {
			JSONObject obj = new JSONObject(data);
			Integer id = obj.getInt("userid");
			User user = userService.getUserById(id);
			if (user != null) {
				ResponseEntity<User> ok = ResponseEntity.ok(user);
				return ok;
			}
		}
		ResponseEntity<Void> notFound = ResponseEntity.notFound().build();
		return notFound;
	}

	@PostMapping("/pass/login/google")
	public String GoolgleLogin(@RequestBody String credentialJSON) {
		String credential = new JSONObject(credentialJSON).getString("credential");
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
				// Specify the CLIENT_ID of the app that accesses the backend:
				.setAudience(Collections
						.singletonList("655138938513-u7u4vrhur12fmjujfubctji8eflv4usn.apps.googleusercontent.com"))
				// Or, if multiple clients access the backend:
				// .setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
				.build();
		JSONObject result = new JSONObject();
		// (Receive idTokenString by HTTPS POST)
		try {
			GoogleIdToken idToken = verifier.verify(credential);
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				// Print user identifier
				String userId = payload.getSubject();
				// Get profile information from payload
				String email = payload.getEmail();

				// URL暫時無用
				String pictureUrl = (String) payload.get("picture");
				String familyName = (String) payload.get("family_name");
				String givenName = (String) payload.get("given_name");
				// 有註冊過
				User dbuser = userService.findUserByEmail(email);
				if (dbuser != null) {
					JSONObject inputjson = new JSONObject().put("userid", dbuser.getId())
							.put("email", dbuser.getEmail()).put("birth", dbuser.getBirth());
					String token = jsonWebTokenUtility.createToken(inputjson.toString(), null);
					result.put("token", token);
					result.put("username", dbuser.getUserFirstname() + dbuser.getUserLastname());
					result.put("message", "歡迎回來");
					result.put("success", true);
				} else {// 沒註冊過
					JSONObject insertUser = new JSONObject().put("userFirstname", familyName)

							.put("userLastname", givenName).put("email", email).put("password",  RandomStringUtils.randomAlphabetic(15))
							.put("phone", "09" + userId.substring(0, 8)).put("birth", LocalDate.now().toString())
							.put("gender", "M").put("isverified", true).put("image", "");
					User user = userService.InsertUser(insertUser);
					if (user != null) {
						result.put("success", true);

						result.put("message", "註冊成功，手機號與生日為系統預設值，請修改您的個人資料");
						JSONObject inputjson = new JSONObject().put("userid", user.getId())
								.put("email", user.getEmail()).put("birth", user.getBirth());
						String token = jsonWebTokenUtility.createToken(inputjson.toString(), null);
						result.put("token", token);
						result.put("username", user.getUserFirstname() + user.getUserLastname());
					} else {
						result.put("success", false);
						result.put("message", "新增失敗");
					}
				}
			} else {
				result.put("success", false);
				result.put("message", "Invalid ID token.請重新操作");
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			return result.toString();
		}

	}


	// 修改密碼 
	@PutMapping("/pass/check/changePaaword/{token}")
	public ResponseEntity<?> changePaaword(@PathVariable(name = "token")

	String token, @RequestBody String password) {
		String data = jsonWebTokenUtility.validateToken(token);
		if (data != null && data.length() != 0) {
			JSONObject obj = new JSONObject(data);
			Integer userid = obj.getInt("userid");
			JSONObject update = new JSONObject(password).put("userid", userid);
			User user = userService.updateUser(update);
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.notFound().build();
	}


	// 修改個人資料 要檔
	@PutMapping("/check/changeUserProfile/{token}")
	public ResponseEntity<?> changeUserProfile(@PathVariable(name = "token") String token, @RequestBody UserDTO userDTO) {
		String data = jsonWebTokenUtility.validateToken(token);
		if (data != null && data.length() != 0) {
			Integer userid = new JSONObject(data).getInt("userid");
			JSONObject update = new JSONObject(userDTO).put("userid", userid);
			User user = userService.updateUser(update);
			return ResponseEntity.ok(user);
		}
		return ResponseEntity.notFound().build();
	}

	//Link to Email驗證
	@PutMapping("/pass/verify-email/{token}")
	public String userEmailVerify(@PathVariable(name = "token") String token) {
		JSONObject result = new JSONObject();
		// 檢查token並解析
		String data = jsonWebTokenUtility.validateToken(token);
		// tokern有效
		if (data != null && data.length() != 0) {
			Integer userid = new JSONObject(data).getInt("userid");
			JSONObject update = new JSONObject().put("userid", userid).put("isverified", true);
			userService.updateUser(update);
			result.put("success", true);
			result.put("message", "郵件驗證成功");

		} else {
			result.put("success", false);
			result.put("message", "郵件驗證失敗，請重新獲取驗證信進行驗證");
		}
		return result.toString();
	}

	
	//圖片上傳
	@PostMapping("/pass/uploadUserPhoto/{token}")

	public String uploadPohto(@PathVariable(name = "token") String token, @RequestParam MultipartFile file)
			throws IOException {
		String data = jsonWebTokenUtility.validateToken(token);
		if (data != null && data.length() != 0) {
			Integer userid = new JSONObject(data).getInt("userid");
			userService.updatePhoto(userid, file);
		}
		return null;
	}

	//圖片讀取
	@GetMapping("/pass/finduserphoto/{email}")

	public ResponseEntity<?> downloadPhoto(@PathVariable(name = "email") String email) {
		User user = userService.findUserByEmail(email);
		if (user!=null) {
			byte[] photoFile = user.getUserPhoto();
			if(photoFile!=null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_JPEG);
				return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
			}
		}
		    return  ResponseEntity.notFound().build();
	}
	
	
	//發送忘記密碼信
	@GetMapping("/pass/sendForgetPasswordEmail/{email}")
	public ResponseEntity<?> sendForgetPasswordEmail  (@PathVariable(name = "email") String email){
		User user = userService.findUserByEmail(email);
		if (user!=null) {
			JSONObject inputjson = new JSONObject().put("userid", user.getId()).put("email", user.getEmail());
			String token = jsonWebTokenUtility.createToken(inputjson.toString(), null);
			emailSenderComponent.sendForgetPasswordEmail(user.getEmail(), token);
			return  ResponseEntity.ok(user);
		}
		System.out.println("error");
		return ResponseEntity.notFound().build();
	}
	
	
	//發送驗證信箱信
	@GetMapping("/pass/sendVeriftEmail/{token}")
	public  ResponseEntity<?> sendVeriftEmail (@PathVariable(name = "token") String token){
		String data = jsonWebTokenUtility.validateToken(token);
		if(data!=null) {
			JSONObject userinfo =new JSONObject(data);
			emailSenderComponent.sendEmail(userinfo.getString("email"),token);
			return ResponseEntity.ok(userinfo);
		}
		return ResponseEntity.notFound().build();
	}
	
	
	
	
	//多條件查詢
//	@GetMapping("/backside/findUsers")
//	public ResponseEntity<List<User>> findUsers(@RequestParam Map<String, String> param){
//		JSONObject obj =new JSONObject(param);
//		return ResponseEntity.ok(userService.findUsers(obj)) ;
//	}
	
//	//找到有多少人
//	@GetMapping("/backside/countUsers")
//	public ResponseEntity<Long> countUsers(@RequestParam Map<String, String> param){
//		JSONObject obj =new JSONObject(param);
//		return ResponseEntity.ok(userService.countUsers(obj));
//	}
	

	//多條件查詢
	@GetMapping("/backside/findUsers")
	public String findUsers(@RequestParam Map<String, String> param){
		JSONObject obj =new JSONObject(param);
		List<User> users = userService.findUsers(obj);
		long count = userService.countUsers(obj);
		JSONArray array =new JSONArray();
		
		 JSONObject result = new JSONObject();
		
		if(users!=null && !users.isEmpty()) {
			for(User user:users) {
				JSONObject iteam=new JSONObject();
				iteam.put("id", user.getId())
					 .put("userFirstname", user.getUserFirstname())
					 .put("userLastname", user.getUserLastname())
					 .put("email", user.getEmail())
					 .put("phone", user.getPhone())
					 .put("birth", user.getBirth())
					 .put("gender", user.getGender())
					 .put("consumption", user.getConsumption())
					 .put("userlevel", user.getUserlevel())
					 .put("registrationDate", user.getRegistrationDate())
					 .put("isverified",user.getIsverified());
				array.put(iteam);
			}
			result.put("list",array);
		}
		result.put("count", count);
		return result.toString();	
	}
	
	@DeleteMapping("/backside/delect-user/{id}")
	public ResponseEntity<?>  delectUser (@PathVariable(name = "id") Integer id){
		 if (userService.deleteByUserId(id)) {
			 return ResponseEntity.ok().build();
		}
		 return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/backside/update-user/{id}")
	public ResponseEntity<?>  updateUser (@PathVariable(name = "id") Integer id,@RequestBody String json){
	
		JSONObject obj =new JSONObject(json).put("userid", id);
		User user =userService.updateUser(obj);
		 if (user!=null) {
			 return ResponseEntity.ok(user);
		}
		 return ResponseEntity.notFound().build();
	}

	@GetMapping("/backside/find-user/{id}")
	public ResponseEntity<?> findUserById (@PathVariable(name = "id") Integer id){
		User user = userService.getUserById(id);
		if(user!=null) {
		return ResponseEntity.ok(user);
		}
		return ResponseEntity.notFound().build();
	}

}
