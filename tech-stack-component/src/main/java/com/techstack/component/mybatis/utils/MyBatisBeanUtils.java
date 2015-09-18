package com.techstack.component.mybatis.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

public class MyBatisBeanUtils extends BeanUtils {
	
	/** 
     * 将一个 Map 对象转化为一个 JavaBean 
     * @param type 要转化的类型 
     * @param map 包含属性值的 map 
	 * @return 
     * @return 转化出来的 JavaBean 对象 
     * @throws IntrospectionException 如果分析类属性失败 
     * @throws IllegalAccessException 如果实例化 JavaBean 失败 
     * @throws InstantiationException 如果实例化 JavaBean 失败 
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败 
     */  
    @SuppressWarnings("rawtypes")  
    public static <Model> Model convertMap(Class<Model> type, Map map)  
            throws IntrospectionException, IllegalAccessException,  
            InstantiationException, InvocationTargetException {  
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性  
        Model obj = type.newInstance(); // 创建 JavaBean 对象  
  
        // 给 JavaBean 对象的属性赋值  
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
  
            if (map.containsKey(propertyName)) {  
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。  
                Object value = map.get(propertyName);  
  
                Object[] args = new Object[1];  
                args[0] = value;  
  
                descriptor.getWriteMethod().invoke(obj, args);  
            }  
        }  
        return obj;  
    }
    
    /**
     * @Description: 将一个 Map 对象转化为一个 JavaBean 
     * @param @param obj
     * @param @param map
     * @param @return
     * @param @throws IntrospectionException
     * @param @throws IllegalAccessException
     * @param @throws InstantiationException
     * @param @throws InvocationTargetException    
     * @return Model
     */
    @SuppressWarnings("rawtypes")  
    public static <Model> Model convertMap(Model obj, Map map)  
            throws IntrospectionException, IllegalAccessException,  
            InstantiationException, InvocationTargetException {  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass()); // 获取类属性  
        //Model obj = type.newInstance(); // 创建 JavaBean 对象  
  
        // 给 JavaBean 对象的属性赋值  
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();  
        for (int i = 0; i< propertyDescriptors.length; i++) {  
            PropertyDescriptor descriptor = propertyDescriptors[i];  
            String propertyName = descriptor.getName();  
  
            if (map.containsKey(propertyName)) {  
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。  
                Object value = map.get(propertyName);  
  
                Object[] args = new Object[1];  
                args[0] = value;  
  
                descriptor.getWriteMethod().invoke(obj, args);  
            }  
        }  
        return obj;  
    }  
	
	
    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String,Object> convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
    
    /**
     * @Description: 将一个简单的JavaBean写进文件，文件名为className+.bean
     * @param @param path 存放目录，结尾必须已"//"结尾
     * @param @param bean   
     * @return void
     */
    public static void writeBean(String path,Object bean){
    	try{
    		File file = new File(path);
    		file.mkdirs();
    		String filePath = path+bean.getClass().getSimpleName()+".bean";
    		file = new File(filePath);
    		file.createNewFile();
			FileOutputStream outStream = new FileOutputStream(filePath);  
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);  
			objectOutputStream.writeObject(bean);  
			outStream.close();  
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    @SuppressWarnings({ "resource", "unchecked" })
	public static <Model> Model fileToBean(Class<Model> type, String filePath){
		try {
			FileInputStream freader = new FileInputStream(filePath);
			ObjectInputStream objectInputStream = new ObjectInputStream(freader);  
	        return (Model) objectInputStream.readObject();  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;
    }
    
    @SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//File file = new File("E://111//");
		//file.mkdirs();
    	Map<String,Object> tempMap = new HashMap<String, Object>();
    	tempMap.put("name", "小明");
    	writeBean("E://111//",tempMap);
    	
    	tempMap = fileToBean(HashMap.class,"E://111//HashMap.bean");
    	System.out.println(tempMap.get("name"));
	}

}
