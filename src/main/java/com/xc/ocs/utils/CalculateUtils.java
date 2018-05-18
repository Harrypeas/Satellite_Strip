package com.xc.ocs.utils;

import java.util.List;

public class CalculateUtils {

	//判断某元素是否在数组中重复
	public static boolean WhetherRepeatNode(List<List<String>> list,String node,int tag)
	{
		for(int i = 0; i<list.size(); i++)
		{
			if(node.equals(list.get(i).get(tag)))
				break;
			else if(i == list.size()-1)
				return false;	
		}
		return true;
		
	}
	
	//扩充数组
    public static void expendSize (int Array[] , int incre )
    {
        int nowArray[] = new int[Array.length+incre];//新数组  
        System.arraycopy(Array, 0, nowArray, 0, Array.length);//将a数组内容复制新数组b  
        Array = nowArray;//改变引用  
        System.out.println("扩容后数组Array容量为为："+Array.length+"  数组Array内容：");   
    	
    }
 
}
