package com.dennis.simplealbum.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dennis.simplealbum.po.LoginJudgement;
import com.dennis.simplealbum.utils.SubjectUtil;

@Controller
@RequestMapping("/Kikyo")
public class LoginModuleController {

	@RequestMapping("/login")
	public @ResponseBody LoginJudgement login(String username, String password, HttpServletRequest req, HttpServletResponse resp) {
		
		LoginJudgement judge = new LoginJudgement();
		judge.setLogin(false);
		
		//Subject currentSubject = SubjectUtil.getCurrentSubject(session.getId());
		
		
		//首次登陆执行代码
		//启用支持Session servlet自动返回名为“Set-cookie”的cookie（Jsessionid=xxx）
		HttpSession session = req.getSession();
		//自定义工具类新建一个subject
		Subject subject = SubjectUtil.createSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		try {
			
			subject.login(token);
			judge.setLogin(true);
			//如果认证成功，用sessionID作key将subject存入服务器内存中，下次进入首页是直接验证是否存在该SessionID并且拿出subject看是否islogin()就可以免去登录了
			SubjectUtil.saveCurrentSubject(session.getId(), subject);
			
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			judge.setErrorMsg("UnknownAccount");
			
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			judge.setErrorMsg("IncorrectCredentials");

		} catch (Exception e) {
			e.printStackTrace();
			judge.setErrorMsg("LoginError");
		}


		return judge;
	}
	
	
	@RequestMapping("/check")
	public @ResponseBody LoginJudgement check(HttpServletRequest req) throws Exception {
		
		Cookie[] cookies = req.getCookies();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				System.out.println(cookie.getName());
				System.out.println(cookie.getValue());
			}
		}
		
		
		
		LoginJudgement judge = new LoginJudgement();
		judge.setLogin(false);
		judge.setErrorMsg("Please Login");
		
		HttpSession session = req.getSession(false);
		if(session !=null) {
			
			System.out.println("////////////////" + session.getId() + "/////////////");
			
		}
		
		if(session == null) {
			System.out.println("////////////no session////////////");
			
			return judge;
			
		}
		//如果曾经成功登录，这里就返回true
		Subject currentSubject = SubjectUtil.getCurrentSubject(session.getId());
		if(currentSubject != null) {
			if(currentSubject.isAuthenticated()) {
				judge.setLogin(true);
				return judge;
			}
		}
		
		
		return judge;
	}
	
	
	@RequestMapping("/logout")
	public void logout(HttpServletRequest req) {
		
		HttpSession session = req.getSession(false);
		
		Subject currentSubject = SubjectUtil.getCurrentSubject(session.getId());
		
		try {
			currentSubject.logout();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SubjectUtil.deleteSubject(session.getId());
		
		session.invalidate();
		
		
		
	}

}
