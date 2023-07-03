package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
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
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import org.apache.log4j.Logger;    

@RestController
public class EmployeeController {

	private static Logger logger = Logger.getLogger(EmployeeController.class); 
	

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
			exception.add("The details are not present for id " + id);
			logger.error("ID is not present.");

			throw new DetailsNotFoundException(0, "NOT_FOUND", exception);
		} else {
			logger.info("ID is present and data is retrieved");
			return new ResponseEntity<CustomGetResponse>(eDAO.getById(id), HttpStatus.OK);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {

		List<String> exceptions = new ArrayList<>();

		if (employee.getName() == "" || employee.getEmail() == "" || employee.getDepartment() == ""
				|| employee.getGender() == "" || employee.getDob() == "") {
			exceptions.add("no field should be empty");
		}
		if (employee.getName().length() < 5) {
			exceptions.add("name must have atleast 5 characters");
		}
		if (!employee.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.]+[a-zA-Z]{2,}$")) {
			exceptions.add("email id is not right format");
		}
		if (!employee.getGender().matches("Male|Female|Other")) {
			exceptions.add("Gender must be specified as Male|Female|Other");
		}
		if (!employee.getDob().matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[0-2])-(19|20)\\d{2}$")) {
			exceptions.add("Date must be specified as dd-mm-yyyy");
		}
		if (exceptions.isEmpty()) {
			return new ResponseEntity<CustomPostResponse>(eDAO.save(employee), HttpStatus.CREATED);
		} else {
			throw new ConstraintException(0, "NOT VALID", exceptions);
		}

	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<CustomPostResponse> updateEmployee(@Valid @RequestBody Employee employee,@PathVariable int id) {
		
		if(eDAO.getById(id) != null) {
			List<String> exceptions = new ArrayList<>();

			if (employee.getName() == "" || employee.getEmail() == "" || employee.getDepartment() == ""
					|| employee.getGender() == "" || employee.getDob() == "") {
				exceptions.add("no field should be empty");
			}
			if (employee.getName().length() < 5) {
				exceptions.add("name must have atleast 5 characters");
			}
			if (!employee.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.]+[a-zA-Z]{2,}$")) {
				exceptions.add("email id is not right format");
			}
			if (!employee.getGender().matches("Male|Female|Other")) {
				exceptions.add("Gender must be specified as Male|Female|Other");
			}
			if (!employee.getDob().matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[0-2])-(19|20)\\d{2}$")) {
				exceptions.add("Date must be specified as dd-mm-yyyy");
			}
			if (exceptions.isEmpty()) {
				return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.OK);
			} else {
				throw new ConstraintException(0, "NOT VALID", exceptions);
			}
		}
		else {
			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.NOT_FOUND);
		}	
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int id) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 0) {
		
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			throw new DetailsNotFoundException(0, "NOT_DELETED", exception);
		
		}
		else {
			return new ResponseEntity<CustomDeleteResponse>(eDAO.delete(id),HttpStatus.OK);
		}
		
	}

}
