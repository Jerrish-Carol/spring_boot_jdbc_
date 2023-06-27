package com.isteer.springbootjdbc.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.isteer.springbootjdbc.response.CustomErrorResponse;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = { DetailsNotFoundException.class })
	public ResponseEntity<Object> handleDetailsNotFoundException(DetailsNotFoundException exception) {
		long StatusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(StatusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}
	
	

}
