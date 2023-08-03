package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.isteer.springbootjdbc.MessageService;
import com.isteer.springbootjdbc.dao.RoleDaoImpl;
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@RestController
public class RoleController {
	
	private static final Logger auditlogger = LogManager.getLogger(RoleController.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: Role Controller";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: Role Controller";
	
	@Autowired
	private MessageService messageservice;
	
	@Autowired
	private RoleDaoImpl rDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("/role")
	public ResponseEntity<List<Role>> getAllRoles(){
		auditlogger.info(iLog,"All details",StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsDisplayedMessage());
		return new ResponseEntity<>(rDAO.getAll(),HttpStatus.OK);
		
	}
	
	@GetMapping("/role/{roleId}")
	public ResponseEntity<Role> getRoleById(@PathVariable long roleId) {
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, roleId) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for the role id " + roleId);
			auditlogger.warn(wLog, roleId,StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNotFoundMessage(), exception);

			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNotFoundMessage(), exception);
		} else {
			auditlogger.info(iLog,roleId ,StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsDisplayedMessage());
			return new ResponseEntity<>(rDAO.getById(roleId), HttpStatus.OK);
		}

	}
	
	@PostMapping("/role")
	public ResponseEntity<CustomRolePostResponse> saveRole(@RequestBody Role role){
		List<String> exceptions = rDAO.validateRoles(role);
		if (exceptions.isEmpty()) {
			auditlogger.info(iLog,"New Entity",StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsSavedMessage());
			return new ResponseEntity<>(rDAO.save(role),HttpStatus.CREATED);
		}
		else {
			auditlogger.warn(wLog,"New Entity",StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(),exceptions);
			throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getConstraintsInvalidMessage(), exceptions);
		}
		
	}
	
	@PutMapping("/role/{roleId}")
	public ResponseEntity<CustomRolePostResponse> update(@RequestBody Role role, @PathVariable long roleId) {
		
		if (rDAO.getById(roleId) != null) {

			List<String> exceptions = rDAO.validateRoles(role);

			if (exceptions.isEmpty()) {
				auditlogger.info(iLog,roleId,StatusCodes.SUCCESS.getStatusCode(),messageservice.getFieldValidatedMessage());
				return new ResponseEntity<>(rDAO.update(role, roleId), HttpStatus.OK);
			} else {
				auditlogger.warn(wLog,roleId,StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(), exceptions );
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),messageservice.getConstraintsInvalidMessage(), exceptions);
			}
		} else {
			auditlogger.warn(wLog,roleId,StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getNoContentToUpdateMessage(),"Id is not present" );
			return new ResponseEntity<>(rDAO.update(role, roleId), HttpStatus.NOT_FOUND);
		}

	}
	
	@DeleteMapping("/role/{roleId}")
	public ResponseEntity<CustomDeleteResponse> deleteById(@PathVariable long roleId) {
		
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, roleId) == 0) {
			
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			auditlogger.warn(wLog,roleId,StatusCodes.NOT_FOUND.getStatusCode(),messageservice.getNoContentToDeleteMessage(), exception );
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageservice.getNoContentToDeleteMessage(), exception);
		
		}
		else {
			auditlogger.warn(wLog,roleId ,StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsDeletedMessage());
			return new ResponseEntity<>(rDAO.delete(roleId),HttpStatus.OK);
		}
		
		
		
	}
	
	

}
