package com.pangnongfu.server.web.utils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
public class DataTraslator {
	public static final int YEAR=1000*60*60*24*30*12;
	public static final int MONTH=1000*60*60*24*30;
	public static final int DAY=1000*60*60*24;
	public static final int HOUR=1000*60*60;
	public static final int MIN=1000*60;

	public static final Map<Integer, Integer> LEAP_MONTH_DAYS_MAP = new HashMap<Integer, Integer>() {
		{
			put(1, 31);
			put(2, 28);
			put(3, 31);
			put(4, 30);
			put(5, 31);
			put(6, 30);
			put(7, 31);
			put(8, 31);
			put(9, 30);
			put(10, 31);
			put(11, 31);
			put(12, 30);
		}
	};

	public static final Map<Integer, Integer> NOLEAP_MONTH_DAYS_MAP = new HashMap<Integer, Integer>() {
		{
			put(1, 31);
			put(2, 29);
			put(3, 31);
			put(4, 30);
			put(5, 31);
			put(6, 30);
			put(7, 31);
			put(8, 31);
			put(9, 30);
			put(10, 31);
			put(11, 31);
			put(12, 30);
		}
	};

	public static String GetDistance(double lat_a, double lng_a, double lat_b,
			double lng_b) {
		double EARTH_RADIUS = 6378137;
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return DistanceToString((int)s);
	}



	public static String LongToTimePast(long DueTime) {
		long year, month, hour, min;
		long timeNow = new Date().getTime();
		long time = timeNow - DueTime;

		time = time % DAY;
		hour = time / HOUR;
		time -= hour *HOUR;
		min = time /MIN;
		time -= min * MIN;

		String a2 = longToFormatString(DueTime, "HH时mm分");
		year = Integer.parseInt(longToFormatString(timeNow, "yyyy"))
				- Integer.parseInt(longToFormatString(DueTime, "yyyy"));

		month = Integer.parseInt(longToFormatString(timeNow, "MM"))
				- Integer.parseInt(longToFormatString(DueTime, "MM"));

		int dayDiff = Integer.parseInt(longToFormatString(timeNow, "dd"))
				- Integer.parseInt(longToFormatString(DueTime, "dd"));

		if (year != 0)
			return longToFormatString(DueTime, "yy/MM/dd HH:mm");
		if (month != 0)
			return longToFormatString(DueTime, "yy/MM/dd HH:mm");

		if (dayDiff == 0)
			if (hour != 0)
				return hour + "小时" + min + "分钟前";
			else if (hour == 0) {
				if (min == 0)
					return "刚刚";
				else
					return min + "分钟前";
			}

		if (dayDiff == 1)
			return "昨天" + a2;
		if (dayDiff == 2)
			return "前天" + a2;

		return longToFormatString(DueTime, "yy/MM/dd HH:mm");
	}



	private  static String longToFormatString(long time, String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format); // "HH时mm分"
		String result = dateformat.format(new Date(time));
		return result;
	}

	private static String DistanceToString(int distance) {
		if(distance <= 50){
			return "50米内";
		}
		if(distance > 50 && distance <= 100 ){
			return "100米内";
		}
		if(distance > 100 && distance <= 1000){
			int i = distance / 100;
			return i + "00米内";
		}
		return distance / 1000 + "千米内";
	}

	public static String getDateNow() {
		String date = new Date(new Date().getTime()).toString();
		return date;
	}

}
