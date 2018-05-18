package com.xc.ocs.object;

import java.util.ArrayList;
import java.util.Calendar;

import com.xc.worldwind.object.MyPoint;

public class TimeAndSpace {

	ArrayList<MyPoint> Polygon;
	private Calendar StartTime;
	private Calendar EndTime;
	private int sensortag;

	public TimeAndSpace()
	{
		StartTime = null;
		EndTime = null;
		sensortag = 0;
		Polygon = new ArrayList<MyPoint> ();				
	}
	public TimeAndSpace(Calendar start,Calendar end)
	{
		StartTime = start;
		EndTime = end;
		Polygon = new ArrayList<MyPoint> ();
		sensortag = 0;
	}
	
	public TimeAndSpace(Calendar start,Calendar end,ArrayList<MyPoint> Polygon1)
	{
		StartTime = start;
		EndTime = end;
		Polygon = new ArrayList<MyPoint> ();
		Polygon = Polygon1;
		sensortag = 0;
	}
	
	
	public Calendar getStartTime()
	{
		return StartTime;
	}
	
	public Calendar getEndTime()
	{
		return EndTime;
	}
	
	
	public int getSensorTag()
	{
		return sensortag;
	}
	
	public void setSensorTag(int newTag)
	{
		sensortag = newTag;
	}
	
	
	public void SetStartTime(Calendar newStart)
	{
		StartTime = newStart;
	}
	
	public void SetEndTime(Calendar newEnd)
	{
		EndTime = newEnd;
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
