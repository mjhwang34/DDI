package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class ProteinResponse {
	
	@ApiModelProperty(value="공격을 하는 약물: inhibitor 또는 inducer", example="inhibitor")
	private String p;
	
	@ApiModelProperty(value="영향을 받는 약물: substrate", example="substrate")
	private String v;
	
	@ApiModelProperty(value="단백질 코드", example="P33261")
	private String protein;
	
	@ApiModelProperty(value="단백질 이름", example="Cytochrome P450 2C19")
	private String polyname;
}
