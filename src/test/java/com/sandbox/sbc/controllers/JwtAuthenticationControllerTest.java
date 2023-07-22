package com.sandbox.sbc.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandbox.sbc.requests.DbUserRequest;
import com.sandbox.sbc.requests.JwtRequest;
import com.sandbox.sbc.responses.JwtResponse;
import com.sandbox.sbc.services.JwtUserDetailsService;
import com.sandbox.sbc.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationControllerTest {

    @Autowired
    private JwtAuthenticationController jwtAuthenticationController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    void createAuthenticationTokenShouldBeSuccessful() throws Exception {

        JwtRequest request = new JwtRequest();
        request.setUsername("mock");
        request.setPassword("mock");

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(getMockedAuthentication());
        Mockito.when(jwtUserDetailsService.loadUserByUsername(Mockito.any())).thenReturn(getMockedUserDetail());
        Mockito.when(jwtTokenUtil.generateToken(Mockito.any())).thenReturn("mockedToken");

        JwtResponse mockedResponse = new JwtResponse();
        mockedResponse.setUsername("mock");
        mockedResponse.setToken("mockedToken");

        JwtResponse response = jwtAuthenticationController.createAuthenticationToken(request).getBody();

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getUsername());
        Assertions.assertNotNull(response.getToken());

        Assertions.assertEquals(mockedResponse.getUsername(), response.getUsername());
        Assertions.assertEquals(mockedResponse.getToken(), response.getToken());
    }

    @Test
    void createAuthenticationTokenShouldFailForDisabledUser() throws Exception {

        JwtRequest request = new JwtRequest();
        request.setUsername("mock");
        request.setPassword("mock");

        Mockito.doThrow(DisabledException.class).when(authenticationManager).authenticate(Mockito.any());

        Assertions.assertThrows(DisabledException.class, () -> jwtAuthenticationController.createAuthenticationToken(request));

    }

    @Test
    void createAuthenticationTokenShouldFailForBadCredentialException() throws Exception {

        JwtRequest request = new JwtRequest();
        request.setUsername("mock");
        request.setPassword("mock");

        Mockito.doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(Mockito.any());

        Assertions.assertThrows(BadCredentialsException.class, () -> jwtAuthenticationController.createAuthenticationToken(request));

    }

    @Test
    void saveUserShouldBeSuccessful() throws Exception {

        DbUserRequest request = getMockedDbUserRequest();

        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(request);

        Mockito.doNothing().when(jwtUserDetailsService).saveUser(Mockito.any());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated());
    }

    @Test
    void saveUserShouldFailForNullUsername() throws Exception {

        DbUserRequest request = getMockedDbUserRequest();
        request.setUsername(null);

        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUserShouldFailForNullPassword() throws Exception {

        DbUserRequest request = getMockedDbUserRequest();
        request.setPassword(null);

        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUserShouldFailForEmptyUser() throws Exception {

        DbUserRequest request = getMockedDbUserRequest();
        request.setUsername("");

        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUserShouldFailForEmptyPassword() throws Exception {

        DbUserRequest request = getMockedDbUserRequest();
        request.setPassword("");

        ObjectMapper mapper = new ObjectMapper();
        String req = mapper.writeValueAsString(request);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isBadRequest());
    }

    private DbUserRequest getMockedDbUserRequest(){

        DbUserRequest request = new DbUserRequest();

        request.setUsername("mock");
        request.setPassword("mock");

        return request;
    }

    private UserDetails getMockedUserDetail() {

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return "mock";
            }

            @Override
            public String getUsername() {
                return "mock";
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    private Authentication getMockedAuthentication() {

        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return null;
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "mock";
            }
        };
    }
}
