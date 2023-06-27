package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class DetailsNotFoundException extends RuntimeException {

	private long Status;
	private String message;
	private List<String> exception;

	public DetailsNotFoundException(long Status, String message, List<String> exception) {
		super();
		this.Status = Status;
		this.message = message;
		this.exception = exception;
	}

}
