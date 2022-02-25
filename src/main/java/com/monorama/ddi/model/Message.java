package com.monorama.ddi.model;

import com.monorama.ddi.StatusEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("응답 메세지")
public class Message {
	
	@ApiModelProperty(value="응답코드", example="200")
	private String code;
	
	@ApiModelProperty(value="응답 메세지", example="정상 응답")
    private String message;
	
	@ApiModelProperty(value="응답 데이터")
    private Object data;

    public Message() {
        this.code = StatusEnum.OK.getStatusCode();
        this.data = null;
        this.message = "정상 응답";
    }
    
}