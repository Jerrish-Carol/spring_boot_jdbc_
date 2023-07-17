package com.isteer.springbootjdbc.response;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class CustomDeleteResponse {
	
	private long statusCode;

	private String message;

	private List<String> statement;

	public CustomDeleteResponse(long statusCode, String message, List<String> statement) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.statement = statement;
	}

}
