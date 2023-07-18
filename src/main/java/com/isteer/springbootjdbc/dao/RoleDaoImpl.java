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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.MessageProperties;
import com.isteer.springbootjdbc.exception.DetailsNotFoundException;
import com.isteer.springbootjdbc.exception.SqlSyntaxException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.model.VariableDeclaration;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;
import com.isteer.springbootjdbc.statuscode.StatusCodes;

@Repository
public class RoleDaoImpl implements RoleDao {
	
	@Autowired
	private MessageProperties messageproperties;

	private static final Logger logger = LogManager.getLogger(RoleDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public CustomRolePostResponse save(Role roles) {

		try {

			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

			if (jdbcTemplate.update(con -> {
				PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_ROLES_QUERY,
						Statement.RETURN_GENERATED_KEYS);

				ps.setString(1, roles.getRoles());
				ps.setString(2, roles.getProject());
				ps.setBoolean(3, roles.isBillable());
				ps.setString(4, roles.getHierarchicalLevel());
				ps.setString(5, roles.getBuName());
				ps.setString(6, roles.getBuHead());
				ps.setString(7, roles.getHrManager());

				return ps;
			}, keyHolder) == 1) {
				roles.setRoleId(keyHolder.getKey().longValue());
			}
		}
			catch(NullPointerException nullexceptions) {
				
				List<String> exceptions = new ArrayList<>();
				exceptions.add(nullexceptions.getMessage());
				throw new SqlSyntaxException(StatusCodes.CONFLICT.getStatusCode(), messageproperties.getConstraintsInvalidMessage(), exceptions);
			
			
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(),exceptions);
		}

		return new CustomRolePostResponse(StatusCodes.SUCCESS.getStatusCode(),messageproperties.getDetailsSavedMessage(), roles);

	}

	@Override
	public List<Role> getAll() {

		return jdbcTemplate.query(SqlQueries.GET_ROLES_QUERY, new ResultSetExtractor<List<Role>>() {

			public List<Role> extractData(ResultSet rs) throws SQLException {
				List<Role> roles = new ArrayList<>();

				try {

					while (rs.next()) {
						Role role = new Role();
						role.setRoleId(rs.getLong(VariableDeclaration.ROLE_ID));
						role.setRoles(rs.getString(VariableDeclaration.ROLES));
						role.setHierarchicalLevel(rs.getString(VariableDeclaration.HIERARCHICAL_LEVEL));
						role.setProject(rs.getString(VariableDeclaration.PROJECT));
						role.setBillable(rs.getBoolean(VariableDeclaration.BILLABLE));
						role.setBuName(rs.getString(VariableDeclaration.BUHEAD));
						role.setBuHead(rs.getString(VariableDeclaration.BUNAME));
						role.setHrManager(rs.getString(VariableDeclaration.HRMANAGER));
						roles.add(role);

					}
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(),
							exceptions);
				}
				return roles;
			}

		});

	}

	@Override
	public Role getById(long roleId) {

		return jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new ResultSetExtractor<Role>() {

			public Role extractData(ResultSet rs) throws SQLException {

				Role role = new Role();
				try {

					while (rs.next()) {

						role.setRoleId(rs.getLong("roleId"));
						role.setRoles(rs.getString("role"));
						role.setProject(rs.getString("project"));
						role.setBillable(rs.getBoolean("billable"));
						role.setHierarchicalLevel(rs.getString("hierarchicalLevel"));
						role.setBuName(rs.getString("buName"));
						role.setBuHead(rs.getString("buHead"));
						role.setHrManager(rs.getString("hrManager"));

					}
					return role;
				} catch (DataAccessException exception) {
					List<String> exceptions = new ArrayList<>();
					exceptions.add(exception.getMessage());
					throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(), exceptions);
				}
			}
		});
	}

	@Override
	public CustomRolePostResponse update(Role roles, long roleId) {

		try {
			if (jdbcTemplate.update(SqlQueries.UPDATE_ROLES_BY_ID_QUERY, roles.getRoles(), roles.getProject(),
					roles.isBillable(), roles.getHierarchicalLevel(), roles.getBuName(), roles.getBuHead(),
					roles.getHrManager(), roleId) >= 1) {

				Role role = jdbcTemplate.query(SqlQueries.GET_ROLES_BY_ID_QUERY, new ResultSetExtractor<Role>() {

					public Role extractData(ResultSet rs) throws SQLException {

						Role role = new Role();
						while (rs.next()) {
							role.setRoleId(rs.getLong("roleId"));
							role.setRoles(rs.getString("roles"));
							role.setProject(rs.getString("project"));
							role.setBillable(rs.getBoolean("billable"));
							role.setHierarchicalLevel(rs.getString("hierarchicalLevel"));
							role.setBuName(rs.getString("buName"));
							role.setBuHead(rs.getString("buHead"));
							role.setHrManager(rs.getString("hrManager"));

						}
						return role;
					}
				}, roleId);

				return new CustomRolePostResponse(StatusCodes.SUCCESS.getStatusCode(),
						messageproperties.getDetailsSavedMessage(), role);
			} else {
				List<String> exception = new ArrayList<>();
				exception.add("Provide all details required");
				throw new DetailsNotFoundException(StatusCodes.BAD_REQUEST.getStatusCode(),messageproperties.getNoContentToUpdateMessage(), exception);
			}

		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(),
				messageproperties.getBadSqlSyntaxErrorMessage(), exceptions);
		}
	}

	@Override
	public CustomDeleteResponse delete(long roleId) { // using lambda expressions

		List<String> statement = new ArrayList<>();
		try {
			statement.add("Data in id " + roleId + " is deleted"); 
			jdbcTemplate.update(SqlQueries.DELETE_ROLES_BY_ID_QUERY, roleId);
		} catch (DataAccessException exception) {
			List<String> exceptions = new ArrayList<>();
			exceptions.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(),
					 exceptions);
		}

		return new CustomDeleteResponse(StatusCodes.SUCCESS.getStatusCode(), messageproperties.getDetailsDeletedMessage(), statement);

	}

	public List<String> validateRoles(Role roles) {

		List<String> exceptions = new ArrayList<>();

		try {

			if (roles.getRoles().equals("")|| roles.getHierarchicalLevel().equals("") || roles.getProject().equals("")
					|| !roles.isBillable() || roles.getBuHead().equals("") || roles.getBuName().equals("")
					|| roles.getHrManager().equals("")) {
				exceptions.add("no fields should be empty");
				logger.error("no fields should be empty");
			}
		} catch (DataAccessException exception) {
			List<String> list = new ArrayList<>();
			list.add(exception.getMessage());
			throw new SqlSyntaxException(StatusCodes.BAD_REQUEST.getStatusCode(), messageproperties.getBadSqlSyntaxErrorMessage(), exceptions);
		}

		return exceptions;
	}


	public List<String> validateRole(Employee employee) {

		List<String> exception = new ArrayList<>();

		Role roles = employee.getRole();

		if (roles.getRoles().equals("")) {
			exception.add("Role is required for Role details");
		}

		if (roles.getProject().equals("")) {
			exception.add("Project details is required for Role details");
		}

		if (!roles.isBillable()) {
			exception.add("Billable details is required for Role details");
		}

		if (roles.getHierarchicalLevel().equals("")) {
			exception.add("hierachical level details is required for Role details");
		}

		if (roles.getBuName().equals("")) {
			exception.add("Business unit details is required for Role details");
		}

		if (roles.getBuHead().equals("")) {
			exception.add("Business unit head details is required for Role details");
		}

		if (roles.getHrManager().equals("")) {
			exception.add("Hr_Manager details is required for Role details");
		}

		return exception;
	}

}
