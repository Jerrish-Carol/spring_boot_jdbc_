package com.isteer.springbootjdbc.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.isteer.springbootjdbc.response.CustomErrorResponse;
import javax.validation.ConstraintViolationException;
import org.springframework.jdbc.BadSqlGrammarException;


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
	
	@ExceptionHandler(value = { DetailsNotProvidedException.class })
	public ResponseEntity<Object> handleDetailsNotProvidedException(DetailsNotProvidedException exception) {
		long StatusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(StatusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}
	
	
	@ExceptionHandler(value = { ConstraintException.class })
	public ResponseEntity<Object> handleConstraintException(ConstraintException exception) {
		long StatusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(StatusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);
	}
	
	@Override
	@ExceptionHandler(value = { ConstraintViolationException.class })
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<String> exceptions = new ArrayList<>();
		exception.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			exceptions.add(fieldName + " " + message);
		});
		CustomErrorResponse customResponse = new CustomErrorResponse(0, "BAD REQUEST", exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);

	}
	
	@Override
	@ExceptionHandler(value = { MethodNotAllowedException.class })
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status,
			WebRequest request) {

		List<String> exceptions = new ArrayList<>();

		exceptions.add(exception.getLocalizedMessage());

		CustomErrorResponse customResponse = new CustomErrorResponse(0, PAGE_NOT_FOUND_LOG_CATEGORY, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.BAD_REQUEST);
	}
	

	@ExceptionHandler(value = { SqlSyntaxException.class })
	public ResponseEntity<Object> handleSqlSyntaxException(SqlSyntaxException exception) {
		long StatusCode = exception.getStatus();
		List<String> exceptions = exception.getException();
		String message = exception.getMessage();

		CustomErrorResponse customResponse = new CustomErrorResponse(StatusCode, message, exceptions);

		return new ResponseEntity<>(customResponse, HttpStatus.NOT_FOUND);

	}
	
}
