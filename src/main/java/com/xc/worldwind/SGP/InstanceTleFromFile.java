package com.xc.worldwind.SGP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class InstanceTleFromFile 
{
	public static List<Tle> tles;
	
	/**
	  * ��ȡtxt�ļ�������
	  * @param file ��Ҫ��ȡ���ļ�����
	  * @return �����ļ�����
	  */
	public static List<String> txt2String(File file)
	{  
		List<String> result = new ArrayList<String>(); 
		try
		{ 
			BufferedReader br = new BufferedReader(new FileReader(file));//����һ��BufferedReader������ȡ�ļ� 
			String s = null;
			while((s = br.readLine())!=null)
			{
				//ʹ��readLine������һ�ζ�һ��  
				result.add(s);
				//result = result + "\n" +s;
			}  
			br.close();    
		}
		catch(Exception e)
		{     
			e.printStackTrace();
		} 
		return result;
	}
	
	public static void instanceTles()
	{
		String dirPath = System.getProperty("user.dir");
		dirPath+="\\tles";
		tles = new ArrayList<Tle>();
		
		File file=new File(dirPath);
		File[] tempList = file.listFiles();
		System.out.println("��Ŀ¼�¶��������"+tempList.length);
		
		List<String> tleStrs;
		for (int i = 0; i < tempList.length; i++) 
		{
		   if (tempList[i].isFile()) 
		   {
			   tleStrs = txt2String(tempList[i]);
			   for(int j=0;j<tleStrs.size();j+=3)
			   {
				   tles.add(new Tle(tleStrs.get(j),tleStrs.get(j+1),tleStrs.get(j+2)));
			   }
			   //System.out.println("��     ����"+tempList[i]);
		   }
		}
	}
	
	public static void output()
	{
		if(tles !=null && tles.size()>0)
		{
			Tle tle = new Tle();
			for(int i=0;i<tles.size();i++)
			{
				tle = tles.get(i);
				System.out.println(i+"\n"+tle.getName()+"\n"+tle.getLine1()+"\n"+tle.getLine2());
			}
		}
	}
	
	public static void main(String[] args)
	{
		instanceTles();
		
		output();
	}
}
