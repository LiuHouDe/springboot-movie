package com.ispan.theater.service;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;
import com.ispan.theater.domain.User;
import com.ispan.theater.repository.UserRepository;

@Service
public class UserService {
	

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User InsertUser(JSONObject obj) {
		try {
			String userFirstname = obj.isNull("userFirstname") ? null : obj.getString("userFirstname");
			String userLastname = obj.isNull("userLastname") ? null : obj.getString("userLastname");
			String password = obj.isNull("password") ? null : obj.getString("password");
			String email = obj.isNull("email") ? null : obj.getString("email");
			String phone = obj.isNull("phone") ? null : obj.getString("phone");
			String birth = obj.isNull("birth") ? null : obj.getString("birth");
			String gender = obj.isNull("gender") ? null : obj.getString("gender");
//			String photo = obj.isNull("image") ? null : obj.("image");
			Boolean isverified = obj.isNull("isverified") ? null : obj.getBoolean("isverified");

			// 必填項目
			if (password == null ||password.length()==0|| email == null || email.length()==0|| phone== null||phone.length()==0) {
				return null;
			}

			User user = new User();
			user.setUserFirstname(userFirstname);
			user.setUserLastname(userLastname);
			// 密碼加密
			user.setPassword(passwordEncoder.encode(password));
			user.setEmail(email);
			user.setPhone(phone);
			if (birth != null && birth.length() != 0) {
				LocalDate temp = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				user.setBirth(temp);
			} else {
				user.setBirth(null);
			}
			user.setGender(gender);
			user.setRegistrationDate(new Date());
			user.setModifiedDate(new Date());
			user.setConsumption(0.0);
			user.setUserlevel(0);
			if (isverified == null) {
				user.setIsverified(false);
			} else {
				user.setIsverified(isverified);
			}
//			user.setUserPhoto(photo);
			return userRepository.save(user);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUserById(Integer id) {
		if (id != null) {
			Optional<User> optional = userRepository.findById(id);
			if (optional.isPresent()) {
				return optional.get();
			}
		}
		return null;
	}

	public User updateUser(JSONObject obj) {
		Integer userid = obj.isNull("userid") ? null : obj.getInt("userid");
		String userFirstname = obj.isNull("userFirstname") ? null : obj.getString("userFirstname");
		String userLastname = obj.isNull("userLastname") ? null : obj.getString("userLastname");
		String password = obj.isNull("password") ? null : obj.getString("password");
		String email = obj.isNull("email") ? null : obj.getString("email");
		String phone = obj.isNull("phone") ? null : obj.getString("phone");
		String birth = obj.isNull("birth") ? null : obj.getString("birth");
		
		String registrationDate =obj.isNull("registrationDate") ? null: obj.getString("registrationDate");
		
		String gender = obj.isNull("gender") ? null : obj.getString("gender");
		Double consumption = obj.isNull("consumption") ? null : obj.getDouble("consumption");
		Integer userlevel = obj.isNull("userlevel") ? null : obj.getInt("userlevel");
		Boolean isverified = obj.isNull("isverified") ? null : obj.getBoolean("isverified");
		// 圖片要直接加在這裡?

		String userPhoto = obj.isNull("userPhoto") ? null : obj.getString("userPhoto");

		Optional<User> optional = userRepository.findById(userid);
		
		if(optional.isEmpty()) {
			return null;
		}
		User update = optional.get();
		if (userFirstname != null && userFirstname.length() > 0) {
			update.setUserFirstname(userFirstname);
		}
		if (userLastname != null && userLastname.length() > 0) {
			update.setUserLastname(userLastname);
		}
		if (password != null && password.length() > 0) {
			update.setPassword(passwordEncoder.encode(obj.getString("password")));
		}
		if (email != null && email.length() > 0) {
			update.setEmail(email);
		}
		if (phone != null && phone.length() > 0) {
			update.setPhone(phone);
		}
		if (birth != null && birth.length() > 0) {
			LocalDate temp = LocalDate.parse(obj.getString("birth"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			update.setBirth(temp);
		}
		
		if(registrationDate!=null&&registrationDate.length()>0) {
			try {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(registrationDate);
				update.setRegistrationDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (gender != null && gender.length() > 0) {
			update.setGender(gender);
		}
		if (consumption != null) {
			update.setConsumption(consumption);
		}
		if (userlevel != null) {
			update.setUserlevel(userlevel);
		}
		if (isverified != null) {
			update.setIsverified(isverified);
		}
		// 照片
		if (userPhoto != null) {
//			update.setUserPhoto(userPhoto);
		}

		update.setModifiedDate(new Date());
		User user = userRepository.save(update);
		return user;
	}

	
	
	
	
	
	public boolean deleteByUserId(Integer id) {
		if (id != null) {
			Optional<User> optional = userRepository.findById(id);
			if (optional.isPresent()) {
				userRepository.deleteById(id);
				return true;
			}
		}
		return false;
	}

	public User checkLogin(JSONObject obj) {
		String userName = obj.isNull("username") ? null : obj.getString("username");
		String userPassword = obj.isNull("password") ? null : obj.getString("password");
		// 透過Email找是否有該使用者
		User dbusers = userRepository.findByEmail(userName);
		if (dbusers == null) {
			// 沒有找到返回
			return null;
		} else {
			// 取得資料庫密碼
			String dbEncodedPwd = dbusers.getPassword();
			// 透過Bcrypt 比對密碼是否相等
			Boolean result = passwordEncoder.matches(userPassword, dbEncodedPwd);
			if (result) {
				return dbusers;
			} else {
				return null;
			}
		}
	}

	public boolean existByEmail(String email) {
		if (email != null && email.length() > 0) {
			User result = userRepository.findByEmail(email);
			if (result != null) {
				return true;
			}
		}
		return false;
	}

	public boolean existByPhone(String phone) {
		if (phone != null && phone.length() > 0) {
			List<User> result = userRepository.findByPhone(phone);
			if (!result.isEmpty()) {
				return true;
			}
		}
		return false;
	}


	public Boolean existByPhoneOrEmail(String email, String phone) {
		if (email != null && email.length() != 0 || phone != null && phone.length() != 0) {
			List<User> result = userRepository.findByEmailOrPhone(email, phone);
			if (!result.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public User findUserByEmail(String email) {
		if (email != null && email.length() != 0) {
			return userRepository.findByEmail(email);
		}
		return null;
	}
	

	public User updatePhoto (Integer userid,MultipartFile photoFile) throws IOException {
		
		Optional<User> optional = userRepository.findById(userid);
		if (optional.isPresent()) {
			User user = optional.get();
			user.setUserPhoto(photoFile.getBytes());

			user.setModifiedDate(new Date());
			userRepository.save(user);
			return user;
		}
		return null;
	}
	
	

	public List<User> findUsers (JSONObject obj){
			return userRepository.find(obj);
	}
	
	public long countUsers(JSONObject obj) {
		return userRepository.count(obj);
	}

}
