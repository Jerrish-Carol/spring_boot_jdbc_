package com.isteer.springbootjdbc.dao;

import java.util.List;

import com.isteer.springbootjdbc.model.Role;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.response.CustomRolePostResponse;

public interface RoleDao {

	List<Role> getAll();

	Role getById();

	CustomRolePostResponse save();

	CustomRolePostResponse update(Role role, long role_id);

	CustomDeleteResponse delete();

}
