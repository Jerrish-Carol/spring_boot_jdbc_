package com.isteer.springbootjdbc.dao;

import java.util.List;

import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface EmployeeDao {

	CustomPostResponse save(Employee employee);
	
	CustomPostResponse update(Employee employee, long id);
	
	CustomDeleteResponse delete(long id);
	
	List<Employee> getAll();
	
	Employee getById(long id);

	List<String> validateEmployee(Employee employee);

	
}
