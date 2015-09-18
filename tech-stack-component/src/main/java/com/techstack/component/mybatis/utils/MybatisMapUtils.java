package com.techstack.component.mybatis.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class MybatisMapUtils {
	/**
	 * 将返回的MAP的键进行格式化，配合使用，IS_ADMIN->isAdmin
	 */
	public static Map<String,Object> mapKeyFormat(Map<String,Object> tempMap){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		for(String i :tempMap.keySet()){
			char[] ch = i.toCharArray();
			Object o = tempMap.get(i);
			if(Character.isUpperCase(ch[0])&&Character.isUpperCase(ch[1])){
				StringBuffer s = new StringBuffer();
				String[] k =i.split("_");
				s.append(k[0].toLowerCase());
				for(int b=1;b<k.length;b++){
					String c =StringUtils.capitalize(k[b].toLowerCase());
					s.append(c);
				}
				resultMap.put(s.toString(), o);
			}else{
				resultMap.put(i, o);
			}
		}
		return resultMap;
	}
}
