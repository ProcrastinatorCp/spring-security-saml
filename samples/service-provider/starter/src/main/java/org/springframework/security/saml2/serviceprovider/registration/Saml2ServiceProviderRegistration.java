/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.springframework.security.saml2.serviceprovider.registration;

import java.util.LinkedList;
import java.util.List;

import org.springframework.security.saml2.credentials.Saml2X509Credential;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;


/**
 * Configuration object that represents a local(hosted) service provider
 */
public class Saml2ServiceProviderRegistration {

	private final String entityId;
	private final List<Saml2X509Credential> credentials = new LinkedList<>();

	public Saml2ServiceProviderRegistration(String entityId,
											List<Saml2X509Credential> credentials) {
		notNull(entityId, "entityId is required");
		notEmpty(credentials, "at least one private key and certificate is required for signed and encrypted messages");
		credentials.stream().forEach(c -> {
			notNull(c.getPrivateKey(), "private key required in all credentials");
			notNull(c.getCertificate(), "certificate required in all credentials");
		});
		this.entityId = entityId;
		this.credentials.addAll(credentials);
	}

	public List<Saml2X509Credential> getSaml2Credentials() {
		return credentials;
	}

	public String getEntityId() {
		return entityId;
	}

}