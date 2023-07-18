package com.isteer.springbootjdbc.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.isteer.springbootjdbc.MessageProperties;
import com.isteer.springbootjdbc.dao.*;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.EmployeeResult;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.service.EmployeeService;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
public class EmployeeController {
	
	private static final Logger logger = LogManager.getLogger(EmployeeController.class);
	
	@Autowired
	private MessageProperties messageproperties;

	@Autowired
	private EmployeeService eService;

	@Autowired
	private EmployeeDao eDAO;

	@Autowired
	private AddressDao aDAO;

	@Autowired
	private RoleDao rDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	@GetMapping(value="/employees",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeResult>> getData() {
		List<EmployeeResult> combinedData = eDAO.getDataFromTables();
		return ResponseEntity.ok(combinedData);
	}
	
	
	@GetMapping(value = "/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeResult>> getDataUsingId(@PathVariable long employeeId) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, employeeId) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for id " + employeeId);
			logger.error("ID is not present.");

			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getNotFoundMessage(), exception);
		} else {
			logger.info("ID is present and data is retrieved");

			List<EmployeeResult> combinedData = eDAO.getDataFromTablesUsingId(employeeId);
			return ResponseEntity.ok(combinedData);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {

		List<String> exceptions = eDAO.validateEmployee(employee);

		List<String> exception = aDAO.validateAddresses(employee);

		if (exceptions.isEmpty() && exception.isEmpty()) {
			return new ResponseEntity<>(eService.saveEmployeeWithAddresses(employee),
					HttpStatus.CREATED);
		} else if (!exception.isEmpty() && exceptions.isEmpty()) {
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getDetailsNotProvidedMessage(), exception);
		} else {
			exceptions.addAll(exception);
			throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),messageproperties.getConstraintsInvalidMessage(),exceptions);
		}

	}

	@PutMapping("/employees/{employeeId}")
	public ResponseEntity<CustomPostResponse> updateEmployee(@Valid @RequestBody Employee employee,
			@PathVariable long employeeId) throws SQLException {

		if (employee.getEmployeeId() == employeeId) {

			List<String> employeeexceptions = eDAO.validateEmployee(employee);

			List<String> addressexception = aDAO.validateAddresses(employee);

			List<String> roleexception = rDAO.validateRole(employee);

			List<String> exception = new ArrayList<>();

			exception.addAll(employeeexceptions);
			exception.addAll(addressexception);
			exception.addAll(roleexception);

			if (exception.isEmpty()) {
				return new ResponseEntity<>(
						eService.updateAndRetrieveEmployeesWithGroupBy(employee, employeeId), HttpStatus.OK);
			} else {

				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getDetailsNotProvidedMessage(), exception);
			}
			
		} else {
			return new ResponseEntity<>(eDAO.update(employee, employeeId), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int employeeId) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, employeeId) == 0) {

			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			logger.error("ID is not present, nothing to delete");
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getNoContentToDeleteMessage(), exception);

		} else {
			logger.info("ID is present, details deleted");
			return new ResponseEntity<>(eDAO.delete(employeeId), HttpStatus.OK);
		}

	}

	 

}
