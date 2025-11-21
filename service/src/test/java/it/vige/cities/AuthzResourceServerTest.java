package it.vige.cities;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for {@link OAuth2ResourceServerApplication}.
 *
 * @author Josh Cummings
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthzResourceServerTest {

	@Autowired
	MockMvc mvc;

	@Test
	void testValidBearerToken() throws Exception {
		// Test with default values
		this.mvc.perform(get("/cities")).andExpect(status().isOk())
				.andExpect(content().string(containsString("level")));
		
		// Test with explicit country and language parameters
		this.mvc.perform(get("/cities").param("country", "IT").param("language", "it"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("level")));
		
		// Test with different country and language
		this.mvc.perform(get("/cities").param("country", "GB").param("language", "en"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("level")));
	}

	@Test
	@WithMockUser(roles = "admin")
	void testOnlyAdminUsers() throws Exception {
		this.mvc.perform(post("/update").contentType(MediaType.APPLICATION_JSON).content("{ \"provider\":\"NONE\" }"))
				.andExpect(status().isOk())
				.andExpect(content().string(containsString("")));
	}

	@Test
	@WithMockUser(roles = "user")
	void testNonAdminUsers() throws Exception {
		this.mvc.perform(post("/update").contentType(MediaType.APPLICATION_JSON).content("{ \"provider\":\"NONE\" }"))
				.andExpect(status().isForbidden());
	}

	@Test
	void testInvalidBearerToken() throws Exception {
		this.mvc.perform(get("/cities")).andExpect(status().isOk());
	}
}
