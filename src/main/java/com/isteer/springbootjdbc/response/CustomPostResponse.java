package com.isteer.springbootjdbc.response;

import lombok.Data;
import com.isteer.springbootjdbc.model.Employee;

@Data
public class CustomPostResponse {

	private Employee employee;

	private long StatusCode;

	private String message;

	public CustomPostResponse(long StatusCode, String message, Employee employee) {
		super();
		this.StatusCode = StatusCode;
		this.message = message;
		this.employee = employee;
	}

}
