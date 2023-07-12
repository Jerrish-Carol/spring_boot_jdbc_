package com.isteer.springbootjdbc.dao;

import java.util.List;

import javax.validation.Valid;

import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;

public interface RoleDao {

	List<Role> getAll();

	Role getById(long role_id);

	CustomRolePostResponse save(Role role);

	CustomRolePostResponse update(Role role, long role_id);

	CustomDeleteResponse delete(long role_id);

	List<String> validateRoles(Role role);

	Role update_role(Role roles, long role_id);

	List<String> validateRole(@Valid Employee employee);


}
