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
	
	//@ApiModelProperty(value="������")
	private int seq;
	
	//@ApiModelProperty(value="���� ko:1 en:2")
	private int user_seq;
	
	//@ApiModelProperty(value="����Ű")
	private String session_key;
	
	//@ApiModelProperty(value="���� ������Ʈ")
	private String ua;
	
	//@ApiModelProperty(value="ip�ּ�")
	private String ip;       
	
	//@ApiModelProperty(value="������ �ð�")
	private Timestamp create_time;
	
	
	public HashMap<String, String> getLoginData() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("session_key", this.session_key);
		return map;
		
	}
}
