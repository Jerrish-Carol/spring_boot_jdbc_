package com.isteer.springbootjdbc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.Logger;
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
	
	private static Logger logger = Logger.getLogger(RoleController.class); 
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private RoleService rService;
	
	@Autowired
	private RoleDao rDAO;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@GetMapping("/role")
	public ResponseEntity<List<Role>> getAllRoles(){
		return new ResponseEntity<List<Role>>(rDAO.getAll(),HttpStatus.OK);
		
	}
	
	@GetMapping("/role/{role_id}")
	public ResponseEntity<Role> getRoleById(@PathVariable long role_id) {
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, role_id) == 0) {
			List<String> exception = new ArrayList<>();
			exception.add("The details are not present for the role id " + role_id);
			logger.error("Role ID is not present.");

			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(),
					messageSource.getMessage("error.notfound", null, Locale.getDefault()), exception);
		} else {
			return new ResponseEntity<Role>(rDAO.getById(role_id), HttpStatus.OK);
		}

	}
	
	@PostMapping("/role")
	public ResponseEntity<CustomRolePostResponse> saveRole(@RequestBody Role role){
		List<String> exceptions = rDAO.validateRoles(role);
		if (exceptions.isEmpty()) {
			return new ResponseEntity<CustomRolePostResponse>(rDAO.save(role),HttpStatus.CREATED);
		}
		else {
			throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(), messageSource.getMessage("error.constrainsinvalid", null, Locale.getDefault()), exceptions);
		}
		
	}
	
	@PutMapping("/role/{role_id}")
	public ResponseEntity<CustomRolePostResponse> update(@RequestBody Role role, @PathVariable long role_id) {
		
		if (rDAO.getById(role_id) != null) {

			List<String> exceptions = rDAO.validateRoles(role);

			if (exceptions.isEmpty()) {
				return new ResponseEntity<CustomRolePostResponse>(rDAO.update(role, role_id), HttpStatus.OK);
			} else {
				throw new ConstraintException(StatusCodes.BAD_REQUEST.getStatusCode(),
						messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exceptions);
			}
		} else {
			return new ResponseEntity<CustomRolePostResponse>(rDAO.update(role, role_id), HttpStatus.NOT_FOUND);
		}

	}
	
	@DeleteMapping("/role/{role_id}")
	public ResponseEntity<CustomDeleteResponse> deleteById(@PathVariable long role_id) {
		
		if (jdbcTemplate.queryForObject(SqlQueries.CHECK_ROLE_ID_IS_PRESENT_QUERY, Long.class, role_id) == 0) {
			
			List<String> exception = new ArrayList<>();
			exception.add("Not data present to delete");
			logger.error("ID is not present, nothing to delete");
			throw new DetailsNotFoundException(StatusCodes.NOT_FOUND.getStatusCode(), messageSource.getMessage("error.nocontenttodelete", null, Locale.getDefault()), exception);
		
		}
		else {
			logger.info("ID is present, details deleted");
			return new ResponseEntity<CustomDeleteResponse>(rDAO.delete(role_id),HttpStatus.OK);
		}
		
		
		
	}
	
	

}
