package com.xc.worldwind.SGP;
//
//Coord.java
//
//Copyright (c) 2003-2012 Michael F. Henry
//Version 10/2012
//
//using System.Globalization;

/// <summary>
/// Class to encapsulate geocentric coordinates.
/// 封装地心坐标类
/// </summary>
public class Geo  
{
   /// Latitude, in radians. A negative value indicates latitude south.
   /// 纬度值（弧度）负数表示南纬
   /// </summary>
   public double LatitudeRad;

   /// <summary>
   /// Longitude, in radians. A negative value indicates longitude west.
   /// 经度值（弧度）负数表示西经
    /// </summary>
   public double LongitudeRad;

   /// <summary>
   /// Latitude, in degrees. A negative value indicates latitude south.
   /// 纬度值（度）负数表示南纬
   /// </summary>
   public double LatitudeDeg; 

   /// <summary>
   /// Longitude, in degrees. A negative value indicates longitude west.
   /// 经度值（弧度）负数表示西经
    /// </summary>
   public double LongitudeDeg;

   /// <summary>
   /// Altitude, in kilometers, above the ellipsoid model.
   /// 海拔（公里）椭球体模型
   /// </summary>
   public double Altitude;

   /// Creates a Geo object from a source Geo object.
   public Geo(Geo geo)
   {
      LatitudeRad  = geo.LatitudeRad;
      LongitudeRad = geo.LongitudeRad;
      LatitudeDeg = Globals.ToDegrees(LatitudeRad);
      if(Globals.ToDegrees(LongitudeRad) >= 180)
		   LongitudeDeg = Globals.ToDegrees(LongitudeRad) - 360;
      else
   	   LongitudeDeg = Globals.ToDegrees(LongitudeRad);
      Altitude     = geo.Altitude;
   }

   /// <summary>
   /// Creates a new instance of the class with the given components.
   /// </summary>
   /// <param name="radLat">Latitude, in radians. Negative values indicate
   /// latitude south.</param>
   /// <param name="radLon">Longitude, in radians. Negative value indicate longitude
   /// west.</param>
   /// <param name="kmAlt">Altitude above the ellipsoid model, in kilometers.</param>
   public Geo(double radLat, double radLon, double kmAlt)
   {
      LatitudeRad  = radLat;
      LongitudeRad = radLon;
      LatitudeDeg = Globals.ToDegrees(LatitudeRad);
      if(Globals.ToDegrees(LongitudeRad) >= 180)
		   LongitudeDeg = Globals.ToDegrees(LongitudeRad) - 360;
      else
   	   LongitudeDeg = Globals.ToDegrees(LongitudeRad);
      Altitude     = kmAlt;
   }

   /// <summary>
   /// Creates a new instance of the class given ECI coordinates.
   /// </summary>
   /// <param name="eci">The ECI coordinates.</param>
   /// <param name="date">The Julian date.</param>
   public Geo(Eci eci, Julian date)
   {
	   this(eci.Position,(Globals.AcTan(eci.Position.Y, eci.Position.X) - date.ToGmst()) % Globals.TwoPi);
	   LatitudeDeg = Globals.ToDegrees(LatitudeRad);
	   
	   if(Globals.ToDegrees(LongitudeRad) >= 180)
		   LongitudeDeg = Globals.ToDegrees(LongitudeRad) - 360;
       else
    	   LongitudeDeg = Globals.ToDegrees(LongitudeRad);
   }

   /// <summary>
   /// Creates a new instance of the class given XYZ coordinates.
   /// </summary>
   private Geo(Vector pos, double theta)
   {
      theta = theta % Globals.TwoPi;

      if (theta < 0.0)
      {
         theta += Globals.TwoPi;
      }

      double r = Math.sqrt(Globals.Sqr(pos.X) + Globals.Sqr(pos.Y));
      double e2 = Globals.F * (2.0 - Globals.F);
      double lat = Globals.AcTan(pos.Z, r);

      final double DELTA = 1.0e-07;
      double phi;
      double c;

      do
      {
         phi = lat;
         c = 1.0 / Math.sqrt(1.0 - e2 * Globals.Sqr(Math.sin(phi)));
         lat = Globals.AcTan(pos.Z + Globals.Xkmper * c * e2 * Math.sin(phi), r);
      }
      while (Math.abs(lat - phi) > DELTA);

      LatitudeRad  = lat;
      LongitudeRad = theta;
      Altitude = (r / Math.cos(lat)) - Globals.Xkmper * c;
   }

   /// Converts to a string representation of the form "38.0N 045.0W 500m".
   public String ToString()
   {
      boolean latNorth = (LatitudeRad  >= 0.0);
      boolean lonEast  = (LongitudeRad >= 0.0);

      // latitude in degrees
      String str = String.format("{0:00.000}{1} ",
                                 Math.abs(LatitudeDeg),
                                 (latNorth ? 'N' : 'S'));
      // longitude in degrees
      str += String.format( "{0:000.000}{1} ",
                           Math.abs(LongitudeDeg),
                           (lonEast ? 'E' : 'W'));
      // elevation in meters
      str += String.format("{0:F3}km", Altitude);

      return str;
   }

}