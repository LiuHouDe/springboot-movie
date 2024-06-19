package com.ispan.theater.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.theater.domain.User;
import com.ispan.theater.repository.UserRepository;

@SpringBootTest
public class testUser {

	@Autowired
	UserService userService;
	@Autowired
	UserRepository userRepository;



//	@Test
	public void testInsertUser() {
		JSONObject userJson = new JSONObject().put("userFirstname", "1111").put("userLastname", "小名")
				.put("password", "2225").put("email", "3331@gmail.com").put("phone", "09811787165")
				.put("birth", "1999-01-06").put("gender", "M");
		System.out.print(userJson);
		User user = userService.InsertUser(userJson);
	}

//	@Test
	public void testFindByEmailOrPhone() {
		List<User> user = userRepository.findByEmailOrPhone("email", "phone");
		System.out.println(user);
	}

//	@Test
	public void testUserUpdate() {
		JSONObject userJson = new JSONObject().put("id", "1").put("userFirstname", "777").put("userLastname", "小名")
				.put("password", "6666").put("email", "333@gmail.com").put("phone", "0981787165")
				.put("birth", "1988-01-06").put("gender", "M").put("isverified", "false");
		User user = userService.updateUser(userJson);
	}

//	@Test
	public void testDeleteUser() {
		boolean result = userService.deleteByUserId(1);
		System.out.println(result);

	}

//	@Test
	public void testLogin() {
		JSONObject userJson = new JSONObject().put("userName", "3331@gmail.com").put("password", "2225");
		System.out.println(userService.checkLogin(userJson).getId());

	}


//	@Test
	public void testExistByEmail() {
		System.out.println(userService.existByEmail("3331@gmail.com"));
	}

	// 大量產生測試資料
//	@Test  
	public void testLogInsertUser() throws Exception {
		int randomInt = new Random().nextInt(15) + 3;

		// 隨機日期最大最小值
		int minDay = (int) LocalDate.of(1970, 1, 1).toEpochDay();
		int maxDay = (int) LocalDate.of(2024, 5, 20).toEpochDay();

		for (int i = 0; i <= 300; i++) {
			// 隨機產生日期
			Random rand = new Random();
			long randDay = minDay + rand.nextInt(maxDay - minDay);
			LocalDate randBirthDay = LocalDate.ofEpochDay(randDay);

			JSONObject userJson = new JSONObject()
					.put("userFirstname", RandomStringUtils.random(1, 0x4e00, 0x9fff, false, false))
					.put("userLastname", RandomStringUtils.random(2, 0x4e00, 0x9fff, false, false))
					.put("password", RandomStringUtils.randomAlphabetic(randomInt))
					.put("email", RandomStringUtils.randomAlphabetic(randomInt) + "@gmail.com")
					.put("phone", "09" + RandomStringUtils.randomNumeric(8)).put("birth", randBirthDay.toString())
					.put("gender", "M");
			System.out.println(userJson);
			User user = userService.InsertUser(userJson);
			user.setConsumption(Math.floor(Math.random() * 10000));
			user.setUserlevel((int)Math.floor(Math.random() * 12));
			rand = new Random();
			long newrandDay = minDay + rand.nextInt(maxDay - minDay);
			randBirthDay = LocalDate.ofEpochDay(newrandDay);
			String pattern = "yyyy-MM-dd";
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			String dateString = randBirthDay.toString();
			try {
				Date date = dateFormat.parse(dateString);
				user.setRegistrationDate(date);
				if (Math.random() > 0.5) {
					user.setIsverified(true);
				} else {
					user.setIsverified(false);
				}
				if (Math.random() > 0.5) {
					user.setGender("M");
				} else {
					user.setGender("F");
				}

				userRepository.save(user);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
