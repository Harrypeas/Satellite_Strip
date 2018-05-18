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
 * @author �ܳ�
 * @class ����ʵ����
 * @function ��������ʵ����Ϣ
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
	 * ��ȡ���ǵ�ǰʱ�̵ĵ���λ��
	 * @return Postion��γ������͸߳���Ϣ
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
	 * ��ȡ����ĳʱ�̵ĵ���λ��
	 * @return Postion��γ������͸߳���Ϣ
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
	 * ��ȡ���ǵ�ǰʱ�̵ĵ���λ��
	 * @return ��������ϵ��ʾ����ά�����Geo
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
	 * ��ȡ��ǰ�����Ϣ
	 * @param cal1 ��ʼ�۲����ڸ�ʽʱ��
	 * @param cal2 �����۲����ڸ�ʽʱ��
	 * @return Position����
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
	 * ���ò���õ�ǰ����㴦��������Ϣ
	 * @return AbstractBrowserBalloon����
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
			lat="��γ"+lat.subSequence(1, 6)+lat.substring(lat.length()-1);
		}
		else if(log.length() >= 6){
			lat="��γ"+lat.subSequence(0, 6)+lat.substring(lat.length()-1);
		}
		if(log.subSequence(0, 1).equals("-"))
		{
			log="����"+log.subSequence(1, 6)+log.substring(log.length()-1);
		}
		else if(log.length() >= 6) {
			log="����"+log.subSequence(0, 6)+log.substring(log.length()-1);
		}
		
		String htmlString = "<b>�������ƣ�"+getTle().getName()+"</b><br>"
				+"<b>γ�ȣ�</b>"+lat+"<br>"
				+"<b>���ȣ�</b>"+log+"<br>"
				+"<b>�߶ȣ�</b>"+balloonPosition.getAltitude()+"m"+"<br>"
				+"<b>���������</b><br>"+getTle().getLine1()+"<br>"+getTle().getLine2()+"<br>"
				+"<b>�����ǣ�</b>"+Globals.ToDegrees(orbit.Inclination)+"���ȣ�"+"<br>"
				+"<b>������뾶��</b>"+orbit.SemiMajor+"in AE units"+"<br>"
				+"<b>����̰뾶��</b>"+orbit.SemiMinor+"in AE units"+"<br>"
				+"<b>�����ʣ�</b>"+orbit.Eccentricity+"<br>"
				+"<b>�����ྭ��</b>"+Globals.ToDegrees(orbit.RAAN)+"<br>"
				+"<b>���ص㣺</b>"+orbit.Perigee+"km"+"<br>"
				+"<b>Զ�ص㣺</b>"+orbit.Apogee+"km"+"<br>"
				+"<b>�˶����ڣ�</b>"+orbit.period()+"min"+"<br>";
		AbstractBrowserBalloon balloon = new GlobeBrowserBalloon(htmlString, balloonPosition);  
        BalloonAttributes attrs = new BasicBalloonAttributes();
        attrs.setSize(Size.fromPixels(700, 300));
        balloon.setAttributes(attrs);
        balloon.setVisible(false);
        this.bollon = balloon;
		return balloon;
	}
	
	/**
	 * ���ò���õ�ǰ����㴦��PointPlacemark�㣬����balloon����
	 * @return PointPlacemark����
	 */
//	public PointPlacemark getPlacemark()
//	{
//		Position balloonPosition = getCurrentPosition();
//		AbstractBrowserBalloon balloon = bollon;
//		PointPlacemark placemark = new PointPlacemark(balloonPosition);
//        PointPlacemarkAttributes attr = new PointPlacemarkAttributes();
//        if(VariableManage.isShowSensorName == true)//��������
//        	 placemark.setLabelText(getPopular_name());       
//        if(VariableManage.isShowPins == true)//������
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
