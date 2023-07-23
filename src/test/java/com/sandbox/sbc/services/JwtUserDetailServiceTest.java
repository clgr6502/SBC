package com.sandbox.sbc.services;

import com.sandbox.sbc.entities.DbUser;
import com.sandbox.sbc.repositories.DbUserRepository;
import com.sandbox.sbc.requests.DbUserRequest;
import com.sandbox.sbc.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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

        ResponseStatusException caught = Assertions.assertThrows(ResponseStatusException.class,
                () -> jwtUserDetailsService.loadUserByUsername("mock"));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, caught.getStatus());

    }

    @Test
    void saveUserShouldBeSuccessful() {

        DbUserRequest request = getMockedDbRequest();
        Mockito.when(dbUserRepository.save(Mockito.any())).thenReturn(getMockedDbUser());

        jwtUserDetailsService.saveUser(request);
    }

    @Test
    void saveUserShouldFailForForbiddenName() {

        DbUserRequest req = getMockedDbRequest();
        req.setUsername("admin");

        ResponseStatusException caught = Assertions.assertThrows(ResponseStatusException.class,
                () -> jwtUserDetailsService.saveUser(req));

        Assertions.assertEquals(HttpStatus.FORBIDDEN, caught.getStatus());
    }

    @Test
    void saveUserShouldFailForAlreadyExistingName() {

        DbUserRequest req = getMockedDbRequest();

        Mockito.when(dbUserRepository.findByUsername(Mockito.any())).thenReturn(Optional.of(getMockedDbUser()));

        ResponseStatusException caught = Assertions.assertThrows(ResponseStatusException.class,
                () -> jwtUserDetailsService.saveUser(req));

        Assertions.assertEquals(HttpStatus.CONFLICT, caught.getStatus());
    }

    private User getAdmin() {
        return new User("admin", "$2a$12$sZUSIhyVLN7MByX.CzRVf.70b/985QVkGzOJHrF8xavhZpLqd9MfW", List.of((GrantedAuthority) () -> "admin"));
    }

    private DbUser getMockedDbUser() {

        DbUser user = new DbUser();

        user.setId(1L);
        user.setUsername("mock");
        user.setPassword("mockPass");
        user.setFlag(false);

        return user;
    }

    private DbUserRequest getMockedDbRequest() {

        DbUserRequest request = new DbUserRequest();

        request.setUsername("mock");
        request.setPassword("mockPass");
        return request;
    }
}
