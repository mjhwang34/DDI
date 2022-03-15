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
			this.ref_0 = "FDA ��ǰ�������� ������ ����Դϴ�.";
		    this.ref_1 = "�ΰ����� ������� �Դϴ�.";
		    this.detail_gt = prepetratorName+"�� "+victimName+"�� AUC�� "+value_p+"%��ŭ ������Ŵ.";
		    this.detail_eq = prepetratorName+"�� "+victimName+"�� AUC�� ������ ���� ����.";
		    this.detail_lt = prepetratorName+"�� "+victimName+"�� AUC�� "+value_p+"%��ŭ ������Ŵ.";
		}
		else if("en".equals(locale)) {
			this.ref_0 = "This is the result extracted from the FDA drug label.";
		    this.ref_1 = "This is the result of DDIP-NN model prediction.";
		    this.detail_gt = prepetratorName+" increases"+victimName+"\'s AUC by "+value+"-fold.";
		    this.detail_eq = prepetratorName+" does not affect"+victimName+"\'s AUC";
		    this.detail_lt = prepetratorName+" decreases "+victimName+"\'s AUC by "+value+"-fold.";
		}
		/*
		this.ko_ref_0 = "FDA ��ǰ�������� ������ ����Դϴ�.";
	    this.ko_ref_1 = "�ΰ����� ������� �Դϴ�.";
	    this.ko_detail_gt = prepetratorName+"��"+victimName+"�� AUC��"+value_p+"%��ŭ ������Ŵ.";
	    this.ko_detail_eq = prepetratorName+"��"+victimName+"�� AUC�� ������ ���� ����.";
	    this.ko_detail_lt = prepetratorName+"��"+victimName+"�� AUC��"+value_p+"%��ŭ ������Ŵ.";
	    this.en_ref_0 = "This is the result extracted from the FDA drug label.";
	    this.en_ref_1 = "This is the result of DDIP-NN model prediction.";
	    this.en_detail_gt = prepetratorName+"increase"+victimName+"\'s AUC by"+value+"-fold.";
	    this.en_detail_eq = prepetratorName+"does not affect"+victimName+"\'s AUC";
	    this.en_detail_lt = prepetratorName+"increase"+victimName+"\'s AUC by"+value+"-fold.";*/
	}

}
