package com.isteer.springbootjdbc.response;

import lombok.Data;

import org.springframework.jdbc.core.RowMapper;

import com.isteer.springbootjdbc.model.Employee;

@Data
public class CustomPostResponse {

	private Employee employee;

	private long StatusCode;

	private String message;

	public CustomPostResponse(long StatusCode, String message, Employee employeeRowMapper) {
		super();
		this.StatusCode = StatusCode;
		this.message = message;
		this.employee = employeeRowMapper;
	}

}
