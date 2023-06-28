package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.isteer.springbootjdbc.dao.*;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

import jakarta.validation.Valid;


@RestController
public class EmployeeController {

	@Autowired
	private EmployeeDAO eDAO;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getEmployees() { 
		return new ResponseEntity<List<Employee>>(eDAO.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<CustomGetResponse> getEmployeeById(@PathVariable int id) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for id "+id);
			throw new DetailsNotFoundException(0, "NOT_FOUND", exception);
		} else {
			return new ResponseEntity<CustomGetResponse>(eDAO.getById(id), HttpStatus.OK);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@Valid @RequestBody Employee employee) {

		if (employee.getName()!="" && employee.getEmail()!="" && employee.getDepartment()!="" && employee.getGender()!="" && employee.getDob()!="") {
			return new ResponseEntity<CustomPostResponse>(eDAO.save(employee), HttpStatus.CREATED) ;
		}
		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotFoundException(0, "NOT_SAVED", exception);
		}
			 
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<CustomPostResponse> updateEmployee(@Valid @RequestBody Employee employee,@PathVariable int id) {
		
		if(eDAO.getById(id) != null) {
			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.NOT_FOUND);
		}	
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int id) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 1) {
		
			return new ResponseEntity<CustomDeleteResponse>(eDAO.delete(id),HttpStatus.OK);
			
		}
		else {
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			throw new DetailsNotFoundException(0, "NOT_DELETED", exception);
		}
		
	}

}
