package com.isteer.springbootjdbc.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;

@Repository
public class EmployeeDaoImpl implements EmployeeDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public CustomPostResponse save(Employee employee) {
		
		if(jdbcTemplate.update("INSERT INTO tbl_employees(name, email, department) VALUES (? ,? ,?)", new Object[] {employee.getName(), employee.getEmail(), employee.getDepartment()})==1) {
			return new CustomPostResponse(1, "SAVED", employee);
		}
		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotFoundException(0, "NOT_SAVED", exception);
		}
		
	}

	@Override
	public CustomPostResponse update(Employee employee, int id) {
		if(employee.getName()!="" && employee.getEmail()!="" && employee.getDepartment()!="") {
			if(jdbcTemplate.update("UPDATE tbl_employees SET name=?, email=?, department=? WHERE id=?", new Object[] {employee.getName(), employee.getEmail(), employee.getDepartment(),id})==0) {
				return new CustomPostResponse(1, "UPDATED", employee);
			}
			else {
				return new CustomPostResponse(0, "NOT_UPDATED", employee);
			}
		}
		else {
			return new CustomPostResponse(0, "DETAILS ARE NOT PROVIDED", employee);
		}
	}

	@Override
	public int delete(int id) {
		return jdbcTemplate.update("DELETE FROM tbl_employees WHERE id=?", id);
	}

	@Override
	public List<Employee> getAll() {
		return jdbcTemplate.query("SELECT * FROM tbl_employees",new BeanPropertyRowMapper<Employee>(Employee.class));
	}

	@Override
	public Employee getById(int id) {
		return jdbcTemplate.queryForObject("SELECT * FROM tbl_employees WHERE id=?",new BeanPropertyRowMapper<Employee>(Employee.class),id);
		
	}

}
