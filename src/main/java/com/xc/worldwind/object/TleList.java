package com.xc.worldwind.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 钟翔
 * @class 卫星双轨数据集合实体类
 * @function 保存txt文件中的卫星双轨等数据集合
 */
public class TleList
{
	private List<TleString> tles;
	
	public TleList()
	{
		tles = new ArrayList<TleString>();
	}
	
	public List<TleString> getTles()
	{
		return tles;
	}
	public void setTles(List<TleString> tles)
	{
		this.tles = tles;
	}
	
	public int getSize()
	{
		return tles.size();
	}
	
	public TleString get(int i)
	{
		if(tles != null && tles.size()>0)
			return tles.get(i);
		return null;
	}
	
	public void addTleString(TleString tle)
	{
		if(tles != null)
		{
			tles.add(tle);
		}
	}
	
	public void addAllTleStrings(List<TleString> tlelst)
	{
		if(tles != null)
		{
			tles.addAll(tlelst);
		}
	}
	
	public boolean contains(TleString tle)
	{
		for(TleString tleString:tles)
		{
			if(tleString.getName().equals(tle.getName()) && tleString.getCategory().equals(tle.getCategory()))
			{
				return true;
			}
		}
		return false;
	}

	public List<String> getCategoryNames()
	{
		List<String> lst = new ArrayList<String>();
		if(tles != null && tles.size()>0)
		 {
			for(TleString tle:tles)
			{
				if(!lst.contains(tle.getCategory()))
				{
					lst.add(tle.getCategory());
				}
			}
		 }
		return lst;
	}
	
	
	public List<TleString> getByCategory(String category)
	{
		List<TleString> lst = new ArrayList<TleString>();
		if(tles != null && tles.size()>0)
		 {
			for(TleString tle:tles)
			{
				if(tle.getCategory().equals(category))
				{
					lst.add(tle);
				}
			}
		 }
		return lst;
	}
	
	//按类别组织所有Tle数据
	public List<Category> organizeByCategory()
	{
		 List<Category> categories = new  ArrayList<Category>();
		 List<String> categoryNames =  getCategoryNames();
		 for(String category:categoryNames)
		 {
			 Category cat = new Category();
			 cat.setName(category);
			 cat.setChildTles(getByCategory(category));
			 categories.add(cat);
		 }
		 return categories;
	}
}
