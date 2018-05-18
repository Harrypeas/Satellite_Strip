package com.xc.worldwind.SGP;

import java.util.Hashtable;
//using System.Globalization;
//
//Tle.java
//
//Copyright (c) 2003-2012 Michael F. Henry
//Version 06/2012
//

//////////////////////////////////////////////////////////////////////////
//
//NASA Two-Line Element Data format
//
//[Reference: Dr. T.S. Kelso / www.celestrak.com]
//
//Two-line element data consists of three lines in the following format:
//
//AAAAAAAAAAAAAAAAAAAAAAAA
//1 NNNNNU NNNNNAAA NNNNN.NNNNNNNN +.NNNNNNNN +NNNNN-N +NNNNN-N N NNNNN
//2 NNNNN NNN.NNNN NNN.NNNN NNNNNNN NNN.NNNN NNN.NNNN NN.NNNNNNNNNNNNNN
//
//Line 0 is a twenty-four-character name.
//
//Lines 1 and 2 are the standard Two-Line Orbital Element Set Format identical
//to that used by NORAD and NASA.  The format description is:
//   
//  Line 1
//  Column    Description
//  01-01     Line Number of Element Data
//  03-07     Satellite Number
//  10-11     International Designator (Last two digits of launch year)
//  12-14     International Designator (Launch number of the year)
//  15-17     International Designator (Piece of launch)
//  19-20     Epoch Year (Last two digits of year)
//  21-32     Epoch (Julian Day and fractional portion of the day)
//  34-43     First Time Derivative of the Mean Motion
//            or Ballistic Coefficient (Depending on ephemeris type)
//  45-52     Second Time Derivative of Mean Motion (decimal point assumed;
//            blank if N/A)
//  54-61     BSTAR drag term if GP4 general perturbation theory was used.
//            Otherwise, radiation pressure coefficient.  (Decimal point assumed)
//  63-63     Ephemeris type
//  65-68     Element number
//  69-69     Check Sum (Modulo 10)
//            (Letters, blanks, periods, plus signs = 0; minus signs = 1)
//  Line 2
//  Column    Description
//  01-01     Line Number of Element Data
//  03-07     Satellite Number
//  09-16     Inclination [Degrees]
//  18-25     Right Ascension of the Ascending Node [Degrees]
//  27-33     Eccentricity (decimal point assumed)
//  35-42     Argument of Perigee [Degrees]
//  44-51     Mean Anomaly [Degrees]
//  53-63     Mean Motion [Revs per day]
//  64-68     Revolution number at epoch [Revs]
//  69-69     Check Sum (Modulo 10)
//     
//  All other columns are blank or fixed.
//       
//Example:
//   
//NOAA 6
//1 11416U          86 50.28438588 0.00000140           67960-4 0  5293
//2 11416  98.5105  69.3305 0012788  63.2828 296.9658 14.24899292346978

public class Tle 
{
	 public enum Line
	   {
	      Zero,
	      One,
	      Two
	   };
	   
	   public enum Field
	   {
	      NoradNum,
	      IntlDesc,
	      SetNumber,     // TLE set number TLE集数量
	      EpochYear,     // Epoch: Last two digits of year 年的最后两个数字（1900---00）
	      EpochDay,      // Epoch: Fractional Julian Day of year 每年的儒略日小数部分
	      OrbitAtEpoch,  // Orbit at epoch 在轨时间
	      Inclination,   // Inclination 倾向
	      Raan,          // R.A. ascending node R.A.升交点
	      Eccentricity,  // Eccentricity 离心率
	      ArgPerigee,    // Argument of perigee 近地点幅角
	      MeanAnomaly,   // Mean anomaly 平均近点角
	      MeanMotion,    // Mean motion 平均运动
	      MeanMotionDt,  // First time derivative of mean motion
	      MeanMotionDt2, // Second time derivative of mean motion
	      BStarDrag      // BSTAR Drag
	   }

	   public enum Unit
	   {
	      Radians,   // radians 弧度
	      Degrees,   // degrees  度数    
	      Native     // TLE format native units (no conversion) TLE格式单位
	   }
	   
	   private String m_Line0;
	   private String m_Line1;
	   private String m_Line2;

	   // Converted fields, in Double.Parse()-able form
	   //private Dictionary<Field, String> m_Field = new Dictionary<Field, String>();
	   //private Dictionary<Integer, Double> m_Cache;
	   
	   private Hashtable<Field, String> m_Field;
	   private Hashtable<Integer, Double> m_Cache;
	   public String getName()
	   {
	      return m_Line0; 
	   }
	   
	   public String getLine1()
	   {
	       return m_Line1; 
	   }

	   public String getLine2()
	   {
	       return m_Line2; 
	   }

	   public String getNoradNumber()
	   {
	       return GetField(Field.NoradNum, false); 
	   }

	   public String getEccentricity()
	   {
	       return GetField(Field.Eccentricity, false); 
	   }

	   public String getInclination()
	   {
	       return GetField(Field.Inclination, true); 
	   }

	   public String getEpoch()
	   {
	         return String.format("%.2f%.8f",
                     GetField(Field.EpochYear), GetField(Field.EpochDay));
	   }

	   public String getIntlDescription()
	   {
	      return GetField(Field.IntlDesc, false); 
	   }

	   public String getSetNumber()
	   {
	       return GetField(Field.SetNumber, false); 
	   }

	   public String getOrbitAtEpoch()
	   {
	      return GetField(Field.OrbitAtEpoch, false); 
	   }

	   public String getRAAscendingNode()
	   {
	       return GetField(Field.Raan, true); 
	   }

	   public String getArgPerigee()
	   {
	       return GetField(Field.ArgPerigee, true); 
	   }

	   public String getMeanAnomaly()
	   {
	      return GetField(Field.MeanAnomaly, true);
	   }

	   public String getMeanMotion()
	   {
	      return GetField(Field.MeanMotion, true);
	   }

	   public String getMeanMotionDt()
	   {
	      return GetField(Field.MeanMotionDt, false); 
	   }

	   public String getMeanMotionDt2()
	   {
	      return GetField(Field.MeanMotionDt2, false); 
	   }

	   public String getBStarDrag()
	   {
	       return GetField(Field.BStarDrag, false); 
	   }

	   public Julian getEpochJulian() throws Exception
	   {
	         int epochYear = (int)GetField(Tle.Field.EpochYear);
	         double epochDay = GetField(Tle.Field.EpochDay);

	         if (epochYear < 57)
	         {
	            epochYear += 2000;
	         }
	         else
	         {
	            epochYear += 1900;
	         }
	         return new Julian(epochYear, epochDay);
	   }
	   
	   // Name
	   private final static int TLE_LEN_LINE_DATA      = 69; private final static int TLE_LEN_LINE_NAME      = 24;

	   // Line 1
	   private final int TLE1_COL_SATNUM        =  2; private final int TLE1_LEN_SATNUM        =  5;
	   private final int TLE1_COL_INTLDESC_A    =  9; private final int TLE1_LEN_INTLDESC_A    =  2;
	   private final int TLE1_COL_INTLDESC_B    = 11; private final int TLE1_LEN_INTLDESC_B    =  3;
	   private final int TLE1_COL_INTLDESC_C    = 14; private final int TLE1_LEN_INTLDESC_C    =  3;
	   private final int TLE1_COL_EPOCH_A       = 18; private final int TLE1_LEN_EPOCH_A       =  2;
	   private final int TLE1_COL_EPOCH_B       = 20; private final int TLE1_LEN_EPOCH_B       = 12;
	   private final int TLE1_COL_MEANMOTIONDT  = 33; private final int TLE1_LEN_MEANMOTIONDT  = 10;
	   private final int TLE1_COL_MEANMOTIONDT2 = 44; private final int TLE1_LEN_MEANMOTIONDT2 =  8;
	   private final int TLE1_COL_BSTAR         = 53; private final int TLE1_LEN_BSTAR         =  8;
	   private final int TLE1_COL_EPHEMTYPE     = 62; private final int TLE1_LEN_EPHEMTYPE     =  1;
	   private final int TLE1_COL_ELNUM         = 64; private final int TLE1_LEN_ELNUM         =  4;

	   // Line 2
	   private final int TLE2_COL_SATNUM        = 2;  private final int TLE2_LEN_SATNUM        =  5;
	   private final int TLE2_COL_INCLINATION   = 8;  private final int TLE2_LEN_INCLINATION   =  8;
	   private final int TLE2_COL_RAASCENDNODE  = 17; private final int TLE2_LEN_RAASCENDNODE  =  8;
	   private final int TLE2_COL_ECCENTRICITY  = 26; private final int TLE2_LEN_ECCENTRICITY  =  7;
	   private final int TLE2_COL_ARGPERIGEE    = 34; private final int TLE2_LEN_ARGPERIGEE    =  8;
	   private final int TLE2_COL_MEANANOMALY   = 43; private final int TLE2_LEN_MEANANOMALY   =  8;
	   private final int TLE2_COL_MEANMOTION    = 52; private final int TLE2_LEN_MEANMOTION    = 11;
	   private final int TLE2_COL_REVATEPOCH    = 63; private final int TLE2_LEN_REVATEPOCH    =  5;

	   public Tle()
	   {
		   
	   }
	   
	   public Tle(String strName, String strLine1, String strLine2)
	   {
	      m_Line0  = strName;
	      m_Line1 = strLine1;
	      m_Line2 = strLine2;
	      Initialize();
	   }

	   // //////////////////////////////////////////////////////////////////////////
	   public Tle(Tle tle)
	   {
		   this.m_Line0 = tle.m_Line0;
		   this.m_Line1 = tle.m_Line0;
		   this.m_Line2 = tle.m_Line0;
		   Initialize();
	   }

	   // //////////////////////////////////////////////////////////////////////////
	   @SuppressWarnings("unchecked")
	private void Initialize()
	   {
	      m_Field = new Hashtable();
	      m_Cache = new Hashtable();

	      m_Field.put(Field.NoradNum, m_Line1.substring(TLE1_COL_SATNUM, TLE1_COL_SATNUM+TLE1_LEN_SATNUM));
	      m_Field.put(Field.IntlDesc, m_Line1.substring(TLE1_COL_INTLDESC_A,TLE1_COL_INTLDESC_A+
                  TLE1_LEN_INTLDESC_A +
                  TLE1_LEN_INTLDESC_B +   
                  TLE1_LEN_INTLDESC_C));
	      m_Field.put(Field.EpochYear, m_Line1.substring(TLE1_COL_EPOCH_A, TLE1_COL_EPOCH_A + TLE1_LEN_EPOCH_A));
	      m_Field.put(Field.EpochDay, m_Line1.substring(TLE1_COL_EPOCH_B, TLE1_COL_EPOCH_B + TLE1_LEN_EPOCH_B));
	      
	      if (m_Line1.toCharArray()[TLE1_COL_MEANMOTIONDT] == '-')
	      {
	         // value is negative
	    	  m_Field.put(Field.MeanMotionDt, "-0");
	      }
	      else
	      {
	    	  m_Field.put(Field.MeanMotionDt, "0");
	      }

	      m_Field.put(Field.MeanMotionDt, m_Field.get(Field.MeanMotionDt) + m_Line1.substring(TLE1_COL_MEANMOTIONDT + 1,
	    		  TLE1_COL_MEANMOTIONDT + 1+TLE1_LEN_MEANMOTIONDT)); 
	      // decimal point assumed; exponential notation
	      m_Field.put(Field.MeanMotionDt2,  ExpToDecimal(m_Line1.substring(TLE1_COL_MEANMOTIONDT2,
	        		 TLE1_COL_MEANMOTIONDT2+TLE1_LEN_MEANMOTIONDT2)));
	      // decimal point assumed; exponential notation
	      m_Field.put(Field.BStarDrag, ExpToDecimal(m_Line1.substring(TLE1_COL_BSTAR, TLE1_COL_BSTAR+TLE1_LEN_BSTAR)));
	      //TLE1_COL_EPHEMTYPE      
	      //TLE1_LEN_EPHEMTYPE   
	      m_Field.put(Field.SetNumber, m_Line1.substring(TLE1_COL_ELNUM, TLE1_COL_ELNUM+TLE1_LEN_ELNUM).trim());
	      // TLE2_COL_SATNUM         
	      // TLE2_LEN_SATNUM         
	      m_Field.put(Field.Inclination,m_Line2.substring(TLE2_COL_INCLINATION, TLE2_COL_INCLINATION+TLE2_LEN_INCLINATION).trim());
	     
	      m_Field.put(Field.Raan, m_Line2.substring(TLE2_COL_RAASCENDNODE, TLE2_COL_RAASCENDNODE + TLE2_LEN_RAASCENDNODE).trim());
	      
	      // Eccentricity: decimal point is assumed
	      m_Field.put(Field.Eccentricity, "0." + m_Line2.substring(TLE2_COL_ECCENTRICITY,
	    		  TLE2_COL_ECCENTRICITY + TLE2_LEN_ECCENTRICITY));
	     
	      m_Field.put(Field.ArgPerigee, m_Line2.substring(TLE2_COL_ARGPERIGEE, TLE2_COL_ARGPERIGEE+TLE2_LEN_ARGPERIGEE).trim());
	     
	      m_Field.put(Field.MeanAnomaly,  m_Line2.substring(TLE2_COL_MEANANOMALY, TLE2_COL_MEANANOMALY+TLE2_LEN_MEANANOMALY).trim());
	     
	      m_Field.put(Field.MeanMotion, m_Line2.substring(TLE2_COL_MEANMOTION, TLE2_COL_MEANMOTION+TLE2_LEN_MEANMOTION).trim());
	     
	      m_Field.put(Field.OrbitAtEpoch, m_Line2.substring(TLE2_COL_REVATEPOCH, TLE2_COL_REVATEPOCH+TLE2_LEN_REVATEPOCH).trim());

	   }

	   public double GetField(Field fld)
	   {
	      return GetField(fld, Unit.Native);
	   }
	   
	 /// <summary>
	   /// Returns the requested TLE data field as a type double.
	   /// </summary>
	   /// <remarks>
	   /// The numeric return values are cached; requesting the same field 
	   /// repeatedly incurs minimal overhead.
	   /// </remarks>
	   /// <param name="fld">The TLE field to retrieve.</param>
	   /// <param name="units">Specifies the units desired.</param>
	   /// <returns>
	   /// The requested field's value, converted to the correct units if necessary.
	   /// </returns>
	   public double GetField(Field fld, Unit units)
	   {
	      // Return cache contents if it exists, else populate cache.
	      int key = Key(units, fld);
	      if (m_Cache.containsKey(key))
	      {
	         // return cached value
	         return m_Cache.get(key);
	      }
	      else
	      {
	         // Value not in cache; add it
	         double valNative = Double.parseDouble(m_Field.get(fld).toString());//, CultureInfo.InvariantCulture);
	         double valConv   = ConvertUnits(valNative, fld, units); 
	         m_Cache.put(key, valConv);
	         return valConv;
	      }
	   }

	   public String GetField(Field fld, boolean appendUnits)
	   {
	      String str = m_Field.get(fld).toString();
	      if (appendUnits)
	      {
	         str += GetUnits(fld);
	      }
	      return str.trim();
	   }
	// Generates a key for the TLE field cache
	   private static int Key(Unit u, Field f)
	   {
	      return (u.ordinal() * 100) + f.ordinal();
	   }

	   /// <summary>
	   /// Converts the given TLE field to the requested units.
	   /// </summary>
	   /// <param name="valNative">Value to convert (native units).</param>
	   /// <param name="fld">Field ID of the value being converted.</param>
	   /// <param name="units">Units to convert to.</param>
	   /// <returns>The converted value.</returns>
	   protected static double ConvertUnits(double valNative,
	                                        Field fld,
	                                        Unit  units)
	   {
	      if (fld == Field.Inclination  ||
	          fld == Field.Raan         ||
	          fld == Field.ArgPerigee   ||
	          fld == Field.MeanAnomaly)
	      {
	         // The native TLE format is degrees
	         if (units == Unit.Radians)
	         {
	            return Globals.ToRadians(valNative);
	         }
	      }
	      // unconverted native format
	      return valNative;
	   }
	   
	// ///////////////////////////////////////////////////////////////////////////
	   protected static String GetUnits(Field fld) 
	   {
	      final String strDegrees    = " degrees";
	      final String strRevsPerDay = " revs / day";

	      if (fld == Field.Inclination  ||
		          fld == Field.Raan         ||
		          fld == Field.ArgPerigee   ||
		          fld == Field.MeanAnomaly)
		  {
	    	  		return strDegrees;
		  }
	      else if(fld == Field.MeanMotion)
	      {
	    	  return strRevsPerDay;
	      }
	      else
	    	  return null; 
	   }
	   
	// //////////////////////////////////////////////////////////////////////////
	   // Converts TLE-style exponential notation of the form [ |+|-]00000[ |+|-]0
	   // to decimal notation. Assumes implied decimal point to the left of the first
	   // number in the string, i.e., 
	   //       " 12345-3" =  0.00012345
	   //       "-23429-5" = -0.0000023429   
	   //       " 40436+1" =  4.0436
	   // Also assumes that lack of a sign character implies a positive value, i.e.,
	   //       " 00000 0" =  0.00000
	   //       " 31415 1" =  3.1415
	   protected static String ExpToDecimal(String str)
	   {
	      final int COL_SIGN     = 0;
	      final int LEN_SIGN     = 1;

	      final int COL_MANTISSA = 1;
	      final int LEN_MANTISSA = 5;

	      final int COL_EXPONENT = 6;
	      final int LEN_EXPONENT = 2;

	      String sign     = str.substring(COL_SIGN,     COL_SIGN+LEN_SIGN);
	      String mantissa = str.substring(COL_MANTISSA, COL_MANTISSA+LEN_MANTISSA);
	      String exponent = str.substring(COL_EXPONENT, COL_EXPONENT+LEN_EXPONENT).trim();

	      double val = Double.parseDouble(sign +"0." + mantissa + "e" + exponent);//, CultureInfo.InvariantCulture);
	      //int sigDigits = mantissa.Length + Math.Abs(int.Parse(exponent, CultureInfo.InvariantCulture));
	      if(exponent.equals("+0") || exponent.equals("-0"))
	    	  exponent = "0";
	      int sigDigits = mantissa.length() + Math.abs(Integer.parseInt(exponent));

	      String strs = String.format("%."+sigDigits+"f ",val);
	      return strs;
	      //return val.ToString("F" + sigDigits);//, CultureInfo.InvariantCulture);
	   }

	   /// <summary>
	   /// Determines if a given string has the expected format of a single 
	   /// line of TLE data.
	   /// </summary>
	   /// <param name="str">The input string.</param>
	   /// <param name="line">The line ID of the input string.</param>
	   /// <returns>True if the input string has the format of 
	   /// the given line ID.</returns>
	   /// <remarks>
	   /// A valid satellite name is less than or equal to TLE_LEN_LINE_NAME
	   ///      characters;
	   /// A valid data line must:
	   ///      Have as the first character the line number
	   ///      Have as the second character a blank
	   ///      Be TLE_LEN_LINE_DATA characters long
	   /// </remarks>
	   public static boolean IsValidFormat(String str, Line line)
	   {
	      str = str.trim();

	      int nLen = str.length();

	      if (line == Line.Zero)
	      {
	         // Satellite name
	         return str.length() <= TLE_LEN_LINE_NAME;
	      }
	      else
	      {
	         // Data line
	         return (nLen == TLE_LEN_LINE_DATA) &&
	                ((str.charAt(0) - '0') == line.ordinal()) &&
	                (str.charAt(1) == ' ');
	      }
	   }

	   // //////////////////////////////////////////////////////////////////////////
	   // Calculate the check sum for a given line of TLE data, the last character
	   // of which is the current checksum. (Although there is no check here,
	   // the current checksum should match the one calculated.)
	   // The checksum algorithm: 
	   //    Each number in the data line is summed, modulo 10.
	   //    Non-numeric characters are zero, except minus signs, which are 1.
	   //
	   static int CheckSum(String str)
	   {
	      // The length is "- 1" because the current (existing) checksum 
	      // character is not included.
	      int len  = str.length() - 1;
	      int xsum = 0;

	      for (int i = 0; i < len; i++)
	      {
	    	  Character ch = str.charAt(i);
	         if (Character.isDigit(ch))
	         {
	            xsum += (ch - '0');
	         }
	         else if (ch == '-')
	         {
	            xsum++;
	         }
	      }
	      return (xsum % 10);
	   }
}

