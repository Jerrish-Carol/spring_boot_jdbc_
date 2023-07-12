package com.isteer.springbootjdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class AddressRowMapper implements RowMapper<List<Address>> {
	
	Address employee = new Address();

	@Override
	public List<Address> mapRow(ResultSet rs, int rowNum) throws SQLException {
		List<Address> addresses = new ArrayList<Address>();

		while (rs.next()) {

			Address address = new Address();
			address.setAddress_id(rs.getLong("address_id"));
			address.setEmployee_id(rs.getLong("employee_id"));
			address.setStreet(rs.getString("street"));
			address.setState(rs.getString("state"));
			address.setCity(rs.getString("city"));
			address.setCountry(rs.getString("country"));
			addresses.add(address);
		}
		return addresses;

	} 

}
