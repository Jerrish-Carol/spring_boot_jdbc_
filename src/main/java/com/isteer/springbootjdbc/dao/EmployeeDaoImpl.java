package com.isteer.springbootjdbc.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;

@Repository // 
public class EmployeeDaoImpl implements EmployeeDAO {

	private static final String INSERT_EMPLOYEE_QUERY="INSERT INTO tbl_employees(name, dob, gender, email, department) VALUES (? ,? ,?, ?, ?)";
	private static final String UPDATE_EMPLOYEES_BY_ID_QUERY="UPDATE tbl_employees SET name=?, dob=?, gender=?, email=?, department=? WHERE id=?";
	private static final String GET_EMPLOYEES_BY_ID_QUERY="SELECT * FROM tbl_employees WHERE ID=?";
	private static final String DELETE_EMPLOYEES_BY_ID_QUERY="DELETE FROM tbl_employees WHERE ID=?";
	private static final String GET_EMPLOYEES_QUERY="SELECT * FROM tbl_employees";
	
	@Autowired
	private JdbcTemplate jdbcTemplate; //spring will create this and put in Ioc Container
	
	@Override
	public CustomPostResponse save(Employee employee) {
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT_EMPLOYEE_QUERY, new String[]{"id"});
            ps.setString(1, employee.getName() );
            ps.setString(2, employee.getDob());
            ps.setString(3, employee.getGender());
            ps.setString(4, employee.getEmail());
            ps.setString(5, employee.getDepartment());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        System.out.println(key.longValue());
		if(jdbcTemplate.update(INSERT_EMPLOYEE_QUERY, new Object[] {employee.getName(),employee.getDob() ,employee.getGender() ,employee.getEmail(), employee.getDepartment()})==1) {
			System.out.println(keyHolder.getKey().longValue());
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
		

		if(employee.getName()!="" && employee.getEmail()!="" && employee.getDepartment()!="") {
			if(jdbcTemplate.update(UPDATE_EMPLOYEES_BY_ID_QUERY, new Object[] {employee.getName(),employee.getDob() ,employee.getGender() ,employee.getEmail(), employee.getDepartment(),id})==1) {

				return new CustomPostResponse(1, "UPDATED", employee);
			}
		}
		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotProvidedException(0, "NOT_SAVED", exception);
		}
		return null;
	}

	@Override
	public CustomDeleteResponse delete(long id) {
		List<String> statement = new ArrayList<>();
		jdbcTemplate.update(DELETE_EMPLOYEES_BY_ID_QUERY, id );
		statement.add("Data in id "+id+" is deleted");
		return new CustomDeleteResponse(1, "DELETED", statement);
	}

	@Override
	public List<Employee> getAll() {
		return jdbcTemplate.query(GET_EMPLOYEES_QUERY,new BeanPropertyRowMapper<Employee>(Employee.class));
	}

	@Override
	public Employee getById(long id) {
		
		return jdbcTemplate.queryForObject(GET_EMPLOYEES_BY_ID_QUERY,new BeanPropertyRowMapper<Employee>(Employee.class),id);
		
	}

}
