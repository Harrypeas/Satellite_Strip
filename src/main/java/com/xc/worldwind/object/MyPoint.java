package com.xc.worldwind.object;


import java.util.ArrayList;
import java.util.TreeSet;

public class MyPoint {
	private double x;
	private double y;
	private int flag;
	private int InorOut;  //in=-1 out=1 确认外边界还是内边界
	private String DiffSensorID;
	public TreeSet<String> SensorInfo=new TreeSet<String>();
	public ArrayList<Integer> DiffRectNumber=new ArrayList<Integer>();
	public TreeSet<Integer> RectNumber=new TreeSet<Integer>();
	
	public MyPoint()
	{
		
	}
	
	public MyPoint(double xx,double yy,int nflag,int inorout)
	{
		x=xx;
		y=yy;
		flag=nflag;
		InorOut=inorout;
		//RectNum=rectnum;
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double nx)
	{
		x=nx;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setY(double ny)
	{
		y=ny;
	}
	
	public int getflag()
	{
		return flag;
	}
	
	public void setflag(int nflag)
	{
		flag=nflag;
	}
	
	public int getInorOut()
	{
		return InorOut;
	}
	
	public void setInorOut(int ninorout)
	{
		InorOut=ninorout;
	}
	
	public String getDiffSensorID()
	{
		return DiffSensorID;
	}
	
	public void setDiffSensorID(String newid)
	{
		DiffSensorID = newid;
	}
//	public String getRectNum()
//	{
//		return RectNum;
//	}
//	
//	public void setRectNum(String nrectnum)
//	{
//		RectNum=nrectnum;
//	}
	
//	public void SortNumber(MyPoint point)
//	{
//		String rectnum=point.getRectNum();
//		String num="";
//		char[] rect=rectnum.toCharArray();
//		for(char a:rect)
//		{
//			int k=0;
//			for(int b=0;b<rect.length;b++)
//			{
//				if (a==rect[b]) {
//					k++;
//					if (k>=2) {
//						rect[b]='0';
//					}
//				}
//			}
//		}
//		num=String.valueOf(rect);
//		num=num.replaceAll("0", "");
//		RectNum=num;
//	}
	
//	public void sortDiffRN(MyPoint a)
//	{
//		char [] tem=a.getRectNum().substring(1, a.getRectNum().length()).toCharArray();
//		String sortstring=a.getRectNum().substring(0, 1);
//		ArrayList<Integer>tempp=new ArrayList<Integer>();
//		for(int i=0;i<tem.length;i++)
//		{
//			tempp.add(Integer.parseInt(String.valueOf(tem[i])));
//			System.out.println(tem[i]);
//		}
//		Collections.sort(tempp);
//		for(Integer j:tempp)
//		{
//			if (sortstring==null) {
//				sortstring=String.valueOf(j);
//			}
//			else sortstring+=String.valueOf(j);
//		}
//		RectNum=sortstring;
//	}
	
//	public  void  sortRectNum(MyPoint a)
//	{
//		char [] tem=a.getRectNum().toCharArray();
//		String sortstring=null;
//		ArrayList<Integer>tempp=new ArrayList<Integer>();
//		for(int i=0;i<tem.length;i++)
//		{
//			tempp.add(Integer.parseInt(String.valueOf(tem[i])));
//			System.out.println(tem[i]);
//		}
//		Collections.sort(tempp);
//		
//		for(Integer j:tempp)
//		{
//			if (sortstring==null) {
//				sortstring=String.valueOf(j);
//			}
//			else sortstring+=String.valueOf(j);
//		}
//		RectNum=sortstring;
//	}
}
