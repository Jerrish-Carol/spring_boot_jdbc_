package com.isteer.springbootjdbc.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.isteer.springbootjdbc.dao.AddressDaoImpl;
import com.isteer.springbootjdbc.dao.EmployeeDaoImpl;
import com.isteer.springbootjdbc.dao.RoleDaoImpl;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDaoImpl eDAO;

	@Autowired
	private AddressDaoImpl aDAO;
	
	@Autowired
	private RoleDaoImpl rDAO;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageSource messageSource;
	
	@Transactional
	public CustomPostResponse saveEmployeeWithAddresses(Employee employee) {
		eDAO.save(employee);
		aDAO.save(employee.getAddresses(), employee.getId()); //success.detailssaved
		rDAO.save(employee.getRole());
		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(), messageSource.getMessage("success.detailssaved", null, Locale.getDefault()), eDAO.getDataFromTablesUsingId(employee.getId()));
	}
	
	@Transactional
	public CustomPostResponse updateAndRetrieveEmployeesWithGroupBy(Employee employee, long id) { //success.detailsupdated
		
		 try {
			 
			eDAO.update(employee, employee.getId());
			aDAO.update(employee.getAddresses(), employee.getId());
    		rDAO.update(employee.getRole(), employee.getRole_id());
    		
		} catch (SQLException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageSource.getMessage("error.badsqlsyntax",null, Locale.getDefault()), exceptions);
		}
		 
		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),messageSource.getMessage("success.detailsupdated", null, Locale.getDefault()), eDAO.getDataFromTablesUsingId(id));

	}
}
