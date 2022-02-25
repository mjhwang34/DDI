package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

//이건 안씀 hashmap으로 대체함
//@ApiModel
@Getter
@Setter
public class DfiAndPaper {
	
	//@ApiModelProperty(example="시퀀스")
	private int seq;
	
	//@ApiModelProperty(example="pmid")
	private int pmid;
	
	//@ApiModelProperty(example="약물 시퀀스")
	private int drug_seq;
	
	//@ApiModelProperty(example="음식 시퀀스")
	private int food_seq;
	
	//@ApiModelProperty(example="핵심 문장")
	private String key_sents;
	
	//@ApiModelProperty(example="음식")
	private String foods;
	
	//@ApiModelProperty(example="약물")
	private String drugs;
	
	//@ApiModelProperty(example="evidence level")
	private String evidence_level;
	
	//@ApiModelProperty(example="제목")
	private String title;
	
	//@ApiModelProperty(example="abstractText")
	private String abstractText;
}
