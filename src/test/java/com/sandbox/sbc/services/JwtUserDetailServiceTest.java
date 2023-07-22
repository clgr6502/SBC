package com.sandbox.sbc.services;

import com.sandbox.sbc.entities.DbUser;
import com.sandbox.sbc.repositories.DbUserRepository;
import com.sandbox.sbc.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
public class JwtUserDetailServiceTest {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private DbUserRepository dbUserRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void loadUserByUsernameShouldBeSuccessfulForAdminUser() {

        User user = getAdmin();
        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("admin");

        Assertions.assertEquals(user.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameShouldBeSuccessfulForUserInDataBase() {

        DbUser dbUser = getMockedDbUser();

        Mockito.when(dbUserRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(dbUser));

        UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("mock");

        Assertions.assertEquals(dbUser.getUsername(), userDetails.getUsername());
        Assertions.assertEquals(dbUser.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsernameForNonExistentUser() {

        Mockito.when(dbUserRepository.findByUsername(Mockito.any())).thenReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> jwtUserDetailsService.loadUserByUsername("mock"));

    }

    private User getAdmin() {
        return new User("admin", "$2a$12$sZUSIhyVLN7MByX.CzRVf.70b/985QVkGzOJHrF8xavhZpLqd9MfW", new ArrayList<>());
    }

    private DbUser getMockedDbUser() {

        DbUser user = new DbUser();

        user.setUsername("mock");
        user.setPassword("mockPass");

        return user;
    }
}
