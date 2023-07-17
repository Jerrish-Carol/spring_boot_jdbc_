package com.isteer.springbootjdbc.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.isteer.springbootjdbc.MessageProperties;
import com.isteer.springbootjdbc.dao.AddressDaoImpl;
import com.isteer.springbootjdbc.dao.EmployeeDaoImpl;
import com.isteer.springbootjdbc.dao.RoleDaoImpl;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@Service
public class EmployeeService {
	

	@Autowired
	private MessageProperties messageproperties;

	@Autowired
	private EmployeeDaoImpl eDAO;

	@Autowired
	private AddressDaoImpl aDAO;

	@Autowired
	private RoleDaoImpl rDAO;


	@Transactional
	public CustomPostResponse saveEmployeeWithAddresses(Employee employee) {
		eDAO.save(employee);
		aDAO.save(employee.getAddresses(), employee.getEmployeeId()); // success.detailssaved
		rDAO.save(employee.getRole());
		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
				messageproperties.getDetailsSavedMessage(),
				eDAO.getDataFromTablesUsingId(employee.getEmployeeId()));
	}

	@Transactional
	public CustomPostResponse updateAndRetrieveEmployeesWithGroupBy(Employee employee, long id) { // success.detailsupdated

		try {

			eDAO.update(employee, employee.getEmployeeId());
			
			if(employee.getAddresses().size() > aDAO.getAddressById(id).size()) {
				for (int i = 0; i < employee.getAddresses().size(); i++) {
		            Address address = employee.getAddresses().get(i);
		            if(address.getAddressId() == 0) {
		            	aDAO.insert(address, employee.getEmployeeId());
		            }
				}
			}
//			else if(employee.getAddresses().size() < aDAO.getAddressById(id).size()) {
//				for (int i = 0; i < employee.getAddresses().size(); i++) {
//		            Address address = employee.getAddresses().get(i);
//		            if(!aDAO.getAddressById(id).contains(address.getAddressId())) {
//		            	aDAO.insert(address, employee.getEmployeeId());
//		            }
//				}
//			}
			else {
				aDAO.update(employee.getAddresses(), employee.getEmployeeId());
			}
		
			rDAO.update(employee.getRole(), employee.getRoleId());

		} catch (SQLException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageproperties.getBadSqlSyntaxErrorMessage(), exceptions);
		}

		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),messageproperties.getDetailsUpdatedMessage(),
				/*messageSource.getMessage(messageproperties.getDetailsUpdatedMessage(), null, Locale.getDefault()),*/
				eDAO.getDataFromTablesUsingId(id));

	}
}
