package com.xc.worldwind.SGP;

import java.util.regex.Pattern;

public class SpatialRect 
{
	private double xMin = 0.0;
	private double xMax = 0.0;
	private double yMin = 0.0;
	private double yMax = 0.0;

	public boolean isCover = false;
	public double getXMin()
	{
		return this.xMin;
	}
	
	public double getYMin()
	{
		return this.yMin;
	}
	
	public double getXMax()
	{
		return this.xMax;
	}
	
	public double getYMax()
	{
		return this.yMax;
	}
	
	public SpatialRect()
	{
		xMin = 0.0;
		xMax = 0.0;
		yMin = 0.0;
		yMax = 0.0;
		isCover = false;
	}
	
	public SpatialRect(double x1,double y1,double x2,double y2,boolean isCover)
	{
		xMin = x1;
		xMax = x2;
		yMin = y1;
		yMax = y2;
		this.isCover = isCover;
	}
	
	public SpatialRect(double x1,double y1,double x2,double y2)
	{
		xMin = x1;
		xMax = x2;
		yMin = y1;
		yMax = y2;
		this.isCover = false;
	}
	
	public SpatialRect(String x1,String y1,String x2,String y2,boolean isCover)
	{
		xMin=yMin=xMax=yMax=0.0;
		Pattern pattern = Pattern.compile("[-?|0-9]*.?[0-9]*"); // [0-9]*
		if(pattern.matcher(x1).matches())
			xMin = Double.parseDouble(x1);
		if(pattern.matcher(x2).matches())
			xMax = Double.parseDouble(x2);
		if(pattern.matcher(y1).matches())
			yMin =Double.parseDouble(y1);
		if(pattern.matcher(y2).matches())
		{
			if(isCover)
				yMax = (Double.parseDouble(y2)+360)%360;
			else
				yMax = Double.parseDouble(y2);
		}
		this.isCover = isCover;
	}
	
	public SpatialRect(String x1,String y1,String x2,String y2)
	{
		xMin=yMin=xMax=yMax=0.0;
		Pattern pattern = Pattern.compile("[-?|0-9]*.?[0-9]*"); // [0-9]*
		if(pattern.matcher(x1).matches())
			xMin = Double.parseDouble(x1);
		if(pattern.matcher(x2).matches())
			xMax = Double.parseDouble(x2);
		if(pattern.matcher(y1).matches())
			yMin =Double.parseDouble(y1);
		if(pattern.matcher(y2).matches())
		{
			if(isCover)
				yMax = (Double.parseDouble(y2)+360)%360;
			else
				yMax = Double.parseDouble(y2);
		}
		this.isCover = false;
	}
	
	public void setxMin(double xMin)
	{
		this.xMin = xMin;
	}
	
	public void setxMax(double xMax)
	{
		this.xMax = xMax;
	}
	
	public void setyMin(double yMin)
	{
		this.yMin = yMin;
	}
	
	public void setyMax(double yMax)
	{
		this.yMax = yMax;
	}
	
	public void setRect(double x1,double y1,double x2,double y2)
	{
		xMin = x1;
		xMax = x2;
		yMin = y1;
		yMax = y2;
	}
}
