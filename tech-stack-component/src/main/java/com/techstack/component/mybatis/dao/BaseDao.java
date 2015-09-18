package com.techstack.component.mybatis.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.techstack.component.mybatis.page.PageBean;
import com.techstack.component.mybatis.page.PageParam;

/**
 * @Title: BaseDao.java 
 * @Description: DAO数据访问层基础类
 * @author zzh
 */
public interface BaseDao{
	
	/**
	 * @Description: id为空则新增，id不空则做更新
	 * @param @param model
	 * @param @return    
	 * @return Model
	 */
	public <Model> Model saveOrUpdate(Model model);
	
	/**
	 * @Description: 根据传入的实体类型和ID获取记录
	 * @param @param modelClass
	 * @param @param id
	 * @param @return    
	 * @return Model
	 */
	public <Model> Model getById(Class<Model> modelClass ,Long id);
	
	/**
	 * @Description: 根据传入的实体查找符合条件的记录
	 * @param @param model
	 * @param @return    
	 * @return List<Model>
	 */
	public <Model> List<Model> getByModel(Model model);
	
	/**
	 * @Description: 根据传入的实体类型和ID删除记录
	 * @param @param modelClass
	 * @param @param id    
	 * @return void
	 */
	public <Model> void deleteById(Class<Model> modelClass ,Long id);
	
	/**
	 * @Description: 根据传入的实体删除符合条件的记录
	 * @param @param model    
	 * @return void
	 */
	public <Model> void deleteByModel(Model model);
	
	/**
	 * @Description: mabatis查询，statement为sql语句的标识，params为命名参数
	 * @param @param statement
	 * @param @param params
	 * @param @return    
	 * @return List<Map>
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> query(String statement,Map params);
	
	/**
	 * @Description: mabatis查询，封装selectOne方法
	 * @param @param statement
	 * @param @param parameter
	 * @param @return    
	 * @return Model
	 */
	public <Model> Model selectOne(String statement, Object parameter);
	
	/**
	 * @Description: mabatis查询，封装selectList方法
	 * @param @param modelClass
	 * @param @param statement
	 * @param @param parameter
	 * @param @return    
	 * @return List<Model>
	 */
	public <Model> List<Model> selectList(String statement, Object parameter);
	
	/**
	 * @Description: 分页查询,用于多表分页查询，需要在对应的Mapper文件写SQL语句
	 * @param @param pageParam
	 * @param @param paramMap
	 * @param @return    
	 * @return PageBean
	 */
	public PageBean listPage(String statement, PageParam pageParam, Map<String, Object> paramMap);
	
	
	/**
	 * @Description: 分页查询，指定modelClass，用于单表分页
	 * @param @param modelClass
	 * @param @param pageParam
	 * @param @param paramMap :通过传入module查找对应条件语句块，可查看BaseMapper.xml配置
	 * @param @return    
	 * @return PageBean
	 */
	public <Model> Page listPage(Class<Model> modelClass, PageParam pageParam, Map<String, Object> paramMap);
}
