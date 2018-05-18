package com.xc.worldwind.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author 钟翔
 * @class 时间角度转工具类
 * @function 将字符串转换成对应的long和int型数据
 */
public class DateTimeUtil
{
	/**
	 * 计算自动刷新时间
	 * @param timespanStr 刷新时间
	 * @return 间隔刷新时间
	 */
	public static long convertToRefreshTimespan(String timespanStr)
	{
		long timespan = 1000*10;
		switch(timespanStr)
		{
			case "0.1s":
				timespan = 100;
				break;
			case "0.5s":
				timespan = 500;
				break;
			case "1s":
				timespan = 1000*1;
				break;
			case "2s":
				timespan = 1000*2;
				break;
			case "3s":
				timespan = 1000*3;
				break;
			case "4s":
				timespan = 1000*4;
				break;
			case "5s":
				timespan = 1000*5;
				break;
			case "10s":
				timespan = 1000*10;
			break;
			case "15s":
				timespan = 1000*15;
			break;
			case "20s":
				timespan = 1000*20;
				break;
			case "30s":
				timespan = 1000*30;
				break;
			case "1min":
				timespan = 1000*60;
				break;
			default:
				timespan = 1000*10;
				break;
		}
		return timespan;
	}
	
	/**
	 * 计算绘制多长时间的轨道
	 * @param timespanStr 卫星运行时间
	 * @return	绘制时间
	 */
	public static long convertToOrbitTimespan(String timespanStr)
	{
		long timespan = 30;
		switch(timespanStr)
		{
			case "0.5hrs":
				timespan = 30;
				break;
			case "1hrs":
				timespan = 60*1;
				break;
			case "2hrs":
				timespan = 60*2;
				break;
			case "3hrs":
				timespan = 60*3;
				break;
			case "4hrs":
				timespan = 60*4;
				break;
			case "5hrs":
				timespan = 60*5;
				break;
			case "6hrs":
				timespan = 60*6;
				break;
			case "12hrs":
				timespan = 60*12;
				break;
			case "24hrs":
				timespan = 60*24;
				break;
			case "48hrs":
				timespan = 60*48;
			break;
			default:
				timespan = 30;
				break;
		}
		return timespan;
	}
	
	/**
	 * 计算每隔多长时间打一次中间点
	 * @param timespanStr 间隔时间
	 * @return	间隔时间
	 */
	public static long convertToOrbitNodeTimespan(String timespanStr)
	{
		long timespan = 6 * 5;
		switch(timespanStr)
		{
			case "1min":
				timespan = 6 * 1;
				break;
			case "2min":
				timespan = 6 * 2;
				break;
			case "3min":
				timespan = 6 * 3;
				break;
			case "4min":
				timespan = 6 * 4;
				break;
			case "5min":
				timespan = 6 * 5;
				break;
			case "10min":
				timespan = 6 * 10;
				break;
			case "15min":
				timespan = 6 * 15;
				break;
			case "20min":
				timespan = 6 * 20;
				break;
			case "30min":
				timespan = 6 * 30;
				break;
			case "1hrs":
				timespan = 6 * 60;
				break;
			default:
				timespan = 6 * 5;
				break;
		}
		return timespan;
	}
	
	/**
	 * 计算覆盖的时间间隔
	 * @param timespanStr 间隔时间
	 * @return	间隔时间
	 */
	public static long convertToCoverageTimespan(String timespanStr)
	{
		long timespan = 1;
		switch(timespanStr)
		{
			case "1min":
				timespan = 1;
				break;
			case "2min":
				timespan = 2;
				break;
			case "3min":
				timespan = 3;
				break;
			case "4min":
				timespan = 4;
				break;
			case "5min":
				timespan = 5;
				break;
			case "10min":
				timespan = 10*1;
				break;
			case "15min":
				timespan = 15*1;
				break;
			case "20min":
				timespan = 20*1;
				break;
			case "30min":
				timespan = 30*1;
				break;
			case "1hrs":
				timespan = 60*1;
				break;
			
		}
		return timespan;
	}
	
	/**
	 * 计算覆盖的角度带小奥
	 * @param Str 角度大小
	 * @return	角度
	 */
	public static double convertToAngle(String Str)
	{
		double timespan = 1;
		switch(Str)
		{
			case "0.1度":
				timespan = 0.1;
				break;
			case "0.2度":
				timespan = 0.2;
				break;
			case "0.3度":
				timespan = 0.3;
				break;
			case "0.4度":
				timespan = 0.4;
				break;
			case "0.5度":
				timespan = 0.5;
				break;
			case "1度":
				timespan = 1;
				break;
			case "2度":
				timespan = 2;
				break;
			case "3度":
				timespan = 3;
				break;
			case "4度":
				timespan = 4;
				break;
			case "5度":
				timespan = 5;
				break;
			case "6":
				timespan = 6;
				break;
			case "7度":
				timespan = 7;
				break;
			case "8度":
				timespan = 8;
				break;
			case "9度":
				timespan = 9;
				break;
			case "10度":
				timespan = 10;
				break;
			case "15度":
				timespan = 15;
				break;
			case "20度":
				timespan = 20;
				break;
			case "25度":
				timespan = 25;
				break;
			case "30度":
				timespan = 30;
				break;
		}
		return timespan;
	}
	
	public static String convertToCoverageType(String Str)
	{
		String type = "zhijieView";
		switch(Str)
		{
			case "直接覆盖":
				type = "zhijieView";
				break;
			case "左右侧视":
				type = "sideView";
				break;
			case "前后俯仰":
				type = "FuYangView";
				break;
		}
		return type;
	}

	public static String DATEFORMAT1 = "yyyy-MM-dd HH:mm:ss";
	
	public static String DATEFORMAT2 = "yyyy-MM-dd HH:mm:ss.SSS";
	
	public static String DATEFORMAT3 = "yyyy-MM-dd(EEE)HH:mm:ss.SSS";
	public static String getCurrentTimeString(String timeZone,String Format)
	{
		final TimeZone zone = TimeZone.getTimeZone(timeZone); //获取中国时区  
		TimeZone.setDefault(zone); //设置时区  
		Date date = new Date();
	    SimpleDateFormat dateFm = new SimpleDateFormat(Format,Locale.US);
	   
		return  dateFm.format(date);
	}
}
