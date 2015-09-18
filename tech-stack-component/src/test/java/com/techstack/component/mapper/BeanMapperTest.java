package com.techstack.component.mapper;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

public class BeanMapperTest {

	@Test
	public void testMapObjectClassOfT() {
		ChildModel childModel = new ChildModel();
		childModel.setId(3L);
		childModel.setCreateDate(new Date());
		childModel.setChildTest("111");
		ChildBean childBean = BeanMapper.map(childModel, ChildBean.class);
		Assert.assertEquals(childModel.getId(), childBean.getId());
		Assert.assertEquals(childModel.getCreateDate(), childBean.getCreateDate());
		Assert.assertEquals(childModel.getChildTest(), childBean.getChildTest());
	}

}
