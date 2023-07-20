package com.sandbox.sbc.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("Cerco l'utente in base all'username");
        
        if ("admin".equals(username)) {
            return new User("admin", "$2a$12$sZUSIhyVLN7MByX.CzRVf.70b/985QVkGzOJHrF8xavhZpLqd9MfW",
                    new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Utente " + username + " non trovato");
        }
    }
}
