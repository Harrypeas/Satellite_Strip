package com.xc.worldwind.SGP;

import java.util.Date;
import java.util.List;

public class Coverage 
{
	public double L;				//矩形长(传感器扫描幅宽) (xml读取)	单位:km(测试 ）
	public double I;					//倾角     (xml读取）		单位:弧度
	private double RS;					//星下点对应的局部半径		单位:km
	private double LatitudeRads;		//星下点对应的地心纬度		单位:弧度
	private double LongitudeRads;		//星下点对应的地心经度		单位:弧度
	private double Altitude;			//卫星高度		单位:km
	private double RLatitude;			//星下点所在的纬度圈半径：	单位:km
	public double lowerLong;
	public double upperLong;
	public double lowerLat;
	public double upperLat;
	public double covRate = 0.0;
	
	private List<Vector> times;
	
	double []x;
	double []y;
	double []x1;
	double []y1;
	int []n;
	int []n1;
	
	private  double xmin,ymin,xmax,ymax;			//多边形外接矩形
	private  Date startTime,endTime;				//卫星观测过境时间

	/// 纬度下限值（度）负数表示南纬
	//public double LatitudeDeg1; 
	/// 纬度上限值（度）负数表示南纬
	//public double LatitudeDeg2; 
	/// 经度下限值（弧度）负数表示西经
	//public double LongitudeDeg1; 
	/// 经度上限值（弧度）负数表示西经
	//public double LongitudeDeg2; 
	
	public Coverage()
	{
		Initialize();
	}
	
	public Coverage(EciTime eci)
	{
		Geo geo = new Geo(eci,eci.Date);
		this.LatitudeRads = geo.LatitudeRad;
		this.LongitudeRads = geo.LongitudeRad;
		this.Altitude = geo.Altitude;
		Initialize();
	}
	
	private void Initialize()
	{
		RS = Globals.Xkmper * (1 - Globals.F)/
			(Math.sqrt(1-Globals.F*(2-Globals.F)*
			 Math.pow(Math.cos(LatitudeRads), 2)));
		RLatitude = RS * Math.cos(LatitudeRads);
	}

	public double maxBeta()
	{
		double temp=5*Math.PI/180;
		double m_rh = Globals.Xkmper + this.Altitude;//地心到卫星的距离
		double m_sinB = Math.cos(temp) * m_rh /Globals.Xkmper;
		double MaxBeta=Math.acos(m_sinB)-temp;
		return MaxBeta;
	}
	/**
	 * 
	 * @param geo 地理坐标点
	 * @param eciTime	
	 * @param i 视场角
	 * @param incline	轨道倾角
	 */
	public void Calculate(Geo geo,EciTime eciTime,double i,double incline)
	{
		I = i;//视场角
		I = Globals.ToRadians(I);
		double m_t = Math.sin(I);
		//System.out.println("m_t:"+m_t);
		double m_rh = Globals.Xkmper + this.Altitude;//地心到卫星的距离
		double m_sinB = m_t * m_rh /Globals.Xkmper;
		//System.out.println("m_sinB:"+m_sinB);
		if (m_sinB>1) {  //m_sinB的值应该在1以内，不然arcsin会得到错误的结果
			m_sinB=1;
		}
		double m_beta = Math.asin(m_sinB) - I;//m_beta覆盖地心角

		double a1=Math.sin(incline)*Math.cos(m_beta);
		double a2=Math.cos(incline)*Math.sin(m_beta);
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		if (sinu>1) {
			sinu=1;
		}
		//Left和Right表示卫星前进方向 左边和右边的点
		double LeftLat=0.0;
		double RightLat=0.0;
		//System.out.println("a1:"+a1+"  a2:"+a2+"  sinu:"+sinu);
		if(Globals.ToDegrees(incline)>90)
		{
			LeftLat=Math.asin(a1*sinu+a2);
			RightLat=Math.asin(a1*sinu-a2);
		}
		else {
			LeftLat=Math.asin(a1*sinu-a2);
			RightLat=Math.asin(a1*sinu+a2);
		}
		lowerLat = LeftLat*180/Math.PI;
		upperLat = RightLat*180/Math.PI;
		
		//lowerLat = geo.LatitudeDeg - 0.1;
		//upperLat = geo.LatitudeDeg + 0.1;

		lowerLat = Globals.ToRadians(lowerLat);//把纬度转化为弧度表示
		upperLat = Globals.ToRadians(upperLat);
		
		double B = m_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;//Rs为星下点的经度、B为覆盖地心角
		double Ul = lowerLat,Ur = upperLat;//Ul与Ur为两个点的纬度
		double Rl =0 , Rr =0;//Rl与Rr为两个矩形条带的两个点
		incline = Globals.ToDegrees(incline);//轨道倾角转化为度数表示
		if(incline>90.0&&incline<180.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				//以下代码由黎杰在此修改测试
				double right=(Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
				double left=(Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
				if (right>1) {
					right=1;
				}
				else if (right<-1) {
					right=-1;
				}
				if (left>1) {
					left=1;
				}
				else if (left<-1) {
					left=-1;
				}
				Rr = Rs + Math.acos(right);
				Rl = Rs - Math.acos(left);
//				Rr = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//				Rl = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
			else if(eciTime.Velocity.Z<0)
			{
				//以下代码由黎杰在此修改测试
				double right=(Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
				double left=(Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
				if (right>1) {
					right=1;
				}
				else if (right<-1) {
					right=-1;
				}
				if (left>1) {
					left=1;
				}
				else if (left<-1) {
					left=-1;
				}
				Rr = Rs - Math.acos(right);
				Rl = Rs + Math.acos(left);
//				Rr = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//				Rl = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
		}
		else if(incline>0.0&&incline<90.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				//以下代码由黎杰在此修改测试
				double right=(Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
				double left=(Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
				if (right>1) {
					right=1;
				}
				else if (right<-1) {
					right=-1;
				}
				if (left>1) {
					left=1;
				}
				else if (left<-1) {
					left=-1;
				}
				Rr = Rs - Math.acos(right);
				Rl = Rs + Math.acos(left);
				
//				Rr = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//				Rl = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
//				System.out.println((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
			else if(eciTime.Velocity.Z<0)
			{
				//以下代码由黎杰在此修改测试
				double right=(Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
				double left=(Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
				if (right>1) {
					right=1;
				}
				else if (right<-1) {
					right=-1;
				}
				if (left>1) {
					left=1;
				}
				else if (left<-1) {
					left=-1;
				}
				Rr = Rs + Math.acos(right);
				Rl = Rs - Math.acos(left);
//				Rr = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//				Rl = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
		}


		lowerLat = Globals.ToDegrees(lowerLat);
		upperLat = Globals.ToDegrees(upperLat);
		lowerLong = Globals.ToDegrees(Rl)>180?Globals.ToDegrees(Rl)-360:Globals.ToDegrees(Rl);
		upperLong = Globals.ToDegrees(Rr)>180?Globals.ToDegrees(Rr)-360:Globals.ToDegrees(Rr);
	}
	//计算侧摆覆盖R和L的经纬度
	public void Calculate(Geo geo,EciTime eciTime,double incline,double i,double xita)
	{
		double Il= 0.0,Ir = 0.0;
		if(Math.abs(xita)-0<0.001)
			Il= Ir = i;
		else
		{
			Il = Math.abs(i-xita);
			Ir =  Math.abs(i+xita);
		}
			
		Il = Globals.ToRadians(Il);
		double m_t = Math.sin(Il);
		double m_rh = Globals.Xkmper + this.Altitude;
		double m_sinB = m_t * m_rh /Globals.Xkmper;
		double L_beta = Math.asin(m_sinB) - Il;
		if (m_sinB>1) {  //m_sinB的值应该在1以内，不然arcsin会得到错误的结果
			m_sinB=1;
		}
		else if (m_sinB < -1) {
			m_sinB = -1;
		}
			
		Ir = Globals.ToRadians(Ir);
		double m_t2 = Math.sin(Ir);
		double m_rh2 = Globals.Xkmper + this.Altitude;
		double m_sinB2 = m_t2 * m_rh2 /Globals.Xkmper;
		double R_beta = Math.asin(m_sinB2) - Ir;
		if (m_sinB2>1) {  //m_sinB的值应该在1以内，不然arcsin会得到错误的结果
			m_sinB2=1;
		}
		else if (m_sinB2 < -1) {
			m_sinB2 = -1;
		}
		double a1=Math.sin(incline)*Math.cos(L_beta);
		double a2=Math.cos(incline)*Math.sin(L_beta);
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		if (sinu > 1) {
			sinu = 1;
		}
		else if (sinu < -1) {
			sinu = -1;
		}
		double LeftLat=0.0;
		if(xita>0&&xita<i||xita<0)
		{
			LeftLat=Math.asin(a1*sinu+a2);
		}
		else
		{
			LeftLat=Math.asin(a1*sinu-a2);
		}
		lowerLat = LeftLat*180/Math.PI;
			
		double a_1=Math.sin(incline)*Math.cos(R_beta);
		double a_2=Math.cos(incline)*Math.sin(R_beta);
		double sinu2=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		if (sinu2 > 1) {
			sinu2 = 1;
		}
		else if (sinu2 < -1) {
			sinu2 = -1;
		}
		double RightLat=0.0;
		if(xita<0&&(-xita)<i||xita>0)
		{
			RightLat=Math.asin(a_1*sinu2-a_2);
		}
		else {
			RightLat=Math.asin(a_1*sinu2+a_2);
		}
		upperLat = RightLat*180/Math.PI;
			

		lowerLat = Globals.ToRadians(lowerLat);
		upperLat = Globals.ToRadians(upperLat);
			
		double L_B = L_beta,R_B = R_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;
		double Ul = lowerLat,Ur = upperLat;
		double Rl =0 , Rr =0;
		incline = Globals.ToDegrees(incline);
		//在计算侧摆的时候，其中一个边界的纬度可能和星下点的不完全相同，做近似处理，如果小数点后5位相等，则表示二者相等	
		if(incline>90.0&&incline<180.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				if(Math.abs(xita)<=i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs + Math.acos(right);
					Rl = Rs - Math.acos(left);
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//原始式子
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//原始式子
				}
				if(xita>i)//向右边侧摆，L,R都在星下点的右边
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs + Math.acos(right);
					Rl = Rs + Math.acos(left);
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
				if(-xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs - Math.acos(right);
					Rl = Rs - Math.acos(left);
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
			}
			else if(eciTime.Velocity.Z<0)
			{
				if(Math.abs(xita)<=i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs - Math.acos(right);
					Rl = Rs + Math.acos(left);
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//原始式子
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//原始式子
				}
				if(xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs - Math.acos(right);
					Rl = Rs - Math.acos(left);
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
				if(-xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs + Math.acos(right);
					Rl = Rs + Math.acos(left);
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
			}
		}
		else if(incline>0.0&&incline<90.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				if(Math.abs(xita)<=i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs + Math.acos(right);
					Rl = Rs - Math.acos(left);
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//原始式子
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//原始式子
				}
				if(xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs + Math.acos(right);
					Rl = Rs + Math.acos(left);
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
				if(-xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs - Math.acos(right);
					Rl = Rs - Math.acos(left);
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
			}
			else if(eciTime.Velocity.Z<0)
			{
				if(Math.abs(xita)<=i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs - Math.acos(right);
					Rl = Rs + Math.acos(left);
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//原始式子
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//原始式子
				}
				if(xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs - Math.acos(right);
					Rl = Rs - Math.acos(left);
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
				if(-xita>i)
				{
					double right = (Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us));
					double left = (Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us));
					if (right > 1) {
						right = 1;
					}
					else if (right < -1) {
						right = -1;
					}
					
					if (left > 1) {
						left = 1;
					}
					else if (left < -1) {
						left = -1;
					}
					Rr = Rs + Math.acos(right);
					Rl = Rs + Math.acos(left);
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
				}
			}
				
		}
		lowerLat = Globals.ToDegrees(lowerLat);
		upperLat = Globals.ToDegrees(upperLat);
		lowerLong = Globals.ToDegrees(Rl)>180?Globals.ToDegrees(Rl)-360:Globals.ToDegrees(Rl);
		upperLong = Globals.ToDegrees(Rr)>180?Globals.ToDegrees(Rr)-360:Globals.ToDegrees(Rr);
	}
	
	//计算俯仰覆盖时的星下点
	public double xingxiaLon;
	public double xingxiaLat;
	public void Calculate_FuYang(Geo geo,EciTime eciTime,double i,double incline,double xita)//xita为侧摆的角度
	{
		I = xita;//俯仰的角度，计算星下点俯仰后与原来星下点连线的地心覆盖角
		I = Globals.ToRadians(I);
		double m_t = Math.sin(I);
		double m_rh = Globals.Xkmper + this.Altitude;//地心到卫星的距离
		double m_sinB = m_t * m_rh /Globals.Xkmper;
		double m_beta = Math.asin(m_sinB) - I;//m_beta覆盖地心角
	
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		double cosu=Math.sqrt(1-sinu*sinu);
		double cotu=cosu/sinu;

		double xingxiaPointlat=Math.asin(Math.sin(geo.LatitudeDeg*Math.PI/180)*(Math.cos(m_beta)+Math.sin(m_beta)*cotu));
		xingxiaPointlat = xingxiaPointlat*180/Math.PI;
		xingxiaPointlat = Globals.ToRadians(xingxiaPointlat);
		double B = m_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;//Rs为星下点的经度、B为覆盖地心角
		double Ul = xingxiaPointlat;
		double Rl =0;
		if((int)(xingxiaPointlat*100000)==(int)(geo.LatitudeRad*100000))//当计算俯仰调整后的星下点纬度时，如果两者在小数点后5位相等，则表示二者相等
		{
			Rl=Rs;
		}
		incline = Globals.ToDegrees(incline);//轨道倾角转化为度数表示
		if(incline>90.0&&incline<180.0&&Rl==0)
		{
			if(eciTime.Velocity.Z>0)
			{
				Rl = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
			else if(eciTime.Velocity.Z<0)
			{
				Rl = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
		}
		else if(incline>0.0&&incline<90.0&&Rl==0)
		{
			if(eciTime.Velocity.Z>0)
			{
				Rl = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
			else if(eciTime.Velocity.Z<0)
			{
				Rl = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
		}
		xingxiaPointlat = Globals.ToDegrees(xingxiaPointlat);
		double xingxiaPointlon = Globals.ToDegrees(Rl)>180?Globals.ToDegrees(Rl)-360:Globals.ToDegrees(Rl);
		
		xingxiaLat=xingxiaPointlat;
		xingxiaLon=xingxiaPointlon;
	
		Geo xingxiaPointGeo=new Geo(eciTime,eciTime.Date);
		xingxiaPointGeo.LatitudeDeg=xingxiaPointlat;xingxiaPointGeo.LongitudeDeg=xingxiaPointlon;
		incline=Globals.ToRadians(incline);
		
		double a = Globals.Xkmper ;
		double b = Globals.Xkmper + this.Altitude;//地心到卫星的距离
		/////计算俯视之后的星下点和卫星之间的距离
		double c=0.0;
		if(Math.sin(xita*Math.PI/180)==0)
		{
			c=geo.Altitude;
		}
		else {
			c=a*Math.sin(B)/Math.sin(xita*Math.PI/180);
		}
		/////计算边界与卫星正下方星下点连线的地心覆盖角
		
		double dic=c*Math.tan(i*Math.PI/180);
		double beita_1=dic/a;
		fun(beita_1,xingxiaPointGeo, incline, eciTime);
	}
	public void fun(double m_beta,Geo geo,double incline,EciTime eciTime)
	{
		double a1=Math.sin(incline)*Math.cos(m_beta);
		double a2=Math.cos(incline)*Math.sin(m_beta);
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		//Left和Right表示卫星前进方向 左边和右边的点
		double LeftLat=0.0;
		double RightLat=0.0;
		if(Globals.ToDegrees(incline)>90)
		{
			LeftLat=Math.asin(a1*sinu+a2);
			RightLat=Math.asin(a1*sinu-a2);
		}
		else {
			LeftLat=Math.asin(a1*sinu-a2);
			RightLat=Math.asin(a1*sinu+a2);
		}
		lowerLat = LeftLat*180/Math.PI;
		upperLat = RightLat*180/Math.PI;
		
		lowerLat = Globals.ToRadians(lowerLat);//把纬度转化为弧度表示
		upperLat = Globals.ToRadians(upperLat);
		
		double B = m_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;//Rs为星下点的经度、B为覆盖地心角
		double Ul = lowerLat,Ur = upperLat;//Ul与Ur为两个点的纬度
		double Rl =0 , Rr =0;//Rl与Rr为两个矩形条带的两个点
		incline = Globals.ToDegrees(incline);//轨道倾角转化为度数表示
		if(incline>90.0&&incline<180.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				Rr = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
				Rl = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
			else if(eciTime.Velocity.Z<0)
			{
				Rr = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
				Rl = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
		}
		else if(incline>0.0&&incline<90.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				Rr = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
				Rl = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
			else if(eciTime.Velocity.Z<0)
			{
				Rr = Rs + Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));
				Rl = Rs - Math.acos((Math.cos(B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));
			}
		}
		lowerLat = Globals.ToDegrees(lowerLat);
		upperLat = Globals.ToDegrees(upperLat);
		lowerLong = Globals.ToDegrees(Rl)>180?Globals.ToDegrees(Rl)-360:Globals.ToDegrees(Rl);
		upperLong = Globals.ToDegrees(Rr)>180?Globals.ToDegrees(Rr)-360:Globals.ToDegrees(Rr);
	}
}
	
	

	
	