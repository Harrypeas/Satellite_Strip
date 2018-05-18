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
 * @author ����
 * @class ���Ǹ�����
 * @function �ṩ���ǿռ串����ع���
 * 
 */


public class SatCoverage {
	public static ArrayList<ArrayList<Vector>> ListA = new ArrayList<ArrayList<Vector>>();		//���Ǳ߽����
	public static ArrayList<ArrayList<Vector>> ListB = new ArrayList<ArrayList<Vector>>();		//���Ǳ߽��ұ�
	public static List<Geo> Positions=null;		//���λ��
	public static List<Vector> clipResult=null;		//�ü����
	public static double covRate = 0.0;				//������
	
	/**
	 * ��ȡ����ĳһʱ��λ��
	 * @param tle tle��������
	 * @param cal ���ڸ�ʽʱ��
	 * @return ��������ϵ��ʾ����ά�����Geo
	 * @throws Exception
	 */
	public static Geo getSatePosition(Tle tle,Calendar cal) throws Exception
	{
    	Orbit orbit = new Orbit(tle);
    	//�������ش�Calendar�Ժ���Ϊ��λ��ʱ��
        double minutes = (cal.getTimeInMillis() - orbit.EpochTime.getTimeInMillis())*1.0/(1000 * 60) - 8*60;
        EciTime eciTime = orbit.GetPosition(minutes);
        if(eciTime==null)//�жϷ��ص����ǲ����Ƿ�Ϊ�գ����Ϊ��ֵ���򷵻ؿ�ֵ
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
	 * �ж��н������Ƿ����ı�
	 */
	private static boolean isTop(Geo geo,Geo geof,Geo geon)
	{
		if((geo.LatitudeDeg-geof.LatitudeDeg)*(geo.LatitudeDeg-geon.LatitudeDeg)>0) //�������ı�
			return true;
		return false;
	}
	/**
	 * �ṩ����ֱ�Ӹ��ǵĸ����������������洢��LsitA��ListB
	 * @param tle tle��������
	 * @param cal1 ��ʼ�۲����ڸ�ʽʱ��
	 * @param cal2 �����۲����ڸ�ʽʱ��
	 * @throws Exception
	 */
	public static void PrintPosVel_1(Tle tle,Calendar cal1,Calendar cal2) throws Exception
    {
        long t1 = cal1.getTimeInMillis();
        long t2 = cal2.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
  
        int step = 1000 * 1;                 //ʱ�������õ���1s
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
            Geo geof = getSatePosition(tle,calf);	//��ǰ�������ǹ����ǰһ�����λ��
            Geo geon = getSatePosition(tle,caln);	//��ǰ�������ǹ���Ϻ�һ�����λ��
            Coverage cov = new Coverage(eciTime);
            cov.Calculate(geo,eciTime,I,orbit.Inclination);//orbit.Inclination ������
            if(isTop(geo,geof,geon))
            	continue;
            tempa.add(new Vector(cov.lowerLat,cov.lowerLong,99999,eciTime.Date.ToTime()));
            tempb.add(new Vector(cov.upperLat,cov.upperLong,99999,eciTime.Date.ToTime()));  
        }
        ListA.add(tempa);
        ListB.add(tempb);
    }
	/**
	 * �ṩ�������Ҳ��ӵĸ����������������洢��LsitA��ListB
	 * @param tle tle��������
	 * @param cal1 ��ʼ�۲����ڸ�ʽʱ��
	 * @param cal2 �����۲����ڸ�ʽʱ��
	 * @param angle ����ƫת�Ƕ�
	 * @throws Exception
	 */
	public static void PrintPosVel_2(Tle tle,Calendar cal1,Calendar cal2,double angle) throws Exception
    {
        long t1 = cal1.getTimeInMillis();
        long t2 = cal2.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
  
        int step = 1000 * 1;                 //ʱ�������õ���15s
//        ListA = new ArrayList<Vector>();//
//        ListB = new ArrayList<Vector>();
       // double I = Math.random()*30+10;//�ӳ���
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
            Geo geof = getSatePosition(tle,calf);	//��ǰ�������ǹ����ǰһ�����λ��
            Geo geon = getSatePosition(tle,caln);	//��ǰ�������ǹ���Ϻ�һ�����λ��*/
            Coverage cov = new Coverage(eciTime);
            cov.Calculate(geo,eciTime,orbit.Inclination,I,angle);//orbit.Inclination ������
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
	 * �ṩ���Ǹ����۲�ĸ����������������洢��LsitA��ListB
	 * @param tle tle��������
	 * @param cal1 ��ʼ�۲����ڸ�ʽʱ��
	 * @param cal2 �����۲����ڸ�ʽʱ��
	 * @param angle ����ƫת�Ƕ�
	 * @throws Exception
	 */
	public static void PrintPosVel_3(Tle tle,Calendar cal1,Calendar cal2,double FunYangAngle) throws Exception
    {
        long t1 = cal1.getTimeInMillis();
        long t2 = cal2.getTimeInMillis();
        Calendar cal = Calendar.getInstance();
  
    	
        int step = 1000 * 15;                 //ʱ�������õ���15s
//        ListA = new ArrayList<Vector>();//
//        ListB = new ArrayList<Vector>();
        //double I = Math.random()*30+10;//�ӳ���
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
            Geo geof = getSatePosition(tle,calf);	//��ǰ�������ǹ����ǰһ�����λ��
            Geo geon = getSatePosition(tle,caln);	//��ǰ�������ǹ���Ϻ�һ�����λ��*/
            Coverage cov = new Coverage(eciTime);
           
            cov.Calculate_FuYang(geo, eciTime, I,orbit.Inclination,FunYangAngle);//������
          
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
