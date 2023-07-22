package com.sandbox.sbc.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DbUserRequest {

    @NotNull(message = "Il nome utente è obbligatorio")
    @NotBlank(message = "Il nome utente è obbligatorio")
    private String username;
    @NotNull(message = "La password è obbligatoria")
    @NotBlank(message = "La password è obbligatoria")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
