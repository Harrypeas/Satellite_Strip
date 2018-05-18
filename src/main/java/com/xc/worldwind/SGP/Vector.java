package com.xc.worldwind.SGP;

import java.util.Calendar;

//
//Vector.java
//
//Copyright (c) 2003-2012 Michael F. Henry
//Version 10/2012
//
/// Encapsulates a simple 4-component vector
public class Vector 
{
	   /// Standard constructor.
	   public Vector()
	   {
	   }

	   /// Creates a new vector from an existing vector.
	   public Vector(Vector v)
	   {
		   this(v.X, v.Y, v.Z, v.W);
	   }

	   /// Creates a new vector with the given XYZ components.
	   public Vector(double x, double y, double z)
	   {
		   X = x;
		   Y = y;
		   Z = z;
	   }

	 /// Creates a new vector with the given XYZ components.
	   public Vector(double x, double y, double z,Calendar date)
	   {
		   X = x;
		   Y = y;
		   Z = z;
		   this.m_date = date;
	   }
	   
	 /// Creates a new vector with the given XYZ components.
	   public Vector(double x, double y, double z,String dateStr)
	   {
		   X = x;
		   Y = y;
		   Z = z;
		   this.dt = dateStr;
	   }
	   /// Creates a new vector with the given XYZ-W components.
	   public Vector(double x, double y, double z, double w)
	   {
		   X = x;
		   Y = y;
		   Z = z;
		   W = w;
	   }

	   public double X,Y,Z,W;
	   public Calendar m_date;
	   public String dt;
	   /// Multiply each component in the vector by a given factor.
	   public void Mul(double factor)
	   {
		   X *= factor;
		   Y *= factor;
		   Z *= factor;
		   W *= Math.abs(factor);
	   }

	   /// Subtracts a vector from this vector.
	   public void Sub(Vector vec)
	   {
		   X -= vec.X;
		   Y -= vec.Y;
		   Z -= vec.Z;
		   W -= vec.W;
	   }

	   /// Calculates the angle, in radians, between this vector and another.
	   public double Angle(Vector vec)
	   {
	      return Math.acos(Dot(vec) / (Magnitude() * vec.Magnitude()));
	   }

	   /// Calculates the magnitude of the vector.
	   public double Magnitude()
	   {
	      return Math.sqrt((X * X) + (Y * Y) + (Z * Z));
	   }

	   /// Calculates the dot product of this vector and another.
	   public double Dot(Vector vec)
	   {
	      return (X * vec.X) + (Y * vec.Y) + (Z * vec.Z);
	   }

	   /// Calculates the distance between two vectors as points in XYZ space.
	   public double Distance(Vector vec)
	   {
	      return Math.sqrt(Math.pow(X - vec.X, 2.0) + 
	                       Math.pow(Y - vec.Y, 2.0) + 
	                       Math.pow(Z - vec.Z, 2.0));
	   }

	   /// <summary>
	   /// Rotates the XYZ coordinates around the X-axis.
	   /// </summary>
	   public void RotateX(double radians)
	   {
	      double y = Y;

	      Y = (Math.cos(radians) * y) - (Math.sin(radians) * Z);
	      Z = (Math.sin(radians) * y) + (Math.cos(radians) * Z);
	   }

	   /// <summary>
	   /// Rotates the XYZ coordinates around the Y-axis.
	   /// </summary>
	   public void RotateY(double radians)
	   {
	      double x = X;

	      X = ( Math.cos(radians) * x) + (Math.sin(radians) * Z);
	      Z = (-Math.sin(radians) * x) + (Math.cos(radians) * Z);
	   }

	   /// <summary>
	   /// Rotates the XYZ coordinates around the Z-axis.
	   /// </summary>
	   public void RotateZ(double radians)
	   {
	      double x = X;

	      X = (Math.cos(radians) * x) - (Math.sin(radians) * Y);
	      Y = (Math.sin(radians) * x) + (Math.cos(radians) * Y);
	   }

	   /// <summary>
	   /// Offsets the XYZ coordinates.
	   /// </summary>
	   public void Translate(double x, double y, double z)
	   {
		   X += x;
		   Y += y;
		   Z += z;
	   }
}


