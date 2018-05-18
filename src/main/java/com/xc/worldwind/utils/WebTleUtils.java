package com.xc.worldwind.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xc.worldwind.function.SatDataUpdate;
import com.xc.worldwind.object.TleList;
import com.xc.worldwind.object.TleString;

/**
 * @author ����
 * @class ����Tle���ݲ�����
 * @function ʵ����TleList�������ļ������н���
 */
public class WebTleUtils 
{
	public static String baseUrl = "http://www.celestrak.com/NORAD/elements";
	
	private static TleList alltles;					//��������tle�б�
	
	private static long index = SatDataUpdate.STARTNUMBER;		//��Ǽ�¼����number

	public static TleList getAlltles()
	{
		return alltles;
	}
	
	public static void updateWeb()
	{
			getAllTles(baseUrl);
	}
	
	public static void updateRedis()
	{
//		Jedis jedis = JedisUtils.Instance();
//		Object satelites = jedis.get("satelites");
//		if(satelites == null)		//redis�����ѹ���
//		{
//			jedis.flushDB();
//			getAllTles(baseUrl);
//			
//			String key = "", value = "";
//			int startIndex = 10000,endIndex = startIndex;
//			for(int i=0;i<alltles.getSize();i++)
//			{
//				TleString tle = (TleString)alltles.get(i);
//				key = tle.getName();
//				jedis.set(key, (10000+i)+"");
//				
//				key = "tle."+(10000+i)+".number";
//				value = (10000+i)+"";
//				jedis.set(key, value);
//				
//				key = "tle."+(10000+i)+".name";
//				value = tle.getName();
//				jedis.set(key, value);
//				
//				key = "tle."+(10000+i)+".category";
//				value = tle.getCategory();
//				jedis.set(key, value);
//				
//				key = "tle."+(10000+i)+".type";
//				value = tle.getType();
//				jedis.set(key, value);
//				
//				key = "tle."+(10000+i)+".tle1";
//				value = tle.getTle1();
//				jedis.set(key, value);
//				
//				key = "tle."+(10000+i)+".tle2";
//				value = tle.getTle2();
//				jedis.set(key, value);
//				
//				endIndex++;
//			}
//			jedis.set("tstartIndex", startIndex+"");
//			jedis.set("tendIndex", endIndex+"");
//
//			jedis.set("satelites", (endIndex-1)+"");
//			jedis.expire("satelites", 5*60);	//5����� 
//		}
//		fillByRedis();
	}

	public static void fillByRedis()
	{
//		Jedis jedis = JedisUtils.Instance();
//		String startIndex = jedis.get("tstartIndex");
//		String endIndex = jedis.get("tendIndex");
//		if(!startIndex.equals("null") && !endIndex.equals("null"))
//		{
//			int start = Integer.valueOf(startIndex);
//			int end = Integer.valueOf(endIndex);
//			String key="";
//			
//			String name,tle1,tle2,category,type,numString;
//			long number = 0;
//			alltles = new TleList();
//			for(int i=start;i<end;i++)
//			{
//				key = "tle."+i+".number";
//				numString = jedis.get(key).toString();
//				number = Long.valueOf(numString);
//				key = "tle."+i+".name";
//				name = jedis.get(key);
//				key = "tle."+i+".tle1";
//				tle1 = jedis.get(key);
//				key = "tle."+i+".tle2";
//				tle2 = jedis.get(key);
//				key = "tle."+i+".category";
//				category = jedis.get(key);
//				key = "tle."+i+".type";
//				type = jedis.get(key);
//				alltles.addTleString(new TleString(number,name,category,type,tle1,tle2));
//			}
//		}
	}
	
	/**
	 * ��ȡurl���е��ַ�����
	 * @param url ��ַ
	 * @return String �������url�����ӵ�����
	 */
	public static String readUrl(String url)
	{
		StringBuilder sb = new StringBuilder();
		InputStream in;
		BufferedReader reader;
		try
		{
			 URL myurl = new URL(url); 
             //�򿪵��� URL �����Ӳ�����һ�����ڴӸ����Ӷ���� InputStream�� 
              in = myurl.openStream(); 
              reader = new BufferedReader(new InputStreamReader(in));
             String line;
             while ((line = reader.readLine()) != null) 
             {
            	 sb.append(line+"\r\n");
             } 
             in.close();
 			 reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return null;
		}
		return sb.toString();
	}
	
	/**
	 * ��url�����й���Tle�����б�,����txt�����ļ���
	 * @param url ��ַ
	 * @param type ��ַ��Ӧ�ؼ���
	 * @return List<TleString>  �������˫�����ݼ���
	 */
	public static List<TleString> readFromWeb(String url,String type)
	{
		List<TleString> tles = new ArrayList<TleString>();
		String content = readUrl(url);//�������ƣ���һ�У��ڶ��н��зָ�txt�ڵ�����
		String category = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));//lastIndexOf() �����ɷ���һ��ָ�����ַ���ֵ�����ֵ�λ��,
		
		//System.out.println(content);
		try
		{
			String filedir = "C:/testData/txtfiles/satelite";
			String filename = url.substring(url.lastIndexOf("/")+1);
			
			if(!new File(filedir).exists())
			{
				new File(filedir).mkdirs();
				/* mkdir()
				 * ֻ�����Ѿ����ڵ�Ŀ¼�д��������ļ��С�
                 * mkdirs()
                 * �����ڲ����ڵ�Ŀ¼�д����ļ��С����磺a\\b,�ȿ��Դ����༶Ŀ¼��
                 */
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filedir+"/"+filename)));
			/*����һ��������,����д�����ڽ�����д��Ӳ��
                            ��������������������
             */
			writer.write(type);
			writer.newLine();
			writer.write(category);
			writer.newLine();
			writer.write(content);
			writer.close();
		}
		catch(Exception e){
		}
		
		if(content == null || content.equals(""))
		{
			return tles;
		}
		
		String []strs = content.split("\r\n");//���س����зָ�����
		if(strs.length%3 ==0)	//�ٴ���֤��ȡtle��ȷ
		{
			for(int i=0;i<strs.length;i+=3)
			{
				tles.add(new TleString(index++,strs[i].trim(),category.trim(),type.trim(),strs[i+1].trim(),strs[i+2].trim()));
			}
		}
		return tles;
	}
	
	/**
	 * ���������淽ʽ��ȡnasa��������Tle���ݣ��������alltles��
	 * @param baseUrl ������ַ
	 */
	@SuppressWarnings("rawtypes")
	public static void getAllTles(String baseUrl)
	{
		alltles = new TleList();
		
		Map<String,String> maps = HttpUtils.extractLinks(baseUrl);
		Iterator<Entry<String,String>> iter = maps.entrySet().iterator();//��map��������ת���ɼ���set���ͣ�Ȼ�������������������ֻ��Լ������͵�����
		index = SatDataUpdate.STARTNUMBER;
		while (iter.hasNext())
		{
			Entry e = iter.next();
			String type = (String) e.getKey();
			String url = (String) e.getValue();
			//System.out.println(category + "��" + url);
			
			//Category categy = new Category(category,new ArrayList<String>());
			List<TleString> tles = readFromWeb(url,type);
			if(tles != null && !tles.isEmpty())
			{
				alltles.addAllTleStrings(tles);
//				for(TleString tle:tles)
//				{
//					if(!alltles.contains(tle))
//					{
//						tle.setType(category);
//						alltles.addTleString(tle);
//					}
//				}
			}
		}
	}

}
