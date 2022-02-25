package com.monorama.ddi.model;

import com.monorama.ddi.StatusEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("���� �޼���")
public class Message {
	
	@ApiModelProperty(value="�����ڵ�", example="200")
	private String code;
	
	@ApiModelProperty(value="���� �޼���", example="���� ����")
    private String message;
	
	@ApiModelProperty(value="���� ������")
    private Object data;

    public Message() {
        this.code = StatusEnum.OK.getStatusCode();
        this.data = null;
        this.message = "���� ����";
    }
    
}