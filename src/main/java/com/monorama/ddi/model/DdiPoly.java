package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "ȸ�� ����", description = "���̵�, �̸�, ��й�ȣ, �̸���, �ּ�, ���Գ�¥�� ���� Domain Class")
public class DdiPoly {
	
	@ApiModelProperty(example="������")
	private int seq;
	
	@ApiModelProperty(example="�๰ ��ȣ")
	private int drug_n;
	
	@ApiModelProperty(example="�ܹ��� �ڵ�")
	private String protein;
	
	@ApiModelProperty(example="�ܹ��� Ÿ��")
	private String type;
	
	@ApiModelProperty(example="�ܹ��� �̸�")
	private String polyname;
	
	@ApiModelProperty(example="����")
	private int version;
}
