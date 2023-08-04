package com.modelop.springbootsaml.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private RelyingPartyRegistrationRepository relyingPartyRegistrationRepository;

	public WebSecurityConfig(RelyingPartyRegistrationRepository relyingPartyRegistrationRepository) {
		this.relyingPartyRegistrationRepository = relyingPartyRegistrationRepository;
	}


	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {

		OpenSamlMetadataResolver openSamlMetadataResolver = new OpenSamlMetadataResolver();
		RelyingPartyRegistrationResolver relyingPartyRegistrationResolver = new DefaultRelyingPartyRegistrationResolver(this.relyingPartyRegistrationRepository);
		Saml2MetadataFilter filter = new Saml2MetadataFilter(relyingPartyRegistrationResolver, openSamlMetadataResolver);
		http.authorizeHttpRequests(authorize -> authorize.anyRequest()
						.authenticated())
				.saml2Login(withDefaults())
				.saml2Logout(withDefaults())
//				.saml2Metadata(withDefaults())
				.addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class);

		return http.build();
	}

}
