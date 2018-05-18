package com.xc.worldwind.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.xc.worldwind.SGP.Coverage;
import com.xc.worldwind.SGP.EciTime;
import com.xc.worldwind.SGP.Geo;
import com.xc.worldwind.SGP.Orbit;
import com.xc.worldwind.SGP.Tle;
import com.xc.worldwind.SGP.Vector;
import com.xc.worldwind.object.InsituSensor;
import com.xc.worldwind.object.MyPoint;

import gov.nasa.worldwind.geom.LatLon;
/**
 * @author 陈雄
 * @class 卫星覆盖类
 * @function 提供卫星空间覆盖相关功能
 * 
 */


public class SatCoverage {
	public static ArrayList<ArrayList<Vector>> ListA = new ArrayList<ArrayList<Vector>>();		//覆盖边界左边
	public static ArrayList<ArrayList<Vector>> ListB = new ArrayList<ArrayList<Vector>>();		//覆盖边界右边
	public static List<Geo> Positions=null;		//轨道位置
	public static List<Vector> clipResult=null;		//裁剪结果
	public static double covRate = 0.0;				//覆盖率
	
	/**
	 * 获取卫星某一时刻位置
	 * @param tle tle根轨数据
	 * @param cal 日期格式时间
	 * @return 地理坐标系表示的三维坐标点Geo
	 * @throws Exception
	 */
	public static Geo getSatePosition(Tle tle,Calendar cal) throws Exception
	{
    	Orbit orbit = new Orbit(tle);
    	//方法返回此Calendar以毫秒为单位的时间
        double minutes = (cal.getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
        EciTime eciTime = orbit.GetPosition(minutes);
        if(eciTime==null)//判断返回的卫星参数是否为空，如果为空值，则返回空值
        {
        	return null;
        }
        else 
        {
        	Geo geo = new Geo(eciTime, eciTime.Date);
            return geo;
		}
	}
	/**
	 * 判断行进方向是否发生改变
	 */
	private static boolean isTop(Geo geo,Geo geof,Geo geon)
	{
		if((geo.LatitudeDeg-geof.LatitudeDeg)*(geo.LatitudeDeg-geon.LatitudeDeg)>0) //方向发生改变
			return true;
		return false;
	}
	/**
	 * 提供卫星直接覆盖的覆盖面积操作，结果存储至LsitA和ListB
	 * @param tle tle根轨数据
	 * @param cal1 开始观测日期格式时间
	 * @param cal2 结束观测日期格式时间
	 * @throws Exception
	 */
	public static void PrintPosVel_1(Tle tle,Calendar cal1,Calendar cal2) throws Exception
    {
        long t1 = cal1.getTimeInMillis();
        long t2 = cal2.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
  
        int step = 1000 * 1;                 //时间间隔设置的是1s
    	ArrayList<Vector> tempa=new ArrayList<Vector>();
    	ArrayList<Vector> tempb=new ArrayList<Vector>();
        double I=10;//10
        for(long t = t1;t <= t2; t = t +step)
        {
        	cal.setTimeInMillis(t);
        	Orbit orbit = new Orbit(tle);
            double minutes = (cal.getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
            EciTime eciTime = orbit.GetPosition(minutes);
            
            Geo geo = new Geo(eciTime, eciTime.Date);
            Date dt =eciTime.Date.ToTime().getTime();
            dt.setTime(dt.getTime()+8*60*60*1000);
            //String s = dt.toLocaleString();
            Calendar calf = Calendar.getInstance();
            Calendar caln = Calendar.getInstance();
            calf.setTimeInMillis(t-step);
            caln.setTimeInMillis(t+step);
            Geo geof = getSatePosition(tle,calf);	//当前运行卫星轨道上前一个点的位置
            Geo geon = getSatePosition(tle,caln);	//当前运行卫星轨道上后一个点的位置
            Coverage cov = new Coverage(eciTime);
            cov.Calculate(geo,eciTime,I,orbit.Inclination);//orbit.Inclination 轨道倾角
            if(isTop(geo,geof,geon))
            	continue;
            tempa.add(new Vector(cov.lowerLat,cov.lowerLong,99999,eciTime.Date.ToTime()));
            tempb.add(new Vector(cov.upperLat,cov.upperLong,99999,eciTime.Date.ToTime()));  
        }
        ListA.add(tempa);
        ListB.add(tempb);
    }
	/**
	 * 提供卫星左右侧视的覆盖面积操作，结果存储至LsitA和ListB
	 * @param tle tle根轨数据
	 * @param cal1 开始观测日期格式时间
	 * @param cal2 结束观测日期格式时间
	 * @param angle 卫星偏转角度
	 * @throws Exception
	 */
	public static void PrintPosVel_2(Tle tle,Calendar cal1,Calendar cal2,double angle) throws Exception
    {
        long t1 = cal1.getTimeInMillis();
        long t2 = cal2.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
  
        int step = 1000 * 1;                 //时间间隔设置的是15s
//        ListA = new ArrayList<Vector>();//
//        ListB = new ArrayList<Vector>();
       // double I = Math.random()*30+10;//视场角
        ArrayList<Vector> tempa=new ArrayList<Vector>();
    	ArrayList<Vector> tempb=new ArrayList<Vector>();
        double I=10;//10
        for(long t = t1;t <= t2; t = t +step)
        {
        	cal.setTimeInMillis(t);
        	Orbit orbit = new Orbit(tle);
            double minutes = (cal.getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
            EciTime eciTime = orbit.GetPosition(minutes);
            
            Geo geo = new Geo(eciTime, eciTime.Date);
            Date dt =eciTime.Date.ToTime().getTime();
            dt.setTime(dt.getTime()+8*60*60*1000);
            Calendar calf = Calendar.getInstance();
            Calendar caln = Calendar.getInstance();
            calf.setTimeInMillis(t-step);
            caln.setTimeInMillis(t+step);
            Geo geof = getSatePosition(tle,calf);	//当前运行卫星轨道上前一个点的位置
            Geo geon = getSatePosition(tle,caln);	//当前运行卫星轨道上后一个点的位置*/
            Coverage cov = new Coverage(eciTime);
            cov.Calculate(geo,eciTime,orbit.Inclination,I,angle);//orbit.Inclination 轨道倾角
            if(isTop(geo,geof,geon))
            	continue;
            if(Double.isNaN(cov.lowerLong)||Double.isNaN(cov.upperLong))
            	continue;
            tempa.add(new Vector(cov.lowerLat,cov.lowerLong,99999,eciTime.Date.ToTime()));
            tempb.add(new Vector(cov.upperLat,cov.upperLong,99999,eciTime.Date.ToTime()));
        }
        ListA.add(tempa);
        ListB.add(tempb);
    }
	/**
	 * 提供卫星俯仰观测的覆盖面积操作，结果存储至LsitA和ListB
	 * @param tle tle根轨数据
	 * @param cal1 开始观测日期格式时间
	 * @param cal2 结束观测日期格式时间
	 * @param angle 卫星偏转角度
	 * @throws Exception
	 */
	public static void PrintPosVel_3(Tle tle,Calendar cal1,Calendar cal2,double FunYangAngle) throws Exception
    {
        long t1 = cal1.getTimeInMillis();
        long t2 = cal2.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
  
    	
        int step = 1000 * 15;                 //时间间隔设置的是15s
//        ListA = new ArrayList<Vector>();//
//        ListB = new ArrayList<Vector>();
        //double I = Math.random()*30+10;//视场角
        ArrayList<Vector> tempa=new ArrayList<Vector>();
    	ArrayList<Vector> tempb=new ArrayList<Vector>();
        double I=10;
        for(long t = t1;t <= t2; t = t +step)
        {
        	cal.setTimeInMillis(t);
        	Orbit orbit = new Orbit(tle);
            double minutes = (cal.getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
            EciTime eciTime = orbit.GetPosition(minutes);
            
            Geo geo = new Geo(eciTime, eciTime.Date);
            Date dt =eciTime.Date.ToTime().getTime();
            dt.setTime(dt.getTime()+8*60*60*1000);
            Calendar calf = Calendar.getInstance();
            Calendar caln = Calendar.getInstance();
            calf.setTimeInMillis(t-step);
            caln.setTimeInMillis(t+step);
            Geo geof = getSatePosition(tle,calf);	//当前运行卫星轨道上前一个点的位置
            Geo geon = getSatePosition(tle,caln);	//当前运行卫星轨道上后一个点的位置*/
            Coverage cov = new Coverage(eciTime);
           
            cov.Calculate_FuYang(geo, eciTime, I,orbit.Inclination,FunYangAngle);//计算仰
          
            if(isTop(geo,geof,geon))
            	continue;
            if(Double.isNaN(cov.lowerLong)||Double.isNaN(cov.upperLong))
            	continue;
            tempa.add(new Vector(cov.lowerLat,cov.lowerLong,99999,eciTime.Date.ToTime()));
            tempb.add(new Vector(cov.upperLat,cov.upperLong,99999,eciTime.Date.ToTime()));
        }
        ListA.add(tempa);
        ListB.add(tempb);
    }
	
	public static ArrayList<MyPoint> getInsituCoverage(LatLon originposition,double radius)
	{
		ArrayList<MyPoint> tempCircle = new ArrayList<>();
		for(int i=0;i<32;i++)
		{
			LatLon latLon = InsituSensor.DisToLatLon(originposition, radius, Math.PI*2/32*i);
			double x = latLon.getLongitude().degrees;
			double y = latLon.getLatitude().degrees;
			MyPoint tempPoint = new MyPoint(x, y, 0, 1);
			tempCircle.add(tempPoint);
		}
		tempCircle.add(tempCircle.get(0));
		Collections.reverse(tempCircle);
		return tempCircle;
	}
}
