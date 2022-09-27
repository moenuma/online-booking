package com.moenuma.online.booking.model.request;

import com.moenuma.online.booking.model.ERole;

import java.util.Set;

public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Set<ERole> roles;

    public SignupRequest(String username, String email, String password, Set<ERole> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<ERole> getRoles() {
        return roles;
    }
}
