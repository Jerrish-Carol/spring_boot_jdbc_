package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

@Repository
public class AddressDaoImpl implements AddressDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public CustomPostResponse save(List<Address> addresses, Long id) {

		jdbcTemplate.batchUpdate(SqlQueries.INSERT_ADDRESS_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Address address = addresses.get(i);
				ps.setLong(1, address.getAddress_id());
				ps.setLong(2, id);
				ps.setString(3, address.getStreet());
				ps.setString(4, address.getCity());
				ps.setString(5, address.getState());
				ps.setString(6, address.getCountry());

			}

			@Override
			public int getBatchSize() {

				System.out.println(addresses.size());
				return addresses.size();
			}

		});
		return null;
	}

	public CustomPostResponse update(List<Address> addresses, Long id) {

		jdbcTemplate.batchUpdate(SqlQueries.UPDATE_ADDRESS_BY_ID_QUERY, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Address address = addresses.get(i);

				ps.setString(1, address.getStreet());
				ps.setString(2, address.getCity());
				ps.setString(3, address.getState());
				ps.setString(4, address.getCountry());
				ps.setLong(5, id);

			}

			@Override
			public int getBatchSize() {

				System.out.println(addresses.size());
				return addresses.size();
			}

		});
		return null;
	}
	
	public List<String> validateAddresses(Employee employee) {
        List<Address> addresses = employee.getAddresses();

        List<String> exception = new ArrayList<>();
        
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);

            if (address.getStreet()=="") {
            	exception.add("Street is required for Address " + (i + 1));
            }

            if (address.getCity()=="") {
            	exception.add("City is required for Address " + (i + 1));
            }
            
            if (address.getState()=="") {
            	exception.add("State is required for Address " + (i + 1));
            }
            
            if (address.getCountry()=="") {
            	exception.add("Country is required for Address " + (i + 1));
            }
        }
		return exception;
    }

}
