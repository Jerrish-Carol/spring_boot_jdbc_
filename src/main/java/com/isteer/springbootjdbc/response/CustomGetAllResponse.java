package com.isteer.springbootjdbc.response;

import java.util.List;

import lombok.Data;
import com.isteer.springbootjdbc.model.Employee;

@Data
public class CustomGetAllResponse {

	private List<Employee> content;

	public CustomGetAllResponse(List<Employee> employee) {
		this.content = employee;
	}
}
