package com.springboot.jwtlogin.user.application;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsService {

    UserDetails loadUserByUsername(String username);

}
