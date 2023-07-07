package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

@Repository
public class RoleDaoImpl implements RoleDao {

	private static Logger logger = Logger.getLogger(EmployeeDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate; 

	public CustomRolePostResponse save(Role role) {

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		if (jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_ROLES_QUERY,
					PreparedStatement.RETURN_GENERATED_KEYS);
		
			ps.setString(1, role.getRole());
			ps.setString(2, role.getHierarchical_level());
			ps.setString(3, role.getProject());
			ps.setBoolean(4, role.isBillable());

			return ps;
		}, keyHolder) == 1) {
			role.setRole_id(keyHolder.getKey().longValue());
		}
		return new CustomRolePostResponse(1, "SAVED", role);

	}

	@Override
	public List<Role> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getById() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomRolePostResponse save() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override public CustomRolePostResponse update(Role role, long role_id) { if
	 * (jdbcTemplate.update(SqlQueries.UPDATE_ROLES_BY_ID_QUERY, new Object[] {
	 * role.getRole(), role.getHierarchical_level(), role.getProject(),
	 * role.isBillable(), role_id }) == 1) {
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
	 * employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
	 * employee.setIsActive(rs.getBoolean("is_active"));
	 * 
	 * } return employee; } }, id);
	 * 
	 * return new CustomPostResponse(1, "Updating employees", e); } else {
	 * List<String> exception = new ArrayList<>();
	 * exception.add("Provide all details required"); throw new
	 * DetailsNotProvidedException(0, "Not saved", exception); } } }
	 */

	@Override
	public CustomDeleteResponse delete() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomRolePostResponse update(Role role, long role_id) {
		// TODO Auto-generated method stub
		return null;
	}
}
