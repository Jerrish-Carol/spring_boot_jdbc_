package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	private static Logger logger = Logger.getLogger(EmployeeDaoImpl.class);
	
	@Autowired
	private MessageSource messageSource;


	@Autowired
	private JdbcTemplate jdbcTemplate; // spring will create this and put in Ioc Container

	public CustomPostResponse save(Employee employee) {

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		try {
			if (jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_EMPLOYEE_QUERY,
						PreparedStatement.RETURN_GENERATED_KEYS);
				// queries are provided along with an indication whether to return auto
				// generated key value
				ps.setString(1, employee.getName());
				ps.setString(2, employee.getDob());
				ps.setString(3, employee.getGender());
				ps.setString(4, employee.getEmail());
				ps.setString(5, employee.getDepartment());

				return ps;
			}, keyHolder) == 1) {
				employee.setId(keyHolder.getKey().longValue());
			}
			return new CustomPostResponse(1, messageSource.getMessage("success.detailssaved", null, Locale.getDefault()), employee);
			
		}
		
		catch(DataAccessException e) {
			System.out.println(e);
		}
		return null;
	}

	@Override
	public CustomPostResponse update(Employee employee, long id) {

		if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY, new Object[] { employee.getName(),
				employee.getDob(), employee.getGender(), employee.getEmail(), employee.getDepartment(), id }) == 1) {

			Employee getemployees = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new ResultSetExtractor<Employee>() {

				public Employee extractData(ResultSet rs) throws SQLException {

					Employee employee = new Employee();
					while (rs.next()) {
						employee.setId(rs.getLong("id"));
						employee.setName(rs.getString("name"));
						employee.setEmail(rs.getString("email"));
						employee.setDob(rs.getString("dob"));
						employee.setGender(rs.getString("gender"));
						employee.setDepartment(rs.getString("department"));
						employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
						employee.setIsActive(rs.getBoolean("is_active"));

					}
					return employee;
				}
			}, id);

			return new CustomPostResponse(1,  messageSource.getMessage("success.detailsupdated", null, Locale.getDefault()), getemployees);
		} else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotProvidedException(0, messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
		}
	}

	@Override
	public CustomDeleteResponse delete(long id) {
		List<String> statement = new ArrayList<>();
		jdbcTemplate.update(SqlQueries.DELETE_EMPLOYEES_BY_ID_QUERY, id);
		jdbcTemplate.update(SqlQueries.DELETE_ADDRESS_BY_ID_QUERY, id);
		statement.add("Data in id " + id + " is deleted");
		return new CustomDeleteResponse(1, messageSource.getMessage("success.detailsdeleted", null, Locale.getDefault()), statement);
	}

	@Override
	public List<Employee> getAll() {
		List<Employee> e = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_QUERY, new ResultSetExtractor<List<Employee>>() {

			public List<Employee> extractData(ResultSet rs) throws SQLException {

				List<Employee> employees = new ArrayList<Employee>();

				while (rs.next()) {
					Employee employee = new Employee();
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
					employee.setAddresses(

							jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID_QUERY,
									new ResultSetExtractor<List<Address>>() {

										@Override
										public List<Address> extractData(ResultSet rset)
												throws SQLException, DataAccessException {
											List<Address> addresses = new ArrayList<Address>();

											while (rset.next()) {

												Address address = new Address();
												address.setAddress_id(rset.getLong("address_id"));
												address.setEmployee_id(rset.getLong("employee_id"));
												address.setStreet(rset.getString("street"));
												address.setState(rset.getString("state"));
												address.setCity(rset.getString("city"));
												address.setCountry(rset.getString("country"));
												addresses.add(address);
											}
											return addresses;

										}
									}, employee.getId()));
					employees.add(employee);

				}
				return employees;
			}
		});

		return e;
	}

	@Override
	public Employee getById(long id) {

		Employee e = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new ResultSetExtractor<Employee>() {

			public Employee extractData(ResultSet rs) throws SQLException {

				Employee employee = new Employee();
				while (rs.next()) {
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
					employee.setAddresses(

							jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID_QUERY,
									new ResultSetExtractor<List<Address>>() {

										@Override
										public List<Address> extractData(ResultSet rset)
												throws SQLException, DataAccessException {
											List<Address> addresses = new ArrayList<Address>();
											System.out.println(rset);
											while (rset.next()) {
												Address address = new Address();
												address.setAddress_id(rset.getLong("address_id"));
												address.setEmployee_id(id);
												address.setStreet(rset.getString("street"));
												address.setState(rset.getString("state"));
												address.setCity(rset.getString("city"));
												address.setCountry(rset.getString("country"));
												addresses.add(address);
											}
											return addresses;

										}
									}, id));

				}
				return employee;
			}
		}, id);

		return e;
	}
	
	public List<String> validateEmployee(Employee employee) {
       
        List<String> exception = new ArrayList<>();

		if (employee.getName() == "" || employee.getEmail() == "" || employee.getDepartment() == ""
				|| employee.getGender() == "" || employee.getDob() == "" || employee.getAddresses() == null) {
			exception.add("no field should be empty");
			logger.error("no field should be empty");
		}
		if (employee.getName().length() < 5) {
			exception.add("name must have atleast 5 characters");
			logger.error("name must have atleast 5 characters");
		}
		if (!employee.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.]+[a-zA-Z]{2,}$")) {
			exception.add("email id is not right format");
			logger.error("email id is not right format");
		}
		if (!employee.getGender().matches("Male|Female|Other")) {
			exception.add("Gender must be specified as Male|Female|Other");
			logger.error("Gender must be specified as Male|Female|Other");
		}
		if (!employee.getDob().matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[0-2])-(19|20)\\d{2}$")) {
			exception.add("Date must be specified as dd-mm-yyyy");
			logger.error("Date must be specified as dd-mm-yyyy");
		}
		return exception;
    }
}
