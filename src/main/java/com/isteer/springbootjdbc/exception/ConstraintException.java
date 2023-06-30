package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class ConstraintException extends RuntimeException {

	private long Status;
	private String message;
	private List<String> exception;

	public ConstraintException(long Status, String message, List<String> exception) {
			super();
			this.Status = Status;
			this.message = message;
			this.exception = exception;
		}

}
