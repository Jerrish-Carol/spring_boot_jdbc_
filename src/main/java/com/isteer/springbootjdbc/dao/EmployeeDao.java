package com.isteer.springbootjdbc.dao;

import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.EmployeeResult;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface EmployeeDao {

	CustomPostResponse save(Employee employee);
	
	CustomPostResponse update(Employee employee, long id) throws SQLException;
	
	CustomDeleteResponse delete(long id);
	
	List<String> validateEmployee(@Valid Employee employee);

	List<EmployeeResult> getDataFromTables();

	List<EmployeeResult> getDataFromTablesUsingId(long id);


	
}
