package com.techstack.component.jpa;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

public class JpaPageUtils {
	
	public static PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortAttribute, Direction sortType){
		if(StringUtils.isEmpty(sortAttribute)){
			sortAttribute = "id";
		}
		if(sortType == null){
			sortType = Direction.DESC;
		}
		Sort sort = new Sort(sortType, sortAttribute);
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
	@SuppressWarnings("rawtypes")
	public static Specification buildSpecification(List<SearchFilter> searchFilterList) {
		Specification spec = DynamicSpecifications.bySearchFilter(searchFilterList);
		return spec;
	}
}
