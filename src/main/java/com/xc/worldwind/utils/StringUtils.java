package com.xc.worldwind.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nasa.worldwind.geom.Position;

/**
 * @author ����
 * @class �ַ���ת��������
 * @function ������ַ����
 */
public class StringUtils
{
	/**
	 * ��Position����ת��Ϊ��γ���ַ���
	 * @param Position ����
	 * @return String �ַ���
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
	 * ��Date����ת��ΪYYYY-MM-DD hh:mm:ss��ʽ
	 * @param Position ����
	 * @return String �ַ���
	 */
	public static String parseFromDate(Date dt)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
		return sdf.format(dt);
	}
}
