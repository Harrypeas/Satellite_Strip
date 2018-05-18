package com.xc.worldwind.function;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.xc.worldwind.object.InsituSensor;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.Globe;

public class InsituDataUpdate {
	private final static String Path = "C:\\testData\\txtfiles\\JSJ-sml-sensor";

	public static ArrayList<InsituSensor> ReadInsituFromText() throws Exception
	{
		ArrayList<InsituSensor> insituSensors = new ArrayList<>();
		try {
			UpdateInsituData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileInputStream inputStream = new FileInputStream(new File("insitusensor.txt"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
		String line = null;
		int index = 0;
		String Position = null;
		double lat = 0;
		double lng = 0;
		Globe globe = new Earth();
		while((line = reader.readLine())!=null)
		{
			if (index == 0 && line.contains("Place")) {
				Position = line.split(" ")[1];
				index ++ ;
			}
			else if (index == 1 && line.contains("Latitude")) {
				lat = Double.valueOf(line.split(" ")[1]);
				index ++ ;
			}
			else if (index == 2 && line.contains("Longitude")) {
				lng = Double.valueOf(line.split(" ")[1]);
				LatLon latLon = new LatLon(Angle.fromDegrees(lat), Angle.fromDegrees(lng));
				double elevation = globe.getElevation(Angle.fromDegrees(lat), Angle.fromDegrees(lng));
				InsituSensor sensor = new InsituSensor(Position, new gov.nasa.worldwind.geom.Position(latLon, elevation), 3);
				insituSensors.add(sensor);
				lat = 0;
				lng = 0;
				Position = null;
				index = 0;
			}
		}
		reader.close();
		inputStream.close();
		return insituSensors;
	}
	
	public static void UpdateInsituData() throws Exception
	{
		File file = new File(Path);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			FileOutputStream ofStream = new FileOutputStream("InsituSensor.txt");
			OutputStreamWriter writer = new OutputStreamWriter(ofStream, "utf-8");
			for(int i = 0; i < filelist.length; i ++)
			{
				String newPath = Path+"\\" + filelist[i];
				boolean record = false;
				FileInputStream fStream = new FileInputStream(new File(newPath));
				BufferedReader reader = new BufferedReader(new InputStreamReader(fStream,"utf-8"));
				StringBuilder builder = new StringBuilder();
				String line=null;
				
				String reg = "<swe:value>\\d+\\.\\d+\\</swe:value>";
				String reg2 = "<sml:value>.+\\</sml:value>";
				Pattern pattern = Pattern.compile(reg);
				Pattern pattern2 = Pattern.compile(reg2);
				int count = 0;
				while((line = reader.readLine()) != null)
				{
					if (line.contains("卫星传感器")) {
						break;
					}
					
					if (line.contains("所属平台名称")) {
						record = true;
					}
					
					if (line.contains("纬度")) {
						record = true;
					}
					if (record == true) {
						Matcher matcher = pattern.matcher(line);
						Matcher matcher2 = pattern2.matcher(line);
						if (matcher2.find() != false) {
						//	System.out.println(matcher2.group());
							String[] temp = matcher2.group().split("\\>|\\<\\/");
							builder.append("Place: "+temp[1]+"\r\n");
							record = false;
						}
						if (matcher.find() != false) {
						//	System.out.println(matcher.group());
							String[] temp = matcher.group().split("\\>|\\<\\/");
							count++;
							if(count == 1)
							{
								builder.append("Latitude: "+temp[1]+"\r\n");
							}
							if(count == 2)
							{
								builder.append("Longitude: "+temp[1]+"\r\n");
								break;
							}
						}
					}
				}
				
				//System.out.println(builder.toString());
				writer.write(builder.toString());
				writer.flush();
				reader.close();
			}
			writer.close();
			ofStream.close();
		}
	}

}
