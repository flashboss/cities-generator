package it.vige.cities;

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class JavaAppApplication {

	public static void main(String[] args) throws Exception {
		run(JavaAppApplication.class, args);
	}
}
