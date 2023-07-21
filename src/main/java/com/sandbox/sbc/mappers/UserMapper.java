package com.sandbox.sbc.mappers;

import com.sandbox.sbc.entities.DbUser;
import com.sandbox.sbc.requests.DbUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    private UserMapper() {}

    public static DbUser dbUserRequestToEntity(DbUserRequest request, PasswordEncoder encoder) {

        DbUser user = new DbUser();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        return user;
    }
}
