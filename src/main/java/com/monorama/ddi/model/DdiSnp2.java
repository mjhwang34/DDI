package com.monorama.ddi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel
@Getter
@Setter
public class DdiSnp2 {
	
	@ApiModelProperty(value="시퀀스", example="84")
	private int seq;
	
	@ApiModelProperty(value="Snp2 코드", example="641")
	private String code;
	
	@ApiModelProperty(value="단백질 코드", example="P04035")
	private String protein;
	
	@ApiModelProperty(value="단백질 이름", example="3-hydroxy-3-methylglutaryl-coenzyme A reductase")
	private String protein_name;
	
	@ApiModelProperty(value="genotype", example="HMGCR")
	private String genotype;
	
	@ApiModelProperty(value="rsname", example="rs17244841")
	private String rsname;
	
	@ApiModelProperty(value="allele", example="")
	private String allele;
	
	@ApiModelProperty(value="changev", example="T Allele")
	private String changev;
	
	@ApiModelProperty(value="설명", example="Patients with this genotype have a lesser reduction in LDL cholesterol with simvastatin.")
	private String description;
	
	@ApiModelProperty(value="레퍼런스", example="A")
	private String reference;
	
	@ApiModelProperty(value="ref_freq", example="0.979033")
	private String ref_freq;
	
	@ApiModelProperty(value="alt_freq", example="T:0.0209668,")
	private String alt_freq;
	
	@ApiModelProperty(value="버전", example="0")
	private int version;
}
