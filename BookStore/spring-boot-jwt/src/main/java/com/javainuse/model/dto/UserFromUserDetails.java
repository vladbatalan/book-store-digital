package com.javainuse.model.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class UserFromUserDetails extends User {

    public UserFromUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public UserFromUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public UserFromUserDetails(com.javainuse.model.erd.User user){
        super(
                user.getUsername(), user.getPassword(), 
                new ArrayList<GrantedAuthority>(Collections.singleton(new SimpleGrantedAuthority(user.getRol()))));
        
        
    }
}
