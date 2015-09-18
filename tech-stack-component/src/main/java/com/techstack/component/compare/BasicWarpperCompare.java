package com.techstack.component.compare;

/**
 * 基础类型包装类的value比较
 * 
 */
public class BasicWarpperCompare {

	public static boolean valueCompare(Integer i, Integer j) {
		if (i != null) {
			return i.equals(j);
		} else if (j != i) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean valueCompare(Float i, Float j) {
		if (i != null) {
			return i.equals(j);
		} else if (j != i) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean valueCompare(Long i, Long j) {
		if (i != null) {
			return i.equals(j);
		} else if (j != i) {
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean valueCompare(Double i, Double j) {
		if (i != null) {
			return i.equals(j);
		} else if (j != i) {
			return false;
		} else {
			return true;
		}
	}
}
