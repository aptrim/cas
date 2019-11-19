package com.web;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.servlet.http.HttpSessionEvent;

@SpringBootApplication
public class CasSecuredAppApplication {
	
	@Value("${cas.url}")
	private String casServerUrl;

	public static void main(String[] args) {
		SpringApplication.run(CasSecuredAppApplication.class, args);
	}

	@Bean
	public ServiceProperties serviceProperties() {
	  ServiceProperties serviceProperties = new ServiceProperties();
	  serviceProperties.setService("http://localhost:8081/login/cas");
	  serviceProperties.setSendRenew(false);
	  return serviceProperties;
	}

	@Bean
	@Primary
	public AuthenticationEntryPoint authenticationEntryPoint(ServiceProperties sP) {
	  CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
	  entryPoint.setLoginUrl(casServerUrl + "/login");
	  entryPoint.setServiceProperties(sP);
	  return entryPoint;
	}

	@Bean
	public TicketValidator ticketValidator() {
	  return new Cas30ServiceTicketValidator(casServerUrl);
	}

	@Bean
	public CasAuthenticationProvider casAuthenticationProvider() {
	  CasAuthenticationProvider provider = new CasAuthenticationProvider();
	  provider.setServiceProperties(serviceProperties());
	  provider.setTicketValidator(ticketValidator());
	  provider.setAuthenticationUserDetailsService(authenticationUserDetailsService());
	  provider.setKey("CAS_PROVIDER_LOCALHOST_9000");
	  return provider;
	}


	@Bean
	public SecurityContextLogoutHandler securityContextLogoutHandler() {
	  return new SecurityContextLogoutHandler();
	}
	
	@Bean
	public AuthenticationUserDetailsService authenticationUserDetailsService() {
	  return new CustomAuthenticationProvider();
	}

	@Bean
	public LogoutFilter logoutFilter() {
	  LogoutFilter logoutFilter = new LogoutFilter(
			  casServerUrl + "/logout", securityContextLogoutHandler());
	  logoutFilter.setFilterProcessesUrl("/logout/cas");
	  return logoutFilter;
	}

	@Bean
	public SingleSignOutFilter singleSignOutFilter() {
	  SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
	  singleSignOutFilter.setCasServerUrlPrefix(casServerUrl);
	  singleSignOutFilter.setIgnoreInitConfiguration(true);
	  return singleSignOutFilter;
	}

	@EventListener
	public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(HttpSessionEvent event) {
	  return new SingleSignOutHttpSessionListener();
	}
}