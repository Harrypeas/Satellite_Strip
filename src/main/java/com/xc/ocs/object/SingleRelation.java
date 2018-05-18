package com.xc.ocs.object;
import java.util.ArrayList;

import com.xc.worldwind.object.MyPoint;

public class SingleRelation {
	public ArrayList<MyPoint> spacePolygon;
	public ArrayList<String> timeRange;
	
	public String timeRelation;
	public String spaceRelation;
	
	public String relation;
	public SingleRelation ()
	{
		this.spacePolygon = new ArrayList<MyPoint>();
		this.timeRange = new ArrayList<String>();
	}
	public void add(ArrayList<MyPoint> Polygon,ArrayList<String>  timeRange)
	 {
			this.spacePolygon = Polygon;
			this.timeRange = timeRange;	 
	 }
	public void add(String spaceRelation,ArrayList<MyPoint> Polygon,String timeRelation,ArrayList<String>timeRange)
	 {     
		    this.timeRelation=timeRelation;
			this.spacePolygon = Polygon;
			this.timeRange = timeRange;
			this.spaceRelation=spaceRelation;	 
	 }
	public String getSpaceRelation() 
	{
		return spaceRelation;
	}
	public ArrayList<MyPoint> getPolygon() 
	{
		return spacePolygon;
		
	}
	public ArrayList<String> getTime()
	{
		return timeRange;
		
	}
	public String getTimeRelation() 
	{
		return timeRelation;
	}

}
