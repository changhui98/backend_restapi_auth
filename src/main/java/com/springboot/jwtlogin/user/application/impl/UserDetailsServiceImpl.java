package com.springboot.jwtlogin.user.application.impl;

import com.springboot.jwtlogin.user.infra.repository.UserRepository;
import com.springboot.jwtlogin.user.application.UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("[loadUserByUsername] loadUserByUsername 수행. username : {}", username);

        return userRepository.getByUid(username).orElseThrow(
            ()-> new UsernameNotFoundException("존재하지 않는 회원입니다.")
        );
    }
}
