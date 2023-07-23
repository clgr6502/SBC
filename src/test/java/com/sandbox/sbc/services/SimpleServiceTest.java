package com.sandbox.sbc.services;

import com.sandbox.sbc.entities.DbUser;
import com.sandbox.sbc.repositories.DbUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@SpringBootTest
public class SimpleServiceTest {

    @Autowired
    private SimpleService simpleService;

    @MockBean
    private DbUserRepository dbUserRepository;

    @Test
    void modifyUserAttribShouldBeSuccessfulAndSetFlagToTrue() {

        DbUser user = getMockedDbUser();
        user.setFlag(true);

        Mockito.when(dbUserRepository.findById(Mockito.any())).thenReturn(Optional.of(getMockedDbUser()));
        Mockito.when(dbUserRepository.save(Mockito.any())).thenReturn(user);

        simpleService.modifyUserAttrib("t", 1L);

        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("mock", user.getUsername());
        Assertions.assertEquals("mockPass", user.getPassword());
        Assertions.assertTrue(user.getFlag());

    }

    @Test
    void modifyUserAttribShouldBeSuccessfulAndSetFlagToFalse() {

        DbUser user = getMockedDbUser();
        Mockito.when(dbUserRepository.findById(Mockito.any())).thenReturn(Optional.of(user));

        simpleService.modifyUserAttrib("f", 1L);

        Assertions.assertEquals(1L, user.getId());
        Assertions.assertEquals("mock", user.getUsername());
        Assertions.assertEquals("mockPass", user.getPassword());
        Assertions.assertFalse(user.getFlag());
    }

    @Test
    void modifyUserAttribShouldFailForUserNotFound() {

        Mockito.when(dbUserRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        ResponseStatusException caught = Assertions.assertThrows(ResponseStatusException.class,
                () ->simpleService.modifyUserAttrib("t", 1L));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, caught.getStatus());
    }

    @Test
    void modifyUserAttribShouldFailForInvalidParameter() {

        Mockito.when(dbUserRepository.findById(Mockito.any())).thenReturn(Optional.of(getMockedDbUser()));

        ResponseStatusException caught = Assertions.assertThrows(ResponseStatusException.class,
                () ->simpleService.modifyUserAttrib("a", 1L));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, caught.getStatus());
    }

    private DbUser getMockedDbUser() {

        DbUser user = new DbUser();

        user.setId(1L);
        user.setUsername("mock");
        user.setPassword("mockPass");
        user.setFlag(false);

        return user;
    }

}
