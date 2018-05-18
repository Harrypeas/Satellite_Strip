package com.xc.worldwind.manage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.xc.worldwind.function.InsituDataUpdate;
import com.xc.worldwind.object.InsituSensor;

import gov.nasa.worldwind.geom.Position;

public class InsituDataManage {
	public static ArrayList<InsituSensor> InsituSensors;
	public static ArrayList<InsituSensor> queryInsituSensors = new ArrayList<>(); 
	public static ArrayList<InsituSensor> checkedInsituSensors = new ArrayList<>();
	
	public static void initializeCollection() throws IOException
	{
		//File InsituFile = new File();
		if (InsituSensors == null) {
			try {
				ArrayList<InsituSensor> sensors = InsituDataUpdate.ReadInsituFromText();
				InsituSensors = new ArrayList<>();
				InsituSensors = sensors;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void updateCollection() throws Exception
	{
		InsituDataUpdate.UpdateInsituData();
	}
	
	public static ArrayList<InsituSensor> getInsituSensors()
	{
		return InsituSensors;
	}

	public static InsituSensor getInsituSensorByName(String Name)
	{
		InsituSensor insitu = null;
		for(InsituSensor sensor:InsituSensors)
		{
			if (Name.equals(sensor.getInsituName())) {
				insitu = sensor;
				return insitu;
			}
		}
		return insitu;
	}
	
	public static String getInsituSensorName(InsituSensor sensor)
	{
		String SensorName = "";
		if (sensor.getInsituName() == null) {
			SensorName = "No Name";
		}
		else SensorName = sensor.getInsituName();
		return SensorName;
	}
	
	public static String getInsituSensorPosition(InsituSensor sensor)
	{
		String Position = "";
		if (sensor.getInsituPosition() == null) {
			Position = "null";
		}
		else
		{
			Position ="Longitude: " + sensor.getInsituPosition().getLongitude().toString() + "\r\n" +
					"Latitude: " + sensor.getInsituPosition().getLatitude().toString() + "\r\n" +
					"Elevation: " + sensor.getInsituPosition().getElevation() + "\r\n" +
					"Altitude: " + sensor.getInsituPosition().getAltitude();
		}
		return Position;
	}
	
	public static String getInsituSensorRadius(InsituSensor sensor)
	{
		String radius = "";
		if (Double.isNaN(sensor.getInsituRadius())) {
			radius = "null";
		}
		else radius = "Radius: " + String.valueOf(sensor.getInsituRadius());
		return radius;
	}
	
	public static ArrayList<InsituSensor> getCheckedInsituSensors()
	{
		return checkedInsituSensors;
	}
	
//	public static ArrayList<InsituSensor> getQueryInsitus(List<Position> PolygonalBoundaryList,ArrayList<InsituSensor> insituSensors) throws Exception
//	{	
//		return 	SatSpatialQuery.doInsituQuery(PolygonalBoundaryList, insituSensors);	
//	}
	
}
