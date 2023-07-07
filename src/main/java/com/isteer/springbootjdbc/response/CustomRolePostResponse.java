package com.isteer.springbootjdbc.response;

import com.isteer.springbootjdbc.model.Role;

import lombok.Data;

@Data
public class CustomRolePostResponse {
	
		private Role role;

		private long StatusCode;

		private String message;

		public CustomRolePostResponse(long StatusCode, String message, Role role) {
			super();
			this.StatusCode = StatusCode;
			this.message = message;
			this.role = role;
		}

	

}
