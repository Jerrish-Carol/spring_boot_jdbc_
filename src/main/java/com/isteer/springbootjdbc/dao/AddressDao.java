package com.isteer.springbootjdbc.dao;

import java.util.List;
import javax.validation.Valid;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;

public interface AddressDao {

	CustomPostResponse save(List<Address> addresses, Long id);
	
	CustomPostResponse update(List<Address> addresses, Long id);

	List<String> validateAddresses(@Valid Employee employee);

	List<Address> getAll();

	List<Address> getAddressById(long id);
	
	Address insert(Address address, long id) ;
}
