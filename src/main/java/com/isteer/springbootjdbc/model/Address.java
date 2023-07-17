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
public class Address implements Serializable { //since this class is called as a list in the parent class -> it is serialized to bytes , stored in DB then deserialized

	private static final long serialVersionUID = 1L;

	private long addressId;
	
	private long employeeId;
	
	private String street;
	
	private String city;

	private String state;

	private String country;

}
