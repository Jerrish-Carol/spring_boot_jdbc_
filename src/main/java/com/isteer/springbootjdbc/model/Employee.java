package com.isteer.springbootjdbc.model;

import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	// @Column(name = "name") not required if field name is as same as column name
	@NonNull
	@Size(min = 4, message = "Username must be atleast 4 characters")
	private String name;

	@NonNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String dob;

	@NotEmpty
	private String gender;

	@Value("${employee.isActive}")
	private Boolean isActive;

	@Value("${employee.isAccountLocked}")
	private Boolean isAccountLocked;

	@NotEmpty
	@Email(message = "email address is not valid")
	private String email;

	@NotEmpty
	private String department;

}
