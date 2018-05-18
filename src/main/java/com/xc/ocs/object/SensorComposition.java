package com.xc.ocs.object;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SensorComposition {
	private String compName;
	private List<String> sensorid;
	private int type;//0 ����  1 ��� 2Ƶ�� 3����
	private Point pos;
	public SensorComposition(){
		sensorid=new ArrayList<String>();
	}
	
	public void setName(String compName)
	{
		this.compName = compName;
	}
	
	public String getName()
	{
		return this.compName;
	}
	
	public void setSensorsid(List<String> sensorid)
	{
		this.sensorid.addAll(sensorid);
	}
	
	public List<String> getSensorsid()
	{
		return this.sensorid;
	}
	
	public void setType(int type)
	{
		this.type = type;
	}
	
	public int getType()
	{
		return this.type;
	}
	
	public void setPos(Point point)
	{
		this.pos = point;
	}
	
	public Point getPos()
	{
		return this.pos;
	}
}
