package com.xc.ocs.ontOperator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import com.xc.worldwind.SGP.Tle;
import com.xc.worldwind.function.SatCoverage;
import com.xc.worldwind.object.MyPoint;

/**
 * 传感器时空计算相关类[与观测区域]
 * 
 * */
public class TimeAndSpatialUtil
{
	/**计算遥感传感器进出观测区域的时间情况(数据存储在sensorTSOC中)
	 * @throws ParseException
	 */
	//
	//For Wangke
	public static boolean DrawLongTimeSpace = false;
	//
	public static int tagIndex = 0;
	/**用于切割轨道**/
	public static ArrayList<ArrayList<MyPoint>> ToOrbits(Tle tle,String timeSet[]) throws Exception
	{
		ArrayList<MyPoint>LeftRealOrbit = new ArrayList<MyPoint>();
		ArrayList<MyPoint>RightRealOrbit = new ArrayList<MyPoint>();
		ArrayList<ArrayList<MyPoint>> RealOrbits = new ArrayList<ArrayList<MyPoint>>();
		ArrayList<ArrayList<MyPoint>> Ltemp = new ArrayList<ArrayList<MyPoint>>();
		ArrayList<ArrayList<MyPoint>> Rtemp = new ArrayList<ArrayList<MyPoint>>();
		LeftRealOrbit = getLeftOrbits(tle,timeSet);
		RightRealOrbit = getRightOrbits(tle,timeSet);

		MyPoint LtempPt = null;
		MyPoint RtempPt = null;
		int visitL=0;
		int visitR=0;
		int lmark=0;
		int rmark=0;
		boolean Lreverse = false;
		boolean Rreverse = false;
		for(int i = 0;i<LeftRealOrbit.size()-1;i++)
		{
			if ((LeftRealOrbit.get(i).getX()<0&&LeftRealOrbit.get(i+1).getX()>0)&&(LeftRealOrbit.get(i).getX() - LeftRealOrbit.get(i+1).getX()<-250)) {
				Lreverse = true;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (LtempPt != null) {
					MyPoint newtemp = new MyPoint(180, LtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
				double intersectY = LeftRealOrbit.get(i).getY()+(LeftRealOrbit.get(i).getY()-LeftRealOrbit.get(i+1).getY())/
						(LeftRealOrbit.get(i).getX()-(LeftRealOrbit.get(i+1).getX()-360))*(-180-LeftRealOrbit.get(i).getX());
				LtempPt = new MyPoint(-180, intersectY, 0, 1);
				Ltemp.get(visitL).add(LtempPt);
				visitL++;
				lmark = i+1;
			}
			else if (LeftRealOrbit.get(i).getX()-LeftRealOrbit.get(i+1).getX()>250) {
				Lreverse = false;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (LtempPt!=null) {
					MyPoint newtemp = new MyPoint(-180, LtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
				double intersectY = LeftRealOrbit.get(i).getY()+(LeftRealOrbit.get(i).getY()-LeftRealOrbit.get(i+1).getY())/
						(LeftRealOrbit.get(i).getX()-(360+LeftRealOrbit.get(i+1).getX()))*(180-LeftRealOrbit.get(i).getX());
				LtempPt = new MyPoint(180, intersectY, 0, 1);
				Ltemp.get(visitL).add(LtempPt);
				visitL++;
				lmark = i+1;
			}
			if (visitL == 0 && i==LeftRealOrbit.size()-2) {   //如果到了最后一个点依旧只有一段
				Ltemp.add(LeftRealOrbit);
			}
			if (i==LeftRealOrbit.size()-1) {
				System.out.println("its i");
			}
			if (visitL != 0 && i== LeftRealOrbit.size()-2) {
				ArrayList<MyPoint>segment = new ArrayList<>();
				if (LtempPt!=null) {
					if (Lreverse == true) {
						MyPoint newtemp = new MyPoint(180, LtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
					else if (Lreverse == false) {
						MyPoint newtemp = new MyPoint(-180, LtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
			}
		}
		
		for(int i = 0;i<RightRealOrbit.size()-1;i++)
		{
			if ((RightRealOrbit.get(i).getX()<0&&RightRealOrbit.get(i+1).getX()>0)&&(RightRealOrbit.get(i).getX() - RightRealOrbit.get(i+1).getX()<-250)) {
				Rreverse = true;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (RtempPt != null) {
					MyPoint newtemp = new MyPoint(180, RtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				double intersectY = RightRealOrbit.get(i).getY()+(RightRealOrbit.get(i).getY()-RightRealOrbit.get(i+1).getY())/
						(RightRealOrbit.get(i).getX()-(RightRealOrbit.get(i+1).getX()-360))*(-180-RightRealOrbit.get(i).getX());
				RtempPt = new MyPoint(-180, intersectY, 0, 1);
				Rtemp.get(visitR).add(RtempPt);
				visitR++;
				rmark = i+1;
			}
			else if (RightRealOrbit.get(i).getX()-RightRealOrbit.get(i+1).getX()>250) {
				Rreverse = false;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (RtempPt!=null) {
					MyPoint newtemp = new MyPoint(-180, RtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				double intersectY = RightRealOrbit.get(i).getY()+(RightRealOrbit.get(i).getY()-RightRealOrbit.get(i+1).getY())/
						(RightRealOrbit.get(i).getX()-(RightRealOrbit.get(i+1).getX()+360))*(180-RightRealOrbit.get(i).getX());
				RtempPt = new MyPoint(180, intersectY, 0, 1);
				Rtemp.get(visitR).add(RtempPt);
				visitR++;
				rmark = i+1;
			}
			if (visitR == 0 && i==RightRealOrbit.size()-2) {
				Rtemp.add(RightRealOrbit);
			}
			if (visitR != 0 && i== RightRealOrbit.size()-2) {
				ArrayList<MyPoint>segment = new ArrayList<>();
				if (RtempPt!=null) {
					if (Rreverse == true) {
						MyPoint newtemp = new MyPoint(180, RtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
					else if (Rreverse == false) {
						MyPoint newtemp = new MyPoint(-180, RtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				}
		}
		
		if (Ltemp.get(0).get(0).getX()<Ltemp.get(0).get(1).getX()) {
			for(ArrayList<MyPoint> temp:Ltemp)
			{
				Collections.reverse(temp);
			}
			for(ArrayList<MyPoint> temp:Rtemp)
			{
				Collections.reverse(temp);
			}
		}
		if(Ltemp.size()!=Rtemp.size())
		{
			return RealOrbits;
		}
		for(int i=0;i<Ltemp.size();i++)
		{
			ArrayList<MyPoint> temp = new ArrayList<>();
			temp.addAll(Rtemp.get(i));
			Collections.reverse(temp);
			Ltemp.get(i).addAll(temp);
			Ltemp.get(i).add(Ltemp.get(i).get(0));
		}
		RealOrbits.addAll(Ltemp);
		return RealOrbits;
	}
	
	
	/**
	 * 重载ToOrbits
	 * rollAngle:侧摆角度
	 */
	public static ArrayList<ArrayList<MyPoint>> ToOrbits(Tle tle,String timeSet[],double rollAngle) throws Exception
	{
		ArrayList<MyPoint>LeftRealOrbit = new ArrayList<MyPoint>();
		ArrayList<MyPoint>RightRealOrbit = new ArrayList<MyPoint>();
		ArrayList<ArrayList<MyPoint>> RealOrbits = new ArrayList<ArrayList<MyPoint>>();
		ArrayList<ArrayList<MyPoint>> Ltemp = new ArrayList<ArrayList<MyPoint>>();
		ArrayList<ArrayList<MyPoint>> Rtemp = new ArrayList<ArrayList<MyPoint>>();
		LeftRealOrbit = getLeftOrbits(tle,timeSet,rollAngle);
		RightRealOrbit = getRightOrbits(tle,timeSet,rollAngle);

		MyPoint LtempPt = null;
		MyPoint RtempPt = null;
		int visitL=0;
		int visitR=0;
		int lmark=0;
		int rmark=0;
		boolean Lreverse = false;
		boolean Rreverse = false;
		for(int i = 0;i<LeftRealOrbit.size()-1;i++)
		{
			if ((LeftRealOrbit.get(i).getX()<0&&LeftRealOrbit.get(i+1).getX()>0)&&(LeftRealOrbit.get(i).getX() - LeftRealOrbit.get(i+1).getX()<-250)) {
				Lreverse = true;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (LtempPt != null) {
					MyPoint newtemp = new MyPoint(180, LtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
				double intersectY = LeftRealOrbit.get(i).getY()+(LeftRealOrbit.get(i).getY()-LeftRealOrbit.get(i+1).getY())/
						(LeftRealOrbit.get(i).getX()-(LeftRealOrbit.get(i+1).getX()-360))*(-180-LeftRealOrbit.get(i).getX());
				LtempPt = new MyPoint(-180, intersectY, 0, 1);
				Ltemp.get(visitL).add(LtempPt);
				visitL++;
				lmark = i+1;
			}
			else if (LeftRealOrbit.get(i).getX()-LeftRealOrbit.get(i+1).getX()>250) {
				Lreverse = false;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (LtempPt!=null) {
					MyPoint newtemp = new MyPoint(-180, LtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
				double intersectY = LeftRealOrbit.get(i).getY()+(LeftRealOrbit.get(i).getY()-LeftRealOrbit.get(i+1).getY())/
						(LeftRealOrbit.get(i).getX()-(360+LeftRealOrbit.get(i+1).getX()))*(180-LeftRealOrbit.get(i).getX());
				LtempPt = new MyPoint(180, intersectY, 0, 1);
				Ltemp.get(visitL).add(LtempPt);
				visitL++;
				lmark = i+1;
			}
			if (visitL == 0 && i==LeftRealOrbit.size()-2) {   //如果到了最后一个点依旧只有一段
				Ltemp.add(LeftRealOrbit);
			}
			if (i==LeftRealOrbit.size()-1) {
				System.out.println("its i");
			}
			if (visitL != 0 && i== LeftRealOrbit.size()-2) {
				ArrayList<MyPoint>segment = new ArrayList<>();
				if (LtempPt!=null) {
					if (Lreverse == true) {
						MyPoint newtemp = new MyPoint(180, LtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
					else if (Lreverse == false) {
						MyPoint newtemp = new MyPoint(-180, LtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
			}
		}
		
		for(int i = 0;i<RightRealOrbit.size()-1;i++)
		{
			if ((RightRealOrbit.get(i).getX()<0&&RightRealOrbit.get(i+1).getX()>0)&&(RightRealOrbit.get(i).getX() - RightRealOrbit.get(i+1).getX()<-250)) {
				Rreverse = true;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (RtempPt != null) {
					MyPoint newtemp = new MyPoint(180, RtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				double intersectY = RightRealOrbit.get(i).getY()+(RightRealOrbit.get(i).getY()-RightRealOrbit.get(i+1).getY())/
						(RightRealOrbit.get(i).getX()-(RightRealOrbit.get(i+1).getX()-360))*(-180-RightRealOrbit.get(i).getX());
				RtempPt = new MyPoint(-180, intersectY, 0, 1);
				Rtemp.get(visitR).add(RtempPt);
				visitR++;
				rmark = i+1;
			}
			else if (RightRealOrbit.get(i).getX()-RightRealOrbit.get(i+1).getX()>250) {
				Rreverse = false;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (RtempPt!=null) {
					MyPoint newtemp = new MyPoint(-180, RtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				double intersectY = RightRealOrbit.get(i).getY()+(RightRealOrbit.get(i).getY()-RightRealOrbit.get(i+1).getY())/
						(RightRealOrbit.get(i).getX()-(RightRealOrbit.get(i+1).getX()+360))*(180-RightRealOrbit.get(i).getX());
				RtempPt = new MyPoint(180, intersectY, 0, 1);
				Rtemp.get(visitR).add(RtempPt);
				visitR++;
				rmark = i+1;
			}
			if (visitR == 0 && i==RightRealOrbit.size()-2) {
				Rtemp.add(RightRealOrbit);
			}
			if (visitR != 0 && i== RightRealOrbit.size()-2) {
				ArrayList<MyPoint>segment = new ArrayList<>();
				if (RtempPt!=null) {
					if (Rreverse == true) {
						MyPoint newtemp = new MyPoint(180, RtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
					else if (Rreverse == false) {
						MyPoint newtemp = new MyPoint(-180, RtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				}
		}
		
		if (Ltemp.get(0).get(0).getX()<Ltemp.get(0).get(1).getX()) {
			for(ArrayList<MyPoint> temp:Ltemp)
			{
				Collections.reverse(temp);
			}
			for(ArrayList<MyPoint> temp:Rtemp)
			{
				Collections.reverse(temp);
			}
		}
		if(Ltemp.size()!=Rtemp.size())
		{
			return RealOrbits;
		}
		for(int i=0;i<Ltemp.size();i++)
		{
			ArrayList<MyPoint> temp = new ArrayList<>();
			temp.addAll(Rtemp.get(i));
			Collections.reverse(temp);
			Ltemp.get(i).addAll(temp);
			Ltemp.get(i).add(Ltemp.get(i).get(0));
		}
		RealOrbits.addAll(Ltemp);
		return RealOrbits;
	}
	
	/**
	 * 计算俯仰操作ToOrbits_pitch
	 */
	public static ArrayList<ArrayList<MyPoint>> ToOrbits_pitch(Tle tle,String timeSet[],double pitchAngle) throws Exception
	{
		ArrayList<MyPoint>LeftRealOrbit = new ArrayList<MyPoint>();
		ArrayList<MyPoint>RightRealOrbit = new ArrayList<MyPoint>();
		ArrayList<ArrayList<MyPoint>> RealOrbits = new ArrayList<ArrayList<MyPoint>>();
		ArrayList<ArrayList<MyPoint>> Ltemp = new ArrayList<ArrayList<MyPoint>>();
		ArrayList<ArrayList<MyPoint>> Rtemp = new ArrayList<ArrayList<MyPoint>>();
		LeftRealOrbit = getLeftOrbits_pitch(tle,timeSet,pitchAngle);
		RightRealOrbit = getRightOrbits_pitch(tle,timeSet,pitchAngle);

		MyPoint LtempPt = null;
		MyPoint RtempPt = null;
		int visitL=0;
		int visitR=0;
		int lmark=0;
		int rmark=0;
		boolean Lreverse = false;
		boolean Rreverse = false;
		for(int i = 0;i<LeftRealOrbit.size()-1;i++)
		{
			if ((LeftRealOrbit.get(i).getX()<0&&LeftRealOrbit.get(i+1).getX()>0)&&(LeftRealOrbit.get(i).getX() - LeftRealOrbit.get(i+1).getX()<-250)) {
				Lreverse = true;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (LtempPt != null) {
					MyPoint newtemp = new MyPoint(180, LtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
				double intersectY = LeftRealOrbit.get(i).getY()+(LeftRealOrbit.get(i).getY()-LeftRealOrbit.get(i+1).getY())/
						(LeftRealOrbit.get(i).getX()-(LeftRealOrbit.get(i+1).getX()-360))*(-180-LeftRealOrbit.get(i).getX());
				LtempPt = new MyPoint(-180, intersectY, 0, 1);
				Ltemp.get(visitL).add(LtempPt);
				visitL++;
				lmark = i+1;
			}
			else if (LeftRealOrbit.get(i).getX()-LeftRealOrbit.get(i+1).getX()>250) {
				Lreverse = false;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (LtempPt!=null) {
					MyPoint newtemp = new MyPoint(-180, LtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
				double intersectY = LeftRealOrbit.get(i).getY()+(LeftRealOrbit.get(i).getY()-LeftRealOrbit.get(i+1).getY())/
						(LeftRealOrbit.get(i).getX()-(360+LeftRealOrbit.get(i+1).getX()))*(180-LeftRealOrbit.get(i).getX());
				LtempPt = new MyPoint(180, intersectY, 0, 1);
				Ltemp.get(visitL).add(LtempPt);
				visitL++;
				lmark = i+1;
			}
			if (visitL == 0 && i==LeftRealOrbit.size()-2) {   //如果到了最后一个点依旧只有一段
				Ltemp.add(LeftRealOrbit);
			}
			if (i==LeftRealOrbit.size()-1) {
				System.out.println("its i");
			}
			if (visitL != 0 && i== LeftRealOrbit.size()-2) {
				ArrayList<MyPoint>segment = new ArrayList<>();
				if (LtempPt!=null) {
					if (Lreverse == true) {
						MyPoint newtemp = new MyPoint(180, LtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
					else if (Lreverse == false) {
						MyPoint newtemp = new MyPoint(-180, LtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
				}
				segment.addAll(LeftRealOrbit.subList(lmark, i+1));
				Ltemp.add(segment);
			}
		}
		
		for(int i = 0;i<RightRealOrbit.size()-1;i++)
		{
			if ((RightRealOrbit.get(i).getX()<0&&RightRealOrbit.get(i+1).getX()>0)&&(RightRealOrbit.get(i).getX() - RightRealOrbit.get(i+1).getX()<-250)) {
				Rreverse = true;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (RtempPt != null) {
					MyPoint newtemp = new MyPoint(180, RtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				double intersectY = RightRealOrbit.get(i).getY()+(RightRealOrbit.get(i).getY()-RightRealOrbit.get(i+1).getY())/
						(RightRealOrbit.get(i).getX()-(RightRealOrbit.get(i+1).getX()-360))*(-180-RightRealOrbit.get(i).getX());
				RtempPt = new MyPoint(-180, intersectY, 0, 1);
				Rtemp.get(visitR).add(RtempPt);
				visitR++;
				rmark = i+1;
			}
			else if (RightRealOrbit.get(i).getX()-RightRealOrbit.get(i+1).getX()>250) {
				Rreverse = false;
				ArrayList<MyPoint> segment = new ArrayList<>();
				if (RtempPt!=null) {
					MyPoint newtemp = new MyPoint(-180, RtempPt.getY(), 0, 1);
					segment.add(newtemp);
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				double intersectY = RightRealOrbit.get(i).getY()+(RightRealOrbit.get(i).getY()-RightRealOrbit.get(i+1).getY())/
						(RightRealOrbit.get(i).getX()-(RightRealOrbit.get(i+1).getX()+360))*(180-RightRealOrbit.get(i).getX());
				RtempPt = new MyPoint(180, intersectY, 0, 1);
				Rtemp.get(visitR).add(RtempPt);
				visitR++;
				rmark = i+1;
			}
			if (visitR == 0 && i==RightRealOrbit.size()-2) {
				Rtemp.add(RightRealOrbit);
			}
			if (visitR != 0 && i== RightRealOrbit.size()-2) {
				ArrayList<MyPoint>segment = new ArrayList<>();
				if (RtempPt!=null) {
					if (Rreverse == true) {
						MyPoint newtemp = new MyPoint(180, RtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
					else if (Rreverse == false) {
						MyPoint newtemp = new MyPoint(-180, RtempPt.getY(), 0, 1);
						segment.add(newtemp);
					}
				}
				segment.addAll(RightRealOrbit.subList(rmark, i+1));
				Rtemp.add(segment);
				}
		}
		
		if (Ltemp.get(0).get(0).getX()<Ltemp.get(0).get(1).getX()) {
			for(ArrayList<MyPoint> temp:Ltemp)
			{
				Collections.reverse(temp);
			}
			for(ArrayList<MyPoint> temp:Rtemp)
			{
				Collections.reverse(temp);
			}
		}
		if(Ltemp.size()!=Rtemp.size())
		{
			return RealOrbits;
		}
		for(int i=0;i<Ltemp.size();i++)
		{
			ArrayList<MyPoint> temp = new ArrayList<>();
			temp.addAll(Rtemp.get(i));
			Collections.reverse(temp);
			Ltemp.get(i).addAll(temp);
			Ltemp.get(i).add(Ltemp.get(i).get(0));
		}
		RealOrbits.addAll(Ltemp);
		return RealOrbits;
	}
	
	public static ArrayList<MyPoint> getLeftOrbits(Tle tle,String timeSet[]) throws Exception
	{
		ArrayList<MyPoint> left = new ArrayList<MyPoint>();
	    int interval = getMinutesInterval(timeSet[0], timeSet[1]);
		Calendar StartDate = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 
		StartDate.setTime(dateFormat.parse(timeSet[0]));
		newDate.setTime(dateFormat.parse(timeSet[0]));
   	 	for(int min = 0;min < interval;min++)
		{	
   	 		StartDate.setTime(newDate.getTime());
   	 		newDate.set(Calendar.SECOND, newDate.getTime().getSeconds()+5);
			SatCoverage.PrintPosVel_1(tle, StartDate, newDate);    	
			left.add(new MyPoint(SatCoverage.ListA.get(0).get(0).Y, SatCoverage.ListA.get(0).get(0).X, 0, 1));
			SatCoverage.ListA.clear();
			SatCoverage.ListB.clear();
		}
   	 	
   	 	return left;
	}
	/**
	 * rollAngle:侧摆角度
	 * 重载getLeftOrbits
	 */
	
	public static ArrayList<MyPoint> getLeftOrbits(Tle tle,String timeSet[],double rollAngle) throws Exception
	{
		ArrayList<MyPoint> left = new ArrayList<MyPoint>();
	    int interval = getMinutesInterval(timeSet[0], timeSet[1]);
		Calendar StartDate = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 
		StartDate.setTime(dateFormat.parse(timeSet[0]));
		newDate.setTime(dateFormat.parse(timeSet[0]));
   	 	for(int min = 0;min < interval;min++)
		{	
   	 		StartDate.setTime(newDate.getTime());
   	 		newDate.set(Calendar.SECOND, newDate.getTime().getSeconds()+5);
			SatCoverage.PrintPosVel_2(tle, StartDate, newDate,rollAngle);    	
			left.add(new MyPoint(SatCoverage.ListA.get(0).get(0).Y, SatCoverage.ListA.get(0).get(0).X, 0, 1));
			SatCoverage.ListA.clear();
			SatCoverage.ListB.clear();
		}
   	 	
   	 	return left;
	}
	/**
	 * 卫星俯仰观测getLeftOrbits_pitch
	 */
	public static ArrayList<MyPoint> getLeftOrbits_pitch(Tle tle,String timeSet[],double pitchAngle) throws Exception
	{
		ArrayList<MyPoint> left = new ArrayList<MyPoint>();
	    int interval = getMinutesInterval(timeSet[0], timeSet[1]);
		Calendar StartDate = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 
		StartDate.setTime(dateFormat.parse(timeSet[0]));
		newDate.setTime(dateFormat.parse(timeSet[0]));
   	 	for(int min = 0;min < interval;min++)
		{	
   	 		StartDate.setTime(newDate.getTime());
   	 		newDate.set(Calendar.SECOND, newDate.getTime().getSeconds()+5);
			SatCoverage.PrintPosVel_3(tle, StartDate, newDate,pitchAngle);    	
			left.add(new MyPoint(SatCoverage.ListA.get(0).get(0).Y, SatCoverage.ListA.get(0).get(0).X, 0, 1));
			SatCoverage.ListA.clear();
			SatCoverage.ListB.clear();
		}
   	 	
   	 	return left;
	}
	
	
	/**
	 * 重载getRightOrbits
	 * rollAngle:侧摆角度
	 */
	public static ArrayList<MyPoint> getRightOrbits(Tle tle,String timeSet[],double rollAngle) throws Exception
	{
		ArrayList<MyPoint> right = new ArrayList<MyPoint>();
	    int interval = getMinutesInterval(timeSet[0], timeSet[1]);
	    Calendar StartDate = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 
		StartDate.setTime(dateFormat.parse(timeSet[0]));
		newDate.setTime(dateFormat.parse(timeSet[0]));
   	 	for(int min = 0;min < interval;min++)
		{	
   	 		StartDate.setTime(newDate.getTime());
   	 		newDate.set(Calendar.SECOND, newDate.getTime().getSeconds()+5);
			SatCoverage.PrintPosVel_2(tle, StartDate, newDate,rollAngle);    	
			right.add(new MyPoint(SatCoverage.ListB.get(0).get(0).Y, SatCoverage.ListB.get(0).get(0).X, 0, 1));
			SatCoverage.ListA.clear();
			SatCoverage.ListB.clear();
		}
   	 	return right;	 	
	}
	/**
	 * 卫星俯仰观测getRightOrbits_pitch
	 */
	public static ArrayList<MyPoint> getRightOrbits_pitch(Tle tle,String timeSet[],double pitchAngle) throws Exception
	{
		ArrayList<MyPoint> right = new ArrayList<MyPoint>();
		int interval = getMinutesInterval(timeSet[0], timeSet[1]);
		Calendar StartDate = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 
		StartDate.setTime(dateFormat.parse(timeSet[0]));
		newDate.setTime(dateFormat.parse(timeSet[0]));
		for(int min = 0;min < interval;min++)
		{	
			StartDate.setTime(newDate.getTime());
			newDate.set(Calendar.SECOND, newDate.getTime().getSeconds()+5);
			SatCoverage.PrintPosVel_3(tle, StartDate, newDate,pitchAngle);    	
			right.add(new MyPoint(SatCoverage.ListB.get(0).get(0).Y, SatCoverage.ListB.get(0).get(0).X, 0, 1));
			SatCoverage.ListA.clear();
			SatCoverage.ListB.clear();
		}
		return right;	 	
	}
	
	
	public static ArrayList<MyPoint> getRightOrbits(Tle tle,String timeSet[]) throws Exception
	{
		ArrayList<MyPoint> right = new ArrayList<MyPoint>();
	    int interval = getMinutesInterval(timeSet[0], timeSet[1]);
	    Calendar StartDate = Calendar.getInstance();
		Calendar newDate = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 
		StartDate.setTime(dateFormat.parse(timeSet[0]));
		newDate.setTime(dateFormat.parse(timeSet[0]));
   	 	for(int min = 0;min < interval;min++)
		{	
   	 		StartDate.setTime(newDate.getTime());
   	 		newDate.set(Calendar.SECOND, newDate.getTime().getSeconds()+5);
			SatCoverage.PrintPosVel_1(tle, StartDate, newDate);    	
			right.add(new MyPoint(SatCoverage.ListB.get(0).get(0).Y, SatCoverage.ListB.get(0).get(0).X, 0, 1));
			SatCoverage.ListA.clear();
			SatCoverage.ListB.clear();
		}
   	 	return right;	 	
	}
	
	/** 获取时间间隔有多少
	 */
	@SuppressWarnings("deprecation")
	public static int getMinutesInterval(String start,String end) throws ParseException
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:m:s"); 

		Calendar timeStart = Calendar.getInstance();
		Calendar timeEnd = Calendar.getInstance();	
		timeStart.setTime(dateFormat.parse(start));
		timeEnd.setTime(dateFormat.parse(end));
		//空间标准化

		int interval=timeEnd.getTime().getHours()*60+timeEnd.getTime().getMinutes()-(timeStart.getTime().getHours()*60+timeStart.getTime().getMinutes());
		return interval*12;
	}
}
