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
 * @author ����
 * @class ��������ײ㹤����
 * @function ������ַ����
 */
public class HttpUtils
{
	public static URL netUrl;
	public static HttpURLConnection con;
	public static String PREFIX="http://www.celestrak.com/NORAD/elements";
	public static String SUFFIX=".txt";
	
	//��֤�Ƿ�Ϊtle�ļ�
	public static boolean validateUrl(String url)
	{
		if(url.startsWith(PREFIX) && url.endsWith(SUFFIX))
		{
			return true;
		}
		return false;
	}
		
		
	/**
	 * ʹ��Jsoup�⣬��ȡ����
	 * @param url ��ַ
	 * @return �Թؼ��ֺ���ַ����ɵ�map����
	 */
	public static Map<String,String> extractLinks(String url)
	{
		Map<String,String> urllinks = new HashMap<String,String>();
		try
		{
			System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(5000));// ��ʱ����
            System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(5000)); // ��ʱ��ȡ  
			netUrl = new URL(url);
			con = (HttpURLConnection)netUrl.openConnection();//����һ�� URLConnection ��������ʾ�� URL �����õ�Զ�̶�������ӡ�  
	
			int state = con.getResponseCode();
			if(state == 200)
			{
				Document doc = Jsoup.connect(url).timeout(0).get();	
				Elements links = doc.select("a[href]");//��ȡ����href��a
				for (Element link : links) 
				{
					String linkUrl = link.absUrl("href");//��ȡ����·������link.attr("abs:href")һ��
					boolean retval = validateUrl(linkUrl);
					if(retval)
					{				
						urllinks.put(link.text(), link.attr("abs:href"));//��ȡ�ı�����text(String value) �����ı�����
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
