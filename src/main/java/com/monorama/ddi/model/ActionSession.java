package com.monorama.ddi.model;

import java.sql.Timestamp;
import java.util.HashMap;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//@ApiModel
@Getter
@Setter
public class ActionSession {
	
	//@ApiModelProperty(value="시퀀스")
	private int seq;
	
	//@ApiModelProperty(value="언어설정 ko:1 en:2")
	private int user_seq;
	
	//@ApiModelProperty(value="세션키")
	private String session_key;
	
	//@ApiModelProperty(value="유저 에이전트")
	private String ua;
	
	//@ApiModelProperty(value="ip주소")
	private String ip;       
	
	//@ApiModelProperty(value="생성된 시간")
	private Timestamp create_time;
	
	
	public HashMap<String, String> getLoginData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("session_key", this.session_key);
		return map;
		
	}
}
