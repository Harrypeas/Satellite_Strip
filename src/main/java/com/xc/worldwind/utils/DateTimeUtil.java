package com.xc.worldwind.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author ����
 * @class ʱ��Ƕ�ת������
 * @function ���ַ���ת���ɶ�Ӧ��long��int������
 */
public class DateTimeUtil
{
	/**
	 * �����Զ�ˢ��ʱ��
	 * @param timespanStr ˢ��ʱ��
	 * @return ���ˢ��ʱ��
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
	 * ������ƶ೤ʱ��Ĺ��
	 * @param timespanStr ��������ʱ��
	 * @return	����ʱ��
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
	 * ����ÿ���೤ʱ���һ���м��
	 * @param timespanStr ���ʱ��
	 * @return	���ʱ��
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
	 * ���㸲�ǵ�ʱ����
	 * @param timespanStr ���ʱ��
	 * @return	���ʱ��
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
	 * ���㸲�ǵĽǶȴ�С��
	 * @param Str �Ƕȴ�С
	 * @return	�Ƕ�
	 */
	public static double convertToAngle(String Str)
	{
		double timespan = 1;
		switch(Str)
		{
			case "0.1��":
				timespan = 0.1;
				break;
			case "0.2��":
				timespan = 0.2;
				break;
			case "0.3��":
				timespan = 0.3;
				break;
			case "0.4��":
				timespan = 0.4;
				break;
			case "0.5��":
				timespan = 0.5;
				break;
			case "1��":
				timespan = 1;
				break;
			case "2��":
				timespan = 2;
				break;
			case "3��":
				timespan = 3;
				break;
			case "4��":
				timespan = 4;
				break;
			case "5��":
				timespan = 5;
				break;
			case "6":
				timespan = 6;
				break;
			case "7��":
				timespan = 7;
				break;
			case "8��":
				timespan = 8;
				break;
			case "9��":
				timespan = 9;
				break;
			case "10��":
				timespan = 10;
				break;
			case "15��":
				timespan = 15;
				break;
			case "20��":
				timespan = 20;
				break;
			case "25��":
				timespan = 25;
				break;
			case "30��":
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
			case "ֱ�Ӹ���":
				type = "zhijieView";
				break;
			case "���Ҳ���":
				type = "sideView";
				break;
			case "ǰ����":
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
		final TimeZone zone = TimeZone.getTimeZone(timeZone); //��ȡ�й�ʱ��  
		TimeZone.setDefault(zone); //����ʱ��  
		Date date = new Date();
	    SimpleDateFormat dateFm = new SimpleDateFormat(Format,Locale.US);
	   
		return  dateFm.format(date);
	}
}
