package com.isteer.springbootjdbc.model;

import java.io.Serializable;

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
public class Address implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long address_id;
	
	private long employee_id;
	
	private String street;
	
	private String city;
		
	private String state;
		
	private String country;

}
