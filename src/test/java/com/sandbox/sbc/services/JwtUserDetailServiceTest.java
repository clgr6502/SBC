package com.sandbox.sbc.services;

import com.sandbox.sbc.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

@SpringBootTest
public class JwtUserDetailServiceTest {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void loadUserByUsernameShouldBeSuccessful() {

        User user = getMockedUser();
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("admin");

        Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameForNonExistentUser() {

        Assertions.assertThrows(ResponseStatusException.class, () -> jwtUserDetailsService.loadUserByUsername("mock"));

    }

    private User getMockedUser() {
        return new User("admin", "$2a$12$sZUSIhyVLN7MByX.CzRVf.70b/985QVkGzOJHrF8xavhZpLqd9MfW", new ArrayList<>());
    }
}
