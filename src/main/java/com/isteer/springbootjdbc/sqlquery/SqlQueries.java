package com.isteer.springbootjdbc.sqlquery;

public class SqlQueries {

	public static final String INSERT_EMPLOYEE_QUERY="INSERT INTO tbl_employees(name, dob, gender, email, department) VALUES (? ,? ,?, ?, ?)";
	public static final String UPDATE_EMPLOYEES_BY_ID_QUERY="UPDATE tbl_employees SET name=?, dob=?, gender=?, email=?, department=? WHERE id=?";
	public static final String GET_EMPLOYEES_BY_ID_QUERY="SELECT * FROM tbl_employees WHERE ID=?";
	public static final String DELETE_EMPLOYEES_BY_ID_QUERY="DELETE FROM tbl_employees WHERE ID=?";
	public static final String GET_EMPLOYEES_QUERY="SELECT * FROM tbl_employees";
	public static final String CHECK_ID_IS_PRESENT_QUERY="SELECT EXISTS(SELECT * from tbl_employees WHERE ID=?)";

}
