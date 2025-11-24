package it.vige.cities;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Spring Boot application entry point for Cities Generator REST service
 * 
 * @author lucastancapiano
 */
@EnableWebMvc
@SpringBootApplication
public class JavaAppApplication {

	/**
	 * Main method to start the Spring Boot application
	 * 
	 * @param args command line arguments
	 * @throws Exception if there is a problem starting the application
	 */
	public static void main(String[] args) throws Exception {
		run(JavaAppApplication.class, args);
	}
}
