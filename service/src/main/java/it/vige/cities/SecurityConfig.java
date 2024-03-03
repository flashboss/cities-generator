package it.vige.cities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	public final static String ADMIN_ROLE = "admin";

	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		// @formatter:off
		http.authorizeHttpRequests(customizer -> customizer.requestMatchers("/update").hasAnyRole(ADMIN_ROLE)
				.anyRequest().permitAll()).csrf(csrf -> csrf.disable());
        // @formatter:on

		return http.build();
	}
}