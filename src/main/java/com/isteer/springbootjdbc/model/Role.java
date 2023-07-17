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
public class Role implements Serializable{

	private static final long serialVersionUID = -2087834523356714516L;

	//@JsonAlias("roleId")
	private long roleId;
	
	private String roles;
	
	private String project;
	
	private boolean billable;
	
	//@JsonAlias("hierarchicalLevel")
	private String hierarchicalLevel;
	
	private String buName;
	
	private String buHead;
	
	private String hrManager;
	
	
}


/*
 * role
 * 
Software Developer/Engineer
Full Stack Developer
Front-end Developer
Back-end Developer
Web Developer
Mobile App Developer
DevOps Engineer
Data Scientist
Data Analyst
Database Administrator
Systems Administrator
Network Engineer
Security Analyst
IT Project Manager
Associate Consultant
Associate senior Architect
Business Analyst
Quality Assurance(QA) Engineer
UX/UI Designer
Technical Support Specialist
Help Desk Technician
IT Auditor
Cloud Engineer
IT Operations Manager
IT Security Manager
IT Director
Chief Information Officer (CIO)
Chief Technology Officer (CTO)
 * 
 * 
 */

/*
 * Entry-level/Junior hierarchical_level:
 * 
 * Junior Developer/Programmer 
 * Junior Software Engineer 
 * Junior Systems Administrator 
 * Technical Support Analyst 
 * 
 * Mid-Level Positions:
 * 
 * Software Developer/Engineer 
 * Systems Administrator 
 * Database Administrator
 * Network Engineer 
 * Quality Assurance Analyst 
 * Business Analyst 
 * 
 * Senior-Level Positions:
 * 
 * Senior Software Engineer 
 * Senior Systems Administrator 
 * Technical Lead Solution
 * Architect Project Manager 
 * IT Manager 
 * 
 * Management Positions:
 * 
 * IT Director 
 * IT Operations Manager 
 * Development Manager 
 * Infrastructure Manager
 * CIO (Chief Information Officer) 
 * CTO (Chief Technology Officer) 
 * 
 * Executive Positions:
 * 
 * Vice President of Technology 
 * Chief Information Security Officer (CISO) 
 * Chief Digital Officer (CDO) 
 * CEO (Chief Executive Officer)
 */

//projects
//
//AET
//Acecash Express
//Alpha
//Andersen Windows
//AppSteer
//Archer Aviation
//Atomic Brands
//BI
//Backstop Solutions
//Boehringer Ingelheim
//Brahm
//CAT, WOC, GRAB
//CSU
//CSU
//Centria
//Cheetha
//Curebay-CMS
//DLR
//DataWareHouse
//DevOps
//DigiMIMS
//Dominion Energy
//Dominion Energy
//E-Track
//ECHO
//ENERCARE
//Electra
//Essilor - EDI
//Essilor - Tibco
//Etisalat
//FedEx: L3 Support
//Fountain House
//GNIC
//Getsecured
//Grab
//HR & Ops
//Hemlock
//IMO
//JPMC
//JSW
//Jayman BUILT
//Kawan
//LMS
//Labcorp
//Lennox
//MFA
//MFT
//MSI Kodiak XDM_Provisioning
//Mako
//Mindmill
//Multi Entity Management
//NG-CAT
//Nielsen-CINA-PMS
//Nielsen: 1BBO
//Nielsen: D2o_Coverage
//Nielsen: DevOps
//Nielsen: GNIC
//Nielsen: Navigator
//Not Joined
//OSG
//On Training
//On-Call Support
//Onboarding Formalities
//Porte
//Pre Sales
//ResMed
//Sabre
//Sales
//Sapiens
//TBDEL
//TechM
//Tibco - Veritas - Subscription Platform
//Tibco Metcash
//Tibco(Lear Corporation)
//Umnaiah Telecom
//Vituity
//WOC
//X - Peri
//Ziggo
//eTrack