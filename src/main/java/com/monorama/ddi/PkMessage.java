package com.monorama.ddi;

import lombok.Getter;

@Getter
public class PkMessage {
	String ref_0;
	String ref_1;
	String detail_gt;
	String detail_eq;
	String detail_lt;

	/*
	String ko_ref_0;
	String ko_ref_1;
	String ko_detail_gt;
	String ko_detail_eq;
	String ko_detail_lt;
	String en_ref_0;
	String en_ref_1;
	String en_detail_gt;
	String en_detail_eq;
	String en_detail_lt;*/
	
	public PkMessage(String locale, String prepetratorName, String victimName, String value_p, String value) {
		if("ko".equals(locale)) {
			this.ref_0 = "FDA 약품설명서에서 추출한 결과입니다.";
		    this.ref_1 = "인공지능 예측결과 입니다.";
		    this.detail_gt = prepetratorName+"은 "+victimName+"의 AUC를 "+value_p+"%만큼 증가시킴.";
		    this.detail_eq = prepetratorName+"은 "+victimName+"의 AUC에 영향을 주지 않음.";
		    this.detail_lt = prepetratorName+"은 "+victimName+"의 AUC를 "+value_p+"%만큼 증가시킴.";
		}
		else if("en".equals(locale)) {
			this.ref_0 = "This is the result extracted from the FDA drug label.";
		    this.ref_1 = "This is the result of DDIP-NN model prediction.";
		    this.detail_gt = prepetratorName+" increases"+victimName+"\'s AUC by "+value+"-fold.";
		    this.detail_eq = prepetratorName+" does not affect"+victimName+"\'s AUC";
		    this.detail_lt = prepetratorName+" decreases "+victimName+"\'s AUC by "+value+"-fold.";
		}
		/*
		this.ko_ref_0 = "FDA 약품설명서에서 추출한 결과입니다.";
	    this.ko_ref_1 = "인공지능 예측결과 입니다.";
	    this.ko_detail_gt = prepetratorName+"은"+victimName+"의 AUC를"+value_p+"%만큼 증가시킴.";
	    this.ko_detail_eq = prepetratorName+"은"+victimName+"의 AUC에 영향을 주지 않음.";
	    this.ko_detail_lt = prepetratorName+"은"+victimName+"의 AUC를"+value_p+"%만큼 증가시킴.";
	    this.en_ref_0 = "This is the result extracted from the FDA drug label.";
	    this.en_ref_1 = "This is the result of DDIP-NN model prediction.";
	    this.en_detail_gt = prepetratorName+"increase"+victimName+"\'s AUC by"+value+"-fold.";
	    this.en_detail_eq = prepetratorName+"does not affect"+victimName+"\'s AUC";
	    this.en_detail_lt = prepetratorName+"increase"+victimName+"\'s AUC by"+value+"-fold.";*/
	}

}
