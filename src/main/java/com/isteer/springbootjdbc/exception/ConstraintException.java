package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ConstraintException extends RuntimeException {

	private final long status;
	private final String message;
	private final List<String> exception;

	public ConstraintException(long status, String message, List<String> exception) {
			super();
			this.status = status;
			this.message = message;
			this.exception = exception;
		}

}
