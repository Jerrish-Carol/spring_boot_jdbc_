package com.isteer.springbootjdbc.response;

import lombok.Data;

import java.util.List;

import com.isteer.springbootjdbc.model.Employee;

@Data
public class CustomGetResponse {

	private List<Employee> employee;

	public CustomGetResponse(List<Employee> employee) {
		this.employee = employee;
	}
}
