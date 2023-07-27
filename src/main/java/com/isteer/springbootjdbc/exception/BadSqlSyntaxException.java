package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class BadSqlSyntaxException extends RuntimeException {

	private static long status;
	private final String message;
	private final List<String> exception;

	public BadSqlSyntaxException(long status, String message, List<String> exception) {
			super();
			this.status = status;
			this.message = message;
			this.exception = exception;
		}

}
