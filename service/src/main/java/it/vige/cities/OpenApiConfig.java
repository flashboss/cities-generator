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

@Configuration
public class OpenApiConfig {

	@Value("${keycloak.auth-server-url}")
	private String authServerUrl;

	@Value("${keycloak.realm}")
	private String realm;

	@Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
	private String resource;

	private static final String OAUTH_SCHEME_NAME = "Cities Generator Auth";

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

	private SecurityScheme createOAuthScheme() {
		OAuthFlows flows = createOAuthFlows();
		return new SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(flows);
	}

	private OAuthFlows createOAuthFlows() {
		OAuthFlow flow = createAuthorizationCodeFlow();
		return new OAuthFlows().implicit(flow);
	}

	private OAuthFlow createAuthorizationCodeFlow() {
		return new OAuthFlow().authorizationUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth");
	}
}
