package com.monorama.ddi;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum StatusEnum {
	OK("200", "OK", HttpStatus.OK),
    NOT_FOUND("404", "NOT_FOUND", HttpStatus.NOT_FOUND),
    LOGIN_FAIL("401", "LOGIN_FAIL", HttpStatus.UNAUTHORIZED)
    ;

    String statusCode;
    String statusMessage;
    HttpStatus httpStatus;

    StatusEnum(String statusCode, String statusMessage, HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.httpStatus = httpStatus;
    }

}

