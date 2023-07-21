package com.sandbox.sbc.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@SpringBootTest
public class JwtTokenUtilsTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Test
    void tokenShouldBeValidated() {

        UserDetails userDetails = getMockedUserDetail();
        String token = jwtTokenUtil.generateToken(userDetails);

        Assertions.assertTrue(jwtTokenUtil.validateToken(token, userDetails));
    }

    @Test
    void validateTokenShouldFailForUnequalUsernames() {

        UserDetails legitUserDetails = getMockedUserDetail();
        String token = jwtTokenUtil.generateToken(legitUserDetails);
        UserDetails fakeUserDetails = otherMockedUserDetail();


        Assertions.assertFalse(jwtTokenUtil.validateToken(token, fakeUserDetails));
    }

    @Test
    void validateTokenShouldFailForExpiredToken() {

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY4OTg2OTE0NSwiaWF0IjoxNjg5ODY4MjQ1fQ.qTuGZ8njrVwbkmYof2-zA73sxllIkMmlhXCzh5LJ2e_kuza3yKFsJXbyv0LSIGUdi_zSwYOFX-NNtOQO8jilVQ";
        UserDetails userDetails = getMockedUserDetail();

        Assertions.assertFalse(jwtTokenUtil.validateToken(token, userDetails));
    }


    private UserDetails otherMockedUserDetail() {

        //TODO implementare una classe di builders
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
                return "admin";
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
}