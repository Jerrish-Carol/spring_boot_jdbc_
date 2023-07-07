package com.isteer.springbootjdbc.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.isteer.springbootjdbc.dao.AddressDaoImpl;
import com.isteer.springbootjdbc.dao.EmployeeDaoImpl;
import com.isteer.springbootjdbc.exception.ConstraintException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeDaoImpl eDAO;

	@Autowired
	private AddressDaoImpl aDAO;
	
	@Autowired
	private MessageSource messageSource;
	
	@Transactional
	public CustomPostResponse saveEmployeeWithAddresses(Employee employee) {
		eDAO.save(employee);
		aDAO.save(employee.getAddresses(), employee.getId()); //success.detailssaved
		return new CustomPostResponse(0, messageSource.getMessage("success.detailssaved", null, Locale.getDefault()), eDAO.getById(employee.getId()));
	}
	
	@Transactional
	public CustomGetResponse getEmployeeWithAddressesandId(long id) {
		eDAO.getById(id);
		return new CustomGetResponse(eDAO.getById(id));

	}
	
	@Transactional
	public CustomPostResponse updateEmployeeWithAddressesandId(Employee employee) { //success.detailsupdated
		eDAO.update(employee, employee.getId());
		aDAO.update(employee.getAddresses(), employee.getId());
		return new CustomPostResponse(0,messageSource.getMessage("success.detailsupdated", null, Locale.getDefault()), eDAO.getById(employee.getId()));

	}
}
