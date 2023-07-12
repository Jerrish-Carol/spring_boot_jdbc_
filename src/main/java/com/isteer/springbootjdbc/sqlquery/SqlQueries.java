package com.isteer.springbootjdbc.sqlquery;

public class SqlQueries {

	public static final String INSERT_EMPLOYEE_QUERY="INSERT INTO tbl_employees(name, dob, gender, email, department, role_id) VALUES (? ,? ,?, ?, ?, ?)";
	public static final String INSERT_ADDRESS_QUERY="INSERT INTO tbl_address(address_id, employee_id,street,city,state,country) VALUES (? ,? ,?, ?, ?, ?)";
	public static final String INSERT_ROLES_QUERY="INSERT INTO tbl_roles(role, project, billable, hierarchical_level, bu_name, bu_head, hr_manager) VALUES (? ,? ,?, ?, ?, ?, ?)";
	
	public static final String UPDATE_EMPLOYEES_BY_ID_QUERY="UPDATE tbl_employees SET name = ?, dob = ?, gender = ?, is_active = ?,is_account_locked = ?, email = ?, department = ?, role_id = ? WHERE id = ?";
	public static final String UPDATE_ADDRESS_BY_ID_QUERY="UPDATE tbl_address SET street=?, city=?, state=?, country=? WHERE employee_id=?";
	public static final String UPDATE_ROLES_BY_ID_QUERY="UPDATE tbl_roles SET role=?, project=?, billable=?, hierarchical_level=?, bu_name=?, bu_head=?, hr_manager=? WHERE role_id=?";
	
	public static final String GET_EMPLOYEES_BY_ID_QUERY="SELECT * FROM tbl_employees WHERE ID=?";
	public static final String GET_ADDRESS_BY_ID_QUERY="SELECT * FROM tbl_address WHERE employee_id=?";
	public static final String GET_ROLES_BY_ID_QUERY="SELECT * FROM tbl_roles WHERE role_id=?";
	
	public static final String DELETE_EMPLOYEES_BY_ID_QUERY="DELETE FROM tbl_employees WHERE ID=?";
	public static final String DELETE_ROLES_BY_ID_QUERY="DELETE FROM tbl_roles WHERE role_id=?";
	public static final String DELETE_ADDRESS_BY_ID_QUERY="DELETE FROM tbl_address WHERE employee_id=?";
	
	public static final String GET_EMPLOYEES_QUERY="SELECT * FROM tbl_employees";
	public static final String GET_ADDRESS_QUERY="SELECT * FROM tbl_address";
	public static final String GET_ROLES_QUERY="SELECT * FROM tbl_roles";
	
	public static final String CHECK_ID_IS_PRESENT_QUERY="SELECT EXISTS(SELECT * from tbl_employees WHERE ID=?)";
	public static final String CHECK_ROLE_ID_IS_PRESENT_QUERY="SELECT EXISTS(SELECT * from tbl_roles WHERE role_id=?)";

	public static final String GET_EMPLOYEES_AND_ROLE_QUERY="SELECT * FROM tbl_employees FULL OUTER JOIN tbl_roles ON tbl_employees.id =  tbl_roles.role_id";
	public static final String GET_EMPID_AND_ROLEID="SELECT * FROM tbl_employees FULL OUTER JOIN tbl_roles ON tbl_employees.id =  tbl_roles.role_id";
		
	public static final String GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_QUERY="SELECT t1.id, t1.name, t1.dob, t1.gender, t1.is_active, t1.is_account_locked, t1.email, t1.department, t1.role_id, " +
            "t2.employee_id, t2.address_id, t2.street, t2.city, t2.state, t2.country, " +
            "t3.role_id, t3.role, t3.project, t3.billable, t3.hierarchical_level, t3.bu_name, t3.bu_head, t3.hr_manager " +
            "FROM tbl_employees t1 " +
            "LEFT JOIN tbl_address t2 ON t1.id = t2.employee_id " +
            "LEFT JOIN tbl_roles t3 ON t1.role_id = t3.role_id " +
            "GROUP BY t1.id, t1.name, t1.dob, t1.gender, t1.is_active, t1.is_account_locked, t1.email, t1.department, t1.role_id, " +
            "t2.employee_id, t2.address_id, t2.street, t2.city, t2.state, t2.country, " +
            "t3.role_id, t3.role, t3.project, t3.billable, t3.hierarchical_level, t3.bu_name, t3.bu_head, t3.hr_manager";
	
	public static final String GET_EMPLOYEE_ADDRESSES_ROLE_GROUPBY_USING_ID_QUERY="SELECT t1.id, t1.name, t1.dob, t1.gender, t1.is_active, t1.is_account_locked, t1.email, t1.department, t1.role_id, " +
            "t2.employee_id, t2.address_id, t2.street, t2.city, t2.state, t2.country, " +
            "t3.role_id, t3.role, t3.project, t3.billable, t3.hierarchical_level, t3.bu_name, t3.bu_head, t3.hr_manager " +
            "FROM tbl_employees t1 " +
            "LEFT JOIN tbl_address t2 ON t1.id = t2.employee_id " +
            "LEFT JOIN tbl_roles t3 ON t1.role_id = t3.role_id " +
            "WHERE t1.id = ? " +
            "GROUP BY t1.id, t1.name, t1.dob, t1.gender, t1.is_active, t1.is_account_locked, t1.email, t1.department, t1.role_id, "+
            "t2.employee_id, t2.address_id, t2.street, t2.city, t2.state, t2.country, "+
            "t3.role_id, t3.role, t3.project, t3.billable, t3.hierarchical_level, t3.bu_name, t3.bu_head, t3.hr_manager";
	
	
}
