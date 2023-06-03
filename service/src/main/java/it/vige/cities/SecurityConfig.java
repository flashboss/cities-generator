package it.vige.cities;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain web(HttpSecurity http) throws Exception {
		http.httpBasic(auth -> auth.disable()).formLogin(auth -> auth.disable())
				.authorizeHttpRequests((authorize) -> authorize.requestMatchers("/**").permitAll()
						.requestMatchers("/update").hasAuthority("admin").anyRequest().authenticated());
		return http.build();
	}

}
