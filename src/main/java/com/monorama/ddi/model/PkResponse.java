package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class PkResponse {
	
	@ApiModelProperty(value="������", example="2")
	private int seq;
	
	@ApiModelProperty(value="������ �ϴ� �๰ ��ȣ", example="303")
	private int prepetrator;
	
	@ApiModelProperty(value="������ �޴� �๰ ��ȣ", example="1017")
	private int victim;
	
	@ApiModelProperty(value="fold", example="1.00717")
	private float fold;
	
	@ApiModelProperty(value="���۷���", example="1")
	private int reference;
	
	@ApiModelProperty(value="����", example="0")
	private int version;
	
	@ApiModelProperty(value="������ �ϴ� �๰�� �̸�", example="Ertapenem")
	private String prepetrator_name;
	
	@ApiModelProperty(value="������ �޴� �๰�� �̸�", example="Minocyline")
	private String victim_name;
	
	@ApiModelProperty(value="ref", example="�ΰ����� ������� �Դϴ�.")
	private String ref;
	
	@ApiModelProperty(value="value", example="0.01")
	private String value;
	
	@ApiModelProperty(value="value�� �ۼ�Ƽ��", example="1")
	private int value_p;
	
	@ApiModelProperty(value="�� ����", example="Ertapenem�� Minocycline�� AUC�� 1%��ŭ ������Ŵ.")
	private String detail;
}

