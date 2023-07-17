package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.springbootjdbc.MessageProperties;
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.EmployeeResult;
import com.isteer.springbootjdbc.model.VariableDeclaration;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {

	private static Logger logger = Logger.getLogger(EmployeeDaoImpl.class);
	
	@Autowired
	private MessageProperties messageproperties;

	@Autowired
	private JdbcTemplate jdbcTemplate; // spring will create this and put in Ioc Container

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public CustomPostResponse save(Employee employee) {

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		try {
			if (jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_EMPLOYEE_QUERY,
						Statement.RETURN_GENERATED_KEYS);
				// queries are provided along with an indication whether to return auto
				// generated key value
				ps.setString(1, employee.getName());
				ps.setString(2, employee.getDob());
				ps.setString(3, employee.getGender());
				ps.setString(4, employee.getEmail());
				ps.setString(5, employee.getDepartment());
				ps.setLong(6, employee.getRoleId());

				return ps;
			}, keyHolder) == 1) {
				employee.setEmployeeId(keyHolder.getKey().longValue());
			}
		} catch(NullPointerException nullexceptions) {
			
			List<String> exceptions = new ArrayList<>();
			exceptions.add(nullexceptions.getMessage());
			throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(), messageproperties.getDuplicateKeyMessage(), exceptions);
		
		} catch (DuplicateKeyException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(), messageproperties.getDuplicateKeyMessage(),exceptions);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(),exceptions);

		}

		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(), messageproperties.getDetailsSavedMessage(),
				getDataFromTablesUsingId(employee.getEmployeeId()));
	}

	@Override
	public CustomPostResponse update(Employee employee, long employeeId) throws SQLException {

		try {

			if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY, employee.getName(), employee.getDob(),
					employee.getGender(), employee.getIsActive(), employee.getIsAccountLocked(), employee.getEmail(),
					employee.getDepartment(), employee.getRoleId(), employeeId) >= 1) {

				jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new ResultSetExtractor<Employee>() {

					public Employee extractData(ResultSet rs) {

						Employee employee = new Employee();
						try {
							while (rs.next()) {
								employee.setEmployeeId(rs.getLong(VariableDeclaration.EMPLOYEE_ID));
								employee.setName(rs.getString("name"));
								employee.setDob(rs.getString("dob"));
								employee.setGender(rs.getString(VariableDeclaration.GENDER));
								employee.setIsAccountLocked(rs.getBoolean(VariableDeclaration.IS_ACCOUNT_LOCKED));
								employee.setIsActive(rs.getBoolean(VariableDeclaration.IS_ACTIVE));
								employee.setEmail(rs.getString(VariableDeclaration.EMAIL));
								employee.setDepartment(rs.getString(VariableDeclaration.DEPARTMENT));
								employee.setRoleId(rs.getLong(VariableDeclaration.ROLE_ID));

							}
						} catch (SQLException exception) {
							List<String> exceptions = new ArrayList<>();
							exceptions.add(exception.getMessage());
							throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(),exceptions);
						}
						return employee;
					}
				}, employeeId);

				return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(), messageproperties.getDetailsUpdatedMessage(),getDataFromTablesUsingId(employeeId));

			} else {
				List<String> exception = new ArrayList<>();
				exception.add("Provide all details required");
				throw new DetailsNotProvidedException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getDetailsNotProvidedMessage(), exception);
			}

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(),exceptions);

		}

	}

	@Override
	public CustomDeleteResponse delete(long employeeId) {
		List<String> statement = new ArrayList<>();
		try {
			jdbcTemplate.update(SqlQueries.DELETE_EMPLOYEES_BY_ID_QUERY, employeeId);
			jdbcTemplate.update(SqlQueries.DELETE_ADDRESS_BY_ID_QUERY, employeeId);
			statement.add("Data in id " + employeeId + " is deleted");
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(), exceptions);
		}
		return new CustomDeleteResponse(StatusCodes.SUCCESS.getStatusCode(), messageproperties.getDetailsDeletedMessage(),statement);
	}

	public List<String> validateEmployee(Employee employee) {

		List<String> exception = new ArrayList<>();

		try {

			if (employee.getName().equals("") || employee.getEmail().equals("") || employee.getDepartment().equals("")
					|| employee.getGender().equals("") || employee.getDob().equals("") || employee.getRoleId() == 0
					|| employee.getAddresses() == null) {
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
			if (!employee.getDob().matches("^(0?[1-9]|[12]\\d|3[01])-(0?[1-9]|1[0-2])-(19|20)\\d{2}$")) {
				exception.add("Date must be specified as dd-mm-yyyy");
				logger.error("Date must be specified as dd-mm-yyyy");
			}
			return exception;

		} catch (DataAccessException exceptions) {
			List<String> list = new ArrayList<>();
			list.add(exceptions.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(), list);
		}
	}


	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<EmployeeResult> getDataFromTables() {
	    String sql = "Call GetAllEmployeeDetails()";
	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        EmployeeResult employee = new EmployeeResult();
	        employee.setEmployeeId(rs.getLong("employeeId"));
	        employee.setName(rs.getString("name"));
	        employee.setDob(rs.getString("dob"));
	        employee.setGender(rs.getString("gender"));
	        employee.setActive(rs.getBoolean("isActive"));
	        employee.setAccountLocked(rs.getBoolean("isAccountLocked"));
	        employee.setEmail(rs.getString("email"));
	        employee.setDepartment(rs.getString("department"));
	        employee.setRoleId(rs.getLong(VariableDeclaration.ROLE_ID));

	        String addressesJson = rs.getString("addresses");
	        List<EmployeeResult.Address> addresses = null;
	        if (addressesJson != null) {
	            try {
					addresses = objectMapper.readValue(addressesJson, new TypeReference<List<EmployeeResult.Address>>() {});
				} catch (JsonProcessingException exceptions) {
					List<String> list = new ArrayList<>();
					list.add(exceptions.getMessage());
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getJsonParseExceptionMessage(), list);
				}
	        }
	        employee.setAddresses(addresses);

	        String rolesJson = rs.getString("roles");
	        List<EmployeeResult.Role> roles = null;
	        if (rolesJson != null) {
	            try {
					roles = objectMapper.readValue(rolesJson, new TypeReference<List<EmployeeResult.Role>>() {});
				} catch (JsonProcessingException exceptions) {
					List<String> list = new ArrayList<>();
					list.add(exceptions.getMessage());
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getJsonParseExceptionMessage(), list);
				}
	        }
	        employee.setRoles(roles);

	        return employee;
	    });

	}

	public List<EmployeeResult> getDataFromTablesUsingId(long employeeId) {
	    String sql = "CALL GetEmployeeDetailsById(?)";

	    return jdbcTemplate.query(sql, ps -> ps.setLong(1, employeeId), rs -> {
	        List<EmployeeResult> employeeList = new ArrayList<>();

	        while (rs.next()) {
	            EmployeeResult employee = new EmployeeResult();
	            employee.setEmployeeId(rs.getLong("employeeId"));
	            employee.setName(rs.getString("name"));
	            employee.setDob(rs.getString("dob"));
	            employee.setGender(rs.getString("gender"));
	            employee.setActive(rs.getBoolean("isActive"));
	            employee.setAccountLocked(rs.getBoolean("isAccountLocked"));
	            employee.setEmail(rs.getString("email"));
	            employee.setDepartment(rs.getString("department"));
	            employee.setRoleId(rs.getLong("roleId"));

	            String addressesJson = rs.getString("addresses");
	            List<EmployeeResult.Address> addresses = null;
	            if (addressesJson != null) {
	                try {
	                    addresses = objectMapper.readValue(addressesJson, new TypeReference<List<EmployeeResult.Address>>() {});
	                } catch (JsonProcessingException exceptions) {
						List<String> list = new ArrayList<>();
						list.add(exceptions.getMessage());
						throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getJsonParseExceptionMessage(), list);
	                }
	            }
	            employee.setAddresses(addresses);

	            String rolesJson = rs.getString("roles");
	            List<EmployeeResult.Role> roles = null;
	            if (rolesJson != null) {
	                try {
	                    roles = objectMapper.readValue(rolesJson, new TypeReference<List<EmployeeResult.Role>>() {});
	                } catch (JsonProcessingException exceptions) {
						List<String> list = new ArrayList<>();
						list.add(exceptions.getMessage());
						throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getJsonParseExceptionMessage(), list);
	                }
	            }
	            employee.setRoles(roles);

	            employeeList.add(employee);
	        }

	        return employeeList;
	    });
	}
	
}

	


