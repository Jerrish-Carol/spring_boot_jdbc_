package com.isteer.springbootjdbc.response;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class CustomDeleteResponse {
	
	private long StatusCode;

	private String message;

	private List<String> statement;

	public CustomDeleteResponse(long StatusCode, String message, List<String> statement) {
		super();
		this.StatusCode = StatusCode;
		this.message = message;
		this.statement = statement;
	}

}
