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
		
		
		//�״ε�½ִ�д���
		//����֧��Session servlet�Զ�������Ϊ��Set-cookie����cookie��Jsessionid=xxx��
		HttpSession session = req.getSession();
		//�Զ��幤�����½�һ��subject
		Subject subject = SubjectUtil.createSubject();
		
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		try {
			
			subject.login(token);
			judge.setLogin(true);
			//�����֤�ɹ�����sessionID��key��subject����������ڴ��У��´ν�����ҳ��ֱ����֤�Ƿ���ڸ�SessionID�����ó�subject���Ƿ�islogin()�Ϳ�����ȥ��¼��
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
		//��������ɹ���¼������ͷ���true
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
