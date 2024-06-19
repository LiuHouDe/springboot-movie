package com.ispan.theater.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeConverter {
	
	public static String toString(Date datetime, String format) {
		String result = "";
		try {
			if (datetime != null) {
				result = new SimpleDateFormat(format).format(datetime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date parse(String datetime, String format) {
		Date result = new Date();
		try {
			result = new SimpleDateFormat(format).parse(datetime);
		} catch (Exception e) {
			result = new Date();
			e.printStackTrace();
		}
		return result;
	}
		
	public static String createSqlDatetime(Date datetime) {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(datetime).toString();
	}
	
	public static String createSqlDatetimeECPay(Date datetime) {
		return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(datetime).toString();
	}
}
