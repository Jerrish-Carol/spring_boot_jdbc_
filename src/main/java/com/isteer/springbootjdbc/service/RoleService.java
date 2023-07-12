package com.isteer.springbootjdbc.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.springbootjdbc.dao.EmployeeDaoImpl;
import com.isteer.springbootjdbc.dao.RoleDaoImpl;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomRoleGetResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

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
