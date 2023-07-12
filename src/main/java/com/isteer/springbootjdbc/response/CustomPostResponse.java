package com.isteer.springbootjdbc.response;

import lombok.Data;

import java.util.List;

import com.isteer.springbootjdbc.model.Employee;

@Data
public class CustomPostResponse {

	private List<Employee> employee;

	private long StatusCode;

	private String message;

	public CustomPostResponse(long StatusCode, String message, List<Employee> employeeRowMapper) {
		super();
		this.StatusCode = StatusCode;
		this.message = message;
		this.employee = employeeRowMapper;
	}

}
