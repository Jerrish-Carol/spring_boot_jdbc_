package com.isteer.springbootjdbc.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public class RoleRowMapper implements RowMapper<Role> {

	@Override
	public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
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
}
