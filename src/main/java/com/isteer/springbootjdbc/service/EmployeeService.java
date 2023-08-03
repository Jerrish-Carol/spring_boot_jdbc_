package com.isteer.springbootjdbc.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.isteer.springbootjdbc.MessageService;
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
	private MessageService messageservice;
	
	private static final Logger auditlogger = LogManager.getLogger(EmployeeService.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: Employee Service Class";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: Employee Service Class";


	@Transactional
	public CustomPostResponse saveEmployeeWithAddresses(Employee employee) {
		
		try {
			eDAO.save(employee);
			aDAO.save(employee.getAddresses(), employee.getEmployeeId()); // success.detailssaved
			rDAO.save(employee.getRole());
			
			auditlogger.info(iLog, employee.getEmployeeId() ,StatusCodes.SUCCESS.getStatusCode(),
     				messageservice.getDetailsSavedMessage());
			return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
					messageservice.getDetailsSavedMessage(),
					eDAO.getDataFromTablesUsingId(employee.getEmployeeId()));
		}
		catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, employee.getEmployeeId(),StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exception);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}
		
	}

	@Transactional
	public CustomPostResponse updateAndRetrieveEmployeesWithGroupBy(Employee employee, long id) { // success.detailsupdated

		try {

			eDAO.update(employee, employee.getEmployeeId());
				
			aDAO.update(employee.getAddresses(), id);
		
			rDAO.update(employee.getRole(), employee.getRoleId());

		} catch (SQLException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, employee.getEmployeeId(),StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

		auditlogger.info(wLog,employee.getEmployeeId() ,StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsUpdatedMessage());
		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),messageservice.getDetailsUpdatedMessage(),
				eDAO.getDataFromTablesUsingId(id));

	}
}
