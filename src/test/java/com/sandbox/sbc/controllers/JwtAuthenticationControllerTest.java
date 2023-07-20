package com.sandbox.sbc.controllers;

import com.sandbox.sbc.requests.JwtRequest;
import com.sandbox.sbc.responses.JwtResponse;
import com.sandbox.sbc.services.JwtUserDetailsService;
import com.sandbox.sbc.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@SpringBootTest
public class JwtAuthenticationControllerTest {

    @Autowired
    private JwtAuthenticationController jwtAuthenticationController;

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
