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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;

	@Value("${keycloak.realm}")
	private String realm;
	
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

	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new RealmRolesConverter());
		return jwtAuthenticationConverter;
	}

	private static class RealmRolesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
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

	@Bean
	JwtDecoder jwtDecoder() {
		// Costruisci jwk-set-uri dinamicamente da auth-server-url e realm
		String jwkSetUri = authServerUrl.replaceAll("/$", "") + "/realms/" + realm + "/protocol/openid-connect/certs";
		return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
	}
}