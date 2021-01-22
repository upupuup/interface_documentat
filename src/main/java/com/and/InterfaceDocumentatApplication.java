package com.and;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class InterfaceDocumentatApplication {

	public static void main(String[] args) {
		SpringApplication.run(InterfaceDocumentatApplication.class, args);
	}

}
