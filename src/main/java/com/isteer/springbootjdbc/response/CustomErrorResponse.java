package com.isteer.springbootjdbc.response;

import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CustomErrorResponse {

	private long statusCode;

	private String message;

	private List<String> exception;

	public CustomErrorResponse(long statusCode, String message, List<String> exception) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.exception = exception;
	}

}
