package com.techstack.component.mapper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;

import com.google.common.collect.Lists;

/**
 * * 简单封装Dozer, 实现深度转换Bean<->Bean的Mapper.实现:
 * 
 * 1. 持有Mapper的单例. 2. 返回值类型转换. 3. 批量转换Collection中的所有对象. 4.
 * 区分创建新的B对象与将对象A值复制到已存在的B对象两种函数.
 * 
 */
public class BeanMapper {

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();

	/**
	 * 基于Dozer转换对象的类型.
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		if(source != null){
			return dozer.map(source, destinationClass);
		}else {
			return null;
		}
	}
	
	public static <T> T map(Object source, Class<T> destinationClass, String... ignoreProperties) {
		if(source != null){
			Map<String, Object> resultMap = new HashMap<String, Object>();  
			Field[] fields = source.getClass().getDeclaredFields();
			for (Field field : fields) {  
				Object value = null;  
		        try {  
		            value = field.get(source);  
		        } catch (Exception e) {  
		            e.printStackTrace();  
		        }  
		        boolean flag = true;  
		        String key = field.getName();  
		        for (String ignoreProperty:ignoreProperties) {  
		            if (key.equals(ignoreProperty)) {  
		                flag = false;  
		                break;  
		            }  
		        }  
		        if (flag) {  
		            resultMap.put(key, value);  
		        }  
		    }  
	        return map(resultMap, destinationClass);  
	    } else {
			return null;
		}
	}

	/**
	 * 基于Dozer转换Collection中对象的类型.
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList,
			Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			if(sourceObject!=null){
				T destinationObject = dozer.map(sourceObject, destinationClass);
				destinationList.add(destinationObject);
			}
		}
		return destinationList;
	}

	/**
	 * 基于Dozer将对象A的值拷贝到对象B中.
	 */
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}
}
