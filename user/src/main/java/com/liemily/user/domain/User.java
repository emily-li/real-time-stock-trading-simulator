package com.liemily.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Emily Li on 21/09/2017.
 */
@Entity
public class User {
    @Id
    private String username;

    public String getUsername() {
        return username;
    }
}
