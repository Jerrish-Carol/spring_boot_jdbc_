package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class SqlSyntaxException extends RuntimeException{
	//if we don't extend exception or runtime exception then -> Type mismatch: cannot convert from Class<SqlSyntaxException> to Class<? extends Throwable>
	private long status;
	private String message;
	private List<String> exception;

	public SqlSyntaxException(long status, String message, List<String> exception) {
		super();
		this.status = status;
		this.message = message;
		this.exception = exception;
	}


}
