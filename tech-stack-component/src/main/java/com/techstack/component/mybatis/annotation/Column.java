package com.techstack.component.mybatis.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Column.java 
 * @Description: 自定义mybatis注解：在实体的属性上写列注解（BaseEntity是在getter方法上）
 * @author zzh
 */
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	public abstract String name();
}
