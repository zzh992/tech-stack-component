package com.techstack.component.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 基础DAO接口，默认继承了spring data
 * jpa的JpaRepository接口，该接口暴露了多个方法，有些方法可能在具体的dao中是应该不允许使用的
 * 。JpaSpecificationExecutor允许使用Criteria查询
 * http://www.ibm.com/developerworks/cn/opensource/os-cn-spring-jpa/
 * http://blog.csdn.net/z69183787/article/details/30265243
 * 
 * @param <Model>
 *            实体
 * @param <ID>
 */
public interface BaseRepository<Model,ID extends Serializable> extends JpaRepository<Model, ID>,JpaSpecificationExecutor<Model> {

}
