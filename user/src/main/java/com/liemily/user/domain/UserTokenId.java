package com.liemily.user.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Emily Li on 08/10/2017.
 */
@Embeddable
public class UserTokenId implements Serializable {
    private String username;
    private String token;

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
