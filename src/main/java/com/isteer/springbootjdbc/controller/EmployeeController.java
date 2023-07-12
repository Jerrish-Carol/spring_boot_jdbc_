package com.isteer.springbootjdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.service.EmployeeService;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

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
	private RoleDao rDAO;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	/*
	 * @GetMapping("/employees") public ResponseEntity<List<Employee>>
	 * getEmployees() { logger.info("Data is retrieved"); return new
	 * ResponseEntity<List<Employee>>(eDAO.getAll(), HttpStatus.OK); }
	 */

	/*
	 * @GetMapping("/employees/{id}") public ResponseEntity<CustomGetResponse>
	 * getEmployeeWithAddressById(@PathVariable long id) {
	 * 
	 * if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY,
	 * Long.class, id) == 0) { List<String> exception = new ArrayList<>();
	 * exception.add("The details are not present for id " + id);
	 * logger.error("ID is not present.");
	 * 
	 * throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(),
	 * messageSource.getMessage("error.notfound", null, Locale.getDefault()) ,
	 * exception); } else { logger.info("ID is present and data is retrieved");
	 * return new
	 * ResponseEntity<CustomGetResponse>(eService.getEmployeeWithAddressesandId(id),
	 * HttpStatus.OK); } }
	 * 
	 * @GetMapping("/employee/{id}") public ResponseEntity<CustomGetResponse>
	 * getEmployeeWithRoleById(@PathVariable long id) {
	 * 
	 * if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY,
	 * Long.class, id) == 0) { List<String> exception = new ArrayList<>();
	 * exception.add("The details are not present for id " + id);
	 * logger.error("ID is not present.");
	 * 
	 * throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(),
	 * messageSource.getMessage("error.notfound", null, Locale.getDefault()) ,
	 * exception); } else { logger.info("ID is present and data is retrieved");
	 * return new
	 * ResponseEntity<CustomGetResponse>(eService.getEmployeeWithRoleById(id),
	 * HttpStatus.OK); } }
	 */

	@PostMapping("/employees")
	public ResponseEntity<CustomPostResponse> saveEmployee(@RequestBody Employee employee) {

	   List<String> exceptions = eDAO.validateEmployee(employee);
		
	   List<String> exception = aDAO.validateAddresses(employee);
	   
		if (exceptions.isEmpty() && exception.isEmpty()) {
			return new ResponseEntity<CustomPostResponse>(eService.saveEmployeeWithAddresses(employee),HttpStatus.CREATED);
		}
		else if(!exception.isEmpty() && exceptions.isEmpty()) {
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
		}
		else {
			exceptions.addAll(exception);
			throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageSource.getMessage("error.constrainsinvalid", null, Locale.getDefault()), exceptions);
		}

	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<CustomPostResponse> updateEmployee(@Valid @RequestBody Employee employee,@PathVariable long id) throws SQLException {
		
		if(employee.getId()==id) {
			
			List<String> employeeexceptions = eDAO.validateEmployee(employee);
			
			List<String> addressexception = aDAO.validateAddresses(employee);
		
			List<String> roleexception = rDAO.validateRole(employee);
			
			List<String> exception = new ArrayList<>();
			
			exception.addAll(employeeexceptions);
			exception.addAll(addressexception);
			exception.addAll(roleexception);
			
			if (exception.isEmpty()) {
				return new ResponseEntity<CustomPostResponse>(eService.updateAndRetrieveEmployeesWithGroupBy(employee,id), HttpStatus.OK);
			} else {
				
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),
						messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
			}
		} else {
			return new ResponseEntity<CustomPostResponse>(eDAO.update(employee, id), HttpStatus.NOT_FOUND);
		}
}
		//employee = eDAO.getDataFromTablesUsingId(id);
//		try {
//
//			if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY,employee.getName(), employee.getDob(), employee.getGender(), employee.getIs_active(), employee.getIs_account_locked(),
//	                employee.getEmail(), employee.getDepartment(), employee.getRole_id(), id) >= 1) {
//
//				Employee getemployees = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY,
//						new ResultSetExtractor<Employee>() {
//
//							public Employee extractData(ResultSet rs) {
//
//								Employee employee = new Employee();
//								try {
//									while (rs.next()) {
//										employee.setId(rs.getLong("id"));
//										employee.setName(rs.getString("name"));
//										employee.setDob(rs.getString("dob"));
//										employee.setGender(rs.getString("gender"));
//										employee.setIs_account_locked(rs.getBoolean("is_account_locked"));
//										employee.setIs_active(rs.getBoolean("is_active"));
//										employee.setEmail(rs.getString("email"));
//										employee.setDepartment(rs.getString("department"));
//										employee.setRole_id(rs.getLong("role_id"));
//										
//									}
//								} catch (SQLException exception) {
//									List<String> exceptions = new ArrayList<>();
//									exceptions.add(exception.getMessage());
//									throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
//											messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()),
//											exceptions);
//								}
//								return employee;
//							}
//						}, id);
//				
//				//System.out.println(getemployees);
//
//				return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
//						messageSource.getMessage("success.detailsupdated", null, Locale.getDefault()), eDAO.getDataFromTablesUsingId(id));
//
//			} else {
//				List<String> exception = new ArrayList<>();
//				exception.add("Provide all details required");
//				throw new DetailsNotProvidedException(StatusCodes.BAD_REQUEST.getStatusCode(),
//						messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
//			}
//
//		} catch (DataAccessException exception) {
//			List<String> exceptions = new ArrayList<>();
//			exceptions.add(exception.getMessage());
//			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
//					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
//
//		}
		
	
	
	@DeleteMapping("/employees/{id}")
	public ResponseEntity<CustomDeleteResponse> deleteEmployeeById(@PathVariable int id) {

		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 0) {
		
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			logger.error("ID is not present, nothing to delete");
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageSource.getMessage("error.nocontenttodelete", null, Locale.getDefault()), exception);
		
		}
		else {
			logger.info("ID is present, details deleted");
			return new ResponseEntity<CustomDeleteResponse>(eDAO.delete(id),HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/allemployeedetails")
    public ResponseEntity<List<Employee>> getData() {
        List<Employee> combinedData = eDAO.getDataFromTables();
        return ResponseEntity.ok(combinedData);
    }
	
	@GetMapping("/allemployeedetails/{id}")
    public ResponseEntity<List<Employee>> getDataUsingId(@PathVariable long id) {
		
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ID_IS_PRESENT_QUERY, Long.class, id) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for id " + id);
			logger.error("ID is not present.");
		
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageSource.getMessage("error.notfound", null, Locale.getDefault()) , exception);
		} else {
			logger.info("ID is present and data is retrieved");
			
        List<Employee> combinedData = eDAO.getDataFromTablesUsingId(id);
        return ResponseEntity.ok(combinedData);
		}
    }

}
