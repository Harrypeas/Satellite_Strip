package com.xc.worldwind.object;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.xc.worldwind.SGP.EciTime;
import com.xc.worldwind.SGP.Geo;
import com.xc.worldwind.SGP.Globals;
import com.xc.worldwind.SGP.Orbit;
import com.xc.worldwind.SGP.Tle;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.AbstractBrowserBalloon;
import gov.nasa.worldwind.render.BalloonAttributes;
import gov.nasa.worldwind.render.BasicBalloonAttributes;
import gov.nasa.worldwind.render.GlobeBrowserBalloon;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Offset;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Size;
import gov.nasa.worldwind.util.WWUtil;

/**
 * @author 熊畅
 * @class 卫星实体类
 * @function 保存卫星实体信息
 */
public class Satelite
{
	private Long number;
	private String popular_name;
	private String catelog_name;
	private String type;
	private Position position;
	private Tle tle;
	public AbstractBrowserBalloon bollon;
	
	
	public void setTle(Tle tle)
	{
		this.tle = tle;
	}
	public Tle getTle()
	{
		return this.tle;
	}
	
	public void setNumber(Long number)
	{
		this.number = number;
	}
	public Long getNumber()
	{
		return this.number;
	}
	
	public void setPopular_name(String popular_name)
	{
		this.popular_name = popular_name;
	}
	public String getPopular_name()
	{
		return this.popular_name;
	}
	
	public void setCatelog_name(String catelog_name)
	{
		this.catelog_name = catelog_name;
	}
	public String getCatelog_name()
	{
		return this.catelog_name;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return this.type;
	}
	
	public void setPosition(Position position)
	{
		this.position = position;
	}
	public Position getPosition()
	{
		return this.position;
	}
	
	/**
	 * 获取卫星当前时刻的地理位置
	 * @return Postion经纬度坐标和高程信息
	 */
	public Position getCurrentPosition() 
	{
		Position pos = null;
		try
		{
			Orbit orbit = new Orbit(this.tle);
			double minutes = (Calendar.getInstance().getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
			EciTime eciTime = orbit.GetPosition(minutes);
			Geo geo= new Geo(eciTime, eciTime.Date);
        
			pos = new Position(Angle.fromDegrees(geo.LatitudeDeg), 
        		Angle.fromDegrees(geo.LongitudeDeg), geo.Altitude*1000);
		}
		catch(Exception ex)
		{
			return pos;
		}
		this.setPosition(pos);
        return pos;
	}
	
	/**
	 * 获取卫星某时刻的地理位置
	 * @return Postion经纬度坐标和高程信息
	 */
	public Position getPosition(long timeInMillis) 
	{
		Position pos = null;
		try
		{
			Orbit orbit = new Orbit(this.tle);
			double minutes = (timeInMillis - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
			EciTime eciTime = orbit.GetPosition(minutes);
			Geo geo= new Geo(eciTime, eciTime.Date);
        
			pos = new Position(Angle.fromDegrees(geo.LatitudeDeg), 
        		Angle.fromDegrees(geo.LongitudeDeg), geo.Altitude*1000);
		}
		catch(Exception ex)
		{
			return pos;
		}
		this.setPosition(pos);
        return pos;
	}
	
	/**
	 * 获取卫星当前时刻的地理位置
	 * @return 地理坐标系表示的三维坐标点Geo
	 */
	public Geo getCurrentGeo() 
	{
		Geo geo = null;
		try
		{
			Orbit orbit = new Orbit(this.tle);
			double minutes = (Calendar.getInstance().getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
			EciTime eciTime = orbit.GetPosition(minutes);
			geo = new Geo(eciTime, eciTime.Date);
		}
		catch(Exception ex)
		{
			return geo;
		}
        return geo;
	}
	
	/**
	 * 获取当前轨道信息
	 * @param cal1 开始观测日期格式时间
	 * @param cal2 结束观测日期格式时间
	 * @return Position集合
	 * @throws Exception
	 */
//	public  List<Position> getCurrentOrbits(long cal1,long cal2)
//    {
//		List<Position> Positions =  new ArrayList<Position>();
//		try
//		{
//			Calendar cal = Calendar.getInstance();
//			int step = VariableManage.Computeinterval; // 1*5s
//			
//			Geo geo = null;
//			Position pos = null;
//			for(long t = cal1;t <= cal2; t = t +step)
//			{
//				cal.setTimeInMillis(t);
//				Orbit orbit = new Orbit(tle);
//				double minutes = (cal.getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
//				EciTime eciTime = orbit.GetPosition(minutes);
//             
//				geo = new Geo(eciTime, eciTime.Date);
//				pos = new Position(Angle.fromDegrees(geo.LatitudeDeg),Angle.fromDegrees(geo.LongitudeDeg),geo.Altitude*1000);
//
//				Positions.add(pos);
//			}
//		}
//		catch(Exception ex)
//		{
//			ex.getMessage();
//			ex.printStackTrace();
//		}
//        return Positions;
//    }
	
	/**
	 * 设置并获得当前坐标点处的气泡信息
	 * @return AbstractBrowserBalloon对象
	 */
	public AbstractBrowserBalloon getBalloon() 
	{
		Position balloonPosition = getCurrentPosition();

		Orbit orbit = null;
		try {
			orbit = new Orbit(tle);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String lat=balloonPosition.latitude.toString();
		String log=balloonPosition.longitude.toString();
		if(lat.subSequence(0, 1).equals("-"))
		{
			lat="南纬"+lat.subSequence(1, 6)+lat.substring(lat.length()-1);
		}
		else if(log.length() >= 6){
			lat="北纬"+lat.subSequence(0, 6)+lat.substring(lat.length()-1);
		}
		if(log.subSequence(0, 1).equals("-"))
		{
			log="西经"+log.subSequence(1, 6)+log.substring(log.length()-1);
		}
		else if(log.length() >= 6) {
			log="东经"+log.subSequence(0, 6)+log.substring(log.length()-1);
		}
		
		String htmlString = "<b>卫星名称："+getTle().getName()+"</b><br>"
				+"<b>纬度：</b>"+lat+"<br>"
				+"<b>经度：</b>"+log+"<br>"
				+"<b>高度：</b>"+balloonPosition.getAltitude()+"m"+"<br>"
				+"<b>轨道参数：</b><br>"+getTle().getLine1()+"<br>"+getTle().getLine2()+"<br>"
				+"<b>轨道倾角：</b>"+Globals.ToDegrees(orbit.Inclination)+"（度）"+"<br>"
				+"<b>轨道长半径：</b>"+orbit.SemiMajor+"in AE units"+"<br>"
				+"<b>轨道短半径：</b>"+orbit.SemiMinor+"in AE units"+"<br>"
				+"<b>离心率：</b>"+orbit.Eccentricity+"<br>"
				+"<b>升交赤经：</b>"+Globals.ToDegrees(orbit.RAAN)+"<br>"
				+"<b>近地点：</b>"+orbit.Perigee+"km"+"<br>"
				+"<b>远地点：</b>"+orbit.Apogee+"km"+"<br>"
				+"<b>运动周期：</b>"+orbit.period()+"min"+"<br>";
		AbstractBrowserBalloon balloon = new GlobeBrowserBalloon(htmlString, balloonPosition);  
        BalloonAttributes attrs = new BasicBalloonAttributes();
        attrs.setSize(Size.fromPixels(700, 300));
        balloon.setAttributes(attrs);
        balloon.setVisible(false);
        this.bollon = balloon;
		return balloon;
	}
	
	/**
	 * 设置并获得当前坐标点处的PointPlacemark点，并与balloon关联
	 * @return PointPlacemark对象
	 */
//	public PointPlacemark getPlacemark()
//	{
//		Position balloonPosition = getCurrentPosition();
//		AbstractBrowserBalloon balloon = bollon;
//		PointPlacemark placemark = new PointPlacemark(balloonPosition);
//        PointPlacemarkAttributes attr = new PointPlacemarkAttributes();
//        if(VariableManage.isShowSensorName == true)//卫星名称
//        	 placemark.setLabelText(getPopular_name());       
//        if(VariableManage.isShowPins == true)//卫星向径
//   		    placemark.setLineEnabled(true);
//        placemark.setAltitudeMode(WorldWind.ABSOLUTE);
//        attr.setScale(0.6);
//        attr.setImageOffset(new Offset(19d, 8d, AVKey.PIXELS, AVKey.PIXELS));
//        attr.setLabelOffset(new Offset(0.9d, 0.6d, AVKey.FRACTION, AVKey.FRACTION));
//        attr.setLineMaterial(new Material(WWUtil.makeRandomColor(null)));
//        attr.setLineWidth(0d);
//        attr.setImageAddress("images/sate3_32.png");
//        placemark.setAttributes(attr);
//        placemark.setValue(AVKey.BALLOON,balloon);
//		return placemark;
//	}
	
}
