package com.isteer.springbootjdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class EmployeeRowMapper implements RowMapper<Employee> {
	
	Employee employee = new Employee();

	@Override
	public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		Employee employee = new Employee();
		employee.setId(rs.getLong("id"));
		employee.setName(rs.getString("name"));
		employee.setDob(rs.getString("dob"));
		employee.setGender(rs.getString("gender"));
		employee.setIs_account_locked(rs.getBoolean("is_account_locked"));
		employee.setIs_active(rs.getBoolean("is_active"));
		employee.setEmail(rs.getString("email"));
		employee.setDepartment(rs.getString("department"));
		employee.setRole_id(rs.getLong("role_id"));
		return employee;
	}
	

}
