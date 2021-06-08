package com.saml.spsaml;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;

@Configuration
public class SecurityConfig {
//	@Bean
//	RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
//		RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
//				.fromMetadataLocation("https://simplesaml-for-spring-saml.apps.pcfone.io/saml2/idp/metadata.php")
//				.registrationId("one")
//				.build();
//		return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
//	}
	
	@Bean
	RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
		RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
				.fromMetadataLocation("http://localhost:8080/auth/realms/samlsample/protocol/saml/descriptor")
				.registrationId("one")
				.build();
		return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
	}
	
//	RelyingPartyRegistration relyingPartyRegistration2 = RelyingPartyRegistrations
//	        .fromMetadataLocation("http://localhost:8080/metadata")
//	        .registrationId("my-id")
//	        .build();
	

}
