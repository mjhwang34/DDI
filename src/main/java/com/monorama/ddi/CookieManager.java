package com.monorama.ddi;

import java.util.HashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieManager {

	public CookieManager() {
		
	}
	
	public static HashMap<String, String> getAllCookies(HttpServletRequest req) {
		Cookie[] cookieList = req.getCookies();
		HashMap<String, String> cookieHashMap = new HashMap<String, String>();
		for(Cookie cookie: cookieList) {
			System.out.println(cookie.getName()+" "+cookie.getValue());
			cookieHashMap.put(cookie.getName(), cookie.getValue());
		}
		return cookieHashMap;
	}

}
