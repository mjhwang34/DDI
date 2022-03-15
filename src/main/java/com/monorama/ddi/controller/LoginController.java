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
@Api(tags="로그인")
public class LoginController {
	private LoginService loginService;
	
	@Autowired 
	public LoginController(LoginService loginService) {
		this.loginService = loginService;
	}
	
	@PostMapping("/login")
	@ApiOperation(value="로그인", notes="로그인을 할 수 있는 기능")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="로그인 성공", response=Message.class),
		@ApiResponse(code=404, message="입력된 패스워드와 일치하는 정보가 없음")
	})
	@ApiImplicitParams(value= {
		@ApiImplicitParam(name="locale", value="언어설정: ko 또는 en", example="ko"), 
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
		
		log.info("!!!비밀번호 : {}", map.get("passwd"));
		log.info("!!!언어: {}", locale);
		
		log.info("***비밀번호 : {}", map.get("passwd"));
		
		HashMap<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put("passwd", map.get("passwd"));
		loginInfo.put("locale", locale);
		
		//int tempUserSeq = loginService.getLoginResult(loginInfo);
		//String userSeq = Integer.toString(tempUserSeq);
		
		String userSeq = loginService.getLoginResult(loginInfo);

		System.out.println("userseq : " + userSeq);
		
		if( userSeq != null ) { // 로그인 pw가 맞다면
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
	@ApiOperation(value="로그인 여부 확인", notes="로그인이 되어 있는지 확인할 수 있는 기능")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="로그인 상태",  response=Message.class),
		@ApiResponse(code=401, message="로그인 상태가 아님")
	})
	@ApiImplicitParam(name="locale", value="언어설정: ko 또는 en", example="ko")
	public Object isLogin(
			@RequestHeader(value="locale") String locale, 
			HttpServletRequest req, HttpServletResponse res) {
		System.out.println("언어:" + locale);
		
		HashMap <String, String> cookieHashMap = CookieManager.getAllCookies(req);
		
		String sessionKey = null;
		sessionKey = cookieHashMap.get("sk_"+locale); // 많은 쿠키 중 필요한 쿠키 찾기
		
		if(sessionKey == null) { //쿠키가 존재하지 않을 때
			throw new AuthException();			
		}
		
		System.out.println("세션키:" + sessionKey);
		
		int userSeq = loginService.getUserSeqByLocale(locale);
		
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put("session_key", sessionKey);
		info.put("user_seq", userSeq);
		
		ActionSession actionSession = loginService.getActionSessionByKey(info);
		if(actionSession == null) { //쿠키값이 있음에도 유효하지 않다
			Cookie cookie = new Cookie("sk_"+locale, null);
			res.addCookie(cookie); // 삭제하고 쿠키를 다시 response로 보내줘야함
			throw new AuthException();
		}
		
		Message message = new Message();
		return message;
	}
	
	@PutMapping("/change_password")
	@ApiOperation(value="비밀번호 바꾸기", notes="비밀번호를 바꿀 수 있는 기능")
	@ApiResponses(value= {
		@ApiResponse(code=200, message="비밀번호 변경 완료",  response=Message.class),
		@ApiResponse(code=404, message="입력된 패스워드와 일치하는 정보가 없음")
	})
	@ApiImplicitParams(value= {
			@ApiImplicitParam(name="locale", value="언어설정: ko 또는 en", example="ko"), 
			@ApiImplicitParam(name="curPasswd", value="현재 비밀번호", example="1111"), 
			@ApiImplicitParam(name="newPasswd", value="새로운 비밀번호", example="2222"), 
	})
	public Object change_password(
			@RequestHeader(value="locale") String locale, 
			@RequestParam("curPasswd") String curPasswd,
			@RequestParam("newPasswd") String newPasswd) {
		HashMap<String, String> loginInfo = new HashMap<String, String>();
		loginInfo.put("passwd", curPasswd);
		loginInfo.put("locale", locale);
		String userSeq = loginService.getLoginResult(loginInfo);
		
		if( userSeq != null ) { // 로그인 pw가 맞다면
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
