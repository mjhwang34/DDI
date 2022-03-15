package com.monorama.ddi.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.monorama.ddi.mapper.MainMapper;
import com.monorama.ddi.model.ActionSession;

@Service
public class LoginService {
	private final MainMapper mainMapper;
	
	@Autowired
	public LoginService(MainMapper mainMapper) { this.mainMapper=mainMapper;}
	
	public String getLoginResult(HashMap <String, String> loginInfo) {
		return mainMapper.getLoginResult(loginInfo);
	}
	
	public int insertActionSession(ActionSession actionSession) {
		return mainMapper.insertActionSession(actionSession);
	}
	
	public ActionSession getActionSessionByKey(HashMap <String, Object> info) {
		return mainMapper.getActionSessionByKey(info);
	}
	
	public int getUserSeqByLocale(String locale) {
		return mainMapper.getUserSeqByLocale(locale);
	}
	
	public int changePasswd(HashMap <String, String> loginInfo) {
		return mainMapper.changePasswd(loginInfo);
	}
}