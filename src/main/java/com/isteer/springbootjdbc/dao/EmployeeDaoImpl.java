package com.isteer.springbootjdbc.dao;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

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
				ps.setLong(6, employee.getRole_id());
				// System.out.println(employee.getRole_id());

				return ps;
			}, keyHolder) == 1) {
				employee.setId(keyHolder.getKey().longValue());
			}
		} catch (DuplicateKeyException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(),
					messageSource.getMessage("error.duplicatekey", null, Locale.getDefault()), exceptions);

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);

		}

		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
				messageSource.getMessage("success.detailssaved", null, Locale.getDefault()), getDataFromTablesUsingId(employee.getId()));
	}

	@Override
	public CustomPostResponse update(Employee employee, long id) throws SQLException {

		try {

			if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY,employee.getName(), employee.getDob(), employee.getGender(), employee.getIs_active(), employee.getIs_account_locked(),
	                employee.getEmail(), employee.getDepartment(), employee.getRole_id(), id) >= 1) {

				Employee getemployees = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY,
						new ResultSetExtractor<Employee>() {

							public Employee extractData(ResultSet rs) {

								Employee employee = new Employee();
								try {
									while (rs.next()) {
										employee.setId(rs.getLong("id"));
										employee.setName(rs.getString("name"));
										employee.setDob(rs.getString("dob"));
										employee.setGender(rs.getString("gender"));
										employee.setIs_account_locked(rs.getBoolean("is_account_locked"));
										employee.setIs_active(rs.getBoolean("is_active"));
										employee.setEmail(rs.getString("email"));
										employee.setDepartment(rs.getString("department"));
										employee.setRole_id(rs.getLong("role_id"));
										
									}
								} catch (SQLException exception) {
									List<String> exceptions = new ArrayList<>();
									exceptions.add(exception.getMessage());
									throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
											messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()),
											exceptions);
								}
								return employee;
							}
						}, id);
				
				//System.out.println(getemployees);

				return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
						messageSource.getMessage("success.detailsupdated", null, Locale.getDefault()), getDataFromTablesUsingId(id));

			} else {
				List<String> exception = new ArrayList<>();
				exception.add("Provide all details required");
				throw new DetailsNotProvidedException(StatusCodes.BAD_REQUEST.getStatusCode(),
						messageSource.getMessage("error.detailsnotprovided", null, Locale.getDefault()), exception);
			}

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);

		}
	
	}

	@Override
	public CustomDeleteResponse delete(long id) {
		List<String> statement = new ArrayList<>();
		try {
			jdbcTemplate.update(SqlQueries.DELETE_EMPLOYEES_BY_ID_QUERY, id);
			jdbcTemplate.update(SqlQueries.DELETE_ADDRESS_BY_ID_QUERY, id);
			statement.add("Data in id " + id + " is deleted");
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
		}
		return new CustomDeleteResponse(StatusCodes.SUCCESS.getStatusCode(),
				messageSource.getMessage("success.detailsdeleted", null, Locale.getDefault()), statement);
	}

	/*
	 * @Override public List<Employee> getAll() {
	 * 
	 * try { List<Employee> e = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_QUERY,
	 * new ResultSetExtractor<List<Employee>>() {
	 * 
	 * public List<Employee> extractData(ResultSet rs) throws SQLException {
	 * 
	 * List<Employee> employees = new ArrayList<Employee>();
	 * 
	 * while (rs.next()) { Employee employee = new Employee();
	 * employee.setId(rs.getLong("id")); employee.setName(rs.getString("name"));
	 * employee.setEmail(rs.getString("email"));
	 * employee.setDob(rs.getString("dob"));
	 * employee.setGender(rs.getString("gender"));
	 * employee.setDepartment(rs.getString("department"));
	 * employee.setIs_account_locked(rs.getBoolean("is_account_locked"));
	 * employee.setIs_active(rs.getBoolean("is_active"));
	 * employee.setRole_id(rs.getLong("role_id")); employee.setAddresses(
	 * 
	 * jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID_QUERY, new
	 * ResultSetExtractor<List<Address>>() {
	 * 
	 * @Override public List<Address> extractData(ResultSet rset) throws
	 * SQLException, DataAccessException { List<Address> addresses = new
	 * ArrayList<Address>();
	 * 
	 * while (rset.next()) {
	 * 
	 * Address address = new Address();
	 * address.setAddress_id(rset.getLong("address_id"));
	 * address.setEmployee_id(rset.getLong("employee_id"));
	 * address.setStreet(rset.getString("street"));
	 * address.setState(rset.getString("state"));
	 * address.setCity(rset.getString("city"));
	 * address.setCountry(rset.getString("country")); addresses.add(address); }
	 * return addresses;
	 * 
	 * } }, employee.getId())); employee.setRole(
	 * 
	 * jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new
	 * ResultSetExtractor<Role>() {
	 * 
	 * @Override public Role extractData(ResultSet rset) throws SQLException,
	 * DataAccessException { System.out.println(rset); Role role = new Role(); while
	 * (rset.next()) { role.setRole_id(rs.getLong("role_id"));
	 * role.setRole(rset.getString("role"));
	 * role.setProject(rset.getString("project"));
	 * role.setBillable(rset.getBoolean("billable"));
	 * role.setHierarchical_level(rset.getString("hierarchical_level"));
	 * role.setHr_manager(rset.getString("hr_manager"));
	 * role.setBu_name(rset.getString("bu_name"));
	 * role.setBu_head(rset.getString("bu_head")); } return role;
	 * 
	 * } },rs.getLong("role_id"))); employees.add(employee);
	 * 
	 * } return employees; } });
	 * 
	 * return e; } catch (DataAccessException exception) { List<String> exceptions =
	 * new ArrayList<>(); exceptions.add(exception.getMessage()); throw new
	 * SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
	 * messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()),
	 * exceptions); } }
	 * 
	 * @Override public Employee getById(long id) {
	 * 
	 * try {
	 * 
	 * Employee e = jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new
	 * ResultSetExtractor<Employee>() {
	 * 
	 * public Employee extractData(ResultSet rs) throws SQLException {
	 * 
	 * Employee employee = new Employee(); while (rs.next()) {
	 * employee.setId(rs.getLong("id")); employee.setName(rs.getString("name"));
	 * employee.setEmail(rs.getString("email"));
	 * employee.setDob(rs.getString("dob"));
	 * employee.setGender(rs.getString("gender"));
	 * employee.setDepartment(rs.getString("department"));
	 * employee.setIs_account_locked(rs.getBoolean("is_account_locked"));
	 * employee.setIs_active(rs.getBoolean("is_active"));
	 * employee.setRole_id(rs.getLong("role_id")); employee.setAddresses(
	 * 
	 * jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID_QUERY, new
	 * ResultSetExtractor<List<Address>>() {
	 * 
	 * @Override public List<Address> extractData(ResultSet rset) throws
	 * SQLException, DataAccessException { List<Address> addresses = new
	 * ArrayList<Address>(); while (rset.next()) { Address address = new Address();
	 * address.setAddress_id(rset.getLong("address_id"));
	 * address.setEmployee_id(id); address.setStreet(rset.getString("street"));
	 * address.setState(rset.getString("state"));
	 * address.setCity(rset.getString("city"));
	 * address.setCountry(rset.getString("country")); addresses.add(address); }
	 * return addresses;
	 * 
	 * } }, id)); employee.setRole(
	 * 
	 * jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new
	 * ResultSetExtractor<Role>() {
	 * 
	 * @Override public Role extractData(ResultSet rset) throws SQLException,
	 * DataAccessException { Role role = new Role(); while (rset.next()) {
	 * role.setRole_id(rs.getLong("role_id")); role.setRole(rset.getString("role"));
	 * role.setProject(rset.getString("project"));
	 * role.setBillable(rset.getBoolean("billable"));
	 * role.setHierarchical_level(rset.getString("hierarchical_level"));
	 * role.setHr_manager(rset.getString("hr_manager"));
	 * role.setBu_name(rset.getString("bu_name"));
	 * role.setBu_head(rset.getString("bu_head")); } return role;
	 * 
	 * } },rs.getLong("role_id")));
	 * 
	 * } return employee; } }, id);
	 * 
	 * return e; } catch (DataAccessException exception) { List<String> exceptions =
	 * new ArrayList<>(); exceptions.add(exception.getMessage()); throw new
	 * SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
	 * messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()),
	 * exceptions); } }
	 */

	public List<String> validateEmployee(Employee employee) {

		List<String> exception = new ArrayList<>();

		try {

			if (employee.getName() == "" || employee.getEmail() == "" || employee.getDepartment() == ""
					|| employee.getGender() == "" || employee.getDob() == "" || employee.getRole_id() == 0
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
			if (!employee.getDob().matches("^(0?[1-9]|[12][0-9]|3[01])-(0?[1-9]|1[0-2])-(19|20)\\d{2}$")) {
				exception.add("Date must be specified as dd-mm-yyyy");
				logger.error("Date must be specified as dd-mm-yyyy");
			}
			return exception;

		} catch (DataAccessException exceptions) {
			List<String> list = new ArrayList<>();
			list.add(exceptions.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), list);
		}
	}

	public Employee getRoleById(long id) {

		try {

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
						employee.setIs_account_locked(rs.getBoolean("is_account_locked"));
						employee.setIs_active(rs.getBoolean("is_active"));
						employee.setRole_id(rs.getLong("role_id"));
						employee.setRole(

								jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new ResultSetExtractor<Role>() {

									@Override
									public Role extractData(ResultSet rset) throws SQLException, DataAccessException {
										System.out.println(rset);
										Role role = new Role();
										while (rset.next()) {
											role.setRole(rset.getString("role"));
											role.setProject(rset.getString("project"));
											role.setBillable(rset.getBoolean("billable"));
											role.setHierarchical_level(rset.getString("hierarchical_level"));
											role.setHr_manager(rset.getString("hr_manager"));
											role.setBu_name(rset.getString("bu_name"));
											role.setBu_head(rset.getString("bu_head"));
										}
										return role;

									}
								}, rs.getLong("role_id")));
					}
					return employee;
				}
			}, id);

			return e;
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
		}
	}

	public List<Employee> getDataFromTables() {

		List<Employee> employees = new ArrayList<>();

		jdbcTemplate.query(SqlQueries.GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_QUERY, (rs) -> {
			if (rs.next()) {
				do {
					Long id = rs.getLong("id");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String dob = rs.getString("dob");
					String gender = rs.getString("gender");
					String department = rs.getString("department");
					boolean is_account_locked = rs.getBoolean("is_account_locked");
					boolean is_active = rs.getBoolean("is_active");
					Long role_id = rs.getLong("role_id");

					Employee employee = employees.stream().filter(emp -> emp.getId() == id).findFirst().orElse(null);

					if (employee == null) {
						employee = new Employee();
						employee.setId(id);
						employee.setName(name);
						employee.setEmail(email);
						employee.setDob(dob);
						employee.setGender(gender);
						employee.setName(department);
						employee.setIs_account_locked(is_account_locked);
						employee.setIs_active(is_active);
						employee.setRole_id(role_id);
						employees.add(employee);
					}

					Address address = new Address();
					address.setEmployee_id(rs.getLong("employee_id"));
					address.setAddress_id(rs.getLong("address_id"));
					address.setStreet(rs.getString("street"));
					address.setCity(rs.getString("city"));
					address.setState(rs.getString("state"));
					address.setCountry(rs.getString("country"));
					employee.getAddresses().add(address);

					Role role = new Role();
					role.setRole_id(rs.getLong("role_id"));
					role.setRole(rs.getString("role"));
					role.setProject(rs.getString("project"));
					role.setBillable(rs.getBoolean("billable"));
					role.setHierarchical_level(rs.getString("hierarchical_level"));
					role.setBu_name(rs.getString("bu_name"));
					role.setBu_head(rs.getString("bu_head"));
					role.setHr_manager(rs.getString("hr_manager"));
					employee.setRole(role);
				} while (rs.next());
				return null;
			}

			return null;
		});

		return employees;

	}

	public List<Employee> getDataFromTablesUsingId(long id) {

		List<Employee> employees = new ArrayList<>();

		jdbcTemplate.query(SqlQueries.GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_USING_ID_QUERY, preparedStatement -> {
			preparedStatement.setLong(1, id);
		}, (rs) -> {
			if (rs.next()) {
				do {
					Long emp_id = id;
					String name = rs.getString("name");
					String email = rs.getString("email");
					String dob = rs.getString("dob");
					String gender = rs.getString("gender");
					String department = rs.getString("department");
					boolean is_account_locked = rs.getBoolean("is_account_locked");
					boolean is_active = rs.getBoolean("is_active");
					Long role_id = rs.getLong("role_id");

					Employee employee = employees.stream().filter(emp -> emp.getId() == emp_id).findFirst()
							.orElse(null);

					if (employee == null) {
						employee = new Employee();
						employee.setId(id);
						employee.setName(name);
						employee.setEmail(email);
						employee.setDob(dob);
						employee.setGender(gender);
						employee.setDepartment(department);
						employee.setIs_account_locked(is_account_locked);
						employee.setIs_active(is_active);
						employee.setRole_id(role_id);
						employees.add(employee);
					}

					Address address = new Address();
					address.setEmployee_id(rs.getLong("employee_id"));
					address.setAddress_id(rs.getLong("address_id"));
					address.setStreet(rs.getString("street"));
					address.setCity(rs.getString("city"));
					address.setState(rs.getString("state"));
					address.setCountry(rs.getString("country"));
					employee.getAddresses().add(address);

					Role role = new Role();
					role.setRole_id(rs.getLong("role_id"));
					role.setRole(rs.getString("role"));
					role.setProject(rs.getString("project"));
					role.setBillable(rs.getBoolean("billable"));
					role.setHierarchical_level(rs.getString("hierarchical_level"));
					role.setBu_name(rs.getString("bu_name"));
					role.setBu_head(rs.getString("bu_head"));
					role.setHr_manager(rs.getString("hr_manager"));
					employee.setRole(role);
				} while (rs.next());
				return null;
			}

			return null;
		});

		return employees;

	}

	

}
	
//	public CustomPostResponse updateAndRetrieveEmployeesWithGroupBy(Employee emp,long id) {
//		
//		public List<Employee> deserializeEmployeeList(String json) throws IOException {
//		    ObjectMapper objectMapper = new ObjectMapper();
//		    return objectMapper.readValue(json, new TypeReference<List<Employee>>(){});
//		    
//	    // Step 1: Define entity classes for Employee, Address, and Role
//	    
////	    // Step 2: Write SQL UPDATE statements to update the values in each entity table
////	    String updateEmployeesQuery = "UPDATE tbl_employees SET ...";
////	    String updateAddressesQuery = "UPDATE tbl_addresses SET ...";
////	    String updateRolesQuery = "UPDATE tbl_roles SET ...";
//	    
//	    // Step 3: Execute the update statements
//		
//		jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY, emp.getName(), emp.getDob(),
//				emp.getGender(), emp.getIs_active(), emp.getIs_account_locked(), emp.getEmail(),
//				emp.getDepartment(), emp.getRole_id(), id) ; 
//	    //jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY,id);
//	    jdbcTemplate.update(SqlQueries.UPDATE_ADDRESS_BY_ID_QUERY,id);
//	    jdbcTemplate.update(SqlQueries.UPDATE_ROLES_BY_ID_QUERY,emp.getRole_id());
//	    
//	    List<Employee> employees = new ArrayList<>();
//	    
//	    // Step 4: Write SQL SELECT statement with necessary JOIN clauses and GROUP BY
//	    jdbcTemplate.query(SqlQueries.GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_USING_ID_QUERY, preparedStatement -> {
//			preparedStatement.setLong(1, id);
//		}, (rs) -> {
//			if (rs.next()) {
//				do {
//					Long emp_id = id;
//					String name = rs.getString("name");
//					String email = rs.getString("email");
//					String dob = rs.getString("dob");
//					String gender = rs.getString("gender");
//					String department = rs.getString("department");
//					boolean is_account_locked = rs.getBoolean("is_account_locked");
//					boolean is_active = rs.getBoolean("is_active");
//					Long role_id = rs.getLong("role_id");
//
//					Employee employee = employees.stream().filter(empl -> empl.getId() == emp_id).findFirst()
//							.orElse(null);
//
//					if (employee == null) {
//						employee = new Employee();
//						employee.setId(id);
//						employee.setName(name);
//						employee.setEmail(email);
//						employee.setDob(dob);
//						employee.setGender(gender);
//						employee.setDepartment(department);
//						employee.setIs_account_locked(is_account_locked);
//						employee.setIs_active(is_active);
//						employee.setRole_id(role_id);
//						employees.add(employee);
//					}
//
//					Address address = new Address();
//					address.setEmployee_id(rs.getLong("employee_id"));
//					address.setAddress_id(rs.getLong("address_id"));
//					address.setStreet(rs.getString("street"));
//					address.setCity(rs.getString("city"));
//					address.setState(rs.getString("state"));
//					address.setCountry(rs.getString("country"));
//					employee.getAddresses().add(address);
//
//					Role role = new Role();
//					role.setRole_id(rs.getLong("role_id"));
//					role.setRole(rs.getString("role"));
//					role.setProject(rs.getString("project"));
//					role.setBillable(rs.getBoolean("billable"));
//					role.setHierarchical_level(rs.getString("hierarchical_level"));
//					role.setBu_name(rs.getString("bu_name"));
//					role.setBu_head(rs.getString("bu_head"));
//					role.setHr_manager(rs.getString("hr_manager"));
//					employee.setRole(role);
//				} while (rs.next());
//				return null;
//			}
//
//			return null;
//		});
//
//		return new CustomPostResponse(StatusCodes.SUCCESS.getStatusCode(),
//				messageSource.getMessage("success.detailsupdated", null, Locale.getDefault()), getDataFromTablesUsingId(id));
//
//	}
//}
