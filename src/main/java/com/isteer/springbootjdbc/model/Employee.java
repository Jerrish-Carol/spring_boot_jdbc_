package com.isteer.springbootjdbc.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.isteer.springbootjdbc.model.Address;

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
	
	public List<Address> addresses;
	
	
	
}
