package com.isteer.springbootjdbc.exception;

import java.util.List;

import lombok.Data;

@Data
public class SqlSyntaxException extends RuntimeException{
	//if we don't extend exception or runtime exception then -> Type mismatch: cannot convert from Class<SqlSyntaxException> to Class<? extends Throwable>
	private long Status;
	private String message;
	private List<String> exception;

	public SqlSyntaxException(long Status, String message, List<String> exception) {
		super();
		this.Status = Status;
		this.message = message;
		this.exception = exception;
	}


}
