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

package org.springframework.security.saml2.serviceprovider.samples;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.Saml2ServiceProviderConfigurer;
import org.springframework.security.saml2.serviceprovider.registration.Saml2KeyData;
import org.springframework.security.saml2.serviceprovider.registration.Saml2KeyType;

import static java.util.Collections.singletonList;
import static org.springframework.security.saml2.serviceprovider.registration.Saml2KeyData.signatureVerificationKey;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String SIMPLESAMLPHP_IDP_INITIATE_URL =
		"http://simplesaml-for-spring-saml.cfapps.io/saml2/idp/SSOService.php?spentityid=http://localhost:8080/sample-sp";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http
			.authorizeRequests()
				.mvcMatchers("/", "/index").permitAll()
				.and()
			.logout()
				.logoutSuccessUrl("/")
				.and()
			//saml security
			.apply(
				Saml2ServiceProviderConfigurer.saml2Login()
					.serviceProviderEntityId("http://localhost:8080/sample-sp")
					.addServiceProviderKey(new Saml2KeyData(
							"sp-signing-key",
							privateKey,
							certificate,
							privateKeyPassphrase,
							Saml2KeyType.SIGNING
						)
					)
					.addIdentityProvider(idp -> {
							idp.setEntityId("http://simplesaml-for-spring-saml.cfapps.io/saml2/idp/metadata.php");
							idp.setVerificationKeys(singletonList(
								signatureVerificationKey(
									"simplesamlphp-verification-key",
									idpCertificate
								)
							));
						}
					)
			)
		;
		//@formatter:on
	}

	//service provider keys (local keys)
	private String privateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
		"Proc-Type: 4,ENCRYPTED\n" +
		"DEK-Info: DES-EDE3-CBC,7C8510E4CED17A9F\n" +
		"\n" +
		"SRYezKuY+AgM+gdiklVDBQ1ljeCFKnW3c5BM9sEyEOfkQm0zZx6fLr0afup0ToE4\n" +
		"iJGLxKw8swAnUAIjYda9wxqIEBb9mILyuRPevyfzmio2lE9KnARDEYRBqbwD9Lpd\n" +
		"vwZKNGHHJbZAgcUNfhXiYakmx0cUyp8HeO3Vqa/0XMiI/HAdlJ/ruYeT4e2DSrz9\n" +
		"ORZA2S5OvNpRQeCVf26l6ODKXnkDL0t5fDVY4lAhaiyhZtoT0sADlPIERBw73kHm\n" +
		"fGCTniY9qT0DT+R5Rqukk42mN2ij/cAr+kdV5colBi1fuN6d9gawCiH4zSb3LzHQ\n" +
		"9ccSlz6iQV1Ty2cRuTkB3zWC6Oy4q0BRlXnVRFOnOfYJztO6c2hD3Q9NxkDAbcgR\n" +
		"YWJWHpd0/HI8GyBpOG7hAS1l6aoleH30QCDOo7N2rFrTAaPC6g84oZOFSqkqvx4R\n" +
		"KTbWRwgJsqVxM6GqV6H9x1LNn2CpBizdGnp8VvnIiYcEvItMJbT1C1yeIUPoDDU2\n" +
		"Ct0Jofw/dquXStHWftPFjpIqB+5Ou//HQ2VNzjbyThNWVGtjnEKwSiHacQLS1sB3\n" +
		"iqFtSN/VCpdOcRujEBba+x5vlc8XCV1qr6x1PbvfPZVjyFdSM6JQidr0uEeDGDW3\n" +
		"TuYC1YgURN8zh0QF2lJIMX3xgbhr8HHNXv60ulcjeqYmna6VCS8AKJQgRTr4DGWt\n" +
		"Afv9BFV943Yp3nHwPC7nYC4FvMxOn4qW4KrHRJl57zcY6VDL4J030CfmvLjqUbuT\n" +
		"LYiQp/YgFlmoE4bcGuCiaRfUJZCwooPK2dQMoIvMZeVl9ExUGdXVMg==\n" +
		"-----END RSA PRIVATE KEY-----";
	private String privateKeyPassphrase = "sppassword";
	private String certificate = "-----BEGIN CERTIFICATE-----\n" +
		"MIICgTCCAeoCCQCuVzyqFgMSyDANBgkqhkiG9w0BAQsFADCBhDELMAkGA1UEBhMC\n" +
		"VVMxEzARBgNVBAgMCldhc2hpbmd0b24xEjAQBgNVBAcMCVZhbmNvdXZlcjEdMBsG\n" +
		"A1UECgwUU3ByaW5nIFNlY3VyaXR5IFNBTUwxCzAJBgNVBAsMAnNwMSAwHgYDVQQD\n" +
		"DBdzcC5zcHJpbmcuc2VjdXJpdHkuc2FtbDAeFw0xODA1MTQxNDMwNDRaFw0yODA1\n" +
		"MTExNDMwNDRaMIGEMQswCQYDVQQGEwJVUzETMBEGA1UECAwKV2FzaGluZ3RvbjES\n" +
		"MBAGA1UEBwwJVmFuY291dmVyMR0wGwYDVQQKDBRTcHJpbmcgU2VjdXJpdHkgU0FN\n" +
		"TDELMAkGA1UECwwCc3AxIDAeBgNVBAMMF3NwLnNwcmluZy5zZWN1cml0eS5zYW1s\n" +
		"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDRu7/EI0BlNzMEBFVAcbx+lLos\n" +
		"vzIWU+01dGTY8gBdhMQNYKZ92lMceo2CuVJ66cUURPym3i7nGGzoSnAxAre+0YIM\n" +
		"+U0razrWtAUE735bkcqELZkOTZLelaoOztmWqRbe5OuEmpewH7cx+kNgcVjdctOG\n" +
		"y3Q6x+I4qakY/9qhBQIDAQABMA0GCSqGSIb3DQEBCwUAA4GBAAeViTvHOyQopWEi\n" +
		"XOfI2Z9eukwrSknDwq/zscR0YxwwqDBMt/QdAODfSwAfnciiYLkmEjlozWRtOeN+\n" +
		"qK7UFgP1bRl5qksrYX5S0z2iGJh0GvonLUt3e20Ssfl5tTEDDnAEUMLfBkyaxEHD\n" +
		"RZ/nbTJ7VTeZOSyRoVn5XHhpuJ0B\n" +
		"-----END CERTIFICATE-----";

	private String idpCertificate = "MIIEEzCCAvugAwIBAgIJAIc1qzLrv+5nMA0GCSqGSIb3DQEBCwUAMIGfMQswCQYD\n" +
		"VQQGEwJVUzELMAkGA1UECAwCQ08xFDASBgNVBAcMC0Nhc3RsZSBSb2NrMRwwGgYD\n" +
		"VQQKDBNTYW1sIFRlc3RpbmcgU2VydmVyMQswCQYDVQQLDAJJVDEgMB4GA1UEAwwX\n" +
		"c2ltcGxlc2FtbHBocC5jZmFwcHMuaW8xIDAeBgkqhkiG9w0BCQEWEWZoYW5pa0Bw\n" +
		"aXZvdGFsLmlvMB4XDTE1MDIyMzIyNDUwM1oXDTI1MDIyMjIyNDUwM1owgZ8xCzAJ\n" +
		"BgNVBAYTAlVTMQswCQYDVQQIDAJDTzEUMBIGA1UEBwwLQ2FzdGxlIFJvY2sxHDAa\n" +
		"BgNVBAoME1NhbWwgVGVzdGluZyBTZXJ2ZXIxCzAJBgNVBAsMAklUMSAwHgYDVQQD\n" +
		"DBdzaW1wbGVzYW1scGhwLmNmYXBwcy5pbzEgMB4GCSqGSIb3DQEJARYRZmhhbmlr\n" +
		"QHBpdm90YWwuaW8wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC4cn62\n" +
		"E1xLqpN34PmbrKBbkOXFjzWgJ9b+pXuaRft6A339uuIQeoeH5qeSKRVTl32L0gdz\n" +
		"2ZivLwZXW+cqvftVW1tvEHvzJFyxeTW3fCUeCQsebLnA2qRa07RkxTo6Nf244mWW\n" +
		"RDodcoHEfDUSbxfTZ6IExSojSIU2RnD6WllYWFdD1GFpBJOmQB8rAc8wJIBdHFdQ\n" +
		"nX8Ttl7hZ6rtgqEYMzYVMuJ2F2r1HSU1zSAvwpdYP6rRGFRJEfdA9mm3WKfNLSc5\n" +
		"cljz0X/TXy0vVlAV95l9qcfFzPmrkNIst9FZSwpvB49LyAVke04FQPPwLgVH4gph\n" +
		"iJH3jvZ7I+J5lS8VAgMBAAGjUDBOMB0GA1UdDgQWBBTTyP6Cc5HlBJ5+ucVCwGc5\n" +
		"ogKNGzAfBgNVHSMEGDAWgBTTyP6Cc5HlBJ5+ucVCwGc5ogKNGzAMBgNVHRMEBTAD\n" +
		"AQH/MA0GCSqGSIb3DQEBCwUAA4IBAQAvMS4EQeP/ipV4jOG5lO6/tYCb/iJeAduO\n" +
		"nRhkJk0DbX329lDLZhTTL/x/w/9muCVcvLrzEp6PN+VWfw5E5FWtZN0yhGtP9R+v\n" +
		"ZnrV+oc2zGD+no1/ySFOe3EiJCO5dehxKjYEmBRv5sU/LZFKZpozKN/BMEa6CqLu\n" +
		"xbzb7ykxVr7EVFXwltPxzE9TmL9OACNNyF5eJHWMRMllarUvkcXlh4pux4ks9e6z\n" +
		"V9DQBy2zds9f1I3qxg0eX6JnGrXi/ZiCT+lJgVe3ZFXiejiLAiKB04sXW3ti0LW3\n" +
		"lx13Y1YlQ4/tlpgTgfIJxKV6nyPiLoK0nywbMd+vpAirDt2Oc+hk";

}

