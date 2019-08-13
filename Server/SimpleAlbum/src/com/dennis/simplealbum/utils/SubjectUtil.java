package com.dennis.simplealbum.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class SubjectUtil {
	
	//可以更换为数据库存储持久化
	private static Map<String, Subject> userMap = new HashMap<String, Subject>();
	
	public static Subject getCurrentSubject(String sessionID) {
				
		return userMap.get(sessionID);
	}
	
	public static void saveCurrentSubject(String sessionID, Subject currentSubject) {
		userMap.put(sessionID, currentSubject);
	}
	
	
	public static Subject createSubject() {
		
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");

		SecurityManager manager = factory.getInstance();

		SecurityUtils.setSecurityManager(manager);
		
		return SecurityUtils.getSubject();
		
		
	}
	
	public static void deleteSubject(String sessionID) {
		userMap.remove(sessionID);
	}

}
