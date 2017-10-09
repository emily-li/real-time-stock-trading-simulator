package com.liemily.user.domain;

import javax.persistence.*;

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
    @OneToOne
    @JoinColumn(name = "username", insertable = false, updatable = false)
    private User user;

    private UserToken() {
    }

    public UserToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
