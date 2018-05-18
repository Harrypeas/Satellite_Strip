package com.xc.worldwind.object;

import java.awt.Color;
import java.awt.Font;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.AbstractBrowserBalloon;
import gov.nasa.worldwind.render.BalloonAttributes;
import gov.nasa.worldwind.render.BasicBalloonAttributes;
import gov.nasa.worldwind.render.GlobeBrowserBalloon;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Size;

public class InsituSensor {
	private String InsituName;
	private Position InsituPosition;
	private double InsituRadius;
	private AbstractBrowserBalloon bollon;
	private boolean showBalloon;
	private BasicBalloonAttributes attributes;
	
	public InsituSensor(String name,Position position,double r)
	{
		InsituName=name;
		InsituPosition=position;
		InsituRadius=r;
		showBalloon = false;
		 initBalloonAtributes();
		//computeInsituCoverage();
	}
	
	public boolean isShowBalloon()
	{
		return showBalloon;
	}
	
	public void setShowBalloon(boolean show)
	{
		showBalloon = show;
	}
	
	public String getInsituName()
	{
		return InsituName;
	}
	
	public void setInsituName(String NewName)
	{
		InsituName = NewName;
	}
	
	public Position getInsituPosition()
	{
		return InsituPosition;
	}

	public void setInsituPosition(Position NewPosition)
	{
		InsituPosition = NewPosition;
	}
	
	public double getInsituRadius()
	{
		return InsituRadius;
	}
	
	public void setInsituRadius(double NewRadius)
	{
		InsituRadius=NewRadius;
	}
	
	public AbstractBrowserBalloon getBalloon() 
	{
		Position balloonPosition = getInsituPosition();

		String lat=balloonPosition.latitude.toString();
		String log=balloonPosition.longitude.toString();
		if(lat.subSequence(0, 1).equals("-"))
		{
			lat="南纬"+lat.subSequence(1, 3)+lat.substring(lat.length()-1);
		}
		else if(log.length() >= 6){
			lat="北纬"+lat.subSequence(0, 3)+lat.substring(lat.length()-1);
		}
		if(log.subSequence(0, 1).equals("-"))
		{
			log="西经"+log.subSequence(1, 3)+log.substring(log.length()-1);
		}
		else if(log.length() >= 6) {
			log="东经"+log.subSequence(0, 3)+log.substring(log.length()-1);
		}
		
		String htmlString ="<font color = \"blue\"><u>" + "<b>卫星名称："+getInsituName()+"</b><br>"
				+"<b>纬度：</b>"+lat+"<br>"
				+"<b>经度：</b>"+log+"<br>"
				+"<b>高度：</b>"+balloonPosition.getAltitude()+"m"+"<br>" + "</u></font>";
		AbstractBrowserBalloon balloon = new GlobeBrowserBalloon(htmlString, balloonPosition);  
        balloon.setAttributes(attributes);
        balloon.setVisible(showBalloon);
        balloon.setValue("InsituName", InsituName);
        this.bollon = balloon;
		return balloon;
	}
	
	public static LatLon DisToLatLon(LatLon origionPostion,double distance,double angle)
	{
		LatLon latLon=null;
		double lat = origionPostion.getLatitude().degrees;
		double lng = origionPostion.getLongitude().degrees;
		double lat1 = lat + distance*Math.cos(angle)/111;
		double lng1 = lng + distance*Math.sin(angle)/(111*Math.cos(lat*Math.PI/180));
		latLon = new LatLon(Angle.fromDegrees(lat1), Angle.fromDegrees(lng1));
		return latLon;
	}
	
//	public PointPlacemark getPlacemark()
//	{
//		Position balloonPosition = getInsituPosition();
//		AbstractBrowserBalloon balloon = bollon;
//		PointPlacemark placemark = new PointPlacemark(balloonPosition);
//        PointPlacemarkAttributes attr = new PointPlacemarkAttributes();
//        if(VariableManage.isShowSensorName == true)//卫星名称
//        	 placemark.setLabelText(getInsituName());  
//        placemark.setAltitudeMode(WorldWind.ABSOLUTE);
//        attr.setScale(0.6);
//        attr.setLabelFont(new Font("宋体", 12, 12));
//        attr.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
//        attr.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
//        attr.setImageAddress("images/instusensor.png");
//        placemark.setAttributes(attr);
//        placemark.setValue(AVKey.BALLOON,balloon);
//		return placemark;
//	}
	
	public void initBalloonAtributes()
	{
		attributes = new BasicBalloonAttributes();
		attributes.setSize(Size.fromPixels(500, 300));
		attributes.setFont(new Font("宋体", 12, 12));
		attributes.setTextColor(Color.BLUE);
	}
	
	public BasicBalloonAttributes getBalloonAttributes()
	{
		return attributes;
	}
	
}
