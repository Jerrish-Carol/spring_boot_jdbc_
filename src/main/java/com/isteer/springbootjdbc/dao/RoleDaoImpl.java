package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@Repository
public class RoleDaoImpl implements RoleDao {

	private static Logger logger = Logger.getLogger(EmployeeDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private MessageSource messageSource;

	public CustomRolePostResponse save(Role role) {
		
		try {

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

			if (jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_ROLES_QUERY,
						PreparedStatement.RETURN_GENERATED_KEYS);
	
				ps.setString(1, role.getRole());
				ps.setString(2, role.getProject());
				ps.setBoolean(3, role.isBillable());
				ps.setString(4, role.getHierarchical_level());
				ps.setString(5, role.getBu_name());
				ps.setString(6, role.getBu_head());
				ps.setString(7, role.getHr_manager());
	
				return ps;
			}, keyHolder) == 1) {
				role.setRole_id(keyHolder.getKey().longValue());
			}
		}
		catch(DataAccessException exception){
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageSource.getMessage("error.badsqlsyntax",null, Locale.getDefault()), exceptions);
		}
		
		return new CustomRolePostResponse(StatusCodes.SUCCESS.getStatusCode(),
				messageSource.getMessage("success.detailssaved", null, Locale.getDefault()), role);
		

	}

	@Override
	public List<Role> getAll() {

		List<Role> role = jdbcTemplate.query(SqlQueries.GET_ROLES_QUERY, new ResultSetExtractor<List<Role>>() {

			public List<Role> extractData(ResultSet rs) throws SQLException {
				List<Role> roles = new ArrayList<Role>();
				
				try {

					while (rs.next()) {
						Role role = new Role();
						role.setRole_id(rs.getLong("role_id"));
						role.setRole(rs.getString("role"));
						role.setHierarchical_level(rs.getString("hierarchical_level"));
						role.setProject(rs.getString("project"));
						role.setBillable(rs.getBoolean("billable"));
						role.setBu_name(rs.getString("bu_name"));
						role.setBu_head(rs.getString("bu_head"));
						role.setHr_manager(rs.getString("hr_manager"));
						roles.add(role);

					}
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
							messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
				}
				return roles;
			}

		});
		return role;

	}

	@Override
	public Role getById(long role_id) {

		Role roles = jdbcTemplate.query(SqlQueries.GET_ROLES_QUERY, new ResultSetExtractor<Role>() {

			public Role extractData(ResultSet rs) throws SQLException {

				Role role = new Role();
				try {

					while (rs.next()) {

						role.setRole_id(rs.getLong("role_id"));
						role.setRole(rs.getString("role"));
						role.setProject(rs.getString("project"));
						role.setBillable(rs.getBoolean("billable"));
						role.setHierarchical_level(rs.getString("hierarchical_level"));
						role.setBu_name(rs.getString("bu_name"));
						role.setBu_head(rs.getString("bu_head"));
						role.setHr_manager(rs.getString("hr_manager"));

					}
					return role;
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
							messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
				}
			}
		});
		return roles;
	}

	@Override
	public CustomRolePostResponse update(Role roles, long role_id) {
		
		try {
		if (jdbcTemplate.update(SqlQueries.UPDATE_ROLES_BY_ID_QUERY, roles.getRole(),
				roles.getProject(), roles.isBillable(), roles.getHierarchical_level(), roles.getBu_name(), roles.getBu_head(), roles.getHr_manager(),role_id) >= 1) {

			Role role = jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new ResultSetExtractor<Role>() {

				public Role extractData(ResultSet rs) throws SQLException {

					Role role = new Role();
					while (rs.next()) {
						role.setRole_id(rs.getLong("role_id"));
						role.setRole(rs.getString("role"));
						role.setProject(rs.getString("project"));
						role.setBillable(rs.getBoolean("billable"));
						role.setHierarchical_level(rs.getString("hierarchical_level"));
						role.setBu_name(rs.getString("bu_name"));
						role.setBu_head(rs.getString("bu_head"));
						role.setHr_manager(rs.getString("hr_manager"));

					}
					return role;
				}
			}, role_id);

			return new CustomRolePostResponse(StatusCodes.SUCCESS.getStatusCode(), messageSource.getMessage("success.detailssaved", null, Locale.getDefault()), role);
		} else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotFoundException(StatusCodes.BAD_REQUEST.getStatusCode(), messageSource.getMessage("error.nocontenttoupdate",null, Locale.getDefault()), exception);
		}
	
		}catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
		}
	}

	
	@Override
	public CustomDeleteResponse delete(long role_id) { //using lambda expressions
		
		List<String> statement = new ArrayList<>();
		try {
			Consumer<String> addToList = Statement -> statement.add("Data in id " + role_id + " is deleted"); //addToList : The addToList variable, which is of type Consumer<String>, represents a lambda expression or a functional interface that defines a behavior to consume a String input.
																												//Statement : Which represents the input String to be consumed by the lambda expression
			jdbcTemplate.update(SqlQueries.DELETE_ROLES_BY_ID_QUERY, role_id);
		}
		catch(DataAccessException exception){
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageSource.getMessage("error.badsqlsyntax",null, Locale.getDefault()), exceptions);
		}
	
	  return new CustomDeleteResponse(StatusCodes.SUCCESS.getStatusCode(), messageSource.getMessage("success.detailsdeleted", null, Locale.getDefault()), statement);
	
	}

	public List<String> validateRoles(Role role) {
	       
		List<String> exceptions = new ArrayList<>();

		try {

			if (role.getRole() == "" || role.getHierarchical_level() == "" || role.getProject() == ""
					|| !role.isBillable() || role.getBu_head() == "" || role.getBu_name() == ""
					|| role.getHr_manager() == "") {
				exceptions.add("no fields should be empty");
				logger.error("no fields should be empty");
			}
		} catch (DataAccessException exception) {
			List<String> list = new ArrayList<>();
			list.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
					messageSource.getMessage("error.badsqlsyntax", null, Locale.getDefault()), exceptions);
		}

		return exceptions;
	}

	@Override
	public Role update_role(Role roles, long role_id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<String> validateRole(Employee employee) {
        
		List<String> exception = new ArrayList<>();

		Role role = employee.getRole();

		if (role.getRole() == "") {
			exception.add("Role is required for Role details");
		}

		if (role.getProject() == "") {
			exception.add("Project details is required for Role details");
		}

		if (!role.isBillable()) {
			exception.add("Billable details is required for Role details");
		}

		if (role.getHierarchical_level() == "") {
			exception.add("hierachical level details is required for Role details");
		}
		
		if (role.getBu_name() == "") {
			exception.add("Business unit details is required for Role details");
		}

		if (role.getBu_head() == "") {
			exception.add("Business unit head details is required for Role details");
		}
		
		if (role.getHr_manager() == "") {
			exception.add("Hr_Manager details is required for Role details");
		}

		return exception;
	}

	

}
