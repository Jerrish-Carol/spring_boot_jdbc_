package com.isteer.springbootjdbc.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import com.isteer.springbootjdbc.exception.DetailsNotProvidedException;
import com.isteer.springbootjdbc.model.Address;
import com.isteer.springbootjdbc.model.Employee;
import com.isteer.springbootjdbc.response.CustomDeleteResponse;
import com.isteer.springbootjdbc.response.CustomGetResponse;
import com.isteer.springbootjdbc.response.CustomPostResponse;
import com.isteer.springbootjdbc.sqlquery.SqlQueries;

@Repository 
public class EmployeeDaoImpl implements EmployeeDAO {
	
	private static Logger logger = Logger.getLogger(EmployeeDaoImpl.class); 
	
	@Autowired
	private JdbcTemplate jdbcTemplate; //spring will create this and put in Ioc Container

	public CustomPostResponse save(Employee employee) { 
		
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		
		if(jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_EMPLOYEE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
     //queries are provided along with an indication whether to return auto generated key value
            ps.setString(1, employee.getName());
            ps.setString(2, employee.getDob());
            ps.setString(3, employee.getGender());
            ps.setString(4, employee.getEmail());
            ps.setString(5, employee.getDepartment());
           
            return ps;
        }, keyHolder)==1) {
			employee.setId(keyHolder.getKey().longValue());
			}
			 return new CustomPostResponse(1, "SAVED", employee);
					 
		}

	@Override
	public CustomPostResponse update(Employee employee, long id) {

		if (jdbcTemplate.update(SqlQueries.UPDATE_EMPLOYEES_BY_ID_QUERY, new Object[] { employee.getName(),
			employee.getDob(), employee.getGender(), employee.getEmail(), employee.getDepartment(), employee.getAddresses(), id }) == 1) {
			employee.setId(id);
			return new CustomPostResponse(1, "UPDATED", jdbcTemplate.queryForObject(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new BeanPropertyRowMapper<Employee>() {
				
				public ResultSetExtractor<List<Address>> addressResultSetExtractor = rset -> {
			        List<Address> addresses = new ArrayList<>();
			        while (rset.next()) {
			            Address address = new Address();
			            address.setAddress_id(rset.getLong("address_id"));
			            address.setEmployee_id(rset.getLong("id"));
			            address.setStreet(rset.getString("street"));
			            address.setCity(rset.getString("city"));
			            address.setCountry(rset.getString("country"));
			            employee.getAddresses().add(address);
			            addresses.add(address);
			            System.out.println(address);
			           
			        }
			        return addresses;
				};
				
				public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
					Employee employee = new Employee();
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
				    
					System.out.println("employee");	
				return employee;
				}

			},id));
		

		}

		else {
			List<String> exception = new ArrayList<>();
			exception.add("Provide all details required");
			throw new DetailsNotProvidedException(0, "NOT_SAVED", exception);
		}
	}

	@Override
	public CustomDeleteResponse delete(long id) {
		List<String> statement = new ArrayList<>();
		jdbcTemplate.update(SqlQueries.DELETE_EMPLOYEES_BY_ID_QUERY, id );
		statement.add("Data in id "+id+" is deleted");
		return new CustomDeleteResponse(1, "DELETED", statement);
	}

	@Override
	public List<Employee> getAll() {
		return jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_QUERY, new BeanPropertyRowMapper<Employee>() {
				
				public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
					Employee employee = new Employee();
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
					
	               
				return employee;
				}	
		});
	}

	@Override
	public Employee getById(long id) {

		Employee e=jdbcTemplate.query(SqlQueries.GET_EMPLOYEES_BY_ID_QUERY, new ResultSetExtractor<Employee>() {
            
           public Employee extractData(ResultSet rs) throws SQLException {
				
					Employee employee = new Employee();
					while(rs.next()) {
					employee.setId(rs.getLong("id"));
					employee.setName(rs.getString("name"));
					employee.setEmail(rs.getString("email"));
					employee.setDob(rs.getString("dob"));
					employee.setGender(rs.getString("gender"));
					employee.setDepartment(rs.getString("department"));
					employee.setIsAccountLocked(rs.getBoolean("is_account_locked"));
					employee.setIsActive(rs.getBoolean("is_active"));
					employee.setAddresses(
							
					jdbcTemplate.query(SqlQueries.GET_ADDRESS_BY_ID_QUERY, new ResultSetExtractor <List<Address>>() {
						
						@Override
						public List<Address> extractData(ResultSet rset) throws SQLException, DataAccessException {
							List<Address> addresses = new ArrayList<Address>();
							logger.info("inside setaddresses");
							System.out.println(rset);
							while(rset.next()) {
								logger.info("inside rs.next");
								Address address = new Address();
								address.setAddress_id(rset.getLong("address_id"));
								address.setEmployee_id(id);
								address.setStreet(rset.getString("street"));
								address.setState(rset.getString("state"));
								address.setCity(rset.getString("city"));
								address.setCountry(rset.getString("country"));
								addresses.add(address);
							}
							return addresses;
							
						}},id));
				
					}
					return employee;
					}},id);
		
		return e;
	}
}
		

	


	
	/*public Employee getEmployeeWithAddresses(Long id) {
        String sql = "SELECT e.id, e.name,e. a.id AS address_id, a.street, a.city, a.country " +
                     "FROM employee e " +
                     "LEFT JOIN address a ON e.id = a.employee_id " +
                     "WHERE e.id = ?";

        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            Employee employee = null;
            while (rs.next()) {
                if (employee == null) {
                    employee = new Employee();
                    employee.setId(rs.getLong("id"));
                    employee.setName(rs.getString("name"));
                    employee.setAddresses(new ArrayList<>());
                }

                Address address = new Address();
                address.setId(rs.getLong("address_id"));
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setCountry(rs.getString("country"));

                employee.getAddresses().add(address);
            }
            return employee;
        });
    }*/

//	public CustomPostResponse save(Employee employee , List<Address> addresses) { 
//		
//		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
//		
//		if(jdbcTemplate.update(con -> {
//            PreparedStatement ps = con.prepareStatement(SqlQueries.INSERT_EMPLOYEE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
//     //queries are provided along with an indication whether to return auto generated key value
//            ps.setString(1, employee.getName());
//            ps.setString(2, employee.getDob());
//            ps.setString(3, employee.getGender());
//            ps.setString(4, employee.getEmail());
//            ps.setString(5, employee.getDepartment());
//            
//          
//            return ps;
//        }, keyHolder)==1) {
//			employee.setId(keyHolder.getKey().longValue());
//			jdbcTemplate.batchUpdate(SqlQueries.INSERT_ADDRESS_QUERY, new BatchPreparedStatementSetter() {
//			       
//				@Override
//	            public void setValues(PreparedStatement ps, int i) throws SQLException {
//	                Address address = addresses.get(i);
//	                ps.setLong(1, address.getAddress_ID());
//	                ps.setLong(2, address.getDoorNo());
//	                ps.setString(3, address.getStreet());
//	                ps.setString(4, address.getState());
//	                ps.setString(5, address.getCity());
//	                employee.setAddress(addresses);
//	                
//	                System.out.println(employee);
//	            }
//				
//				
//	            
//	            @Override
//	            public int getBatchSize() {
//	                return addresses.size();
//	            }
//	            
//	            
//	        });
//			
//			
//			
//			return new CustomPostResponse(1, "SAVED", employee);
//		}
//		else {
//			List<String> exception = new ArrayList<>();
//			exception.add("Provide all details required");
//			throw new DetailsNotProvidedException(0, "NOT_SAVED", exception);
//		}
//	}
	
	/*@Override
	public CustomListPostResponse save(List<Address> addresses) { 
	
		
		jdbcTemplate.batchUpdate(SqlQueries.INSERT_ADDRESS_QUERY, new BatchPreparedStatementSetter() {
			       
			@Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Address address = addresses.get(i);
              
                ps.setLong(1, address.getAddress_id());
                ps.setString(2, address.getStreet());
                ps.setString(3, address.getState());
                ps.setString(4, address.getCity());
                ps.setString(5, address.getCountry());
            }
            
            @Override
            public int getBatchSize() {
                return addresses.size();
            }
        });
		
		return new CustomListPostResponse(1, "SAVED", addresses);
    }
*/
			

	



	