package com.techstack.component.shiro;

import java.util.Set;


/**
 * 使shiro component分离出来，ShiroDbRealm中不用依赖具体项目的user service，具体项目只要实现这个接口并将其注入给ShiroDbRealm即可
 *
 */
public interface ShiroService {
	public ShiroUser findShiroUserByUsername(String username);
	public Set<String> findRolesByByUsername(String username);
	public Set<String> findPermissionsByUsername(String username);
}
