package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@SuppressWarnings("serial")
@Data
public class SqlSyntaxException extends RuntimeException{
	//if we don't extend exception or runtime exception then -> Type mismatch: cannot convert from Class<SqlSyntaxException> to Class<? extends Throwable>
	private final long status;
	private final String message;
	private final List<String> exception;

	public SqlSyntaxException(long status, String message, List<String> exception) {
		super();
		this.status = status;
		this.message = message;
		this.exception = exception;
	}


}
