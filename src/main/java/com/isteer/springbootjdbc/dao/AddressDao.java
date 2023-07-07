package com.isteer.springbootjdbc.dao;

import java.util.List;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface AddressDao {

	CustomPostResponse save(List<Address> addresses, Long id);
	
	CustomPostResponse update(List<Address> addresses, Long id);

	List<String> validateAddresses(Employee employee);
}
