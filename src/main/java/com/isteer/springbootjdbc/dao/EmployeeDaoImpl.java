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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isteer.springbootjdbc.MessageService;
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

	@Autowired
	private MessageService messageservice;
	
	@Autowired
	private JdbcTemplate jdbcTemplate; // spring will create this and put in Ioc Container
	
	private static final Logger auditlogger = LogManager.getLogger(EmployeeDaoImpl.class);
	
	private static String wLog = "Id: {} Status Code: {} Message: {} Exception: {} Layer: Employee DAO Layer";
	
	private static String iLog = "Id: {} Status Code: {} Message: {} Layer: Employee DAO Layer";
	
	private static String wnLog = "Id: New Employee Status Code: {} Message: {} Exception: {} Layer: Employee DAO Layer";
	
	private static String inLog = "Id: New Employee Status Code: {} Message: {} Layer: Employee DAO Layer";

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
		} catch (NullPointerException nullexceptions) {

			List<String> exceptions = new ArrayList<>();
			exceptions.add(nullexceptions.getMessage());
			auditlogger.warn(wnLog, StatusCodes.CONFLICT.getStatusCode(), messageservice.getDuplicateKeyMessage(),exceptions);
			throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(),
					messageservice.getKeyholderNotGeneratedEXceptionMessage(), exceptions);

		} catch (DuplicateKeyException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wnLog, StatusCodes.CONFLICT.getStatusCode(), messageservice.getDuplicateKeyMessage(),exceptions);
			throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(),
					messageservice.getDuplicateKeyMessage(), exceptions);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wnLog, StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(),exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);

		}

		auditlogger.info(inLog, StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsSavedMessage());
		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(), messageservice.getDetailsSavedMessage(),
				getDataFromTablesUsingId(employee.getEmployeeId()));
	}

	@Override
	public CustomPostResponse update(Employee employee, long employeeId) throws SQLException {

		try {

			if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY, employee.getName(), employee.getDob(),
					employee.getGender(), employee.getIsActive(), employee.getIsAccountLocked(), employee.getEmail(),
					employee.getDepartment(), employee.getRoleId(), employeeId) >= 1) {
				
				auditlogger.info(iLog,employeeId, StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsUpdatedMessage());
				return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsUpdatedMessage(), getDataFromTablesUsingId(employeeId));
			}
		}
		 catch (DataAccessException exception) {
				List<String> exceptions = new ArrayList<>();
				exceptions.add(exception.getMessage());
				auditlogger.warn(wLog, employeeId, StatusCodes.BAD_REQUEST.getStatusCode(),
						messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
				throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
						messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
	
			}
		return null;
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
			auditlogger.warn(wLog, employeeId, StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}
		auditlogger.info(iLog, employeeId , StatusCodes.SUCCESS.getStatusCode(),
				messageservice.getDetailsDeletedMessage());
		return new CustomDeleteResponse(StatusCodes.SUCCESS.getStatusCode(),
				messageservice.getDetailsDeletedMessage(), statement);
	}

	public List<String> validateEmployee(Employee employee) {

		List<String> exception = new ArrayList<>();

		try {

			if (employee.getName().equals("") || employee.getEmail().equals("") || employee.getDepartment().equals("")
					|| employee.getGender().equals("") || employee.getDob().equals("") || employee.getRoleId() == 0
					|| employee.getAddresses() == null) {
				exception.add("no field should be empty");

			}
			if (employee.getName().length() < 5) {
				exception.add("name must have atleast 5 characters");
			}
			if (!employee.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+[.]+[a-zA-Z]{2,}$")) {
				exception.add("email id is not right format");
			}
			if (!employee.getGender().matches("Male|Female|Other")) {
				exception.add("Gender must be specified as Male|Female|Other");
			}
			if (!employee.getDob().matches("^(0?[1-9]|[12]\\d|3[01])-(0?[1-9]|1[0-2])-(19|20)\\d{2}$")) {
				exception.add("Date must be specified as dd-mm-yyyy");
			}
			return exception;

		} catch (DataAccessException exceptions) {
			List<String> list = new ArrayList<>();
			list.add(exceptions.getMessage());
			auditlogger.warn(wLog, employee.getEmployeeId() , StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), list);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), list);
		}
	}

	public List<EmployeeResult> getDataFromTables() {

		try {

			String sql = "Call GetAllEmployeeDetails()";
			return jdbcTemplate.query(sql, (rs, rowNum) -> {
				EmployeeResult employee = new EmployeeResult();
				employee.setEmployeeId(rs.getLong(VariableDeclaration.EMPLOYEE_ID));
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
						addresses = objectMapper.readValue(addressesJson,
								new TypeReference<List<EmployeeResult.Address>>() {
								});
					}

					catch (JsonProcessingException exceptions) {
						List<String> list = new ArrayList<>();
						list.add(exceptions.getMessage());
						auditlogger.warn(wLog, employee.getEmployeeId() ,StatusCodes.BAD_REQUEST.getStatusCode(),
								messageservice.getJsonParseExceptionMessage(), list);
						throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
								messageservice.getJsonParseExceptionMessage(), list);
					}

				}
				employee.setAddresses(addresses);

				String rolesJson = rs.getString("roles");
				List<EmployeeResult.Role> roles = null;
				if (rolesJson != null) {
					try {
						roles = objectMapper.readValue(rolesJson, new TypeReference<List<EmployeeResult.Role>>() {
						});
					} catch (JsonProcessingException exceptions) {

						List<String> list = new ArrayList<>();
						list.add(exceptions.getMessage());
						auditlogger.warn(wLog, employee.getEmployeeId() ,StatusCodes.BAD_REQUEST.getStatusCode(),
								messageservice.getJsonParseExceptionMessage(), list);
						throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
								messageservice.getJsonParseExceptionMessage(), list);
					}

				}
				employee.setRoles(roles);

				auditlogger.info(iLog, employee.getEmployeeId() ,StatusCodes.SUCCESS.getStatusCode(),
						messageservice.getDetailsDisplayedMessage());
				return employee;
			});
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			auditlogger.warn(wLog, "All employees" ,StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getJsonParseExceptionMessage(), exceptions);
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageservice.getBadSqlSyntaxErrorMessage(), exceptions);
		}

	}
	
	public List<EmployeeResult> getDataFromTablesUsingId(long employeeId) {
	    String sql = "CALL GetEmployeeDetailsById(?)";
	  
	    return jdbcTemplate.query(sql, ps -> ps.setLong(1, employeeId), this::mapEmployeeResult);
	}

	private List<EmployeeResult> mapEmployeeResult(ResultSet rs) throws SQLException {
	    List<EmployeeResult> employeeList = new ArrayList<>();

	    long employeeId = 0;
	    
	    while (rs.next()) {
	        EmployeeResult employee = new EmployeeResult();
	        employee.setEmployeeId(rs.getLong("employeeId"));
	        employeeId = rs.getLong("employeeId");
	        employee.setName(rs.getString("name"));
	        employee.setDob(rs.getString("dob"));
	        employee.setGender(rs.getString("gender"));
	        employee.setActive(rs.getBoolean("isActive"));
	        employee.setAccountLocked(rs.getBoolean("isAccountLocked"));
	        employee.setEmail(rs.getString("email"));
	        employee.setDepartment(rs.getString("department"));
	        employee.setRoleId(rs.getLong("roleId"));

	        String addressesJson = rs.getString("addresses");
	        List<EmployeeResult.Address> addresses = convertJsonToAddressList(addressesJson);
	        employee.setAddresses(addresses);

	        String rolesJson = rs.getString("roles");
	        List<EmployeeResult.Role> roles = convertJsonToRoleList(rolesJson);
	        employee.setRoles(roles);

	        employeeList.add(employee);
	    }
	    auditlogger.info(iLog, employeeId  ,StatusCodes.SUCCESS.getStatusCode(),
				messageservice.getDetailsDisplayedMessage());
	    return employeeList;
	}

	private List<EmployeeResult.Address> convertJsonToAddressList(String addressesJson) {
		
		List<EmployeeResult.Address> employeeAddress = new ArrayList<>();
	    if (addressesJson != null) {
	        try {
	        	 auditlogger.info(iLog, "Address" ,StatusCodes.SUCCESS.getStatusCode(),
	     				messageservice.getDetailsDisplayedMessage());
	        	return objectMapper.readValue(addressesJson, new TypeReference<List<EmployeeResult.Address>>() {});
	        } catch (JsonProcessingException exceptions) {
	            List<String> list = new ArrayList<>();
	            list.add(exceptions.getMessage());
	            auditlogger.warn(wLog, "Address" ,StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getJsonParseExceptionMessage(), list);
	            throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getJsonParseExceptionMessage(), list);
	        }
	    }
	    return employeeAddress;
	}

	private List<EmployeeResult.Role> convertJsonToRoleList(String rolesJson) {
		List<EmployeeResult.Role> employeeRole = new ArrayList<>();
		
	    if (rolesJson != null) {
	        try {
	        	 auditlogger.info(iLog, "Role" ,StatusCodes.SUCCESS.getStatusCode(),
		     				messageservice.getDetailsDisplayedMessage(),"Employee DAO Layer");
	            return objectMapper.readValue(rolesJson, new TypeReference<List<EmployeeResult.Role>>() {});
	        } catch (JsonProcessingException exceptions) {
	            List<String> list = new ArrayList<>();
	            list.add(exceptions.getMessage());
	            auditlogger.warn(wLog, "Role" ,StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getJsonParseExceptionMessage(), list);
	            throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageservice.getJsonParseExceptionMessage(), list);
	        }
	    }
	    return employeeRole;
	}


}
