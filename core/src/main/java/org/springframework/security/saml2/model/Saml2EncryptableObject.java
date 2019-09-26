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

package org.springframework.security.saml2.model;

import org.springframework.security.saml2.model.encrypt.Saml2DataEncryptionMethod;
import org.springframework.security.saml2.model.encrypt.Saml2KeyEncryptionMethod;
import org.springframework.security.saml2.model.key.Saml2KeyData;

public interface Saml2EncryptableObject<T extends Saml2Object> extends Saml2Object {

	T setEncryptionKey(Saml2KeyData encryptionKey,
					   Saml2KeyEncryptionMethod keyAlgorithm,
					   Saml2DataEncryptionMethod dataAlgorithm);


	Saml2KeyData getEncryptionKey();

	Saml2KeyEncryptionMethod getKeyAlgorithm();

	Saml2DataEncryptionMethod getDataAlgorithm();
}