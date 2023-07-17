package com.isteer.springbootjdbc.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VariableDeclaration {

	private VariableDeclaration() {
		
	}
	
	public static final String IS_ACCOUNT_LOCKED = "ISACCOUNTLOCKED";

	public static final String IS_ACTIVE = "ISACTIVE";
	
	public static final String HR_MANAGER = "HRMANAGER";
	
	public static final String HIERARCHICAL_LEVEL = "HIERARCHICALLEVEL";
	
	public static final String GENDER = "GENDER";
	
	public static final String EMAIL ="EMAIL";

	public static final String DEPARTMENT = "DEPARTMENT";
	
	public static final String PROJECT = "PROJECT";
	
	public static final String BILLABLE = "BILLABLE";
	
	public static final String BUNAME = "BUNAME";
	
	public static final String BUHEAD ="BUHEAD";
	
	public static final String ROLE_ID = "ROLEID";
	
	public static final String EMPLOYEE_ID = "EMPLOYEEID";
	
	public static final String ROLES = "ROLES";
	
	public static final String HRMANAGER ="HRMANAGER";
	
	public static final String ADDRESS_ID = "ADDRESSID";
	
	public static final String COUNTRY = "COUNTRY";
	
	public static final String STREET = "STREET";
	
	public static final String STATE = "STATE";
	
	public static final String CITY = "CITY";
	
}
