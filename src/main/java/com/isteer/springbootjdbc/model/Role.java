package com.isteer.springbootjdbc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {

	private long role_id;
	
	private String role;
	
	private String hierarchical_level;
	
	private String project;
	
	private boolean billable;
}
