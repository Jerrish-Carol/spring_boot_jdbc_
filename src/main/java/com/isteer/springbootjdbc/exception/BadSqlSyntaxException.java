package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class BadSqlSyntaxException extends RuntimeException {

	private long status;
	private String message;
	private List<String> exception;

	public BadSqlSyntaxException(long status, String message, List<String> exception) {
			super();
			this.status = status;
			this.message = message;
			this.exception = exception;
		}

}
