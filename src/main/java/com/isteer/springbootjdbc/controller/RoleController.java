package com.isteer.springbootjdbc.controller;

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

import com.isteer.springbootjdbc.dao.RoleDao;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.service.RoleService;

@RestController
public class RoleController {
	
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
	public ResponseEntity<Role> getRoleById(){
		return new ResponseEntity<Role>(rDAO.getById(),HttpStatus.OK);
		
	}
	
	@PostMapping("/role")
	public ResponseEntity<CustomRolePostResponse> saveRole(){
		return new ResponseEntity<CustomRolePostResponse>(rDAO.save(),HttpStatus.OK);
		
	}
	
	@PutMapping("/role/{role_id}")
	public ResponseEntity<CustomRolePostResponse> update(@RequestBody Role role,@PathVariable long role_id){
		return new ResponseEntity<CustomRolePostResponse>(rDAO.update(role,role_id),HttpStatus.OK);
		
	}
	
	@DeleteMapping("/role/{role_id}")
	public ResponseEntity<CustomDeleteResponse> deleteById() {
		return new ResponseEntity<CustomDeleteResponse>(rDAO.delete(),HttpStatus.OK);
	}
	
	

}
