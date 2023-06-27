package com.isteer.springbootjdbc.response;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomErrorResponse {

	private long StatusCode;

	private String message;

	private List<String> exception;

	public CustomErrorResponse(long StatusCode, String message, List<String> exception) {
		super();
		this.StatusCode = StatusCode;
		this.message = message;
		this.exception = exception;
	}

}
