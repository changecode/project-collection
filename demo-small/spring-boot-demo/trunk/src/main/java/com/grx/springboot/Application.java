package com.grx.springboot;

import java.io.Serializable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@Configuration
//@ComponentScan(basePackages={"com.bpm", "com.grx"})
//@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages={"com.bpm", "com.grx"})
public class Application implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8163563509048445942L;
	
	public static void main(String[] args) {
		// port see application.properties
		SpringApplication.run(Application.class);
	}
}
