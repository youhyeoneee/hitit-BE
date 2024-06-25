package com.pda.utils.security.service;

import com.pda.utils.security.openfeign.AuthClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService  {

    private AuthClient authClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username : " + username);
        UserDetails userDetails = authClient.validateUser(Integer.parseInt(username));
        return userDetails;
    }
}
