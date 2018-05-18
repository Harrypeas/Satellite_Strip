
package com.xc.worldwind.object;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 钟翔
 * @class 卫星类别类
 * @function 对卫星双轨数据进行分类
 */
public class Category 
{
	private String name;
	private List<TleString> childTles;
	
	public Category()
	{
		this.name = "";
		this.childTles = new ArrayList<TleString>();
	}
	
	public Category(String name,List<TleString> childTle)
	{
		this.name = name;
		this.childTles = childTle;
	}
	
	//====================Get与Set方法===================
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
	
	public void setChildTles(List<TleString> childTles)
	{
		this.childTles = childTles;
	}
	public List<TleString> getChildTles()
	{
		return this.childTles;
	}
	
	public void addChildTle(TleString tle)
	{
		childTles.add(tle);
	}
	
	public void deleteChildTle(TleString tle)
	{
		if(childTles.contains(tle))
		{
			childTles.remove(tle);
		}
	}
	
	public boolean contains(TleString tle)
	{
		for(TleString str:childTles)
		{
			if(str.getName().equals(tle.getName()))
				return true;
		}
		return false;
	}
	
	public void output()
	{
		System.out.println(name);
		for(TleString str:childTles)
		{
			System.out.println(str);
		}
	}
}