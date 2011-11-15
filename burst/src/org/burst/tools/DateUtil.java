package org.burst.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
	
	public static String Format1 = "yyyy/MM/dd HH:mm:ss";
	public static String Format2 = "yyyy-MM-dd HH:mm:ss";
	public static String Format3 = "yyyy.MM.dd HH:mm:ss";
	
	public static String getSysTime(){
		SimpleDateFormat df = new SimpleDateFormat(Format1);
		Date date = new Date();
		return df.format(date);
	}
	
	public static Date getDate(String dateString) throws Exception{
		try{
			if(dateString == null){
				return null;
			}
			
			if(dateString.length() == 8){
				dateString = dateString.substring(0, 4) + "/" + 
					dateString.substring(4, 6) + "/" + dateString.substring(6, 8);
			}
			
			if(dateString.length() <= 10){
				dateString = dateString + " 00:00:00";
			}
			
			if(dateString.indexOf("-") > 0){
				dateString = dateString.replaceAll("-", "/");
			}
			
			if(dateString.indexOf(".") > 0){
				dateString = dateString.replaceAll(".", "/");
			}
			//System.out.println(dateString);
			SimpleDateFormat formatter = new SimpleDateFormat(Format1);

			return formatter.parse(dateString);
		}catch(Exception e){
			//System.out.println(e.toString());
			throw new Exception("invalid Date String:" + dateString);
		}
	}
	
	//format YYYY/MM/DD
	public static boolean isYMDFormat1(String dateString){
		if(dateString == null || dateString.length() != 10){
			return false;
		}
		
		if(!dateString.substring(4, 5).equals("/")){
			return false;	
		}
		
		if(!dateString.substring(7, 8).equals("/")){
			return false;	
		}
		
		return true;
	}
	
	//format YYYYMMDD
	public static boolean isYMDFormat2(String dateString){
		if(dateString == null || dateString.length() != 8){
			return false;
		}
		
		return isNumber(dateString);
	}
	
	public static boolean isNumber(String str){
		if(str == null || str.length() == 0){
			return false;
		}
		try{
			Integer.parseInt(str);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static String getDateString(Date date, String format) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static int getWeekOfYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);

		return c.get(Calendar.WEEK_OF_YEAR);
	}
	
	public static int getYear(Date date) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);

		return c.get(Calendar.YEAR);
	}
	
	public static String getYearWeekString(Date date) throws Exception{
		int week = getWeekOfYear(date);
		int year = getYear(date);
		return "" + year + "/" + week;
	}
	
	public static int DateMinus(Date date1, Date date2){
		long time1 = date1.getTime();
		long time2 = date2.getTime();
		
		long sep = (time1 - time2)/(3600*24*1000);
		
		return Integer.parseInt(String.valueOf(sep));
	}
	
	//只比较日期
	public static int compareDay(Date date1, Date date2){
		int rtnBig = 1;
		int rtnEquals = 0;
		int rtnSmall = -1;
		
		Calendar c1 = new GregorianCalendar();
		c1.setTime(date1);
		
		Calendar c2 = new GregorianCalendar();
		c2.setTime(date2);
		
		int value1 = c1.get(Calendar.YEAR);
		int value2 = c2.get(Calendar.YEAR);
		
		if(value1 > value2){
			return rtnBig;
		}else if(value1 < value2){
			return rtnSmall;
		}
		
		//if equals,compare month
		value1 = c1.get(Calendar.MONTH);
		value2 = c2.get(Calendar.MONTH);
		
		if(value1 > value2){
			return rtnBig;
		}else if(value1 < value2){
			return rtnSmall;
		}
		
		//if equals,compare day
		value1 = c1.get(Calendar.DATE);
		value2 = c2.get(Calendar.DATE);
		
		if(value1 > value2){
			return rtnBig;
		}else if(value1 < value2){
			return rtnSmall;
		}
		
		return rtnEquals;
		
	}
	
}
