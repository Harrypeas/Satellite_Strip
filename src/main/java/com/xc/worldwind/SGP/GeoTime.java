package com.xc.worldwind.SGP;
/// <summary>
/// Class to encapsulate a geocentric coordinate and associated time.
/// ��װ���������ʱ���йص���
/// </summary>
public final class GeoTime extends Geo
{
   /// <summary>
   /// The time associated with the coordinates.
   /// �������йص�ʱ��
   /// </summary>
   public Julian Date;
  
   /// Standard constructor.��׼���캯��
   /// <param name="radLat">Latitude, in radians. Negative values indicate
   /// latitude south.</param>γ��ֵ�����ȣ�����Ϊ��γ
   /// <param name="radLon">Longitude, in radians. Negative value indicate longitude
   /// west.</param>����ֵ�����ȣ�����Ϊ����
   /// <param name="kmAlt">Altitude above the ellipsoid model, in kilometers.������ģ���ϵĺ��Σ����</param>
   /// <param name="date">The time associated with the coordinates.�����������ʱ��</param>
   public GeoTime(double radLat, double radLon, double kmAlt, Julian date)
   {
	   super(radLat, radLon, kmAlt);
      Date = date;
   }

   /// <summary>
   /// Constructor accepting Geo and Julian objects.����Geo��Julian����Ĺ��캯��
   /// </summary>
   /// <param name="geo">The Geo object.Geo����</param>
   /// <param name="date">The Julian date.Julianʱ�����</param>
   public GeoTime(GeoTime geo, Julian date)
   {
	   super(geo);
      Date = date;
   }

   /// <summary>
   /// Creates a new instance of the class from ECI-time information.
   /// ��ECI-time��Ϣ�����µ�GeoTimeʵ������
   /// </summary>
   /// <param name="eci">The ECI-time coordinate pair.ECI-time�����</param>
   /// <param name="ellipsoid">The earth ellipsoid model.����������ģ��</param>
   public GeoTime(EciTime eci)
   {
	   super(eci, eci.Date);
      Date = eci.Date;
   }

   /// <summary>
   /// Creates a new instance of the class from ECI coordinates.
   /// ��ECI-time������Ϣ�����µ�GeoTimeʵ������
    /// </summary>
   /// <param name="eci">The ECI coordinates.ECI����</param>
   /// <param name="date">The Julian date.Julianʱ��</param>
   public GeoTime(Eci eci, Julian date)
   {
	   super(eci, date);
      Date = date;
   }
}

/// <summary>
/// Class to encapsulate topo-centric coordinates.
/// ��װtopo-centric�������
/// </summary>
class Topo  
{

   /// <summary>
    /// The azimuth, in radians.��λ�ǣ���λ�����ȣ�
   /// </summary>
   public double AzimuthRad ;

   /// <summary>
   /// The elevation, in radians.���ǣ���λ�����ȣ�
   /// </summary>
   public double ElevationRad ;

   /// <summary>
   /// The azimuth, in degrees.��λ�ǣ���λ���ȣ�
   /// </summary>
   public double AzimuthDeg = Globals.ToDegrees(AzimuthRad);
   
   /// <summary>
   /// The elevation, in degrees.���ǣ���λ���ȣ�
   /// </summary>
   public double ElevationDeg = Globals.ToDegrees(ElevationRad); 

   /// <summary>
   /// The range, in kilometers.��Χ����λ�����
   /// </summary>
   public double Range;

   /// <summary>
   /// The range rate, in kilometers per second. 
   /// A negative value means "towards observer".
   /// ��Χ�ٶȣ�km/s)������ʾ���۲���
   /// </summary>
   public double RangeRate;

   /// <summary>
   /// Creates a new instance of the class from the given components.
   /// �Ӹ�������������µ�ʵ������
   /// </summary>
   /// <param name="radAz">Azimuth, in radians.��λ�ǣ���λ�����ȣ�</param>
   /// <param name="radEl">Elevation, in radians.���ǣ���λ�����ȣ�</param>
   /// <param name="range">Range, in kilometers.��Χ������)</param>
   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
   /// range rate means "towards the observer".��Χ�ٶȣ�km/s)</param>
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
/// ��װtopo-centric�����ʱ�����
/// </summary>
 final class TopoTime extends Topo
{
   /// <summary>
   /// The time associated with the coordinates.
   /// �����������ʱ��
   /// </summary>
   public Julian Date ;

   /// <summary>
   /// Creates an instance of the class from topo and time information.
   /// ��������ʱ�������Ϣ����ʵ������
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
   /// �Ӹ�������������µ�ʵ������
   /// </summary>
   /// <param name="radAz">Azimuth, in radians.��λ�ǣ���λ�����ȣ�</param>
   /// <param name="radEl">Elevation, in radians.���ǣ���λ�����ȣ�</param>
   /// <param name="range">Range, in kilometers.��Χ������)</param>
   /// <param name="rangeRate">Range rate, in kilometers per second. A negative
   /// range rate means "towards the observer".��Χ�ٶȣ�km/s)</param>
  /// <param name="date">The time associated with the coordinates.�����������ʱ��</param>
   public TopoTime(double radAz, double radEl, double range, double rangeRate, Julian date)
   {
	   super(radAz, radEl, range, rangeRate);
      Date = date;
   }
}