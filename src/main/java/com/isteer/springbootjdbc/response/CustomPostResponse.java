package com.isteer.springbootjdbc.response;

import lombok.Data;

import java.util.List;
import com.isteer.springbootjdbc.model.EmployeeResult;

@Data
public class CustomPostResponse {

	private List<EmployeeResult> employee;

	private long statusCode;

	private String message;

	public CustomPostResponse(long statusCode, String message, List<EmployeeResult> employeeRowMapper) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.employee = employeeRowMapper;
	}

}
