package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//@ApiModel
@Getter
@Setter
public class Dfi {
	
	//@ApiModelProperty(example="������")
	private int seq;
	
	//@ApiModelProperty(example="pmid")
	private int pmid;
	
	//@ApiModelProperty(example="�๰ ������")
	private int drug_seq;
	
	//@ApiModelProperty(example="���� ������")
	private int food_seq;
	
	//@ApiModelProperty(example="�ٽ� ����")
	private String key_sents;
	
	//@ApiModelProperty(example="����")
	private String foods;
	
	//@ApiModelProperty(example="�๰")
	private String drugs;
	
	//@ApiModelProperty(example="evidence level")
	private String evidence_level;
}