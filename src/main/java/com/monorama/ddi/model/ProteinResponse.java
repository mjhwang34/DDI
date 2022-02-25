package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class ProteinResponse {
	
	@ApiModelProperty(value="������ �ϴ� �๰: inhibitor �Ǵ� inducer", example="inhibitor")
	private String p;
	
	@ApiModelProperty(value="������ �޴� �๰: substrate", example="substrate")
	private String v;
	
	@ApiModelProperty(value="�ܹ��� �ڵ�", example="P33261")
	private String protein;
	
	@ApiModelProperty(value="�ܹ��� �̸�", example="Cytochrome P450 2C19")
	private String polyname;
}
