package com.isteer.springbootjdbc.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.isteer.springbootjdbc.dao.EmployeeDaoImpl;
import com.isteer.springbootjdbc.dao.RoleDaoImpl;

@Service
public class RoleService {
	
	@Autowired
	private RoleDaoImpl rDAO;
	
	@Autowired
	private EmployeeDaoImpl eDAO;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messageSource;
	
	

	
	
}
