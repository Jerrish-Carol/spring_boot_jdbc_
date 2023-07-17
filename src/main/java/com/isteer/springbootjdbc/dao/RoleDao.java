package com.isteer.springbootjdbc.dao;

import java.util.List;

import javax.validation.Valid;

import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;

public interface RoleDao {

	List<Role> getAll();

	Role getById(long roleId);

	CustomRolePostResponse save(Role role);

	CustomRolePostResponse update(Role role, long roleId);

	CustomDeleteResponse delete(long roleId);

	List<String> validateRoles(Role role);

	List<String> validateRole(@Valid Employee employee);


}
