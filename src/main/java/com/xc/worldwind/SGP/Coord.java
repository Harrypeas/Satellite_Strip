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
///// ��װ����������
///// </summary>
//class Geo  
//{
//   /// Latitude, in radians. A negative value indicates latitude south.
//   /// γ��ֵ�����ȣ�������ʾ��γ
//   /// </summary>
//   public double LatitudeRad;
//
//   /// <summary>
//   /// Longitude, in radians. A negative value indicates longitude west.
//   /// ����ֵ�����ȣ�������ʾ����
//    /// </summary>
//   public double LongitudeRad;
//
//   /// <summary>
//   /// Latitude, in degrees. A negative value indicates latitude south.
//   /// γ��ֵ���ȣ�������ʾ��γ
//   /// </summary>
//   public double LatitudeDeg; 
//
//   /// <summary>
//   /// Longitude, in degrees. A negative value indicates longitude west.
//   /// ����ֵ�����ȣ�������ʾ����
//    /// </summary>
//   public double LongitudeDeg;
//
//   /// <summary>
//   /// Altitude, in kilometers, above the ellipsoid model.
//   /// ���Σ����������ģ��
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
///// ��װ���������ʱ���йص���
///// </summary>
//final class GeoTime extends Geo
//{
//   /// <summary>
//   /// The time associated with the coordinates.
//   /// �������йص�ʱ��
//   /// </summary>
//   public Julian Date;
//  
//   /// Standard constructor.��׼���캯��
//   /// <param name="radLat">Latitude, in radians. Negative values indicate
//   /// latitude south.</param>γ��ֵ�����ȣ�����Ϊ��γ
//   /// <param name="radLon">Longitude, in radians. Negative value indicate longitude
//   /// west.</param>����ֵ�����ȣ�����Ϊ����
//   /// <param name="kmAlt">Altitude above the ellipsoid model, in kilometers.������ģ���ϵĺ��Σ����</param>
//   /// <param name="date">The time associated with the coordinates.�����������ʱ��</param>
//   public GeoTime(double radLat, double radLon, double kmAlt, Julian date)
//   {
//	   super(radLat, radLon, kmAlt);
//      Date = date;
//   }
//
//   /// <summary>
//   /// Constructor accepting Geo and Julian objects.����Geo��Julian����Ĺ��캯��
//   /// </summary>
//   /// <param name="geo">The Geo object.Geo����</param>
//   /// <param name="date">The Julian date.Julianʱ�����</param>
//   public GeoTime(Geo geo, Julian date)
//   {
//	   super(geo);
//      Date = date;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class from ECI-time information.
//   /// ��ECI-time��Ϣ�����µ�GeoTimeʵ������
//   /// </summary>
//   /// <param name="eci">The ECI-time coordinate pair.ECI-time�����</param>
//   /// <param name="ellipsoid">The earth ellipsoid model.����������ģ��</param>
//   public GeoTime(EciTime eci)
//   {
//	   super(eci, eci.Date);
//      Date = eci.Date;
//   }
//
//   /// <summary>
//   /// Creates a new instance of the class from ECI coordinates.
//   /// ��ECI-time������Ϣ�����µ�GeoTimeʵ������
//    /// </summary>
//   /// <param name="eci">The ECI coordinates.ECI����</param>
//   /// <param name="date">The Julian date.Julianʱ��</param>
//   public GeoTime(Eci eci, Julian date)
//   {
//	   super(eci, date);
//      Date = date;
//   }
//}
//
///// <summary>
///// Class to encapsulate topo-centric coordinates.
///// ��װtopo-centric�������
///// </summary>
//class Topo  
//{
//
//   /// <summary>
//    /// The azimuth, in radians.��λ�ǣ���λ�����ȣ�
//   /// </summary>
//   public double AzimuthRad ;
//
//   /// <summary>
//   /// The elevation, in radians.���ǣ���λ�����ȣ�
//   /// </summary>
//   public double ElevationRad ;
//
//   /// <summary>
//   /// The azimuth, in degrees.��λ�ǣ���λ���ȣ�
//   /// </summary>
//   public double AzimuthDeg = Globals.ToDegrees(AzimuthRad);
//   
//   /// <summary>
//   /// The elevation, in degrees.���ǣ���λ���ȣ�
//   /// </summary>
//   public double ElevationDeg = Globals.ToDegrees(ElevationRad); 
//
//   /// <summary>
//   /// The range, in kilometers.��Χ����λ�����
//   /// </summary>
//   public double Range;
//
//   /// <summary>
//   /// The range rate, in kilometers per second. 
//   /// A negative value means "towards observer".
//   /// ��Χ�ٶȣ�km/s)������ʾ���۲���
//   /// </summary>
//   public double RangeRate;
//
//   /// <summary>
//   /// Creates a new instance of the class from the given components.
//   /// �Ӹ�������������µ�ʵ������
//   /// </summary>
//   /// <param name="radAz">Azimuth, in radians.��λ�ǣ���λ�����ȣ�</param>
//   /// <param name="radEl">Elevation, in radians.���ǣ���λ�����ȣ�</param>
//   /// <param name="range">Range, in kilometers.��Χ������)</param>
//   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
//   /// range rate means "towards the observer".��Χ�ٶȣ�km/s)</param>
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
///// ��װtopo-centric�����ʱ�����
///// </summary>
// final class TopoTime extends Topo
//{
//   /// <summary>
//   /// The time associated with the coordinates.
//   /// �����������ʱ��
//   /// </summary>
//   public Julian Date ;
//
//   /// <summary>
//   /// Creates an instance of the class from topo and time information.
//   /// ��������ʱ�������Ϣ����ʵ������
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
//   /// �Ӹ�������������µ�ʵ������
//   /// </summary>
//   /// <param name="radAz">Azimuth, in radians.��λ�ǣ���λ�����ȣ�</param>
//   /// <param name="radEl">Elevation, in radians.���ǣ���λ�����ȣ�</param>
//   /// <param name="range">Range, in kilometers.��Χ������)</param>
//   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
//   /// range rate means "towards the observer".��Χ�ٶȣ�km/s)</param>
//  /// <param name="date">The time associated with the coordinates.�����������ʱ��</param>
//   public TopoTime(double radAz, double radEl, double range, double rangeRate, Julian date)
//   {
//	   super(radAz, radEl, range, rangeRate);
//      Date = date;
//   }
//}