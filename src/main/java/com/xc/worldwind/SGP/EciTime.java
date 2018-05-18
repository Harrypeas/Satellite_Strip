package com.xc.worldwind.SGP;
/// Encapsulates an Earth Centered Inertial coordinate and 
/// associated time.
/// 封装与时间有关地球中心惯性坐标
public class EciTime extends Eci
{
	   /// The time associated with the ECI coordinates.
	   /// 与ECI坐标有关的时间
	   public Julian Date;

	   /// Creates an instance of the class with the given position, velocity, and time.
	   public EciTime(Vector pos, Vector vel, Julian date)
	   {
		   super(pos, vel);
	      Date = date;
	   }

	   /// Creates a new instance of the class from ECI-time coordinates.
	   public EciTime(EciTime eci, Julian date)
	   {
		   this(eci.Position, eci.Velocity, date);
	   }

	   /// Creates a new instance of the class from geodetic coordinates.
	   public EciTime(Geo geo, Julian date)
	   {
		   super(geo,date);
	      Date = date;
	   }

	   /// Creates a new instance of the class from geodetic-time coordinates.
	   public EciTime(GeoTime geo)
	   {
		   this(geo, geo.Date);
	   }

}

