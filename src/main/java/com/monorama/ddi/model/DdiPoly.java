package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "회원 정보", description = "아이디, 이름, 비밀번호, 이메일, 주소, 가입날짜를 가진 Domain Class")
public class DdiPoly {
	
	@ApiModelProperty(example="시퀀스")
	private int seq;
	
	@ApiModelProperty(example="약물 번호")
	private int drug_n;
	
	@ApiModelProperty(example="단백질 코드")
	private String protein;
	
	@ApiModelProperty(example="단백질 타입")
	private String type;
	
	@ApiModelProperty(example="단백질 이름")
	private String polyname;
	
	@ApiModelProperty(example="버전")
	private int version;
}
