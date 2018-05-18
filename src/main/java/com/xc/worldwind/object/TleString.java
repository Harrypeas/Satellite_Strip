package com.xc.worldwind.object;

import java.io.Serializable;

/**
 * @author 钟翔
 * @class 卫星双轨数据实体类
 * @function 保存txt文件中的卫星双轨等数据
 */
public class TleString implements Serializable
{
	private static final long serialVersionUID = 6468963226568732433L;
	
	private long number;
	private String name;
	private String category;
	private String type;
	private String tle1;
	private String tle2;
	
	public TleString()
	{
		number = 0;
		this.name = "";
		category = "";
		type="";
		this.tle1 = "";
		this.tle2 = "";
	}
	
	public TleString(long number,String name,String category,String type,String tle1,String tle2)
	{
		this.number = number;
		this.name = name;
		this.category = category;
		this.type = type;
		this.tle1 = tle1;
		this.tle2 = tle2;
	}
	//====================Get与Set方法===================
	public void setNumber(long number)
	{
		this.number = number;
	}
	public long getNumber()
	{
		return this.number;
	}
//	
	public void setName(String name)
	{
		this.name = name;
	}
	public String getName()
	{
		return this.name;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getCategory()
	{
		return this.category;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	public String getType()
	{
		return this.type;
	}
	
	public void setTle1(String tle1)
	{
		this.tle1 = tle1;
	}
	public String getTle1()
	{
		return this.tle1;
	}
	
	public void setTle2(String tle2)
	{
		this.tle2 = tle2;
	}
	public String getTle2()
	{
		return this.tle2;
	}
	
	public void output()
	{
		//System.out.println("category="+category);
		System.out.println("name="+name);
		System.out.println("tle1="+tle1);
		System.out.println("tle2="+tle2);
	}
}
