package com.xc.worldwind.SGP;

import java.util.Date;
import java.util.List;

public class Coverage 
{
	public double L;				//���γ�(������ɨ�����) (xml��ȡ)	��λ:km(���� ��
	public double I;					//���     (xml��ȡ��		��λ:����
	private double RS;					//���µ��Ӧ�ľֲ��뾶		��λ:km
	private double LatitudeRads;		//���µ��Ӧ�ĵ���γ��		��λ:����
	private double LongitudeRads;		//���µ��Ӧ�ĵ��ľ���		��λ:����
	private double Altitude;			//���Ǹ߶�		��λ:km
	private double RLatitude;			//���µ����ڵ�γ��Ȧ�뾶��	��λ:km
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
	
	private  double xmin,ymin,xmax,ymax;			//�������Ӿ���
	private  Date startTime,endTime;				//���ǹ۲����ʱ��

	/// γ������ֵ���ȣ�������ʾ��γ
	//public double LatitudeDeg1; 
	/// γ������ֵ���ȣ�������ʾ��γ
	//public double LatitudeDeg2; 
	/// ��������ֵ�����ȣ�������ʾ����
	//public double LongitudeDeg1; 
	/// ��������ֵ�����ȣ�������ʾ����
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
		double m_rh = Globals.Xkmper + this.Altitude;//���ĵ����ǵľ���
		double m_sinB = Math.cos(temp) * m_rh /Globals.Xkmper;
		double MaxBeta=Math.acos(m_sinB)-temp;
		return MaxBeta;
	}
	/**
	 * 
	 * @param geo ���������
	 * @param eciTime	
	 * @param i �ӳ���
	 * @param incline	������
	 */
	public void Calculate(Geo geo,EciTime eciTime,double i,double incline)
	{
		I = i;//�ӳ���
		I = Globals.ToRadians(I);
		double m_t = Math.sin(I);
		//System.out.println("m_t:"+m_t);
		double m_rh = Globals.Xkmper + this.Altitude;//���ĵ����ǵľ���
		double m_sinB = m_t * m_rh /Globals.Xkmper;
		//System.out.println("m_sinB:"+m_sinB);
		if (m_sinB>1) {  //m_sinB��ֵӦ����1���ڣ���Ȼarcsin��õ�����Ľ��
			m_sinB=1;
		}
		double m_beta = Math.asin(m_sinB) - I;//m_beta���ǵ��Ľ�

		double a1=Math.sin(incline)*Math.cos(m_beta);
		double a2=Math.cos(incline)*Math.sin(m_beta);
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		if (sinu>1) {
			sinu=1;
		}
		//Left��Right��ʾ����ǰ������ ��ߺ��ұߵĵ�
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

		lowerLat = Globals.ToRadians(lowerLat);//��γ��ת��Ϊ���ȱ�ʾ
		upperLat = Globals.ToRadians(upperLat);
		
		double B = m_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;//RsΪ���µ�ľ��ȡ�BΪ���ǵ��Ľ�
		double Ul = lowerLat,Ur = upperLat;//Ul��UrΪ�������γ��
		double Rl =0 , Rr =0;//Rl��RrΪ��������������������
		incline = Globals.ToDegrees(incline);//������ת��Ϊ������ʾ
		if(incline>90.0&&incline<180.0)
		{
			if(eciTime.Velocity.Z>0)
			{
				//���´���������ڴ��޸Ĳ���
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
				//���´���������ڴ��޸Ĳ���
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
				//���´���������ڴ��޸Ĳ���
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
				//���´���������ڴ��޸Ĳ���
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
	//�����ڸ���R��L�ľ�γ��
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
		if (m_sinB>1) {  //m_sinB��ֵӦ����1���ڣ���Ȼarcsin��õ�����Ľ��
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
		if (m_sinB2>1) {  //m_sinB��ֵӦ����1���ڣ���Ȼarcsin��õ�����Ľ��
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
		//�ڼ����ڵ�ʱ������һ���߽��γ�ȿ��ܺ����µ�Ĳ���ȫ��ͬ�������ƴ������С�����5λ��ȣ����ʾ�������	
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
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//ԭʼʽ��
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//ԭʼʽ��
				}
				if(xita>i)//���ұ߲�ڣ�L,R�������µ���ұ�
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
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//ԭʼʽ��
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//ԭʼʽ��
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
//					Rr = Rs + Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//ԭʼʽ��
//					Rl = Rs - Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//ԭʼʽ��
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
//					Rr = Rs - Math.acos((Math.cos(R_B)-Math.sin(Us)*Math.sin(Ur))/(Math.cos(Ur)*Math.cos(Us)));//ԭʼʽ��
//					Rl = Rs + Math.acos((Math.cos(L_B)-Math.sin(Us)*Math.sin(Ul))/(Math.cos(Ul)*Math.cos(Us)));//ԭʼʽ��
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
	
	//���㸩������ʱ�����µ�
	public double xingxiaLon;
	public double xingxiaLat;
	public void Calculate_FuYang(Geo geo,EciTime eciTime,double i,double incline,double xita)//xitaΪ��ڵĽǶ�
	{
		I = xita;//�����ĽǶȣ��������µ㸩������ԭ�����µ����ߵĵ��ĸ��ǽ�
		I = Globals.ToRadians(I);
		double m_t = Math.sin(I);
		double m_rh = Globals.Xkmper + this.Altitude;//���ĵ����ǵľ���
		double m_sinB = m_t * m_rh /Globals.Xkmper;
		double m_beta = Math.asin(m_sinB) - I;//m_beta���ǵ��Ľ�
	
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		double cosu=Math.sqrt(1-sinu*sinu);
		double cotu=cosu/sinu;

		double xingxiaPointlat=Math.asin(Math.sin(geo.LatitudeDeg*Math.PI/180)*(Math.cos(m_beta)+Math.sin(m_beta)*cotu));
		xingxiaPointlat = xingxiaPointlat*180/Math.PI;
		xingxiaPointlat = Globals.ToRadians(xingxiaPointlat);
		double B = m_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;//RsΪ���µ�ľ��ȡ�BΪ���ǵ��Ľ�
		double Ul = xingxiaPointlat;
		double Rl =0;
		if((int)(xingxiaPointlat*100000)==(int)(geo.LatitudeRad*100000))//�����㸩������������µ�γ��ʱ�����������С�����5λ��ȣ����ʾ�������
		{
			Rl=Rs;
		}
		incline = Globals.ToDegrees(incline);//������ת��Ϊ������ʾ
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
		double b = Globals.Xkmper + this.Altitude;//���ĵ����ǵľ���
		/////���㸩��֮������µ������֮��ľ���
		double c=0.0;
		if(Math.sin(xita*Math.PI/180)==0)
		{
			c=geo.Altitude;
		}
		else {
			c=a*Math.sin(B)/Math.sin(xita*Math.PI/180);
		}
		/////����߽����������·����µ����ߵĵ��ĸ��ǽ�
		
		double dic=c*Math.tan(i*Math.PI/180);
		double beita_1=dic/a;
		fun(beita_1,xingxiaPointGeo, incline, eciTime);
	}
	public void fun(double m_beta,Geo geo,double incline,EciTime eciTime)
	{
		double a1=Math.sin(incline)*Math.cos(m_beta);
		double a2=Math.cos(incline)*Math.sin(m_beta);
		double sinu=Math.sin(geo.LatitudeDeg*Math.PI/180)/Math.sin(incline);
		//Left��Right��ʾ����ǰ������ ��ߺ��ұߵĵ�
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
		
		lowerLat = Globals.ToRadians(lowerLat);//��γ��ת��Ϊ���ȱ�ʾ
		upperLat = Globals.ToRadians(upperLat);
		
		double B = m_beta,Us=geo.LatitudeRad,Rs = geo.LongitudeRad;//RsΪ���µ�ľ��ȡ�BΪ���ǵ��Ľ�
		double Ul = lowerLat,Ur = upperLat;//Ul��UrΪ�������γ��
		double Rl =0 , Rr =0;//Rl��RrΪ��������������������
		incline = Globals.ToDegrees(incline);//������ת��Ϊ������ʾ
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
	
	

	
	