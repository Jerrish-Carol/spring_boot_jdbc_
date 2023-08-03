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
import com.isteer.springbootjdbc.MessageService;
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
	
	private static final Logger auditlogger = LoggerFactory.getLogger(EmployeeController.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: Employee Controller ";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: Employee Controller ";
	
	@Autowired
	private MessageService messageservice;

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
		auditlogger.info(iLog, "All Employees ", StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsDisplayedMessage());
		return ResponseEntity.ok(combinedData);
	}
	
	
	@GetMapping(value = "/employees/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<EmployeeResult>> getDataUsingId(@PathVariable long employeeId) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, employeeId) == 0) {
			List<String> exception = new ArrayList<>();
			auditlogger.warn(wLog,employeeId,StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getNotFoundMessage(), exception);
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNotFoundMessage(), exception);
		} else {
			auditlogger.info(iLog,employeeId, StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsDisplayedMessage());

			List<EmployeeResult> combinedData = eDAO.getDataFromTablesUsingId(employeeId);
			return ResponseEntity.ok(combinedData);
		}
	}

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {

		List<String> exceptions = eDAO.validateEmployee(employee);

		List<String> exception = aDAO.validateAddresses(employee);

		if (exceptions.isEmpty() && exception.isEmpty()) {
			auditlogger.info(iLog,employee.getName(),StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsSavedMessage());
			return new ResponseEntity<>(eService.saveEmployeeWithAddresses(employee),
					HttpStatus.CREATED);
		} else if (!exception.isEmpty() && exceptions.isEmpty()) {
			auditlogger.warn(wLog,employee.getName(),StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getDetailsNotProvidedMessage(), exception );
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getDetailsNotProvidedMessage(), exception);
		} else {
			exceptions.addAll(exception);
			auditlogger.warn(wLog,employee.getName(),StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(), exception);
			throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(),exceptions);
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
				auditlogger.info(iLog,employee.getName() + " Status Code :"+StatusCodes.SUCCESS.getStatusCode()+" Mesage :"+ messageservice.getDetailsUpdatedMessage());
				return new ResponseEntity<>(
						eService.updateAndRetrieveEmployeesWithGroupBy(employee, employeeId), HttpStatus.OK);
			} else {
				auditlogger.warn(wLog,employee.getName(),StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getDetailsNotProvidedMessage(), exception );
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getDetailsNotProvidedMessage(), exception);
			}
			
		} else {
			auditlogger.warn(wLog,employee.getName(),StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNotFoundMessage(), "Employee details not found" );
			return new ResponseEntity<>(eDAO.update(employee, employeeId), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/employees/{employeeId}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int employeeId) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, employeeId) == 0) {

			List<String> exception = new ArrayList<>();
			auditlogger.warn(wLog,employeeId,StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getNoContentToDeleteMessage(), exception);
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNoContentToDeleteMessage(), exception);

		} else {
			auditlogger.info(iLog,employeeId,StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsUpdatedMessage());
			return new ResponseEntity<>(eDAO.delete(employeeId), HttpStatus.OK);
		}

	}
	 
}
