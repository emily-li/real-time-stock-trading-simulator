package com.liemily.user.service;

import com.liemily.user.domain.User;
import com.liemily.user.exception.UserAlreadyExistsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * Created by Emily Li on 08/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIT {
    @Autowired
    private UserService userService;

    @Test(expected = UserAlreadyExistsException.class)
    public void testSameUserCannotBeSavedTwice() throws Exception {
        User user = new User(UUID.randomUUID().toString(), "pwd");
        try {
            userService.save(user);
        } catch (Exception e) {
            throw new AssertionError("Failed to save new user: " + user.getUsername());
        }
        userService.save(user);
    }
}
