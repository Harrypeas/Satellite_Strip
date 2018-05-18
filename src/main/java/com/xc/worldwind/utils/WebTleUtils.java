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
 * @author 钟翔
 * @class 卫星Tle数据操作类
 * @function 实例化TleList对象并与文件流进行交互
 */
public class WebTleUtils 
{
	public static String baseUrl = "http://www.celestrak.com/NORAD/elements";
	
	private static TleList alltles;					//所有卫星tle列表
	
	private static long index = SatDataUpdate.STARTNUMBER;		//标记记录卫星number

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
//		if(satelites == null)		//redis缓存已过期
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
//			jedis.expire("satelites", 5*60);	//5秒过期 
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
	 * 读取url串中的字符内容
	 * @param url 网址
	 * @return String 解析后的url串链接的数据
	 */
	public static String readUrl(String url)
	{
		StringBuilder sb = new StringBuilder();
		InputStream in;
		BufferedReader reader;
		try
		{
			 URL myurl = new URL(url); 
             //打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。 
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
	 * 从url内容中构建Tle对象列表,并将txt存入文件中
	 * @param url 网址
	 * @param type 网址对应关键字
	 * @return List<TleString>  解析后的双轨数据集合
	 */
	public static List<TleString> readFromWeb(String url,String type)
	{
		List<TleString> tles = new ArrayList<TleString>();
		String content = readUrl(url);//按照名称，第一行，第二行进行分割txt内的内容
		String category = url.substring(url.lastIndexOf("/")+1,url.lastIndexOf("."));//lastIndexOf() 方法可返回一个指定的字符串值最后出现的位置,
		
		//System.out.println(content);
		try
		{
			String filedir = "C:/testData/txtfiles/satelite";
			String filename = url.substring(url.lastIndexOf("/")+1);
			
			if(!new File(filedir).exists())
			{
				new File(filedir).mkdirs();
				/* mkdir()
				 * 只能在已经存在的目录中创建创建文件夹。
                 * mkdirs()
                 * 可以在不存在的目录中创建文件夹。诸如：a\\b,既可以创建多级目录。
                 */
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filedir+"/"+filename)));
			/*加了一个缓冲区,缓冲写满后在将数据写入硬盘
                            这样做极大的提高了性能
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
		
		String []strs = content.split("\r\n");//按回车换行分割数组
		if(strs.length%3 ==0)	//再次验证读取tle正确
		{
			for(int i=0;i<strs.length;i+=3)
			{
				tles.add(new TleString(index++,strs[i].trim(),category.trim(),type.trim(),strs[i+1].trim(),strs[i+2].trim()));
			}
		}
		return tles;
	}
	
	/**
	 * 以链接爬虫方式获取nasa卫星所有Tle数据，存入对象alltles中
	 * @param baseUrl 基础网址
	 */
	@SuppressWarnings("rawtypes")
	public static void getAllTles(String baseUrl)
	{
		alltles = new TleList();
		
		Map<String,String> maps = HttpUtils.extractLinks(baseUrl);
		Iterator<Entry<String,String>> iter = maps.entrySet().iterator();//将map类型数据转换成集合set类型，然后获得其迭代器，迭代器只针对集合类型的数据
		index = SatDataUpdate.STARTNUMBER;
		while (iter.hasNext())
		{
			Entry e = iter.next();
			String type = (String) e.getKey();
			String url = (String) e.getValue();
			//System.out.println(category + "→" + url);
			
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
