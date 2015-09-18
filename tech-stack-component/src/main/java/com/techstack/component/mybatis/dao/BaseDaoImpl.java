package com.techstack.component.mybatis.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.techstack.component.mapper.BeanMapper;
import com.techstack.component.mybatis.annotation.Column;
import com.techstack.component.mybatis.annotation.Table;
import com.techstack.component.mybatis.page.PageBean;
import com.techstack.component.mybatis.page.PageParam;
import com.techstack.component.mybatis.utils.MybatisMapUtils;
import com.techstack.component.reflection.Reflections;

//@Repository("baseDao")
public class BaseDaoImpl extends SqlSessionDaoSupport implements BaseDao{
	
	private static final Logger log = LoggerFactory.getLogger(BaseDaoImpl.class);
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <Model>Model saveOrUpdate(Model model) {
		try {
			//System.out.println(model.getClass().getAnnotation(Table.class).name());
			if(model.getClass().isAnnotationPresent(Table.class)){
				Table table = model.getClass().getAnnotation(Table.class);
				String tableName = table.name();
				Field[] fields = model.getClass().getDeclaredFields();
				List<Map> columns = new ArrayList<Map>();
				StringBuffer sql = new StringBuffer();
				StringBuffer columnsName = new StringBuffer();
				StringBuffer value = new StringBuffer();
				
				
				for(Field field : fields){
			            /** 
			             * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的 
			             * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX） 
			             * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范 
			             */  
			           // Method m;
						
						if(field.isAnnotationPresent(Column.class)){
							Column column = field.getAnnotation(Column.class);
							//System.out.println(column.name());
							String columnName = column.name();
							//m = (Method) model.getClass().getMethod("get" + getMethodName(field.getName()));
				            //Object val =  m.invoke(model);// 调用getter方法获取属性值  
							Object val =  Reflections.getFieldValue(model, field.getName());
				            if (val != null) {  
				            	Map columnMap = new HashMap();
				            	columnMap.put("columnName", columnName);
				            	if(field.getType().toString().equals("class java.util.Date")){
				            		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				            		val = format.format(val);
				            	}
				            	columnMap.put("val", val);
				            	columns.add(columnMap);
				                //System.out.println("String type:" + val);  
				            }  
						}
	//		        }
				}
				
				//Long id = (Long) model.getClass().getMethod("get" + getMethodName("id")).invoke(model);
				Long id = (Long) Reflections.getFieldValue(model, "id");
				if(id!=null){
					for(int i =0;i<columns.size();i++){
						value.append(columns.get(i).get("columnName")+"='"+columns.get(i).get("val")+"',");
					}
					//int version =  (Integer) model.getClass().getMethod("get" + getMethodName("version")).invoke(model);
					int version =  (Integer) Reflections.getFieldValue(model, "version");
					value.append("VERSION="+(version+1));
					sql.append("UPDATE "+tableName+" SET "+value+" WHERE ID="+"'"+id+"'");
					log.info("==== info ==== UPDATE SQL:"+sql);
				}else{
					for(int i =0;i<columns.size();i++){
						columnsName.append(columns.get(i).get("columnName")+",");
						value.append("'"+columns.get(i).get("val")+"',");
					}
					columnsName.append("CREATE_TIME,VERSION");
					//Date createTime = (Date) model.getClass().getMethod("get" + getMethodName("createTime")).invoke(model);
					Date createTime = (Date) Reflections.getFieldValue(model, "createTime");
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					value.append("'"+format.format(createTime)+"',"+'0');
					sql.append("INSERT INTO "+tableName+"("+columnsName+") VALUES "+" ("+ value+")");
					log.info("==== info ==== INSERT SQL:"+sql);
				}
				
				Connection connection = sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
				//Connection connection = getSqlSession().getConnection();	TODO: 拿到的是关闭的connection?
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql.toString(),Statement.RETURN_GENERATED_KEYS);
				
				if(id==null){	//如果是INSERT
					ResultSet rs = statement.getGeneratedKeys();	//返回主键
					rs.next();
					Long insertId = rs.getLong(1);
					//Method idSetter = (Method) model.getClass().getMethod("setId", Long.class);
					//idSetter.invoke(model, insertId);
					Reflections.setFieldValue(model, "id", insertId);
					rs.close();
				}
				
				statement.close();
				//connection.close(); TODO : 一关闭的话事务没法交给spring管理，会回滚不了
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <Model>Model getById(Class<Model> modelClass,Long id) {
		try {
			Model model = modelClass.newInstance();
			if(model.getClass().isAnnotationPresent(Table.class)){
				String tableName = model.getClass().getAnnotation(Table.class).name();
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM "+tableName+" WHERE ID='"+id+"'");
				Connection connection = sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
				//Connection connection = getSqlSession().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql.toString());
				ResultSetMetaData metaData =  resultSet.getMetaData();
				Map resultMap = new HashMap();
				int count = 0;
				while(resultSet.next()){
					for(int i=1;i<=metaData.getColumnCount();i++){
						resultMap.put(metaData.getColumnName(i), resultSet.getObject(i));
					}
					count++;
				}
				if(count == 0) {	//若没有记录返回null
					return null;
				}
				resultMap = MybatisMapUtils.mapKeyFormat(resultMap);
				resultSet.close();
//				connection.close(); 
				log.info("==== info ==== getById SQL:"+sql);
				//return MyBatisBeanUtils.convertMap(modelClass, resultMap);
				return BeanMapper.map(resultMap, modelClass);
			}
			
		}	
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}  
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <Model> List<Model> getByModel(Model model) {
		List<Model> modelList = new ArrayList<Model>();
		try{
			if(model.getClass().isAnnotationPresent(Table.class)){
				String tableName = model.getClass().getAnnotation(Table.class).name();
				StringBuffer sql = new StringBuffer();
				sql.append("SELECT * FROM "+tableName+" WHERE 1=1 ");
				/*Method[] methods = model.getClass().getMethods(); //TODO:先去掉ID，VERSION,CREAT_TIME的条件查询
				for(Method method:methods){
					if(method.isAnnotationPresent(Column.class)){
						Column column = method.getAnnotation(Column.class);
						String columnName = column.name();
						Object val =  method.invoke(model);// 调用getter方法获取属性值  
						
						if(method.getReturnType().toString().equals("class java.util.Date")){
		            		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		            		val = format.format(val);
		            	}
						
						if (val != null) {  
			            	sql.append(" AND "+columnName+"='"+val+"'");
			            }  
					}
				}*/
				
				Field[] fields = model.getClass().getDeclaredFields();
				for(Field field:fields){
					Method m;
					if(field.isAnnotationPresent(Column.class)){
						Column column = field.getAnnotation(Column.class);
						String columnName = column.name();
						m = (Method) model.getClass().getMethod("get" + getMethodName(field.getName()));
			            Object val =  m.invoke(model);// 调用getter方法获取属性值  
			            if (val != null) {  
			            	sql.append(" AND "+columnName+"='"+val+"'");
			            }  
					}
				}
				
				Connection connection = sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
				//Connection connection = getSqlSession().getConnection();
				Statement statement = connection.createStatement();
				log.info("==== info ===  getByModel SQL："+sql.toString());
				ResultSet resultSet = statement.executeQuery(sql.toString());
				ResultSetMetaData metaData =  resultSet.getMetaData();
				while(resultSet.next()){
					Map resultMap = new HashMap();
					for(int i=1;i<=metaData.getColumnCount();i++){
						resultMap.put(metaData.getColumnName(i), resultSet.getObject(i));
					}
					resultMap = MybatisMapUtils.mapKeyFormat(resultMap);
					//modelList.add((Model) MyBatisBeanUtils.convertMap(model.getClass(), resultMap));
					modelList.add((Model)BeanMapper.map(resultMap, model.getClass()));
				}
				resultSet.close();
				//connection.close(); 
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return modelList;
	}
	
	@Override
	public <Model> void deleteById(Class<Model> modelClass, Long id) {
		try{
			Model model = modelClass.newInstance();
			if(model.getClass().isAnnotationPresent(Table.class)){
				String tableName = model.getClass().getAnnotation(Table.class).name();
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM "+tableName+" WHERE ID = '"+id+"'" );
				Connection connection = sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
				//Connection connection = getSqlSession().getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql.toString());
				//connection.close(); 
				log.info("==== info ==== deleteById SQL:"+sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	@Override
	public <Model> void deleteByModel(Model model) {
		try{
			if(model.getClass().isAnnotationPresent(Table.class)){
				String tableName = model.getClass().getAnnotation(Table.class).name();
				StringBuffer sql = new StringBuffer();
				sql.append("DELETE FROM "+tableName+" WHERE 1=1 " );
				
				/*Method[] methods = model.getClass().getMethods(); //TODO:先去掉ID，VERSION,CREAT_TIME的条件查询
				for(Method method:methods){
					if(method.isAnnotationPresent(Column.class)){
						Column column = method.getAnnotation(Column.class);
						String columnName = column.name();
						Object val =  method.invoke(model);// 调用getter方法获取属性值  
						if (val != null) {  
			            	sql.append(" AND "+columnName+"='"+val+"'");
			            }  
					}
				}*/
				
				Field[] fields = model.getClass().getDeclaredFields();
				for(Field field:fields){
					Method m;
					if(field.isAnnotationPresent(Column.class)){
						Column column = field.getAnnotation(Column.class);
						String columnName = column.name();
						m = (Method) model.getClass().getMethod("get" + getMethodName(field.getName()));
			            Object val =  m.invoke(model);// 调用getter方法获取属性值  
			            if (val != null) {  
			            	sql.append(" AND "+columnName+"='"+val+"'");
			            }  
					}
				}
				
				Connection connection = sqlSessionTemplate.getSqlSessionFactory().openSession().getConnection();
				//Connection connection = getSqlSession().getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql.toString());
				//connection.close(); 
				log.info("==== info ==== deleteByModel SQL:"+sql);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> query(String statement, Map params) {
		List<Map> resultList = getSqlSession().selectList(statement, params);
		return resultList;
	}
	
	/**
	 * @Description: 获取Mapper命名空间
	 * @param @param sqlId
	 * @param @return    
	 * @return String
	 */
	public String getStatement(String sqlId) {
		String name = this.getClass().getName();
		StringBuffer sb = new StringBuffer();
		sb.append(name).append(".").append(sqlId);
		String statement = sb.toString();

		return statement;
	}
	

	// 把一个字符串的第一个字母大写  
    private static String getMethodName(String fildeName) {  
        byte[] items = fildeName.getBytes();  
        items[0] = (byte) ((char) items[0] - 'a' + 'A');  
        return new String(items);  
    }

	@Override
	public PageBean listPage(String statment,PageParam pageParam, Map<String, Object> paramMap) {
		if (paramMap == null) {
			paramMap = new HashMap<String, Object>();
		}
		// 根据页面传来的分页参数构造SQL分页参数
		paramMap.put("pageFirst", (pageParam.getPageNum() - 1) * pageParam.getNumPerPage());
		paramMap.put("pageSize", pageParam.getNumPerPage());
		paramMap.put("startRowNum", (pageParam.getPageNum() - 1) * pageParam.getNumPerPage());
		paramMap.put("endRowNum", pageParam.getPageNum() * pageParam.getNumPerPage());
		// 统计总记录数
		//Long count = sessionTemplate.selectOne("BaseDao.listPageCount", paramMap);
		// 获取分页数据集
		List<Object> resultList = getSqlSession().selectList(statment, paramMap);
		
		List<Object> list = new ArrayList<Object>();
		int pageBeginNum = (pageParam.getPageNum() - 1) * pageParam.getNumPerPage();	//此页开始记录索引
		int pageEndNum;	//此页最后记录索引
		int surplus = resultList.size() - (pageParam.getPageNum() - 1)* pageParam.getNumPerPage();	//剩余记录数
		if(surplus<pageParam.getNumPerPage()){
			pageEndNum = pageBeginNum+surplus;
		}else{
			pageEndNum = pageBeginNum+pageParam.getNumPerPage();
		}
		for(int i =pageBeginNum; i< pageEndNum;i++){	//返回条数
			list.add(resultList.get(i));
		}
		
		Object isCount = paramMap.get("isCount"); // 是否统计当前分页条件下的数据：1:是，其他为否
		if (isCount != null && "1".equals(isCount.toString())){
			Map<String, Object> countResultMap = new HashMap<String, Object>();
			countResultMap.put("count", list.size());
			return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), resultList.size(), list, countResultMap);
		}else{
			// 构造分页对象
			return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), resultList.size(), list);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <Model> Page listPage(Class<Model> modelClass, PageParam pageParam, Map<String, Object> paramMap) {
		try{
			if (paramMap == null) {
				paramMap = new HashMap<String, Object>();
			}
			//获取表名
			Model model = modelClass.newInstance();
			String tableName = model.getClass().getAnnotation(Table.class).name();
			paramMap.put("tableName", tableName);
			
			// 根据页面传来的分页参数构造SQL分页参数
			paramMap.put("pageFirst", (pageParam.getPageNum() - 1) * pageParam.getNumPerPage());
			paramMap.put("pageSize", pageParam.getNumPerPage());
			paramMap.put("startRowNum", (pageParam.getPageNum() - 1) * pageParam.getNumPerPage());
			paramMap.put("endRowNum", pageParam.getPageNum() * pageParam.getNumPerPage());
			// 获取分页数据集
			List<Map> resultList = getSqlSession().selectList("BaseDao.listPage", paramMap);
			List<Object> list = new ArrayList<Object>();
			
			int pageBeginNum = (pageParam.getPageNum() - 1) * pageParam.getNumPerPage();	//此页开始记录索引
			int pageEndNum;	//此页最后记录索引
			int surplus = resultList.size() - (pageParam.getPageNum() - 1)* pageParam.getNumPerPage();	//剩余记录数
			if(surplus<pageParam.getNumPerPage()){
				pageEndNum = pageBeginNum+surplus;
			}else{
				pageEndNum = pageBeginNum+pageParam.getNumPerPage();
			}
			for(int i =pageBeginNum; i< pageEndNum;i++){	//返回条数
				list.add(MybatisMapUtils.mapKeyFormat(resultList.get(i)));
			}
			
			Object isCount = paramMap.get("isCount"); // 是否统计当前分页条件下的数据：1:是，其他为否
			if (isCount != null && "1".equals(isCount.toString())){
				Map<String, Object> countResultMap = new HashMap<String, Object>();
				countResultMap.put("count", resultList.size());
				return new PageImpl<Object>(list, new PageRequest(pageParam.getPageNum(), pageParam.getNumPerPage()), resultList.size());
				//return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), resultList.size(), list, countResultMap);
			}else{
				// 构造分页对象
				return new PageImpl<Object>(list, new PageRequest(pageParam.getPageNum(), pageParam.getNumPerPage()), resultList.size());
				//return new PageBean(pageParam.getPageNum(), pageParam.getNumPerPage(), resultList.size(), list);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public <Model> Model selectOne(String statement, Object parameter) {
		return getSqlSession().selectOne(statement, parameter);
	}
	
	/**
	 * @Description: 暂未用到
	 * @param @param modelClass
	 * @param @param statement
	 * @param @param parameter
	 * @param @return    
	 * @return Model
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <Model> Model selectOne(Class<Model> modelClass,String statement, Object parameter) {
		try {
			Map resultMap = getSqlSession().selectOne(statement, parameter);
			if(resultMap == null){
				return null;
			}
			resultMap = MybatisMapUtils.mapKeyFormat(resultMap);
			//return  MyBatisBeanUtils.convertMap(modelClass, resultMap);
			return BeanMapper.map(resultMap, modelClass);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}

	@Override
	public <Model> List<Model> selectList(String statement, Object parameter) {
		return getSqlSession().selectList(statement, parameter);
	}


}
