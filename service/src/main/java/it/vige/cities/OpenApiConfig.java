package it.vige.cities;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * OpenAPI configuration for Swagger/OpenAPI documentation
 * Configures OAuth2 security scheme and API documentation
 * 
 * @author lucastancapiano
 */
@Configuration
public class OpenApiConfig {

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
	 * OAuth2 client ID (resource)
	 */
	@Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
	private String resource;

	/**
	 * OAuth scheme name for OpenAPI security
	 */
	private static final String OAUTH_SCHEME_NAME = "Cities Generator Auth";

	/**
	 * Create OpenAPI configuration bean
	 * Configures OAuth2 security scheme and customizes Configuration schema
	 * 
	 * @return the OpenAPI configuration
	 */
	@Bean
	public OpenAPI openAPI() {
		Components components = new Components();
		components.addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme());
		
		// Customize Configuration schema to show language as string with example "it"
		Schema<?> configurationSchema = new Schema<>()
				.addProperty("country", new StringSchema().example("IT"))
				.addProperty("caseSensitive", new Schema<>().type("boolean").example(true))
				.addProperty("duplicatedNames", new Schema<>().type("boolean").example(true))
				.addProperty("provider", new StringSchema().example("GEONAMES"))
				.addProperty("username", new StringSchema().example("username"))
				.addProperty("language", new StringSchema()
						.description("Language code (e.g., 'it', 'en', 'fr', 'de', 'es', 'pt')")
						.example("it"));
		
		components.addSchemas("Configuration", configurationSchema);
		
		return new OpenAPI()
				.components(components)
				.addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME))
				.info(new Info().title("Cities Generator").description("Web console").version("1.0"));
	}

	/**
	 * Create OAuth2 security scheme
	 * 
	 * @return the SecurityScheme configured for OAuth2
	 */
	private SecurityScheme createOAuthScheme() {
		OAuthFlows flows = createOAuthFlows();
		return new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows);
	}

	/**
	 * Create OAuth2 flows configuration
	 * 
	 * @return the OAuthFlows configured with implicit flow
	 */
	private OAuthFlows createOAuthFlows() {
		OAuthFlow flow = createAuthorizationCodeFlow();
		return new OAuthFlows().implicit(flow);
	}

	/**
	 * Create authorization code flow for OAuth2
	 * 
	 * @return the OAuthFlow with authorization URL configured
	 */
	private OAuthFlow createAuthorizationCodeFlow() {
		return new OAuthFlow().authorizationUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth");
	}
}
