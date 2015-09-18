package com.techstack.component.shiro;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


/**
 * 自定义Realm：
 * 域，Shiro从从Realm获取安全数据（如用户、角色、权限），就是说SecurityManager
 * 要验证用户身份，那么它需要从Realm获取相应的用户进行比较以确定用户身份是否合法；
 * 也需要从Realm得到用户相应的角色/权限进行验证用户是否能进行操作；可以把Realm看 成DataSource，即安全数据源。
 *
 */
public class ShiroDbRealm extends AuthorizingRealm{

	protected ShiroService shiroService;

	/**
	 * 认证回调函数,登录时调用.
	 * 验证当前登录的Subject ,该方法的调用时机为Controller的login方法中执行Subject.login()时 
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;// Shiro 提供了一个直接拿来用的UsernamePasswordToken，用于实现用户名/密码Token组，另外其实现了RememberMeAuthenticationToken和HostAuthenticationToken，可以实现记住我及主机验证的支持。
		ShiroUser shiroUser = shiroService.findShiroUserByUsername(token.getUsername());
		if(shiroUser != null){
			return new SimpleAuthenticationInfo(shiroUser,shiroUser.getPassword(),shiroUser.getName());
		}else{
			return null;
		}
	}
	
	
	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 * 为当前登录的Subject授予角色和权限, 该方法的调用时机为需授权资源被访问时 
	 * 如果每次访问需授权资源时都会执行该方法中的逻辑,这表明默认并未启用AuthorizationCache 
	 * 
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRoles(shiroService.findRolesByByUsername(shiroUser.getUsername()));
		info.addStringPermissions(shiroService.findPermissionsByUsername(shiroUser.getUsername()));
		return info;
	}

	public void setShiroService(ShiroService shiroService) {
		this.shiroService = shiroService;
	}

	
	/** 
     * 设定Password校验. 
     */  
	@PostConstruct
	public void initCredentialsMatcher() {
		//重写shiro的密码验证，让shiro用自定义验证
		setCredentialsMatcher(new CustomCredentialsMatcher());
	}
	
}
