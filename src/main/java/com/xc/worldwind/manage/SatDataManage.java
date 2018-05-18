package com.xc.worldwind.manage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JOptionPane;

import com.xc.worldwind.function.SatDataUpdate;
import com.xc.worldwind.object.Satelite;
import com.xc.worldwind.utils.StringUtils;

import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.util.measure.LengthMeasurer;

/**
 * @author 钟翔
 * @class 卫星数据更新类
 * @function 提供从文件中读取和更新卫星数据等方法
 * 
 */
public class SatDataManage
{
	
	private static ArrayList<Satelite> satelites;
	public static List<Satelite> querysateList = new ArrayList<Satelite>();//存放查询出来的卫星	
	public static ArrayList<Satelite> checksateList = new ArrayList<Satelite>();//存放勾选出来的卫星	
	private static Satelite satA;
	private static Satelite satB;
	
	public static void setSatA(Satelite satA)
	{
		SatDataManage.satA = satA;
	}
	
	/**
	 * 根据number查询卫星对象，number为空则清理SatA对象
	 */
	public static void findandSetSatA(String number)
	{
		if(number == null || number.trim().equals(""))
		{
			SatDataManage.satA = null;
			return;
		}
		for(Satelite sat:satelites)
		{
			if(sat.getNumber().toString().equals(number))
			{
				satA = sat;
				return;
			}
		}
		SatDataManage.satA = null;
	}
	
	public static void findandSetSatA1(String name)
	{
		if(name == null || name.trim().equals(""))
		{
			SatDataManage.satA = null;
			return;
		}
		for(Satelite sat:satelites)
		{
			if((sat.getPopular_name() + "  " + sat.getCatelog_name()).equals(name))
			{
				satA = sat;
				return;
			}
		}
		SatDataManage.satA = null;
	}
	
	public static Satelite getSatA()
	{
		return SatDataManage.satA;
	}
	
	public static void setSatB(Satelite satB)
	{
		SatDataManage.satB = satB;
	}
	
	/**
	 * 根据number查询卫星对象，number为空则清理SatB对象
	 */
	public static void findandSetSatB(String number)
	{
		if(number == null || number.trim().equals(""))
		{
			SatDataManage.satB = null;
			return;
		}
		for(Satelite sat:satelites)
		{
			if(sat.getNumber().toString().equals(number))
			{
				SatDataManage.satB = sat;
				return;
			}
		}
		SatDataManage.satB = null;
	}
	
	public static void findandSetSatB1(String name)
	{
		if(name == null || name.trim().equals(""))
		{
			SatDataManage.satB = null;
			return;
		}
		for(Satelite sat:satelites)
		{
			if((sat.getPopular_name() + "  " + sat.getCatelog_name()).equals(name))
			{
				satB = sat;
				return;
			}
		}
		SatDataManage.satB = null;
	}
	
	public static Satelite getSatB()
	{
		return SatDataManage.satB;
	}
	
	/**
	 * 更新文件中的卫星数据
	 */
	public static void updateCollection()
	{
		SatDataUpdate.deleteDir(new File(SatDataUpdate.SATELITE_FILE_PATH));
		ArrayList<Satelite> lst = SatDataUpdate.getSatelitesFromTestFile();
		satelites = lst;
		JOptionPane.showMessageDialog(null,"更新完成"); 
//		VariableManage.refreshButton_timer.cancel();
	}
	
	/**
	 * 初始化卫星文件中的数据
	 */
	public static void initilizeCollection()
	{
		if(satelites == null)
			satelites = new ArrayList<Satelite>();

		ArrayList<Satelite> lst = SatDataUpdate.getSatelitesFromTestFile();;
		satelites = lst;
		//VariableManage.refreshButton_timer.cancel();
	}
	
	/**
	 * 更新所有卫星的当前位置
	 */
	public static void updateSatelitePositions()
	{
		try 
		{
			if(satelites != null && satelites.size()>0)
			{
				for(int i=0;i<satelites.size();i++)
				{
					Position currentPos = satelites.get(i).getCurrentPosition();
					satelites.get(i).setPosition(currentPos);
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	public static ArrayList<Satelite> getSatelites()
	{
		return satelites;
	}

	/**
	 * 读取所有的卫星类别
	 * @return List<String> 卫星类别数据集合
	 */
	public static ArrayList<String> getSateliteCategory()
	{
		ArrayList<String> lst = new ArrayList<String>();
		if(satelites != null)
		{
			for(Satelite sat:satelites)
			{
				if(!lst.contains(sat.getCatelog_name()))
				{
					lst.add(sat.getCatelog_name());
				}
			}
		}
		return lst;
	}
	
	/**
	 * 读取所有的卫星类型
	 * @return List<Satelite> 卫星类型数据集合
	 */
	public static ArrayList<String> getSateliteTypes()
	{
		ArrayList<String> lst = new ArrayList<String>();
		if(satelites != null)
		{
			for(Satelite sat:satelites)
			{
				if(!lst.contains(sat.getType()))
				{
					lst.add(sat.getType());
				}
			}
		}
		return lst;
	}
	
	public static ArrayList<String> getSateliteNumbers()
	{
		ArrayList<String> lst = new ArrayList<String>();
		if(satelites != null)
		{
			for(Satelite sat:satelites)
			{
				if(!lst.contains(sat.getNumber().toString().substring(0, 2)+"xxx"))
				{
					lst.add(sat.getNumber().toString().substring(0, 2)+"xxx");
				}
			}
		}
		for(int i = 0; i<querysateList.size();i++)
		{
			querysateList.remove(0);
		}
		for(int i = 0; i<checksateList.size();i++)
		{
			checksateList.remove(0);
		}
		findandSetSatA("");
		findandSetSatB("");
		return lst;
	}

	/**
	 * 根据名称读取卫星对象
	 * @return Satelite 卫星类型数据
	 */
	public static Satelite getSateliteByName(String satName)
	{
		Satelite sat = null;
		String name = null;
		for(Satelite tempSat:SatDataManage.getSatelites())
		{
			name = tempSat.getPopular_name() + "  " + tempSat.getCatelog_name();
			if(name.equals(satName)){
				sat = tempSat; 
				break;
			}
		}
		return sat;
	}
	
	public static Satelite getSateliteByNumber(String number)
	{
		Satelite sat = null;
		for(Satelite tempSat:SatDataManage.getSatelites())
		{
			if(tempSat.getNumber().toString().equals(number)){
				sat = tempSat; 
				break;
			}
		}
		return sat;
	}
	
	public static ArrayList<Satelite> getSateliteBySecondTag(String tag)
	{
		ArrayList<Satelite> satList = new ArrayList<Satelite>();
		Satelite sat = null;
		for(Satelite tempSat:SatDataManage.getSatelites())
		{
			if(tempSat.getNumber().equals(tag)||tempSat.getType().equals(tag)||tempSat.getCatelog_name().equals(tag)){
				sat = tempSat;
				satList.add(sat);
			}
		}
		return satList;
	}
	
	public static String getSateliteName(Satelite sat)
	{
		String satName = "";
		if(sat == null)
			satName = "-";
		else
			satName = sat.getPopular_name();
		return satName;
	}
	
	public static String getSateliteInfo(Satelite sat)
	{
		String satInfo = "";
		if(sat == null)
			satInfo = "-";
		else
		{
			satInfo += sat.getNumber()+" ";
			satInfo += sat.getCatelog_name()+" ";
			satInfo += sat.getPopular_name();
		}
		return satInfo;
	}
	
	public static String getSatelitePosition(Satelite sat)
	{
		String satPos = "";
		if(sat == null)
			satPos = "-";
		else
		{
			satPos = StringUtils.parseFromSatelitePosition(sat.getPosition());
		}
		return satPos;
	}
	
//	public static double getDistance(Position pos1,Position pos2)
//	{
//		double dist = 0.0;
//		ArrayList<Position> poss = new ArrayList<Position>();
//		
//		poss.add(pos1);
//		poss.add(pos2);
//		LengthMeasurer lenMeasure = new LengthMeasurer();
//		lenMeasure.setPositions(poss);
//		
//		dist = lenMeasure.getLength(VariableManage.worldWindowGLCanvas.getModel().getGlobe());
//		return dist;
//	}
	
//	public static String getDistanceOfAB()
//	{
//		if(satA == null || satB == null)
//			return "-";
//		else
//		{
//			double distance = getDistance(satA.getPosition(),satB.getPosition());
//			return String.format("%.3f", distance/1000)+"km";
//		}
//	}
//
//	public static List<Satelite> getQuerySatelites(List<Position> PolygonalBoundaryList,List<Satelite> sateLiteList) throws Exception
//	{	
//		return 	SatSpatialQuery.doSatQuery(PolygonalBoundaryList, sateLiteList);
//		
//	}
}
