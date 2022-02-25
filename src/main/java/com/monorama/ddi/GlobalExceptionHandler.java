package com.monorama.ddi;

import java.nio.charset.Charset;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.monorama.ddi.model.Message;
import com.monorama.ddi.exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity authException() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
	
	@ExceptionHandler(LoginFailException.class)
	public ResponseEntity loginFailException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Message> NotFoundException(){
		Message message = new Message();
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));


        message.setCode(StatusEnum.NOT_FOUND.getStatusCode());
        message.setMessage("ã�� �� ����");
        
        return new ResponseEntity<>(message, headers, StatusEnum.NOT_FOUND.getHttpStatus());
	}
}