package com.xc.ocs.utils;

import java.util.List;

public class CalculateUtils {

	//�ж�ĳԪ���Ƿ����������ظ�
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
	
	//��������
    public static void expendSize (int Array[] , int incre )
    {
        int nowArray[] = new int[Array.length+incre];//������  
        System.arraycopy(Array, 0, nowArray, 0, Array.length);//��a�������ݸ���������b  
        Array = nowArray;//�ı�����  
        System.out.println("���ݺ�����Array����ΪΪ��"+Array.length+"  ����Array���ݣ�");   
    	
    }
 
}
