package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.isteer.springbootjdbc.MessageProperties;
import com.isteer.springbootjdbc.dao.RoleDao;
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.service.RoleService;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@RestController
public class RoleController {
	
	private static final Logger logger = LogManager.getLogger(RoleController.class);
	
	private static final Logger auditlogger = LogManager.getLogger(RoleController.class);
	
	@Autowired
	private MessageProperties messageproperties;
	
	@Autowired
	private RoleService rService;
	
	@Autowired
	private RoleDao rDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("/role")
	public ResponseEntity<List<Role>> getAllRoles(){
		auditlogger.info("Data retrieved");
		return new ResponseEntity<>(rDAO.getAll(),HttpStatus.OK);
		
	}
	
	@GetMapping("/role/{role_id}")
	public ResponseEntity<Role> getRoleById(@PathVariable long roleId) {
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, roleId) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for the role id " + roleId);
			auditlogger.warn("Data is not present and so not retrieved for ID : " + roleId + " Status Code :" +StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageproperties.getNotFoundMessage());

			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getNotFoundMessage(), exception);
		} else {
			auditlogger.warn("Data is present and so retrieved for ID : " + roleId + " Status Code :" +StatusCodes.SUCCESS.getStatusCode()+" Mesage :"+ messageproperties.getDetailsDisplayedMessage());
			return new ResponseEntity<>(rDAO.getById(roleId), HttpStatus.OK);
		}

	}
	
	@PostMapping("/role")
	public ResponseEntity<CustomRolePostResponse> saveRole(@RequestBody Role role){
		List<String> exceptions = rDAO.validateRoles(role);
		if (exceptions.isEmpty()) {
			auditlogger.info("No validation issues found in details.");
			return new ResponseEntity<>(rDAO.save(role),HttpStatus.CREATED);
		}
		else {
			auditlogger.warn("Data is not saved for role provided " + "Status Code :" + StatusCodes.BAD_REQUEST.getStatusCode() + " Mesage :" + messageproperties.getConstraintsInvalidMessage());
			throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getConstraintsInvalidMessage(), exceptions);
		}
		
	}
	
	@PutMapping("/role/{role_id}")
	public ResponseEntity<CustomRolePostResponse> update(@RequestBody Role role, @PathVariable long roleId) {
		
		if (rDAO.getById(roleId) != null) {

			List<String> exceptions = rDAO.validateRoles(role);

			if (exceptions.isEmpty()) {
				auditlogger.info("No validation issues found in details.");
				return new ResponseEntity<>(rDAO.update(role, roleId), HttpStatus.OK);
			} else {
				auditlogger.warn("Data is not saved for role provided " + "Status Code :" + StatusCodes.BAD_REQUEST.getStatusCode() + " Mesage :" + messageproperties.getConstraintsInvalidMessage());
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),messageproperties.getDetailsNotProvidedMessage(), exceptions);
			}
		} else {
			auditlogger.warn("Data is  " + " Status Code :"+StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageproperties.getNoContentToDeleteMessage());
			return new ResponseEntity<>(rDAO.update(role, roleId), HttpStatus.NOT_FOUND);
		}

	}
	
	@DeleteMapping("/role/{role_id}")
	public ResponseEntity<CustomDeleteResponse> deleteById(@PathVariable long roleId) {
		
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, roleId) == 0) {
			
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			auditlogger.warn("Data is not deleted for ID : " + roleId + " Status Code :"+StatusCodes.NOT_FOUND.getStatusCode()+" Mesage :"+ messageproperties.getNoContentToDeleteMessage());
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageproperties.getNoContentToDeleteMessage(), exception);
		
		}
		else {
			auditlogger.warn("Data is deleted for ID : " + roleId + " Status Code :"+StatusCodes.SUCCESS.getStatusCode()+" Mesage :"+ messageproperties.getDetailsDeletedMessage());
			return new ResponseEntity<>(rDAO.delete(roleId),HttpStatus.OK);
		}
		
		
		
	}
	
	

}
