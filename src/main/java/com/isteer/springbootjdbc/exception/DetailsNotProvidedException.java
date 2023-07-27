package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class DetailsNotProvidedException extends RuntimeException {

	private final long status;
	private final String message;
	private final List<String> exception;

	public DetailsNotProvidedException(long status, String message, List<String> exception) {
		super();
		this.status = status;
		this.message = message;
		this.exception = exception;
	}

}