package com.isteer.springbootjdbc;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootJdbcApplication {
	
	public static void main(String[] args) {
		
		DOMConfigurator.configure("src/main/resources/log4j.xml"); 
		
		SpringApplication.run(SpringbootJdbcApplication.class, args);
	}
	
}