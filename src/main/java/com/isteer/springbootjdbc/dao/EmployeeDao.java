package com.isteer.springbootjdbc.dao;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface EmployeeDao {

	CustomPostResponse save(Employee employee);
	
	CustomPostResponse update(Employee employee, long id) throws SQLException;
	
	CustomDeleteResponse delete(long id);
	
	/*
	 * List<Employee> getAll();
	 * 
	 * Employee getById(long id);
	 */

	List<String> validateEmployee(@Valid Employee employee);

	List<Employee> getDataFromTables();

	List<Employee> getDataFromTablesUsingId(long id);

	//CustomPostResponse updateAndRetrieveEmployeesWithGroupBy(Employee employee,long id);

	

	
}
