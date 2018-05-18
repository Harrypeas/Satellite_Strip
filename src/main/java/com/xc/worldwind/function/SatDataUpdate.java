package com.xc.worldwind.function;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.xc.worldwind.SGP.Tle;
import com.xc.worldwind.object.Satelite;
import com.xc.worldwind.object.TleList;
import com.xc.worldwind.object.TleString;
import com.xc.worldwind.utils.WebTleUtils;

import gov.nasa.worldwind.geom.Position;

public class SatDataUpdate {
	//ԭλ�ļ����λ��
	public final static String INSUITSENSOR_FILE_PATH = "C:\\testData\\txtfiles\\insuitsensorfile.txt";
	//�����ļ�����λ��
	public final static String SATELITE_FILE_PATH = "C:\\testData\\txtfiles\\satelite";

	public final static long STARTNUMBER = 10000;
	
	private static long index = STARTNUMBER;		//��Ǽ�¼����number
	
	/**
	 * ���ļ��ж�ȡ��������
	 * @return List<Satelite> �������ݼ���
	 */
	public static ArrayList<Satelite> getSatelitesFromTestFile()
	{
		ArrayList<Satelite> satelst = new ArrayList<Satelite>();
		try 
		{
			List<TleString> tles = new ArrayList<TleString>();
			File file = new File(SATELITE_FILE_PATH);
			if (file.exists()) 
			{
				index = STARTNUMBER;
				File[] files = file.listFiles();
				for (File file2 : files) 
				{
					if(file2.isFile() && file2.exists()) //�ж��ļ��Ƿ����
					{ 
						StringBuffer sb = new StringBuffer();
	                    InputStreamReader read = new InputStreamReader(new FileInputStream(file2),"utf-8");//���ǵ������ʽ
	                    BufferedReader bufferedReader = new BufferedReader(read);
	                    String lineTxt = null;
	                    while((lineTxt = bufferedReader.readLine()) != null)
	                    {
	                        sb.append(lineTxt+"\r\n");
	                    }
	                    read.close();
	                    
	                    String content = sb.toString();
	                    if(content != null && !content.equals(""))
						{
							String []strs = content.split("\r\n");
							String type = strs[0];
							String category = strs[1];
							if(strs.length%3 ==2)	//�ٴ���֤��ȡtle��ȷ
							{
								for(int i=2;i<strs.length;i+=3)
								{
									tles.add(new TleString(++index,strs[i].trim(),category.trim(),type.trim(),strs[i+1].trim(),strs[i+2].trim()));
								}
							}
						}
						
					}
				}
				Satelite satelite = null;
				
				for(int i=0;i<tles.size();i++)
				{
					satelite = new Satelite();					
					TleString tle = tles.get(i);
					satelite.setTle(new Tle(tle.getName(),tle.getTle1(),tle.getTle2()));
					satelite.setNumber(tle.getNumber());
					satelite.setPopular_name(tle.getName());
					satelite.setCatelog_name(tle.getCategory());
					satelite.setType(tle.getType());					
					Position currentPos = satelite.getCurrentPosition();
					if(currentPos == null)
					{
						System.out.println("error:"+satelite.getNumber()+" "+satelite.getPopular_name()+"λ���޷�����");
						continue;
					}
						
					satelite.setPosition(currentPos);
					satelite.getBalloon();
					satelite.getBalloon();
					satelst.add(satelite);
				}
			}
			else	//�ļ������ڣ���������
			{
				WebTleUtils.getAllTles(WebTleUtils.baseUrl);
				TleList lst = WebTleUtils.getAlltles();
				Satelite satelite = null;
				
				for(int i=0;i<lst.getSize();i++)
				{
					satelite = new Satelite();
					TleString tle = lst.get(i);
					satelite.setNumber(tle.getNumber());
					satelite.setPopular_name(tle.getName());
					satelite.setCatelog_name(tle.getCategory());
					satelite.setType(tle.getType());
					satelite.setTle(new Tle(tle.getName(),tle.getTle1(),tle.getTle2()));
					Position currentPos = satelite.getCurrentPosition();
					if(currentPos == null)
					{
						System.out.println("error:"+satelite.getNumber()+" "+satelite.getPopular_name()+"λ���޷�����");
						continue;
					}
					satelite.setPosition(currentPos);
					satelite.getBalloon();
					satelite.getBalloon();
					satelst.add(satelite);
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return satelst;
	}

	/**
	 * ɾ�������ļ�
	 */
	public static boolean deleteDir(File dir)
	{
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success)
                {
                    return false;
                }
            }
        }
        // Ŀ¼��ʱΪ�գ�����ɾ��
        return dir.delete();
    }
	
}
