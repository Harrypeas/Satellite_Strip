package com.xc.worldwind.utils;

import java.awt.Color;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.airspaces.Airspace;
import gov.nasa.worldwind.render.airspaces.SphereAirspace;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.render.markers.MarkerAttributes;

/**
 * @author 钟翔
 * @class 材质工具类
 * @function 设置球体，线段，箭头等实体对象的材质
 */
public class MeterialUtil 
{
	public static Color makeBrighter(Color color)
	{
		float []hbsComponents = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hbsComponents);
		float hue = hbsComponents[0];
		float saturation =hbsComponents[1];
		float brightness =hbsComponents[2];
		saturation /= 3f;
		brightness*=3f;
		if(saturation<0f)
			saturation = 0f;
		if(brightness>1f)
			brightness = 1f;
		int rgbInt = Color.HSBtoRGB(hue, saturation, brightness);
		return new Color(rgbInt);
	}
	
	public static SphereAirspace makeSphere(double lat,double lng,double altitude,double radius,Color color)
	{
		SphereAirspace sphere = new SphereAirspace();
		sphere.setLocation(LatLon.fromDegrees(lat, lng));
		sphere.setAltitude(altitude);
		sphere.setTerrainConforming(true);
		sphere.setRadius(radius);
		
		Color outlineColor = makeBrighter(color);
		sphere.getAttributes().setDrawOutline(true);
		sphere.getAttributes().setMaterial(new Material(color));
		sphere.getAttributes().setOutlineMaterial(new Material(outlineColor));
		sphere.getAttributes().setOpacity(0.8);
		sphere.getAttributes().setOutlineOpacity(0.9);
		sphere.getAttributes().setOutlineWidth(3.0);
		return sphere;
	}

	public static Marker makeMarkerArrow(Position position,Position position2)
	{
		MarkerAttributes attsArrow = new BasicMarkerAttributes(Material.YELLOW,BasicMarkerShape.ORIENTED_SPHERE,1.0d,5,5);
		attsArrow.setHeadingMaterial(Material.GRAY);
		
		//MarkerAttributes attsLine = new BasicMarkerAttributes(Material.YELLOW,BasicMarkerShape.HEADING_LINE,0.9d);
		Marker marker = new BasicMarker(position, attsArrow);
		
		double x1 , y1, x2, y2;
		
		x1 = position.getLatitude().degrees;
		x2 = position2.getLatitude().degrees;
		y1 = position.getLongitude().degrees;
		y2 = position2.getLongitude().degrees;

		double deltx = x2 - x1;
		
		double delty = y2 - y1;

		double atan = Math.atan(delty/deltx);

		double angle = Angle.fromRadians(atan).degrees;
		if(y2>y1)	//数值增加
		{
			angle += 180;
			angle %= 180;
		}
		else if(y2<y1)
		{
			angle -= 180;
			angle %= 180;
		}
		marker.setHeading(Angle.fromDegrees(angle));
		return marker;
	}
	
	public static Marker makeMarkerLine(Position position)
	{
		MarkerAttributes attsLine = new BasicMarkerAttributes(Material.YELLOW,BasicMarkerShape.HEADING_LINE,0.5d);
		Marker marker = new BasicMarker(position, attsLine);
		return marker;
	}
	
	public static void setupDefaultMaterial(Airspace a,Color color)
	{
		Color outlineColor = makeBrighter(color);
		//绘制轨道的边界线
		a.getAttributes().setDrawOutline(true);
		a.getAttributes().setMaterial(new Material(color));
		a.getAttributes().setOutlineMaterial(new Material(outlineColor));
		a.getAttributes().setOpacity(0.1);
		a.getAttributes().setOutlineOpacity(1);
	}
}
