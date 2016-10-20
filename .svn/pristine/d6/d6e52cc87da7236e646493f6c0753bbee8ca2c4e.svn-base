package com.hisign.datatrans.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {
	
	/**
	 * 拼接sql使用
	 * @param list
	 * @return ('0001','0002','0003','0004')
	 */
	public static String list2sqlIn(List list){
		String rtn = "(";
		for(int i=0; i<list.size(); i++){
			rtn +="'" + list.get(i) + "',";
		}
		rtn = rtn.substring(0, rtn.length()-1);
		rtn+=")";
		return rtn;
	}
	public static String list2sqlIn2(List list){
		String rtn = "(";
		for(int i=0; i<list.size(); i++){
			rtn +=list.get(i) + ",";
		}
		rtn = rtn.substring(0, rtn.length()-1);
		rtn+=")";
		return rtn;
	}
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		
		List list = new ArrayList();
		list.add(1);
		list.add(1);
		list.add(1);
		list.add(1);
		System.out.println(list2sqlIn2(list));
//		for(int i=0; i<12; i++) {
//		String str = addZeroBeforNum(i+1);
//System.out.println("----str-----"+str);		
//		}
	}
	
	/**
	 * 判断字符串是否是纯数字
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length();--i>=0;) {   
			if (!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 将1-9变为01至09
	 */
	public static String addZeroBeforNum(int num) {
		if(num > 0 && num < 10 || num == 0) {
			String str = "";
			str = Integer.toString(num);
			str = "0" + str; 
			return str;
		}else {
			return Integer.toString(num);
		}
	}
	
	/**
	 * 返回两个字符串中间的内容
	 * @param all
	 * @param start
	 * @param end
	 * @return
	 */
	public static String getMiddleString(String all,String start,String end){
		int beginIdx = all.indexOf(start) + start.length();
	    int endIdx = all.indexOf(end);
	    return all.substring(beginIdx, endIdx);
	}
	
}
