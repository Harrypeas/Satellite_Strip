package com.xc.worldwind.SGP;

//
//Orbit.java
//
//Copyright (c) 2005-2012 Michael F. Henry
//Version 06/2012
//
import java.util.Calendar;

/// This class accepts a single satellite's NORAD two-line element
/// set and provides information regarding the satellite's orbit 
/// such as period, axis length, ECI coordinates, velocity, etc.
public class Orbit 
{
	   // Caching variables
	 	private long m_Period = -1;
	   // private TimeSpan m_Period = new TimeSpan(0, 0, 0, -1);
	   // TLE caching variables
	   private double m_Inclination;
	   private double m_Eccentricity;
	   private double m_RAAN;
	   private double m_ArgPerigee;
	   private double m_BStar;
	   private double m_Drag;
	   private double m_MeanAnomaly;
	   private double m_TleMeanMotion;

	   // Caching variables recovered from the input TLE elements
	   private double m_aeAxisSemiMajorRec;  // semimajor axis, in AE units
	   private double m_aeAxisSemiMinorRec;  // semiminor axis, in AE units
	   private double m_rmMeanMotionRec;     // radians per minute
	   private double m_kmPerigeeRec;        // perigee, in km
	   private double m_kmApogeeRec;         // apogee, in km

	   private Tle Tles;
	   private NoradBase NoradModel;
	   public Julian Epoch;
	   public Calendar EpochTime;
	   
	   // "Recovered" from the input elements
	   public double SemiMajor = m_aeAxisSemiMajorRec; 
	   public double SemiMinor = m_aeAxisSemiMinorRec; 
	   public double MeanMotion= m_rmMeanMotionRec;    
	   public double Major = 2.0 * SemiMajor;
	   public double Minor = 2.0 * SemiMinor;   
	   public double Perigee = m_kmPerigeeRec;       
	   public double Apogee = m_kmApogeeRec;        

	   public double Inclination = m_Inclination;   
	   public double Eccentricity = m_Eccentricity;  
	   public double RAAN = m_RAAN;          
	   public double ArgPerigee = m_ArgPerigee;    
	   public double BStar = m_BStar;         
	   public double Drag = m_Drag;          
	   public double MeanAnomaly = m_MeanAnomaly;   
	   private double TleMeanMotion = m_TleMeanMotion; 

	   public String SatNoradId = ""; //Tles.getNoradNumber(); 
	   public String SatName = "";//Tles.getName();      
	   public String SatNameLong = ""; //SatName + " #" + SatNoradId; 

	   // "Recovered" from the input elements
	   public void getValue()
	   {
		    SemiMajor = m_aeAxisSemiMajorRec; 
		    SemiMinor = m_aeAxisSemiMinorRec; 
		    MeanMotion= m_rmMeanMotionRec;    
		    Major = 2.0 * SemiMajor;
		    Minor = 2.0 * SemiMinor;   
		    Perigee = m_kmPerigeeRec;       
		     Apogee = m_kmApogeeRec;        

		    Inclination = m_Inclination;   
		     Eccentricity = m_Eccentricity;  
		     RAAN = m_RAAN;          
		     ArgPerigee = m_ArgPerigee;    
		     BStar = m_BStar;         
		     Drag = m_Drag;          
		     MeanAnomaly = m_MeanAnomaly;   
		     TleMeanMotion = m_TleMeanMotion;
	   }
	   
	   public long period ()
	   {
		   if (m_Period < 0.0)//TotalSeconds:返回TimeSpan值表示的秒数。
           {
              // Calculate the period using the recovered mean motion.
              if (MeanMotion == 0)
              {
                 m_Period = 0;
              }
              else
              {
                 double secs  = (Globals.TwoPi / MeanMotion) * 60.0;
                 int    msecs = (int)((secs - (int)secs) * 1000);
                 m_Period=(long)(secs/60+msecs/1000);
              }
           }
           return m_Period;
	   }
	   
	   /// <summary>
	   /// Standard constructor.
	   /// </summary>
	   /// <param name="tle">Two-line element orbital parameters.</param>
	   public Orbit(Tle tle) throws Exception
	   {
		   Tles     = tle;
		   SatNoradId = Tles.getNoradNumber(); 
		   SatName =Tles.getName();      
		   SatNameLong = SatName + " #" + SatNoradId; 
	      Epoch = Tles.getEpochJulian();

	      EpochTime = Epoch.ToTime();
	      
	      m_Inclination   = GetRad(Tle.Field.Inclination);
	      m_Eccentricity  = Tles.GetField(Tle.Field.Eccentricity);
	      m_RAAN          = GetRad(Tle.Field.Raan);              
	      m_ArgPerigee    = GetRad(Tle.Field.ArgPerigee);        
	      m_BStar         = Tles.GetField(Tle.Field.BStarDrag);   
	      m_Drag          = Tles.GetField(Tle.Field.MeanMotionDt);
	      m_MeanAnomaly   = GetRad(Tle.Field.MeanAnomaly);
	      m_TleMeanMotion = Tles.GetField(Tle.Field.MeanMotion);  

	      getValue();
	      // Recover the original mean motion and semimajor axis from the
	      // input elements.
	      double mm     = TleMeanMotion;
	      double rpmin  = mm * Globals.TwoPi / Globals.MinPerDay;   // rads per minute

	      double a1     = Math.pow(Globals.Xke / rpmin, 2.0 / 3.0);
	      double e      = Eccentricity;
	      double i      = Inclination;
	      double temp   = (1.5 * Globals.Ck2 * (3.0 * Globals.Sqr(Math.cos(i)) - 1.0) / 
	                      Math.pow(1.0 - e * e, 1.5));   
	      double delta1 = temp / (a1 * a1);
	      double a0     = a1 * 
	                     (1.0 - delta1 * 
	                     ((1.0 / 3.0) + delta1 * 
	                     (1.0 + 134.0 / 81.0 * delta1)));

	      double delta0 = temp / (a0 * a0);

	      m_rmMeanMotionRec    = rpmin / (1.0 + delta0);
	      m_aeAxisSemiMajorRec = a0 / (1.0 - delta0);
	      m_aeAxisSemiMinorRec = m_aeAxisSemiMajorRec * Math.sqrt(1.0 - (e * e));
	      m_kmPerigeeRec       = Globals.Xkmper * (m_aeAxisSemiMajorRec * (1.0 - e) - Globals.Ae);
	      m_kmApogeeRec        = Globals.Xkmper * (m_aeAxisSemiMajorRec * (1.0 + e) - Globals.Ae);

	      getValue();
	      if (period() >= 225.0)
	      {
	         //SDP4 - period >= 225 minutes.
	    	  NoradModel = new NoradSDP4(this);
	      }
	      else
	      {
	         // SGP4 - period < 225 minutes
	         NoradModel = new NoradSGP4(this);			//值处理周期小于225min的卫星遥感
	      }
	   }

	   /// Calculate satellite ECI position/velocity for a given time.计算给定时间的卫星位置与速度
	   /// <param name="tsince">Target time, in minutes past the TLE epoch.</param>
	   /// <returns>Kilometer-based position/velocity ECI coordinates.</returns>
	   public EciTime GetPosition(double minutesPastEpoch)
	   {
	      EciTime eci = NoradModel.GetPosition(minutesPastEpoch);
	      // Convert ECI vector units from AU to kilometers
	      double radiusAe = Globals.Xkmper / Globals.Ae;

	      eci.ScalePosVector(radiusAe);                               // km
	      eci.ScaleVelVector(radiusAe * (Globals.MinPerDay / 86400)); // km/sec
	      return eci;
	   }

	   /// <summary>
	   /// Calculate ECI position/velocity for a given time.
	   /// </summary>
	   /// <param name="utc">Target time (UTC).</param>
	   /// <returns>Kilometer-based position/velocity ECI coordinates.</returns>
	   /*public EciTime GetPosition(Calendar utc)
	   {
	      return GetPosition(TPlusEpoch(utc).TotalMinutes);
	   }


	   // ///////////////////////////////////////////////////////////////////////////
	   // Returns elapsed time from epoch to given time.
	   // Note: "Predicted" TLEs can have epochs in the future.
	   public TimeSpan TPlusEpoch(DateTime utc) 
	   {
	      return (utc - EpochTime);
	   }

	   // ///////////////////////////////////////////////////////////////////////////
	   // Returns elapsed time from epoch to current time.
	   // Note: "Predicted" TLEs can have epochs in the future.
	   public TimeSpan TPlusEpoch()
	   {
	      return TPlusEpoch(DateTime.UtcNow);
	   }*/

	   // ///////////////////////////////////////////////////////////////////
	   protected double GetRad(Tle.Field fld) 
	   { 
	      return Tles.GetField(fld, Tle.Unit.Radians); 
	   }

	   // ///////////////////////////////////////////////////////////////////
	   protected double GetDeg(Tle.Field fld) 
	   { 
	      return Tles.GetField(fld, Tle.Unit.Degrees); 
	   }
}


