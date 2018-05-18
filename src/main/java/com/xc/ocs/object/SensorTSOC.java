package com.xc.ocs.object;

import java.util.ArrayList;
import java.util.Calendar;

import com.xc.worldwind.object.MyPoint;

/**
 * 单个传感器与与观测区域相交的相关参数
 * @author boldness-xc
 *
 */
public class SensorTSOC {
	private String SensorID = null;
	private String SensorName = null;
	private Calendar StartTime = null;
	private Calendar EndTime = null;
	private ArrayList<MyPoint> Polygon;	
	private String sensorType = null;	
		
	public SensorTSOC(Calendar start,Calendar end,String id,String type,String name)
	{
		StartTime = start;
		EndTime = end;
		SensorID = id;
		sensorType = type;
		SensorName = name;
	}
	
	
	public Calendar getStartTime()
	{
		return StartTime;
	}
	public String getSensorType()
	{
		return sensorType;
	}
	public Calendar getEndTime()
	{
		return EndTime;
	}
	
	public void SetStartTime(Calendar newStart)
	{
		StartTime = newStart;
	}
	
	public void SetEndTime(Calendar newEnd)
	{
		EndTime = newEnd;
	}
	public void SetType(String Type)
	{
		sensorType = Type;
	}
	public String getSensorID()
	{
		return SensorID;
	}
	
	public void setID(String SensorID1)
	{
		SensorID = SensorID1;
	}
	
	public String getSensorName()
	{
		return SensorName;
	}
	
	public void setName(String name)
	{
		SensorName = name;
	}
	
	public void SetPolygon(ArrayList<MyPoint> Polygon1)
	{
		Polygon = Polygon1;
	}
	
	public ArrayList<MyPoint> getPolygon()
	{
		return Polygon;
	}
}
