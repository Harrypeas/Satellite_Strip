package com.xc.ocs.object;

import java.awt.Point;

public class Sensor {
	private String id;
	private String name;
	private Point[] pos=new Point[5]; //0 sensor 1 主题  2 广度 3频度 4数据
	private String type;
	
	public void setID(String id)
	{
		this.id = id;
	}
	
	public String getID()
	{
		return this.id;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return this.type;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	public void setPos(Point[] _pos){
		for(int i=0;i<5;i++){this.pos[i]=_pos[i];}
		}
	public Point[] getPos(){return this.pos;}
	
}
