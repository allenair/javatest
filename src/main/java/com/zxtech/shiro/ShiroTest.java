package com.zxtech.shiro;

import java.util.Arrays;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroTest {
	private static Logger log = LoggerFactory.getLogger(ShiroTest.class);
	public static void main(String[] args) {
		ShiroTest tt = new ShiroTest();
		
//		tt.loginZero("classpath:shiro-zero.ini");
		tt.loginFirst("classpath:shiro-first.ini","wang","123");
		
	}
	public void loginZero(String fileName) {
		//1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager  
	    Factory<org.apache.shiro.mgt.SecurityManager> factory = new IniSecurityManagerFactory(fileName);
	    //2、得到SecurityManager实例 并绑定给SecurityUtils   
	    org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
	    SecurityUtils.setSecurityManager(securityManager);
	    //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
	    Subject subject = SecurityUtils.getSubject();
	    UsernamePasswordToken token = new UsernamePasswordToken("zhang", "123");
	    try {
	        //4、登录，即身份验证
	        subject.login(token);
	    } catch (AuthenticationException e) {
	    		e.printStackTrace();
	    		
	        //5、身份验证失败
	    		
	    		log.error("===={}===={}===={}=", new String[]{"1", "A", "C"});;
	    }
	    System.out.println(subject.isAuthenticated());//断言用户已经登录
	    //6、退出
	    subject.logout();
	}
	
	public void loginFirst(String fileName, String userName, String passwd) {
		if(this.login(fileName, userName, passwd)) {
			Subject subject = SecurityUtils.getSubject();
//			Object obj = subject.getPrincipal();
			PrincipalCollection principalCollection = subject.getPrincipals();
			for (Object object : principalCollection.asList()) {
				System.out.println(object.toString());
			}
		}
	}
	
	private boolean login(String fileName, String userName, String passwd) throws AuthenticationException{
		//1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<org.apache.shiro.mgt.SecurityManager> factory =
                new IniSecurityManagerFactory(fileName);

        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);

        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, passwd);

        try {
        		subject.login(token);
        		return true;
        }catch(AuthenticationException e) {
        		System.out.println(e.getMessage());
        		return false;
        }
       
	}

}
