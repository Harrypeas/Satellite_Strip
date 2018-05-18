package com.xc.worldwind.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nasa.worldwind.geom.Position;

/**
 * @author 钟翔
 * @class 字符串转换工具类
 * @function 解析网址内容
 */
public class StringUtils
{
	/**
	 * 将Position对象转换为经纬度字符串
	 * @param Position 对象
	 * @return String 字符串
	 */
	public static String parseFromSatelitePosition(Position position)
	{
		StringBuilder sb = new StringBuilder();
		if(position.getLatitude().degrees<0)
			sb.append(position.getLatitude().toDMSString().replaceAll("-", "S:"));
		else
			sb.append("N:"+position.getLatitude().toDMSString());
		sb.append(" ");
		if(position.getLongitude().degrees<0)
			sb.append(position.getLongitude().toDMSString().replaceAll("-", "W:"));
		else
			sb.append("E:"+position.getLongitude().toDMSString());
		sb.append(" ");
		sb.append("A:"+String.format("%.3f", position.getAltitude()/1000)+"km");

		return sb.toString();
	}

	/**
	 * 将Date对象转换为YYYY-MM-DD hh:mm:ss格式
	 * @param Position 对象
	 * @return String 字符串
	 */
	public static String parseFromDate(Date dt)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		return sdf.format(dt);
	}
}
