package it.vige.cities;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security configuration for OAuth2 resource server
 * Configures JWT authentication and authorization using Keycloak
 * 
 * @author lucastancapiano
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * Keycloak authentication server URL
	 */
	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;

	/**
	 * Keycloak realm name
	 */
	@Value("${keycloak.realm}")
	private String realm;
	
	/**
	 * Configure security filter chain
	 * Sets up OAuth2 resource server with JWT authentication
	 * Requires "admin" role for /update* endpoints, permits all other requests
	 * 
	 * @param http the HttpSecurity to configure
	 * @return the configured SecurityFilterChain
	 * @throws Exception if there is a problem configuring security
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(
				customizer -> customizer
					.requestMatchers("/update*").hasRole("admin")
					.anyRequest().permitAll())
				.csrf(csrf -> csrf.disable())
				.oauth2ResourceServer(oauth2 -> oauth2
					.jwt(jwt -> jwt
						.jwtAuthenticationConverter(jwtAuthenticationConverter())
					)
				);
		return http.build();
	}

	/**
	 * Create JWT authentication converter bean
	 * Configures converter to extract roles from JWT realm_access claim
	 * 
	 * @return the configured JwtAuthenticationConverter
	 */
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RealmRolesConverter());
		return jwtAuthenticationConverter;
	}

	/**
	 * Converter to extract realm roles from JWT and convert them to GrantedAuthority
	 * Extracts roles from realm_access.roles claim and prefixes them with "ROLE_"
	 */
	private static class RealmRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
		/**
		 * Convert JWT to collection of GrantedAuthority
		 * Extracts roles from realm_access.roles claim
		 * 
		 * @param jwt the JWT token
		 * @return collection of GrantedAuthority with roles prefixed with "ROLE_"
		 */
		@Override
		public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
			// Estrai realm_access dal JWT
			Map<String, Object> realmAccess = jwt.getClaim("realm_access");
			if (realmAccess == null) {
				return Collections.emptyList();
			}

			// Estrai i ruoli da realm_access.roles
			@SuppressWarnings("unchecked")
			Collection<String> roles = (Collection<String>) realmAccess.get("roles");
			if (roles == null || roles.isEmpty()) {
				return Collections.emptyList();
			}

			// Converti i ruoli in GrantedAuthority con prefisso ROLE_
			return roles.stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
					.collect(Collectors.toList());
		}
	}

	/**
	 * Create JWT decoder bean
	 * Builds JWK Set URI dynamically from Keycloak auth-server-url and realm
	 * 
	 * @return the configured JwtDecoder
	 */
	@Bean
	JwtDecoder jwtDecoder() {
		// Build jwk-set-uri dynamically from auth-server-url and realm
		String jwkSetUri = authServerUrl.replaceAll("/$", "") + "/realms/" + realm + "/protocol/openid-connect/certs";
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}
}