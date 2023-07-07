package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.service.EmployeeService;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import org.apache.log4j.Logger;    

@RestController
public class EmployeeController {

	private static Logger logger = Logger.getLogger(EmployeeController.class); 
	
	@Autowired
	private EmployeeService eService;
	
	@Autowired
	private EmployeeDao eDAO;
	
	@Autowired
	private AddressDao aDAO;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getEmployees() {
		logger.info("Data is retrieved");
		return new ResponseEntity<List<Employee>>(eDAO.getAll(), HttpStatus.OK);
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<CustomGetResponse> getEmployeeById(@PathVariable long id) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for id " + id);
			logger.error("ID is not present.");
		
			throw new DetailsNotFoundException(0, messageSource.getMessage("error.notfound", null, Locale.getDefault()) , exception);
		} else {
			logger.info("ID is present and data is retrieved");
			return new ResponseEntity<CustomGetResponse>(eService.getEmployeeWithAddressesandId(id), HttpStatus.OK);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {

	   List<String> exceptions = eDAO.validateEmployee(employee);
		
	   List<String> exception = aDAO.validateAddresses(employee);
	   
		if (exceptions.isEmpty() && exception.isEmpty()) {
			return new ResponseEntity<CustomPostResponse>(eService.saveEmployeeWithAddresses(employee),HttpStatus.CREATED);
		}
		else if(!exception.isEmpty() && exceptions.isEmpty()) {
			throw new DetailsNotFoundException(0, messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
		}
		else {
			exceptions.addAll(exception);
			throw new ConstraintException(0, messageSource.getMessage("error.constrainsinvalid", null, Locale.getDefault()), exceptions);
		}

	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<CustomPostResponse> updateEmployee(@Valid @RequestBody Employee employee,@PathVariable long id) {
		employee = eDAO.getById(id);

		if (eDAO.getById(id) != null) {
		
			List<String> exceptions = eDAO.validateEmployee(employee);

			List<String> exception = aDAO.validateAddresses(employee);

			if (exceptions.isEmpty() && exception.isEmpty()) {
				return new ResponseEntity<CustomPostResponse>(eService.updateEmployeeWithAddressesandId(employee),
						HttpStatus.OK);
			} else if (!exception.isEmpty() && exceptions.isEmpty()) {
				throw new DetailsNotFoundException(0, messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
			} else {
				exceptions.addAll(exception);
				throw new ConstraintException(0, messageSource.getMessage("error.constrainsinvalid", null, Locale.getDefault()), exceptions);
			}

		} else {

			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.NOT_FOUND);
		}	
	}
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int id) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 0) {
		
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			logger.error("ID is not present, nothing to delete");
			throw new DetailsNotFoundException(0, messageSource.getMessage("error.nocontenttodelete", null, Locale.getDefault()), exception);
		
		}
		else {
			logger.info("ID is present, details deleted");
			return new ResponseEntity<CustomDeleteResponse>(eDAO.delete(id),HttpStatus.OK);
		}
		
	}

}
