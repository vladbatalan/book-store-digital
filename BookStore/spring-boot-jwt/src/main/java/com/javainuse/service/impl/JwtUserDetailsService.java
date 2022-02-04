package com.javainuse.service.impl;

import java.util.ArrayList;
import java.util.Collections;

import com.javainuse.service.UserService;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final UserService userService;

	public JwtUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		com.javainuse.model.erd.User user = userService.getUserByUsername(username);

		if (user != null && user.getUsername().equals(username)) {
			return new User(user.getUsername(), user.getPassword(),
					new ArrayList<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority(user.getRol()))));
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}