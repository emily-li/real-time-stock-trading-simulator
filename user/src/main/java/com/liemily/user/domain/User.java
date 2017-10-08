package com.liemily.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by Emily Li on 21/09/2017.
 */
@Entity
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
    private UserRole role;
    private BigDecimal credits;

    private User() {
    }

    public User(String username, String password) {
        this(username, password, UserRole.USER);
    }

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
        enabled = false;
    }

    public String getUsername() {
        return username;
    }

    public BigDecimal getCredits() {
        return credits == null ? new BigDecimal(0) : credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
