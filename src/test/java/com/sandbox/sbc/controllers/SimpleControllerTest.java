package com.sandbox.sbc.controllers;

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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SimpleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SimpleController simpleController;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void basicInfosTest() throws Exception {

        Jws<Claims> claims = new DefaultJws<>(new DefaultJwsHeader(), new DefaultClaims(), "mock");
        Mockito.when(jwtTokenUtil.getUsernameFromToken(Mockito.any())).thenReturn("admin");
        Mockito.when(jwtTokenUtil.validateToken(Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any())).thenReturn(claims.getSignature());

        mockMvc.perform(get("/simple")
                        .header("Authorization", "Bearer mock"))
                .andExpect(status().isOk());
    }
}
