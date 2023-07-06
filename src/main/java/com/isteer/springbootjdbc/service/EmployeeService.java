package com.isteer.springbootjdbc.service;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Transactional
	public CustomPostResponse saveEmployeeWithAddresses(Employee employee) {
		eDAO.save(employee);
		aDAO.save(employee.getAddresses(), employee.getId());
		return new CustomPostResponse(0, "DATA SAVED", eDAO.getById(employee.getId()));

	}
	
	@Transactional
	public CustomGetResponse getEmployeeWithAddressesandId(long id) {
		eDAO.getById(id);
		//aDAO.getById(id);
		return null;

	}
}
