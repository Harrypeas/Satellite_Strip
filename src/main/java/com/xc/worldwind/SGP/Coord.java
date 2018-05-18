package com.xc.worldwind.SGP;
//package com.zx.worldwind.SGP;
////
////Coord.java
////
////Copyright (c) 2003-2012 Michael F. Henry
////Version 10/2012
////
////using System.Globalization;
//
///// <summary>
///// Class to encapsulate geocentric coordinates.
///// 封装地心坐标类
///// </summary>
//class Geo  
//{
//   /// Latitude, in radians. A negative value indicates latitude south.
//   /// 纬度值（弧度）负数表示南纬
//   /// </summary>
//   public double LatitudeRad;
//
//   /// <summary>
//   /// Longitude, in radians. A negative value indicates longitude west.
//   /// 经度值（弧度）负数表示西经
//    /// </summary>
//   public double LongitudeRad;
//
//   /// <summary>
//   /// Latitude, in degrees. A negative value indicates latitude south.
//   /// 纬度值（度）负数表示南纬
//   /// </summary>
//   public double LatitudeDeg; 
//
//   /// <summary>
//   /// Longitude, in degrees. A negative value indicates longitude west.
//   /// 经度值（弧度）负数表示西经
//    /// </summary>
//   public double LongitudeDeg;
//
//   /// <summary>
//   /// Altitude, in kilometers, above the ellipsoid model.
//   /// 海拔（公里）椭球体模型
//   /// </summary>
//   public double Altitude;
//
//   /// Creates a Geo object from a source Geo object.
//   public Geo(Geo geo)
//   {
//      LatitudeRad  = geo.LatitudeRad;
//      LongitudeRad = geo.LongitudeRad;
//      LatitudeDeg = Globals.ToDegrees(LatitudeRad);
//      if(Globals.ToDegrees(LongitudeRad) >= 180)
//		   LongitudeDeg = Globals.ToDegrees(LongitudeRad) - 360;
//      else
//   	   LongitudeDeg = Globals.ToDegrees(LongitudeRad);
//      Altitude     = geo.Altitude;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class with the given components.
//   /// </summary>
//   /// <param name="radLat">Latitude, in radians. Negative values indicate
//   /// latitude south.</param>
//   /// <param name="radLon">Longitude, in radians. Negative value indicate longitude
//   /// west.</param>
//   /// <param name="kmAlt">Altitude above the ellipsoid model, in kilometers.</param>
//   public Geo(double radLat, double radLon, double kmAlt)
//   {
//      LatitudeRad  = radLat;
//      LongitudeRad = radLon;
//      LatitudeDeg = Globals.ToDegrees(LatitudeRad);
//      if(Globals.ToDegrees(LongitudeRad) >= 180)
//		   LongitudeDeg = Globals.ToDegrees(LongitudeRad) - 360;
//      else
//   	   LongitudeDeg = Globals.ToDegrees(LongitudeRad);
//      Altitude     = kmAlt;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class given ECI coordinates.
//   /// </summary>
//   /// <param name="eci">The ECI coordinates.</param>
//   /// <param name="date">The Julian date.</param>
//   public Geo(Eci eci, Julian date)
//   {
//	   this(eci.Position,(Globals.AcTan(eci.Position.Y, eci.Position.X) - date.ToGmst()) % Globals.TwoPi);
//	   LatitudeDeg = Globals.ToDegrees(LatitudeRad);
//	   
//	   if(Globals.ToDegrees(LongitudeRad) >= 180)
//		   LongitudeDeg = Globals.ToDegrees(LongitudeRad) - 360;
//       else
//    	   LongitudeDeg = Globals.ToDegrees(LongitudeRad);
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class given XYZ coordinates.
//   /// </summary>
//   private Geo(Vector pos, double theta)
//   {
//      theta = theta % Globals.TwoPi;
//
//      if (theta < 0.0)
//      {
//         theta += Globals.TwoPi;
//      }
//
//      double r = Math.sqrt(Globals.Sqr(pos.X) + Globals.Sqr(pos.Y));
//      double e2 = Globals.F * (2.0 - Globals.F);
//      double lat = Globals.AcTan(pos.Z, r);
//
//      final double DELTA = 1.0e-07;
//      double phi;
//      double c;
//
//      do
//      {
//         phi = lat;
//         c = 1.0 / Math.sqrt(1.0 - e2 * Globals.Sqr(Math.sin(phi)));
//         lat = Globals.AcTan(pos.Z + Globals.Xkmper * c * e2 * Math.sin(phi), r);
//      }
//      while (Math.abs(lat - phi) > DELTA);
//
//      LatitudeRad  = lat;
//      LongitudeRad = theta;
//      Altitude = (r / Math.cos(lat)) - Globals.Xkmper * c;
//   }
//
//   /// Converts to a string representation of the form "38.0N 045.0W 500m".
//   public String ToString()
//   {
//      boolean latNorth = (LatitudeRad  >= 0.0);
//      boolean lonEast  = (LongitudeRad >= 0.0);
//
//      // latitude in degrees
//      String str = String.format("{0:00.000}{1} ",
//                                 Math.abs(LatitudeDeg),
//                                 (latNorth ? 'N' : 'S'));
//      // longitude in degrees
//      str += String.format( "{0:000.000}{1} ",
//                           Math.abs(LongitudeDeg),
//                           (lonEast ? 'E' : 'W'));
//      // elevation in meters
//      str += String.format("{0:F3}km", Altitude);
//
//      return str;
//   }
//
//}
//
///// <summary>
///// Class to encapsulate a geocentric coordinate and associated time.
///// 封装地心坐标和时间有关的类
///// </summary>
//final class GeoTime extends Geo
//{
//   /// <summary>
//   /// The time associated with the coordinates.
//   /// 与坐标有关的时间
//   /// </summary>
//   public Julian Date;
//  
//   /// Standard constructor.标准构造函数
//   /// <param name="radLat">Latitude, in radians. Negative values indicate
//   /// latitude south.</param>纬度值（弧度）负数为南纬
//   /// <param name="radLon">Longitude, in radians. Negative value indicate longitude
//   /// west.</param>经度值（弧度）负数为西经
//   /// <param name="kmAlt">Altitude above the ellipsoid model, in kilometers.椭球体模型上的海拔（公里）</param>
//   /// <param name="date">The time associated with the coordinates.与坐标关联的时间</param>
//   public GeoTime(double radLat, double radLon, double kmAlt, Julian date)
//   {
//	   super(radLat, radLon, kmAlt);
//      Date = date;
//   }
//
//   /// <summary>
//   /// Constructor accepting Geo and Julian objects.接受Geo与Julian对象的构造函数
//   /// </summary>
//   /// <param name="geo">The Geo object.Geo对象</param>
//   /// <param name="date">The Julian date.Julian时间对象</param>
//   public GeoTime(Geo geo, Julian date)
//   {
//	   super(geo);
//      Date = date;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class from ECI-time information.
//   /// 从ECI-time信息创建新的GeoTime实例对象
//   /// </summary>
//   /// <param name="eci">The ECI-time coordinate pair.ECI-time坐标对</param>
//   /// <param name="ellipsoid">The earth ellipsoid model.地球椭球体模型</param>
//   public GeoTime(EciTime eci)
//   {
//	   super(eci, eci.Date);
//      Date = eci.Date;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class from ECI coordinates.
//   /// 从ECI-time坐标信息创建新的GeoTime实例对象
//    /// </summary>
//   /// <param name="eci">The ECI coordinates.ECI坐标</param>
//   /// <param name="date">The Julian date.Julian时间</param>
//   public GeoTime(Eci eci, Julian date)
//   {
//	   super(eci, date);
//      Date = date;
//   }
//}
//
///// <summary>
///// Class to encapsulate topo-centric coordinates.
///// 封装topo-centric坐标的类
///// </summary>
//class Topo  
//{
//
//   /// <summary>
//    /// The azimuth, in radians.方位角（单位：弧度）
//   /// </summary>
//   public double AzimuthRad ;
//
//   /// <summary>
//   /// The elevation, in radians.仰角（单位：弧度）
//   /// </summary>
//   public double ElevationRad ;
//
//   /// <summary>
//   /// The azimuth, in degrees.方位角（单位：度）
//   /// </summary>
//   public double AzimuthDeg = Globals.ToDegrees(AzimuthRad);
//   
//   /// <summary>
//   /// The elevation, in degrees.仰角（单位：度）
//   /// </summary>
//   public double ElevationDeg = Globals.ToDegrees(ElevationRad); 
//
//   /// <summary>
//   /// The range, in kilometers.范围（单位：公里）
//   /// </summary>
//   public double Range;
//
//   /// <summary>
//   /// The range rate, in kilometers per second. 
//   /// A negative value means "towards observer".
//   /// 范围速度（km/s)负数表示朝观察者
//   /// </summary>
//   public double RangeRate;
//
//   /// <summary>
//   /// Creates a new instance of the class from the given components.
//   /// 从给定的组件创建新的实例对象
//   /// </summary>
//   /// <param name="radAz">Azimuth, in radians.方位角（单位：弧度）</param>
//   /// <param name="radEl">Elevation, in radians.仰角（单位：弧度）</param>
//   /// <param name="range">Range, in kilometers.范围（公里)</param>
//   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
//   /// range rate means "towards the observer".范围速度（km/s)</param>
//   public Topo(double radAz, double radEl, double range, double rangeRate)
//   {
//      AzimuthRad   = radAz;
//      ElevationRad = radEl;
//      Range     = range;
//      RangeRate = rangeRate;
//   }
//}
//
///// <summary>
///// Class to encapsulate topo-centric coordinates and a time.
///// 封装topo-centric坐标和时间的类
///// </summary>
// final class TopoTime extends Topo
//{
//   /// <summary>
//   /// The time associated with the coordinates.
//   /// 与坐标关联的时间
//   /// </summary>
//   public Julian Date ;
//
//   /// <summary>
//   /// Creates an instance of the class from topo and time information.
//   /// 从拓扑与时间对象信息创建实例对象
//   /// </summary>
//   /// <param name="topo"></param>
//   /// <param name="date"></param>
//   public TopoTime(Topo topo, Julian date)
//   {
//	   super(topo.AzimuthRad, topo.ElevationRad, topo.Range, topo.RangeRate);
//       Date = date;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class from the given components.
//   /// 从给定的组件创建新的实例对象
//   /// </summary>
//   /// <param name="radAz">Azimuth, in radians.方位角（单位：弧度）</param>
//   /// <param name="radEl">Elevation, in radians.仰角（单位：弧度）</param>
//   /// <param name="range">Range, in kilometers.范围（公里)</param>
//   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
//   /// range rate means "towards the observer".范围速度（km/s)</param>
//  /// <param name="date">The time associated with the coordinates.与坐标关联的时间</param>
//   public TopoTime(double radAz, double radEl, double range, double rangeRate, Julian date)
//   {
//	   super(radAz, radEl, range, rangeRate);
//      Date = date;
//   }
//}