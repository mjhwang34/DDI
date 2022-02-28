package com.monorama.ddi;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	public static String makeSessionKey() {
		String uuid = UUID.randomUUID().toString();
		return uuid;
	}
	public static String findIpAddress(HttpServletRequest request) {// proxy 서버 때문 - ip 주소를 proxy 서버가 메인 서버에 전달할 때 필요함
		String ip = null;
		
        ip = request.getHeader("X-Forwarded-For");
        
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_CLIENT_IP"); 
        } 
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("HTTP_X_FORWARDED_FOR"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-Real-IP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("X-RealIP"); 
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
		
		return ip;
	}
	public static String findReferenceMessage(int reference, PkMessage pkMessage) {
		if(reference==0) {
			return pkMessage.getRef_0();
		}
		else if(reference==1) {
			return pkMessage.getRef_1();
		}
		return null;
	}
	public static String findDetailMessage(float foldValue, PkMessage pkMessage) {
		String m;
		if(foldValue>0) {
			m = pkMessage.getDetail_gt();
		}
		else if(foldValue<0) {
			m = pkMessage.getDetail_lt();
		}
		else {
			m = pkMessage.getDetail_eq();
		}
		return m;
	}
}
