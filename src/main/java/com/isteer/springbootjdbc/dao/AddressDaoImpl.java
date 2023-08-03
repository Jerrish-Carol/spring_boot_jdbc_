package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.MessageService;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.VariableDeclaration;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@Repository
public class AddressDaoImpl implements AddressDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MessageService messageservice;
	
	private static final Logger auditlogger = LogManager.getLogger(AddressDaoImpl.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: Address DAO layer";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: Address DAO layer";
	

	public Address insert(Address address, long id) {

		try {

			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

			if (jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_ADDRESS_QUERY,
						Statement.RETURN_GENERATED_KEYS);

				ps.setLong(1, address.getAddressId());
				ps.setLong(2, id);
				ps.setString(3, address.getStreet());
				ps.setString(4, address.getCity());
				ps.setString(5, address.getStreet());
				ps.setString(6, address.getCountry());

				return ps;
			}, keyHolder) == 1) {
				address.setAddressId(keyHolder.getKey().longValue());
			}
		} catch (NullPointerException nullexceptions) {

			List<String> exceptions = new ArrayList<>();
			exceptions.add(nullexceptions.getMessage());
			auditlogger.warn(wLog, address.getAddressId(),StatusCodes.CONFLICT.getStatusCode(), messageservice.getDuplicateKeyMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(), messageservice.getDuplicateKeyMessage(), exceptions);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, address.getAddressId(),StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

		auditlogger.info(iLog, address.getAddressId(),StatusCodes.SUCCESS.getStatusCode(),
				messageservice.getDetailsSavedMessage());
		return address;

	}

	public CustomPostResponse save(List<Address> addresses, Long id) {

		try {

			jdbcTemplate.batchUpdate(SqlQueries.INSERT_ADDRESS_QUERY, new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Address address = addresses.get(i);
					ps.setLong(1, address.getAddressId());
					ps.setLong(2, id);
					ps.setString(3, address.getStreet());
					ps.setString(4, address.getCity());
					ps.setString(5, address.getState());
					ps.setString(6, address.getCountry());

				}

				@Override
				public int getBatchSize() {
					return addresses.size();
				}

			});
			auditlogger.info(iLog, id ,StatusCodes.SUCCESS.getStatusCode(),
					messageservice.getDetailsSavedMessage());
			return null;
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, id ,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}
	}

	public CustomPostResponse update(List<Address> addresses, Long id) {
		try {
			jdbcTemplate.batchUpdate(SqlQueries.UPDATE_ADDRESS_BY_ID_QUERY, new BatchPreparedStatementSetter() {

			
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					Address address = addresses.get(i);
					ps.setLong(1, id);
					ps.setString(2, address.getStreet());
					ps.setString(3, address.getCity());
					ps.setString(4, address.getState());
					ps.setString(5, address.getCountry());
					ps.setLong(6, address.getAddressId());

				}

				@Override
				public int getBatchSize() {

					return addresses.size();
				}

			});
			auditlogger.info(iLog, id ,StatusCodes.SUCCESS.getStatusCode(),
					messageservice.getDetailsUpdatedMessage());
			return null;
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, id ,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

	}


	public List<String> validateAddresses(Employee employee) {
		List<Address> addresses = employee.getAddresses();

		List<String> exception = new ArrayList<>();

		for (int i = 0; i < addresses.size(); i++) {
			Address address = addresses.get(i);

			if (address.getStreet().equals("")) {
				exception.add("Street is required for Address " + (i + 1));
			}

			if (address.getCity().equals("")) {
				exception.add("City is required for Address " + (i + 1));
			}

			if (address.getState().equals("")) {
				exception.add("State is required for Address " + (i + 1));
			}

			if (address.getCountry().equals("")) {
				exception.add("Country is required for Address " + (i + 1));
			}
		}
		return exception;
	}

	@Override
	public List<Address> getAll() {

		return jdbcTemplate.query(SqlQueries.GET_ADDRESS_QUERY, new ResultSetExtractor<List<Address>>() {

			public List<Address> extractData(ResultSet rs) throws SQLException {
				List<Address> addresses = new ArrayList<>();

				try {

					while (rs.next()) {
						Address address = new Address();
						address.setAddressId(rs.getLong(VariableDeclaration.ADDRESS_ID));
						address.setEmployeeId(rs.getLong(VariableDeclaration.EMPLOYEE_ID));
						address.setStreet(rs.getString(VariableDeclaration.STREET));
						address.setCity(rs.getString(VariableDeclaration.CITY));
						address.setState(rs.getString(VariableDeclaration.STATE));
						address.setCountry(rs.getString(VariableDeclaration.COUNTRY));
						addresses.add(address);

					}
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					auditlogger.warn(wLog, "All Addresses" ,StatusCodes.BAD_REQUEST.getStatusCode(),
							messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
							messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
				}
				auditlogger.info(iLog, "All Addresses",StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsDisplayedMessage());
				return addresses;
			}

		});

	}

	@Override
	public List<Address> getAddressById(long id) {

		try {

			return jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID_QUERY, new ResultSetExtractor<List<Address>>() {

				public List<Address> extractData(ResultSet rs) throws SQLException {
					List<Address> addresses = new ArrayList<>();

					while (rs.next()) {
						Address address = new Address();
						address.setAddressId(rs.getLong(VariableDeclaration.ADDRESS_ID));
						address.setEmployeeId(rs.getLong(VariableDeclaration.EMPLOYEE_ID));
						address.setStreet(rs.getString(VariableDeclaration.STREET));
						address.setCity(rs.getString(VariableDeclaration.CITY));
						address.setState(rs.getString(VariableDeclaration.STATE));
						address.setCountry(rs.getString(VariableDeclaration.COUNTRY));

					}

					auditlogger.info(iLog, id ,StatusCodes.SUCCESS.getStatusCode(),
							messageservice.getDetailsDisplayedMessage());
					return addresses;
				}

			}, id);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog,id,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

	}
}
