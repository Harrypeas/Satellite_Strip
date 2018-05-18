package com.xc.worldwind.SGP;
//Eci.java
//
//Copyright (c) 2003-2012 Michael F. Henry
//Version 09/2012
//
/// Encapsulates an Earth Centered Inertial coordinate position/velocity.
/// ��װ��һ���Ե���Ϊ���Ĺ�������λ��/�ٶȡ�
public class Eci 
{

	   public Vector Position;    //λ�ö���
	   public Vector Velocity;    //�ٶȶ���

	   /// Creates a new instance of the class zero position and zero velocity.
	   /// ����λ�����ٶȾ�Ϊ0��ʵ������
	   public Eci()
	   {
		   this(new Vector(), new Vector());
	   }

	   /// Creates a new instance of the class from XYZ coordinates.
	   /// ����һ��XYZ����ϵλ�õ�ʵ������
	   public Eci(Vector pos)
	   {
		   this(pos, new Vector());
	   }

	   /// Creates a new instance of the class with the given position and
	   /// velocity components.
	   /// ����һ��������ϵ���ٶȵ�ʵ������
	   public Eci(Vector pos, Vector vel)
	   {
	      Position = pos;
	      Velocity = vel;
	   }

	   /// Creates a new instance of the class from ECI coordinates.
	   /// ͨ��ECI���괴���µ�ʵ������
	   public Eci(Eci eci)
	   {
	      Position = new Vector(eci.Position);
	      Velocity = new Vector(eci.Velocity);
	   }

	   /// Creates a instance of the class from geodetic coordinates.
	   /// ͨ����ز�������ϵ�����µ�ʵ������
	   /// <param name="geo">The geocentric coordinates.��������</param>
	   /// <param name="date">The Julian date.Julianʱ��</param>
	   /// <remarks>
	   /// Assumes the Earth is an oblate spheroid.��������Ǳ�����
	   /// Reference: The 1992 Astronomical Almanac, page K11�ο�:1992����������,ҳ��K11
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
	   /// ͨ��ϵ���Ŵ���Сλ������
	   /// </summary>
	   public void ScalePosVector(double factor)
	   {
	      Position.Mul(factor);
	   }

	   /// <summary>
	   /// Scale the velocity vector by a factor.
	   /// ͨ��ϵ���Ŵ���С�ٶ�����
	   /// </summary>
	   public void ScaleVelVector(double factor)
	   {
	      Velocity.Mul(factor);
	   }

	   /// <summary>
	   /// Returns a string representation of the coordinate and 
	   /// velocity XYZ values.
	   /// ����һ���ַ�����ʽ������ֵ���ٶ�ֵ
	   /// </summary>
	   /// <returns>The formatted string.��ʽ���ַ���</returns>
	   public String ToString()
	   {
	      return String.format("km:({0:F0}, {1:F0}, {2:F0}) km/s:({3:F1}, {4:F1}, {5:F1})",
	                           Position.X, Position.Y, Position.Z,
	                           Velocity.X, Velocity.Y, Velocity.Z);
	   }
}


