package com.techstack.component.mapper;

import java.io.Serializable;
import java.util.Date;

public abstract class SuperModel implements Serializable{

	private static final long serialVersionUID = -2628192471095840284L;
	
	private Long id;
	
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
