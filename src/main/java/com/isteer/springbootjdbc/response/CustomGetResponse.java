package com.isteer.springbootjdbc.response;

import lombok.Data;
import com.isteer.springbootjdbc.model.Employee;

@Data
public class CustomGetResponse {

	private Employee employee;

	public CustomGetResponse(Employee employee) {
		this.employee = employee;
	}
}
