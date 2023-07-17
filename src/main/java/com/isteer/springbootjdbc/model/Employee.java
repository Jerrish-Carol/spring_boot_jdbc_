package com.isteer.springbootjdbc.model;

import java.util.List;
import java.util.ArrayList;
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

	private long employeeId;

	private String name;

	private String dob;

	private String gender;

	private Boolean isActive;

	private Boolean isAccountLocked;
	
	private String email;

	private String department;
	
	private long roleId;
	
	public List<Address> addresses = new ArrayList<>();
	
	public Role role;

	public void setRoles(List<Role> role) {
	
		
	}
	
	public void getRoles(Role role) {
		
	}
	

}


/*
 * Development/Engineering Department:
 * 
 * Software Development Web Development Mobile App Development Database
 * Development Quality Assurance/Testing 
 * 
 * Infrastructure/Operations Department:
 * 
 * Network Operations System Administration IT Operations/Support Cloud
 * Infrastructure Management Data Center Management 
 * 
 * Security Department:
 * 
 * Information Security Cybersecurity Security Operations Center (SOC) Risk and
 * Compliance Identity and Access Management (IAM) 
 * 
 * Data and Analytics Department:
 * 
 * Data Analysis Business Intelligence Data Engineering Data Science Data
 * Governance Project Management Office (PMO):
 * 
 * Project Management Program Management Portfolio Management Agile/Scrum
 * Management Change Management 
 * 
 * IT Service Management (ITSM):
 * 
 * IT Service Desk Incident Management Problem Management Change Management IT
 * Asset Management 
 * 
 * Enterprise Architecture:
 * 
 * Solution Architecture Infrastructure Architecture Application Architecture
 * Data Architecture Technology Strategy Business Analysis:
 * 
 * Requirements Analysis Process Modeling Stakeholder Management Business
 * Process Improvement Product Management 
 * 
 * IT Governance:
 * 
 * IT Strategy IT Policy and Compliance IT Risk Management IT Audit and Control
 * IT Vendor Management 
 * 
 * Research and Development (R&D):
 * 
 * Technology Research Innovation Prototyping Emerging Technologies
 */

