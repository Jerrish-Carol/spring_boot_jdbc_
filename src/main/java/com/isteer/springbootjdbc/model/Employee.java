package com.isteer.springbootjdbc.model;

import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity  
@Getter
@Setter
@Table(name="tbl_employees") 
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	// @Column(name = "name") not required if field name is as same as column name
	@NonNull
	@Size(min = 4, message = "must be atleast 4 characters")
	private String name;

	@NotEmpty
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String dob;

	@NotEmpty
	@Pattern(regexp="Female|Male|Other", message="must be Female|Male|Other")
	private String gender;

	
	@Value("${employee.isActive}")
	private Boolean isActive;

	@Value("${employee.isAccountLocked}")
	private Boolean isAccountLocked;

	@NotEmpty
	@Email(message = "address is not valid")
	private String email;

	@NotEmpty
	private String department;

}
