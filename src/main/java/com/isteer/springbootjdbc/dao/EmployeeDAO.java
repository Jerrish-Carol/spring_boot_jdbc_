package com.isteer.springbootjdbc.dao;

import java.util.List;

import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface EmployeeDAO {

	CustomPostResponse save(Employee employee);
	
	CustomPostResponse update(Employee employee, int id);
	
	int delete(int id);
	
	List<Employee> getAll();
	
	Employee getById(int id);
}
