package com.isteer.springbootjdbc.controller;

import java.sql.SQLException;
import java.sql.Timestamp;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class EmployeeController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	private static final Logger auditlogger = LoggerFactory.getLogger(EmployeeController.class);
	
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
	

	@GetMapping(value="/employees", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeResult>> getData() {
		List<EmployeeResult> combinedData = eDAO.getDataFromTables();
		auditlogger.info("Data retrieved");
		return ResponseEntity.ok(combinedData);
	}
	
	
	@GetMapping(value = "/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeResult>> getDataUsingId(@PathVariable long employeeId) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, employeeId) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for id " + employeeId);
			auditlogger.warn("Data is not present and so not retrieved for ID : {0} Status Code :  {1} Mesage : {2}" ,employeeId,StatusCodes.NOT_FOUND.getStatusCode(),messageproperties.getNotFoundMessage());
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getNotFoundMessage(), exception);
		} else {
			logger.info("ID is present and data is retrieved");
			auditlogger.info("Data retrieved for ID : " + employeeId);

			List<EmployeeResult> combinedData = eDAO.getDataFromTablesUsingId(employeeId);
			return ResponseEntity.ok(combinedData);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {

		List<String> exceptions = eDAO.validateEmployee(employee);

		List<String> exception = aDAO.validateAddresses(employee);

		if (exceptions.isEmpty() && exception.isEmpty()) {
			auditlogger.info("No validation issues found in :" + employee.getName()+ " details.");
			return new ResponseEntity<>(eService.saveEmployeeWithAddresses(employee),
					HttpStatus.CREATED);
		} else if (!exception.isEmpty() && exceptions.isEmpty()) {
			auditlogger.warn("Data is not saved for : " + employee.getName()+ " Status Code :"+StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageproperties.getDetailsNotProvidedMessage());
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getDetailsNotProvidedMessage(), exception);
		} else {
			exceptions.addAll(exception);
			auditlogger.warn("Data is not saved for : " + employee.getName()+ " Status Code :"+StatusCodes.BAD_REQUEST.getStatusCode()+" Mesage :"+ messageproperties.getConstraintsInvalidMessage());
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
				auditlogger.info("Data is not saved for : " + employee.getName() + " Status Code :"+StatusCodes.BAD_REQUEST.getStatusCode()+" Mesage :"+ messageproperties.getConstraintsInvalidMessage());
				return new ResponseEntity<>(
						eService.updateAndRetrieveEmployeesWithGroupBy(employee, employeeId), HttpStatus.OK);
			} else {
				auditlogger.warn("Data is not saved for : " + employee.getName()+ " Status Code :"+StatusCodes.BAD_REQUEST.getStatusCode()+" Mesage :"+ messageproperties.getDetailsNotProvidedMessage());
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getDetailsNotProvidedMessage(), exception);
			}
			
		} else {
			auditlogger.warn("Data is not saved for : " + employee.getName()+ " Status Code :"+StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageproperties.getNotFoundMessage());
			return new ResponseEntity<>(eDAO.update(employee, employeeId), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int employeeId) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, employeeId) == 0) {

			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			logger.error("ID is not present, nothing to delete");
			auditlogger.warn("Data is not deleted for ID : " + employeeId + " Status Code :"+StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageproperties.getNoContentToDeleteMessage());
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getNoContentToDeleteMessage(), exception);

		} else {
			logger.info("ID is present, details deleted");
			auditlogger.info("Data is deleted for ID : " + employeeId + " Status Code :"+StatusCodes.SUCCESS.getStatusCode()+" Mesage :"+ messageproperties.getDetailsUpdatedMessage());
			return new ResponseEntity<>(eDAO.delete(employeeId), HttpStatus.OK);
		}

	}
	 

}
