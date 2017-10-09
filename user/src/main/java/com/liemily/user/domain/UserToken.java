package com.liemily.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by Emily Li on 08/10/2017.
 */
@Entity
@IdClass(UserTokenId.class)
public class UserToken {
    @Id
    private String username;
    @Id
    private String token;

    private UserToken() {
    }

    public UserToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
