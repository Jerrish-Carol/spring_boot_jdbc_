package com.isteer.springbootjdbc.model;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

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
public class Address implements Serializable { //since this class is called as a list in the parent class -> it is serialized to bytes , stored in DB then deserialized

	private static final long serialVersionUID = 1L;

	private long address_id;
	
	private long employee_id;
	
	@NotBlank(message = "street is required")
	private String street;
	
	@NotBlank(message = "city is required")
	private String city;
		
	@NotBlank(message = "state is required")
	private String state;
		
	@NotBlank(message = "country is required")
	private String country;

}
