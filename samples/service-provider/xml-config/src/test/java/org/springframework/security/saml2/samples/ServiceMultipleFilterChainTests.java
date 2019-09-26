/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.springframework.security.saml2.samples;

import java.util.Collections;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SAML Service Provider With Multiple FilterChains")
public class ServiceMultipleFilterChainTests extends AbstractServiceProviderTestBase {

	@SpringBootConfiguration
	@EnableAutoConfiguration
	@ComponentScan(basePackages = "org/springframework/security/saml2/samples")
	public static class SpringBootApplicationTestConfig {
	}

	@Test
	@DisplayName("pages are secure")
	void securePages() throws Exception {
		for (String s : asList("/", "/resources", "/test")) {
			mockMvc.perform(
				get(s)
			)
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("http://localhost/saml/sp/login"))
			;
		}

		mockMvc.perform(
			get("/")
				.with(authentication(new UsernamePasswordAuthenticationToken("","",Collections.emptyList())))
		)
			.andExpect(status().isOk())
		;

		for (String s : asList("/resources", "/test")) {
			mockMvc.perform(
				get(s)
				.with(authentication(new UsernamePasswordAuthenticationToken("","",Collections.emptyList())))
			)
				.andExpect(status().isNotFound())
			;
		}
	}

	@Test
	@DisplayName("single provider no longer redirects automatically")
	void singleProviderNoAutomaticRedirect() throws Exception {
		mockMvc.perform(
			get("http://localhost/saml/sp/login")
		)
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().string(containsString(
				"\"/saml/sp/authenticate/simplesamlphp\""
			)))
		;
	}

	@Test
	@DisplayName("displays single IDP selection")
	void idpSelection() throws Exception {
		//no redirect without redirect parameter
		mockMvc.perform(
			get("http://localhost/saml/sp/login")
		)
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().string(containsString(
				"\"/saml/sp/authenticate/simplesamlphp\""
			)))
		;
	}

}