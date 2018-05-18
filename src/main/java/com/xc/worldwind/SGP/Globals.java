package com.xc.worldwind.SGP;

import java.io.Serializable;

public class Globals 
{
	   public final static double Pi            = 3.141592653589793;
	   public final static double TwoPi         = 2.0 * Pi;
	   public final static double RadsPerDegree = Pi / 180.0;
	   public final static double DegreesPerRad = 180.0 / Pi;

	   public final static double Gm          = 398601.2;   // Earth gravitational finalant, km^3/sec^2
	   public final static double GeoSyncAlt  = 42241.892;  // km
	   public final static double EarthDiam   = 12800.0;    // km
	   public final static double DaySidereal = (23 * 3600) + (56 * 60) + 4.09;  // sec
	   public final static double DaySolar    = (24 * 3600);   // sec

	   public final static double Ae          = 1.0;
	   public final static double Au          = 149597870.0;  // Astronomical unit (km) (IAU 76)
	   public final static double Sr          = 696000.0;     // Solar radius (km)      (IAU 76)
	   public final static double Xkmper      = 6378.135;     // Earth equatorial radius - kilometers (WGS '72)
	   public final static double F           = 1.0 / 298.26; // Earth flattening (WGS '72)
	   public final static double Ge          = 398600.8;     // Earth gravitational finalant (WGS '72)
	   public final static double J2          = 1.0826158E-3; // J2 harmonic (WGS '72)
	   public final static double J3          = -2.53881E-6;  // J3 harmonic (WGS '72)
	   public final static double J4          = -1.65597E-6;  // J4 harmonic (WGS '72)
	   public final static double Ck2         = J2 / 2.0;
	   public final static double Ck4         = -3.0 * J4 / 8.0;
	   public final static double Xj3         = J3;
	   public final static double Qo          = Ae + 120.0 / Xkmper;
	   public final static double S           = Ae + 78.0  / Xkmper;
	   public final static double MinPerDay   = 1440.0;        // Minutes per day (solar)
	   public final static double SecPerDay   = 86400.0;       // Seconds per day (solar)
	   public final static double OmegaE      = 1.00273790934; // Earth rotation per sidereal day
	   public final static double Xke        = Math.sqrt(3600.0 * Ge / 
	                                        (Xkmper * Xkmper * Xkmper)); // sqrt(ge) ER^3/min^2
	   public static double Qoms2t     = Math.pow((Qo -S), 4); //(QO - S)^4 ER^4
	   
	   public static double Sqr(double x) 
	   {
	      return (x * x);
	   }

	   // ///////////////////////////////////////////////////////////////////////////
	   public static double Fmod2p(double arg)
	   {
	      double modu = (arg % TwoPi);

	      if (modu < 0.0)
	      {
	         modu += TwoPi;
	      }

	      return modu;
	   }

	   // ///////////////////////////////////////////////////////////////////////////
	   // Globals.AcTan()正切函数
	   // ArcTangent of sin(x) / cos(x). The advantage of this function over arctan()
	   // is that it returns the correct quadrant of the angle.返回正确的象限角
	   public static double AcTan(double sinx, double cosx)
	   {
	      double ret;

	      if (cosx == 0.0)
	      {
	         if (sinx > 0.0)
	         {
	            ret = Pi / 2.0;
	         }
	         else
	         {
	            ret = 3.0 * Pi / 2.0;
	         }
	      }
	      else
	      {
	         if (cosx > 0.0)
	         {
	            ret = Math.atan(sinx / cosx);
	         }
	         else
	         {
	            ret = Pi + Math.atan(sinx / cosx);
	         }
	      }
	      return ret;
	   }

	   //弧度到度数的转换
	   public static double ToDegrees(double radians)
	   {
	      return radians * DegreesPerRad;
	   }

	   //度数到弧度的转换
	   public static double ToRadians(double degrees)
	   {
	      return degrees * RadsPerDegree;
	   }	 
	   
	   //求解距离
	   public static double getDistance(double a1,double b1,double a2,double b2)
	   {
		   return Math.sqrt(Math.pow((a1-a2), 2)+Math.pow((b1-b2), 2));
	   }
}


 class PropagationException extends Exception implements Serializable
{
	private static final long serialVersionUID = 1L;

	public PropagationException() { }
	public PropagationException(String message) {super(message); }
	public PropagationException(String message, Exception inner) {super(message,inner); }

   // finalructor used for deserialization.
  /* protected PropagationException(SerializationInfo info, StreamingContext context)
      : base(info, context)
   {
   }*/
}

/// <summary>
 /// The GMT when the satellite orbit decays.
 /// </summary>
 final class DecayException extends PropagationException implements Serializable
{
	private static final long serialVersionUID = 1L;
	//public DateTime DecayTime { get; private set; }

   /// <summary>
   /// The name of the satellite whose orbit decayed.
   /// </summary>
   //public String SatelliteName { get; private set; }
   public DecayException() { }
   public DecayException(String message)  {super(message); }
   public DecayException(String message, Exception inner) {super(message,inner); }
   /*public DecayException(DateTime decayTime, string satelliteName)
   {
      DecayTime = decayTime;
      SatelliteName = satelliteName;
   }

   // finalructor used for deserialization.
   private DecayException(SerializationInfo info, StreamingContext context)
      : base(info, context)
   {
   }*/
}
