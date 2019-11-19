package com.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.web.domain.User;
import com.web.service.CustomUserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationUserDetailsService {

	@Autowired
	private CustomUserService userService;

	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	@Override
	public UserDetails loadUserDetails(Authentication authentication) throws UsernameNotFoundException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		User user = (User) this.userService.loadUserByUsername(name);

		if (user != null) {
			
				return new org.springframework.security.core.userdetails.User(name, password,
		        true, true, true, true,
		        AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
		}
	
		return null;
	}
}
