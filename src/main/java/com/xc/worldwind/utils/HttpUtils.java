package com.xc.worldwind.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author 钟翔
 * @class 网络爬虫底层工具类
 * @function 解析网址内容
 */
public class HttpUtils
{
	public static URL netUrl;
	public static HttpURLConnection con;
	public static String PREFIX="http://www.celestrak.com/NORAD/elements";
	public static String SUFFIX=".txt";
	
	//验证是否为tle文件
	public static boolean validateUrl(String url)
	{
		if(url.startsWith(PREFIX) && url.endsWith(SUFFIX))
		{
			return true;
		}
		return false;
	}
		
		
	/**
	 * 使用Jsoup库，抽取链接
	 * @param url 网址
	 * @return 以关键字和网址串组成的map对象
	 */
	public static Map<String,String> extractLinks(String url)
	{
		Map<String,String> urllinks = new HashMap<String,String>();
		try
		{
			System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(5000));// 超时连接
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(5000)); // 超时读取  
			netUrl = new URL(url);
			con = (HttpURLConnection)netUrl.openConnection();//返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。  
	
			int state = con.getResponseCode();
			if(state == 200)
			{
				Document doc = Jsoup.connect(url).timeout(0).get();	
				Elements links = doc.select("a[href]");//获取含有href的a
				for (Element link : links) 
				{
					String linkUrl = link.absUrl("href");//获取绝对路径，和link.attr("abs:href")一样
					boolean retval = validateUrl(linkUrl);
					if(retval)
					{				
						urllinks.put(link.text(), link.attr("abs:href"));//获取文本内容text(String value) 设置文本内容
					}
				}
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return urllinks;
	}

}
