package com.xc.worldwind.SGP;
//Eci.java
//
//Copyright (c) 2003-2012 Michael F. Henry
//Version 09/2012
//
/// Encapsulates an Earth Centered Inertial coordinate position/velocity.
/// 封装了一个以地球为中心惯性坐标位置/速度。
public class Eci 
{

	   public Vector Position;    //位置对象
	   public Vector Velocity;    //速度对象

	   /// Creates a new instance of the class zero position and zero velocity.
	   /// 创建位置与速度均为0的实例对象
	   public Eci()
	   {
		   this(new Vector(), new Vector());
	   }

	   /// Creates a new instance of the class from XYZ coordinates.
	   /// 创建一个XYZ坐标系位置的实例对象
	   public Eci(Vector pos)
	   {
		   this(pos, new Vector());
	   }

	   /// Creates a new instance of the class with the given position and
	   /// velocity components.
	   /// 创建一个有坐标系与速度的实例对象
	   public Eci(Vector pos, Vector vel)
	   {
	      Position = pos;
	      Velocity = vel;
	   }

	   /// Creates a new instance of the class from ECI coordinates.
	   /// 通过ECI坐标创建新的实例对象
	   public Eci(Eci eci)
	   {
	      Position = new Vector(eci.Position);
	      Velocity = new Vector(eci.Velocity);
	   }

	   /// Creates a instance of the class from geodetic coordinates.
	   /// 通过大地测量坐标系创建新的实例对象
	   /// <param name="geo">The geocentric coordinates.地心坐标</param>
	   /// <param name="date">The Julian date.Julian时间</param>
	   /// <remarks>
	   /// Assumes the Earth is an oblate spheroid.假设地球是扁球体
	   /// Reference: The 1992 Astronomical Almanac, page K11参考:1992年天文年历,页面K11
	   /// Reference: www.celestrak.com (Dr. T.S. Kelso)
	   /// </remarks>
	   public Eci(Geo geo, Julian date)
	   {
	      double lat = geo.LatitudeRad;
	      double lon = geo.LongitudeRad;
	      double alt = geo.Altitude;

	      // Calculate Local Mean Sidereal Time (theta)
	      double theta = date.ToLmst(lon);
	      double c = 1.0 / Math.sqrt(1.0 + Globals.F * (Globals.F - 2.0) *
	                       Globals.Sqr(Math.sin(lat)));
	      double s = Globals.Sqr(1.0 - Globals.F) * c;
	      double achcp = (Globals.Xkmper * c + alt) * Math.cos(lat);

	      Position = new Vector();

	      Position.X = achcp * Math.cos(theta);             // km
	      Position.Y = achcp * Math.sin(theta);             // km
	      Position.Z = (Globals.Xkmper * s + alt) * Math.sin(lat);   // km
	      Position.W = Math.sqrt(Globals.Sqr(Position.X) +
	                             Globals.Sqr(Position.Y) +
	                             Globals.Sqr(Position.Z));  // range, km

	      Velocity = new Vector();
	      double mfactor = Globals.TwoPi * (Globals.OmegaE / Globals.SecPerDay);

	      Velocity.X = -mfactor * Position.Y;               // km / sec
	      Velocity.Y =  mfactor * Position.X;               // km / sec
	      Velocity.Z = 0.0;                                 // km / sec
	      Velocity.W = Math.sqrt(Globals.Sqr(Velocity.X) +  // range rate km/sec^2
	                             Globals.Sqr(Velocity.Y));
	   }

	   /// <summary>
	   /// Scale the position vector by a factor.
	   /// 通过系数放大缩小位置向量
	   /// </summary>
	   public void ScalePosVector(double factor)
	   {
	      Position.Mul(factor);
	   }

	   /// <summary>
	   /// Scale the velocity vector by a factor.
	   /// 通过系数放大缩小速度向量
	   /// </summary>
	   public void ScaleVelVector(double factor)
	   {
	      Velocity.Mul(factor);
	   }

	   /// <summary>
	   /// Returns a string representation of the coordinate and 
	   /// velocity XYZ values.
	   /// 返回一个字符串形式的坐标值与速度值
	   /// </summary>
	   /// <returns>The formatted string.格式化字符串</returns>
	   public String ToString()
	   {
	      return String.format("km:({0:F0}, {1:F0}, {2:F0}) km/s:({3:F1}, {4:F1}, {5:F1})",
	                           Position.X, Position.Y, Position.Z,
	                           Velocity.X, Velocity.Y, Velocity.Z);
	   }
}


