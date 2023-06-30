package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

@Repository 
public class EmployeeDaoImpl implements EmployeeDAO {
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate; //spring will create this and put in Ioc Container
	
	/*ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();*/
	
	@Override
	public CustomPostResponse save(Employee employee) { 
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		

		if(jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_EMPLOYEE_QUERY, new String[]{"id"});
            ps.setString(1, employee.getName() );
            ps.setString(2, employee.getDob());
            ps.setString(3, employee.getGender());
            ps.setString(4, employee.getEmail());
            ps.setString(5, employee.getDepartment());
          
            return ps;
        }, keyHolder)==1) {
			employee.setId(keyHolder.getKey().longValue());
			
			/*Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
			if (!violations.isEmpty()) {
				System.out.println(violations);
			    for (ConstraintViolation<Employee> violation : violations) {
			        System.out.println(violation.getPropertyPath() + ": " + violation.getMessage());
			    }
			    return null;
			} else {
				return new CustomPostResponse(1, "SAVED", employee);
			}*/
		
			return new CustomPostResponse(1, "SAVED", employee);
		}
		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotProvidedException(0, "NOT_SAVED", exception);
		}
		
	}
	

	@Override
	public CustomPostResponse update(Employee employee, long id) {

		if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY, new Object[] { employee.getName(),
			employee.getDob(), employee.getGender(), employee.getEmail(), employee.getDepartment(), id }) == 1) {
			employee.setId(id);
			return new CustomPostResponse(1, "UPDATED", employee);
		}

		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotProvidedException(0, "NOT_SAVED", exception);
		}
	}

	@Override
	public CustomDeleteResponse delete(long id) {
		List<String> statement = new ArrayList<>();
		jdbcTemplate.update(SqlQueries.DELETE_EMPLOYEES_BY_ID_QUERY, id );
		statement.add("Data in id "+id+" is deleted");
		return new CustomDeleteResponse(1, "DELETED", statement);
	}

	@Override
	public List<Employee> getAll() {
		return jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_QUERY, new BeanPropertyRowMapper<Employee>() {
				
				public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
					Employee employee = new Employee();
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
				
				return employee;
				}	
		});
	}

	@Override
	public CustomGetResponse getById(long id) {

		return new CustomGetResponse(jdbcTemplate.queryForObject(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new BeanPropertyRowMapper<Employee>() {
				
				public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
					Employee employee = new Employee();
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
				
				return employee;
				}

		},id));
		
	}
		

}
