package com.isteer.springbootjdbc.exception;

import java.util.List;

public class DetailsNotProvidedException extends RuntimeException {

	private long Status;
	private String message;
	private List<String> exception;

	public DetailsNotProvidedException(long Status, String message, List<String> exception) {
		super();
		this.Status = Status;
		this.message = message;
		this.exception = exception;
	}

}