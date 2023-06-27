package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.isteer.springbootjdbc.dao.EmployeeDAO;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeDAO eDAO;

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getEmployees() { 
		return new ResponseEntity<List<Employee>>(eDAO.getAll(), HttpStatus.OK);
	}
	
	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable int id) {
		 return new ResponseEntity<Employee>(eDAO.getById(id),HttpStatus.OK);
	}
	
	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {
		
		if(employee.getName()!="" && employee.getEmail()!="" && employee.getDepartment()!="") {
			return new ResponseEntity<CustomPostResponse>(eDAO.save(employee),HttpStatus.CREATED) ;
		}
		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotFoundException(0, "NOT_SAVED", exception);
		}
			 
	}
	
	@PutMapping("/employees/{id}")
	public ResponseEntity<CustomPostResponse> updateEmployee(@RequestBody Employee employee,@PathVariable int id) {
		
		if(eDAO.getById(id) != null) {
			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.OK);
		}
		else {
			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee,id),HttpStatus.NOT_FOUND);
		}	
	}
	
	@DeleteMapping("/employees/{id}")
	public String deleteEmployeeById(@PathVariable int id) {
		
			return "No of rows removed from the database : " + eDAO.delete(id) ;
		
	}

}
