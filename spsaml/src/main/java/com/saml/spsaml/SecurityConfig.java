package com.saml.spsaml;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Bean
	RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
		RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
				.fromMetadataLocation("https://simplesaml-for-spring-saml.apps.pcfone.io/saml2/idp/metadata.php")
				.registrationId("one")
				.build();
		return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
	}
	
	protected void configure(HttpSecurity http) throws Exception {
		
		Converter<HttpServletRequest, RelyingPartyRegistration> relyingPartyRegistrationResolver =
		        new DefaultRelyingPartyRegistrationResolver(this.relyingPartyRegistrationRepository());
		Saml2MetadataFilter filter = new Saml2MetadataFilter(
		        relyingPartyRegistrationResolver,
		        new OpenSamlMetadataResolver());

		
		X509Certificate certificate = relyingPartyDecryptionCertificate();
		Resource resource = new ClassPathResource("rp.crt");
		try (InputStream is = resource.getInputStream()) {
		    RSAPrivateKey rsa = RsaKeyConverters.pkcs8().convert(is);
		    return Saml2X509Credential.decryption(rsa, certificate);
		}
		
		
	    http.authorizeRequests(authorize -> authorize
	        .anyRequest().authenticated()
	        ).saml2Login(withDefaults())
	    .addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class);
	    
	    

		
	}


	
	
//	@Bean
//	RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
//		RelyingPartyRegistration relyingPartyRegistration = RelyingPartyRegistrations
//				.fromMetadataLocation("http://localhost:8080/auth/realms/samlsample/protocol/saml/descriptor")
//				.registrationId("one")
//				.build();
//		return new InMemoryRelyingPartyRegistrationRepository(relyingPartyRegistration);
//	}
	
//	RelyingPartyRegistration relyingPartyRegistration2 = RelyingPartyRegistrations
//	        .fromMetadataLocation("http://localhost:8080/metadata")
//	        .registrationId("my-id")
//	        .build();
	

}
