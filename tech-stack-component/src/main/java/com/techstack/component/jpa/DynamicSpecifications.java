package com.techstack.component.jpa;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.techstack.component.jpa.SearchFilter.Logic;
import com.techstack.component.jpa.SearchFilter.Operator;
import com.techstack.component.reflection.Reflections;


public class DynamicSpecifications {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters) {
		return new Specification<T>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!filters.isEmpty()) {
					Predicate predicates = null;
					for (SearchFilter filter : filters) {
						Predicate predicate = null;
						String[] names = StringUtils.split(filter.fieldName, ".");
						/*Path expression = root.get(names[0]);
						
						// the one-to-many search， 但是还未实现多级
						//if(expression.getJavaType().equals(List.class)){
						if(Collection.class.isAssignableFrom(expression.getJavaType())){
							Join namesJoin = root.join(names[0]);
							expression = namesJoin.get(names[1]);
							for (int i = 2; i < names.length; i++) {
								expression = expression.get(names[i]);
							}
							
						}else{
							// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
							for (int i = 1; i < names.length; i++) {
								expression = expression.get(names[i]);
							}
						}*/
						Path expression = buildFiledPathByFiledName(root, names, 0);
						
						
						Object value = filter.value;
						
						if(Date.class.equals(expression.getJavaType())){
							try {
								value = dateFormat.parse(value.toString());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								value = filter.value;
							}
						}
						
						
						// logic operator
						switch (filter.operator) {
							case EQ:
								predicate = builder.equal(expression, value);
								break;
							case NEQ:
								predicate = builder.notEqual(expression, value);
								break;
							case LIKE:
								predicate = builder.like(builder.upper(expression),"%" + value.toString().toUpperCase() + "%");
								break;
							case GT:
								predicate = builder.greaterThan(expression, (Comparable) value);
								break;
							case LT:
								predicate = builder.lessThan(expression, (Comparable) value);
								break;
							case GTE:
								predicate = builder.greaterThanOrEqualTo(expression, (Comparable) value);
								break;
							case LTE:
								predicate = builder.lessThanOrEqualTo(expression, (Comparable) value);
								break;
							case IN:
						        In<Object> in = builder.in(expression);
						        for(String val : value.toString().split(",")){
						        	in.value((Comparable) val);
						        }
						        predicate = in;
								break;
						}
						
						if(predicate!=null){
							if(filter.logic == Logic.OR){
								if(predicates == null){
									predicates = builder.or(predicate);
								}else{
									predicates = builder.or(predicates,predicate);
								}
							}else{
								if(predicates == null){
									predicates = builder.and(predicate);
								}else{
									predicates = builder.and(predicates,predicate);
								}
							}
						}
					}

					return predicates;
				}

				return builder.conjunction();
			}
		};
	}
	
	/**
	 * 根据所给的FiledName构建path，支持model中的collection属性，使用join去实现
	 * @return
	 */
	private static Path buildFiledPathByFiledName(Root root, String[] names, Integer fence){
		Path expression = root.get(names[fence]);
		if(Collection.class.isAssignableFrom(expression.getJavaType())){
			Join namesJoin = root.join(names[fence]);
			expression = namesJoin.get(names[fence+1]);
			for (fence = fence+2; fence < names.length; fence++) {
				expression = buildFiledPathByFiledName(root,names, fence);
			}
			
		}else{
			// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
			for (fence= fence+1; fence < names.length; fence++) {
				expression = buildFiledPathByFiledName(root,names, fence);
			}
			
		}
		return expression;
	}
	
	/**
	 * TODO：支持单对象属性查询，不支持Collection属性级联查询 ，慎用
	 * @param model
	 * @return
	 */
	public static <T> Specification<T> bySearchModel(final T model) {
		/*List<String> fieldNameList = Reflections.getAllFieldName(model);
		for(String fieldName : fieldNameList){
			//if(Reflections.getFieldValue(model, fieldName) != null){
			if(Reflections.getFieldValue(model, fieldName) != null && !Collection.class.isAssignableFrom(Reflections.getFieldValue(model, fieldName).getClass())){
				SearchFilter searchFilter = new SearchFilter(fieldName, Operator.EQ, Reflections.getFieldValue(model, fieldName), Logic.AND);
				filters.add(searchFilter);
			}
		}*/
		List<SearchFilter> filters = new ArrayList<SearchFilter>();
		buildSearchFilterByModel(model,filters, new StringBuffer());
		return bySearchFilter(filters) ;
	}
	
	private static <T> void buildSearchFilterByModel(final T model, List<SearchFilter> filters, StringBuffer rootPath){
		List<String> fieldNameList = Reflections.getAllFieldName(model.getClass());
		for(String fieldName : fieldNameList){
			StringBuffer namePath = new StringBuffer(rootPath.toString()).append(".").append(fieldName);
			if(Reflections.getFieldValue(model, fieldName) == null || Collection.class.isAssignableFrom(Reflections.getFieldValue(model, fieldName).getClass())){	//TODO: 目前由于获取不到collection的泛型class,先不支持这种类型的path构建，过滤掉
				continue;
			}
			Object fieldValue = Reflections.getFieldValue(model, fieldName);
			Boolean isTail = true;
			for(Field field : fieldValue.getClass().getDeclaredFields()){
				if(field.getName().equals("id")){
					isTail = false;
					break;
				}
			}
			if(!isTail){
				buildSearchFilterByModel(fieldValue, filters, namePath);
			}else{
				namePath.deleteCharAt(0);
				SearchFilter searchFilter = new SearchFilter(namePath.toString(), Operator.EQ, fieldValue, Logic.AND);
				filters.add(searchFilter);
			}
		}
	}
}
