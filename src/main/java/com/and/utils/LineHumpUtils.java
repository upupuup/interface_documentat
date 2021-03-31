package com.and.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineHumpUtils {
	/**
	 * 下划线命名转为驼峰命名
	 */
	public static String underlineToHump(String para){
		StringBuilder result = new StringBuilder();
		String a[] = para.split("_");
		for(String s : a){
			if(result.length() == 0){
				result.append(s.toLowerCase());
			}else{
				result.append(s.substring(0, 1).toUpperCase());
				result.append(s.substring(1).toLowerCase());
			}
		}
		return result.toString();
	}
	
	
	/**
	 * 驼峰命名转为下划线命名
	 */
	public static String humpToUnderline(String para){
        StringBuilder sb = new StringBuilder(para);
		//定位
        int temp = 0;
        if (!para.contains("_")) {
        	for(int i = 0 ; i < para.length() ; i ++){ 
        		if(Character.isUpperCase(para.charAt(i))){
        			if(i == 0){
        				continue;
        			}
        			sb.insert(i + temp, "_");
        			temp += 1;
        		}
        	}
        }
        return sb.toString().toLowerCase();
	}

	public static List<Map> humpList(List<Map> ll){
		for(Map<String,Object> oldMap : ll){
			Map<String,Object> newMap = new HashMap<String,Object>();
			for(String key : oldMap.keySet()){
				newMap.put(LineHumpUtils.underlineToHump(key), oldMap.get(key));
			}
			Collections.replaceAll(ll, oldMap, newMap);
		}
		return ll;
	}
	
	public  static <T> List<Map<String, T>> humpListT(List<Map<String,T>> ll){
		for(Map<String,T> oldMap : ll){
			Map<String,T> newMap = new HashMap<String,T>();
			for(String key : oldMap.keySet()){
				newMap.put(LineHumpUtils.underlineToHump(key), oldMap.get(key));
			}
			Collections.replaceAll(ll, oldMap, newMap);
		}
		return ll;
	}
}



