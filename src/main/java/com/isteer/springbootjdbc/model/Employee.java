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


public class Employee {

	private long id;

	private String name;

	private String dob;

	private String gender;

	private Boolean isActive;

	private Boolean isAccountLocked;
	
	private String email;

	private String department;

}
