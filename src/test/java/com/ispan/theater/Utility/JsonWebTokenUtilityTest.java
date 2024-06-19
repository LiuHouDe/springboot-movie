package com.ispan.theater.Utility;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ispan.theater.util.JsonWebTokenUtility;

@SpringBootTest
public class JsonWebTokenUtilityTest {
	
	@Autowired JsonWebTokenUtility jsonWebTokenUtility;
	
	@Test
	public void testCreateToken() {
		JSONObject jsonobj = new JSONObject()
									.put("data1", "This is Data 1")
									.put("data2", "This is Data 2");
		
		String token = jsonWebTokenUtility.createToken(jsonobj.toString(), null);
		
	
		System.out.println(token);
	}
	
	
//	@Test
	public void testValidateToken() {
		String token =  new String("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ7XCJkYXRhMlwiOlwiVGhpcyBpcyBEYXRhIDJcIixcImRhdGExXCI6XCJUaGlzIGlzIERhdGEgMVwifSIsImlhdCI6MTcxNDk2NjUxMCwiZXhwIjoxNzE0OTY3MTEwfQ.o7fVs2HTLK8bVf05w-qWxi_TfM2w6fUzNefwdg6H10M");
		
		String result = jsonWebTokenUtility.validateToken(token);
		
		JSONObject  json = new JSONObject(result);
		System.out.println(json);
	}
	
//	@Test
	public void testValidateEncryptedToken() {
		String token =  new String("eyJwMmMiOjEyMDAwMCwicDJzIjoiZzlvOGxNcE1QNm5VWVQ4Sk9XN2dnclFlbTFEOEpuaTlNcEpud0pxazFtSTAyMmFtVXpBUXNQdGJXWVZTdXh2MkJybW96UDduVnZmZnhrTU9uNlpCR0EiLCJhbGciOiJQQkVTMi1IUzUxMitBMjU2S1ciLCJlbmMiOiJBMjU2R0NNIn0.uPDP_1Ft0RRWVN_eshKWXPbW-n2nOebMz_YIZtRXi3kP1ihwB9bMhg.EsWUALJMr6sGq7VZ.lFo7rOaRRaIaIxIemoSeWarQ_X7eYM8i8zcy9ktpvUwZGY7OfwmdZ6w5aUr1IoomGFoxg3UCZvYbcagac9xvdAekhL8gkxBnjx2kJOkrkY2XGN7Ikyj-vX_4hcIG0TJmfYotfIgFIcahjaHKi5CofdAjQ3hh.u0MYIlBNAMX356vDGiavCA");
		
		String result = jsonWebTokenUtility.validateEncryptedToken(token);
		
		JSONObject  json = new JSONObject(result);
		System.out.println(json);
	}
	
	
	
}
