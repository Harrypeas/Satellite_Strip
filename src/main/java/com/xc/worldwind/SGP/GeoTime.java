package com.xc.worldwind.SGP;
/// <summary>
/// Class to encapsulate a geocentric coordinate and associated time.
/// 封装地心坐标和时间有关的类
/// </summary>
public final class GeoTime extends Geo
{
   /// <summary>
   /// The time associated with the coordinates.
   /// 与坐标有关的时间
   /// </summary>
   public Julian Date;
  
   /// Standard constructor.标准构造函数
   /// <param name="radLat">Latitude, in radians. Negative values indicate
   /// latitude south.</param>纬度值（弧度）负数为南纬
   /// <param name="radLon">Longitude, in radians. Negative value indicate longitude
   /// west.</param>经度值（弧度）负数为西经
   /// <param name="kmAlt">Altitude above the ellipsoid model, in kilometers.椭球体模型上的海拔（公里）</param>
   /// <param name="date">The time associated with the coordinates.与坐标关联的时间</param>
   public GeoTime(double radLat, double radLon, double kmAlt, Julian date)
   {
	   super(radLat, radLon, kmAlt);
      Date = date;
   }

   /// <summary>
   /// Constructor accepting Geo and Julian objects.接受Geo与Julian对象的构造函数
   /// </summary>
   /// <param name="geo">The Geo object.Geo对象</param>
   /// <param name="date">The Julian date.Julian时间对象</param>
   public GeoTime(GeoTime geo, Julian date)
   {
	   super(geo);
      Date = date;
   }

   /// <summary>
   /// Creates a new instance of the class from ECI-time information.
   /// 从ECI-time信息创建新的GeoTime实例对象
   /// </summary>
   /// <param name="eci">The ECI-time coordinate pair.ECI-time坐标对</param>
   /// <param name="ellipsoid">The earth ellipsoid model.地球椭球体模型</param>
   public GeoTime(EciTime eci)
   {
	   super(eci, eci.Date);
      Date = eci.Date;
   }

   /// <summary>
   /// Creates a new instance of the class from ECI coordinates.
   /// 从ECI-time坐标信息创建新的GeoTime实例对象
    /// </summary>
   /// <param name="eci">The ECI coordinates.ECI坐标</param>
   /// <param name="date">The Julian date.Julian时间</param>
   public GeoTime(Eci eci, Julian date)
   {
	   super(eci, date);
      Date = date;
   }
}

/// <summary>
/// Class to encapsulate topo-centric coordinates.
/// 封装topo-centric坐标的类
/// </summary>
class Topo  
{

   /// <summary>
    /// The azimuth, in radians.方位角（单位：弧度）
   /// </summary>
   public double AzimuthRad ;

   /// <summary>
   /// The elevation, in radians.仰角（单位：弧度）
   /// </summary>
   public double ElevationRad ;

   /// <summary>
   /// The azimuth, in degrees.方位角（单位：度）
   /// </summary>
   public double AzimuthDeg = Globals.ToDegrees(AzimuthRad);
   
   /// <summary>
   /// The elevation, in degrees.仰角（单位：度）
   /// </summary>
   public double ElevationDeg = Globals.ToDegrees(ElevationRad); 

   /// <summary>
   /// The range, in kilometers.范围（单位：公里）
   /// </summary>
   public double Range;

   /// <summary>
   /// The range rate, in kilometers per second. 
   /// A negative value means "towards observer".
   /// 范围速度（km/s)负数表示朝观察者
   /// </summary>
   public double RangeRate;

   /// <summary>
   /// Creates a new instance of the class from the given components.
   /// 从给定的组件创建新的实例对象
   /// </summary>
   /// <param name="radAz">Azimuth, in radians.方位角（单位：弧度）</param>
   /// <param name="radEl">Elevation, in radians.仰角（单位：弧度）</param>
   /// <param name="range">Range, in kilometers.范围（公里)</param>
   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
   /// range rate means "towards the observer".范围速度（km/s)</param>
   public Topo(double radAz, double radEl, double range, double rangeRate)
   {
      AzimuthRad   = radAz;
      ElevationRad = radEl;
      Range     = range;
      RangeRate = rangeRate;
   }
}

/// <summary>
/// Class to encapsulate topo-centric coordinates and a time.
/// 封装topo-centric坐标和时间的类
/// </summary>
 final class TopoTime extends Topo
{
   /// <summary>
   /// The time associated with the coordinates.
   /// 与坐标关联的时间
   /// </summary>
   public Julian Date ;

   /// <summary>
   /// Creates an instance of the class from topo and time information.
   /// 从拓扑与时间对象信息创建实例对象
   /// </summary>
   /// <param name="topo"></param>
   /// <param name="date"></param>
   public TopoTime(Topo topo, Julian date)
   {
	   super(topo.AzimuthRad, topo.ElevationRad, topo.Range, topo.RangeRate);
       Date = date;
   }

   /// <summary>
   /// Creates a new instance of the class from the given components.
   /// 从给定的组件创建新的实例对象
   /// </summary>
   /// <param name="radAz">Azimuth, in radians.方位角（单位：弧度）</param>
   /// <param name="radEl">Elevation, in radians.仰角（单位：弧度）</param>
   /// <param name="range">Range, in kilometers.范围（公里)</param>
   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
   /// range rate means "towards the observer".范围速度（km/s)</param>
  /// <param name="date">The time associated with the coordinates.与坐标关联的时间</param>
   public TopoTime(double radAz, double radEl, double range, double rangeRate, Julian date)
   {
	   super(radAz, radEl, range, rangeRate);
      Date = date;
   }
}