package com.isteer.springbootjdbc.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity

public class Employee {

	@NonNull
	private long id;

	// @Column(name = "name") not required if field name is as same as column name
	@NonNull
	@Size(min = 5, message = "must be atleast 5 characters")
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
