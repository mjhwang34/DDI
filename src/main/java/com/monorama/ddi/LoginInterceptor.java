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
	//��Ʈ�ѷ��� ��û�� �Ѱ����� ����
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	log.info("��й�ȣ : {}", request.getParameter("passwd"));
		log.info("���: {}", request.getHeader("locale"));
		
		String cookieVal=null;
		for (Cookie cookie : request.getCookies()) { //���� ���� ��� �� �ʿ��� ��Ű ã��  
	        if("sk_ko".equalsIgnoreCase(cookie.getName())) {
	        	cookieVal = cookie.getValue();
	        }
	        if("sk_en".equalsIgnoreCase(cookie.getName())) {
	        	cookieVal = cookie.getValue();
	        }
	    }
		
		log.info("��Ű value : {} ",cookieVal);
		if(cookieVal != null) { // ��Ű�� �����Ѵٸ�
			log.info("��Ű ������");
			return false;
		}
		else {
			log.info("��Ű ����");
			return true;
		}
    }

    //��Ʈ�ѷ��� ó���� ��ģ ��
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    //view������ ��� ��ûó���� �Ϸ�Ǿ��� ��
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
