package com.sandbox.sbc.controllers;

import com.sandbox.sbc.services.JwtUserDetailsService;
import com.sandbox.sbc.services.SimpleService;
import com.sandbox.sbc.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.impl.DefaultJwsHeader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SimpleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpleController simpleController;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private SimpleService simpleService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void basicInfosTest() throws Exception {

        User user = getMockedUser("user");

        Jws<Claims> claims = new DefaultJws<>(new DefaultJwsHeader(), new DefaultClaims(), "mock");
        Mockito.when(jwtUserDetailsService.loadUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(jwtTokenUtil.getUsernameFromToken(Mockito.any())).thenReturn("user");
        Mockito.when(jwtTokenUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any())).thenReturn(claims.getSignature());

        mockMvc.perform(get("/simple")
                        .header("Authorization", "Bearer mock"))
                .andExpect(status().isOk());
    }

    @Test
    void modifyUserAttributeShouldBeSuccessful() throws Exception {

        User user = getMockedUser("admin");

        Jws<Claims> claims = new DefaultJws<>(new DefaultJwsHeader(), new DefaultClaims(), "mock");
        Mockito.when(jwtUserDetailsService.loadUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(jwtTokenUtil.getUsernameFromToken(Mockito.any())).thenReturn("admin");
        Mockito.when(jwtTokenUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any())).thenReturn(claims.getSignature());

        Mockito.doNothing().when(simpleService).modifyUserAttrib(Mockito.any(), Mockito.any());

        mockMvc.perform(put("/simple/flag/{value}/{id}", "t", 1L)
                        .header("Authorization", "Bearer mock"))
                .andExpect(status().isOk());
    }

    @Test
    void modifyUserAttributeShouldFailForForbiddenAccess() throws Exception {

        User user = getMockedUser("user");

        Jws<Claims> claims = new DefaultJws<>(new DefaultJwsHeader(), new DefaultClaims(), "mock");
        Mockito.when(jwtUserDetailsService.loadUserByUsername(Mockito.any())).thenReturn(user);
        Mockito.when(jwtTokenUtil.getUsernameFromToken(Mockito.any())).thenReturn("user");
        Mockito.when(jwtTokenUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any())).thenReturn(claims.getSignature());

        Mockito.doNothing().when(simpleService).modifyUserAttrib(Mockito.any(), Mockito.any());

        mockMvc.perform(put("/simple/flag/{value}/{id}", "t", 1L)
                        .header("Authorization", "Bearer mock"))
                .andExpect(status().isForbidden());
    }

    private User getMockedUser(String name) {

        User user;

        if ("admin".equals(name)) {
            user = new User("admin", "pass", List.of((GrantedAuthority) () -> "admin"));
        } else {
            user = new User(name, "pass", List.of((GrantedAuthority) () -> "user"));
        }
        return user;
    }
}
