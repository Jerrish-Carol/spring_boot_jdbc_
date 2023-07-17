package com.isteer.springbootjdbc.response;

import com.isteer.springbootjdbc.model.Role;

import lombok.Data;

@Data
public class CustomRoleGetResponse {
	
	private Role role;

	public CustomRoleGetResponse(Role role) {
		this.role = role;
	}

}
