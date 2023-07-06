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
public class Address {
	
	private long address_id;
	
	private long employee_id;
	
	private String street;
	
	private String city;
		
	private String state;
		
	private String country;

}
