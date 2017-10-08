package com.liemily.user.service;

import com.liemily.user.domain.User;
import com.liemily.user.domain.UserRole;
import com.liemily.user.exception.UserAlreadyExistsException;
import com.liemily.user.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

/**
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class UserService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) throws UserAlreadyExistsException {
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        if (userRepository.exists(user.getUsername())) {
            logger.error("User " + user.getUsername() + " already exists");
            throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
        }
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findOne(username);
    }
}
