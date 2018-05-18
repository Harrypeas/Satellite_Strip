package com.xc.ocs.object;
import java.util.ArrayList;

public class SensorMultiRelations 
{
	public String sensorId1;
	public String sensorId2;
	public String sensorName1;
	public String sensorName2;
	public String timeRelation;
	public String spaceRelation;
	public ArrayList<String> theme1;
	public ArrayList<String> theme2;
	public ArrayList<String> themetype1;
	public ArrayList<String> themetype2;
	public ArrayList<SingleRelation> ABrelation;
	public ArrayList<SingleRelation> BArelation;
	
	public SensorMultiRelations()
	{
		this.sensorId1 = null;
		this.sensorId2 = null;
		this.sensorName1 = null;
		this.sensorName2 = null;
		this.theme1=new ArrayList<String>();
		this.theme2=new ArrayList<String>();
		this.themetype1=new ArrayList<String>();
		this.themetype2=new ArrayList<String>();
		this.ABrelation = new ArrayList<SingleRelation>();	 
		this.BArelation = new ArrayList<SingleRelation>();
	}
	public void add(String sensorId1,String sensorId2,String sensorName1,String sensorName2)
	 {
			this.sensorId1 = sensorId1;
			this.sensorId2 = sensorId2;
			this.sensorName1 = sensorName1;
			this.sensorName2 = sensorName2;
	 }

	public void add(String sensorId1,String sensorId2,String sensorName1,
			String sensorName2,ArrayList<SingleRelation> ABrelation)
	 {
			this.sensorId1 = sensorId1;
			this.sensorId2 = sensorId2;
			this.sensorName1 = sensorName1;
			this.sensorName2 = sensorName2;
			this.ABrelation = ABrelation;	 
	 }
	public void addABrelation(SingleRelation relation)
	{
		this.ABrelation.add(relation);	
	}
	public void mergerelation(ArrayList<SingleRelation> relation)
	{
		this.BArelation=relation;	
	}
	public void matchtheme(ArrayList<String> theme,int index) {
		if(index==1)
		theme1=theme;
		else 
		theme2=theme;
	}
	public void matchthemetype(ArrayList<String> themetype,int index) {
		if(index==1)
		themetype1=themetype;
		else 
		themetype2=themetype;
	}
}
