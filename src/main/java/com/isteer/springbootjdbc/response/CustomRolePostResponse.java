package com.isteer.springbootjdbc.response;

import com.isteer.springbootjdbc.model.Role;

import lombok.Data;

@Data
public class CustomRolePostResponse {
	
		private Role role;

		private long statusCode;

		private String message;

		public CustomRolePostResponse(long statusCode, String message, Role role) {
			super();
			this.statusCode = statusCode;
			this.message = message;
			this.role = role;
		}

	

}
