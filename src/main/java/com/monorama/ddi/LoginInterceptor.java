package com.monorama.ddi;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
	//컨트롤러에 요청이 넘겨지기 전에
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	log.info("비밀번호 : {}", request.getParameter("passwd"));
		log.info("언어: {}", request.getHeader("locale"));
		
		String cookieVal=null;
		for (Cookie cookie : request.getCookies()) { //받은 많은 쿠기 중 필요한 쿠키 찾기  
	        if("sk_ko".equalsIgnoreCase(cookie.getName())) {
	        	cookieVal = cookie.getValue();
	        }
	        if("sk_en".equalsIgnoreCase(cookie.getName())) {
	        	cookieVal = cookie.getValue();
	        }
	    }
		
		log.info("쿠키 value : {} ",cookieVal);
		if(cookieVal != null) { // 쿠키가 존재한다면
			log.info("쿠키 존재함");
			return false;
		}
		else {
			log.info("쿠키 없음");
			return true;
		}
    }

    //컨트롤러가 처리를 마친 후
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    //view까지의 모든 요청처리가 완료되었을 때
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
