package com.monorama.ddi.controller;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monorama.ddi.CookieManager;
import com.monorama.ddi.Utils;
import com.monorama.ddi.exception.AuthException;
import com.monorama.ddi.exception.LoginFailException;
import com.monorama.ddi.model.ActionSession;
import com.monorama.ddi.model.Message;
import com.monorama.ddi.service.LoginService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController 
@RequestMapping("/api/user")
@Api(tags="�α���")
public class LoginController {
	private LoginService loginService;
	
	@Autowired 
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping("/login")
	@ApiOperation(value="�α���", notes="�α����� �� �� �ִ� ���")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="�α��� ����", response=Message.class),
		@ApiResponse(code=404, message="�Էµ� �н������ ��ġ�ϴ� ������ ����")
	})
	@ApiImplicitParams(value= {
		@ApiImplicitParam(name="locale", value="����: ko �Ǵ� en", example="ko"), 
		@ApiImplicitParam(name="User-Agent", value="UserAgent", example="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.81 Safari/537.36"),
		@ApiImplicitParam(name="map", value="password", example="1111")
	})
	public Object login(
			@RequestHeader(value="locale") String locale, 
			@RequestHeader(value="User-Agent") String userAgent, 
			@RequestParam HashMap<String, String> map,
			HttpServletRequest req,
			HttpServletResponse res
		
		) {
		
		String ipAddress = Utils.findIpAddress(req);
		
		log.info("!!!��й�ȣ : {}", map.get("passwd"));
		log.info("!!!���: {}", locale);
		
		log.info("***��й�ȣ : {}", map.get("passwd"));
		
		HashMap<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put("passwd", map.get("passwd"));
		loginInfo.put("locale", locale);
		
		//int tempUserSeq = loginService.getLoginResult(loginInfo);
		//String userSeq = Integer.toString(tempUserSeq);
		
		String userSeq = loginService.getLoginResult(loginInfo);

		System.out.println("userseq : " + userSeq);
		
		if( userSeq != null ) { // �α��� pw�� �´ٸ�
			//String userAgent = req.getHeader("User-Agent");
				
			ActionSession actionSession = new ActionSession();
			
			actionSession.setSession_key(Utils.makeSessionKey());
			actionSession.setUser_seq(Integer.parseInt(userSeq));
			actionSession.setIp(ipAddress);
			actionSession.setUa(userAgent);
			
			loginService.insertActionSession(actionSession);
			
			Cookie cookie  = new Cookie("sk_"+locale, actionSession.getSession_key());

			res.addCookie(cookie);
			log.info("{}", cookie);
			
			
			Message message = new Message();
			message.setData(actionSession.getLoginData());
			return message;
		}
		
		throw new LoginFailException();
	}
	
	@GetMapping("/is_login")
	@ApiOperation(value="�α��� ���� Ȯ��", notes="�α����� �Ǿ� �ִ��� Ȯ���� �� �ִ� ���")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="�α��� ����",  response=Message.class),
		@ApiResponse(code=401, message="�α��� ���°� �ƴ�")
	})
	@ApiImplicitParam(name="locale", value="����: ko �Ǵ� en", example="ko")
	public Object isLogin(
			@RequestHeader(value="locale") String locale, 
			HttpServletRequest req, HttpServletResponse res) {
		System.out.println("���:" + locale);
		
		HashMap <String, String> cookieHashMap = CookieManager.getAllCookies(req);
		
		String sessionKey = null;
		sessionKey = cookieHashMap.get("sk_"+locale); // ���� ��Ű �� �ʿ��� ��Ű ã��
		
		if(sessionKey == null) { //��Ű�� �������� ���� ��
			throw new AuthException();			
		}
		
		System.out.println("����Ű:" + sessionKey);
		
		int userSeq = loginService.getUserSeqByLocale(locale);
		
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("session_key", sessionKey);
		info.put("user_seq", userSeq);
		
		ActionSession actionSession = loginService.getActionSessionByKey(info);
		if(actionSession == null) { //��Ű���� �������� ��ȿ���� �ʴ�
			Cookie cookie = new Cookie("sk_"+locale, null);
			res.addCookie(cookie); // �����ϰ� ��Ű�� �ٽ� response�� ���������
			throw new AuthException();
		}
		
		Message message = new Message();
		return message;
	}
	
	@PutMapping("/change_password")
	@ApiOperation(value="��й�ȣ �ٲٱ�", notes="��й�ȣ�� �ٲ� �� �ִ� ���")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="��й�ȣ ���� �Ϸ�",  response=Message.class),
		@ApiResponse(code=404, message="�Էµ� �н������ ��ġ�ϴ� ������ ����")
	})
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="locale", value="����: ko �Ǵ� en", example="ko"), 
			@ApiImplicitParam(name="curPasswd", value="���� ��й�ȣ", example="1111"), 
			@ApiImplicitParam(name="newPasswd", value="���ο� ��й�ȣ", example="2222"), 
	})
	public Object change_password(
			@RequestHeader(value="locale") String locale, 
			@RequestParam("curPasswd") String curPasswd,
			@RequestParam("newPasswd") String newPasswd) {
		HashMap<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put("passwd", curPasswd);
		loginInfo.put("locale", locale);
		String userSeq = loginService.getLoginResult(loginInfo);
		
		if( userSeq != null ) { // �α��� pw�� �´ٸ�
			loginInfo.put("passwd", newPasswd);
			loginService.changePasswd(loginInfo);
		}
		else {
			throw new LoginFailException();
		}
		
		Message message = new Message();
		return message;
	}
	
}
