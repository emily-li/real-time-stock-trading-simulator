package com.liemily.user.service;

import com.liemily.user.domain.User;
import com.liemily.user.domain.UserRole;
import com.liemily.user.domain.UserToken;
import com.liemily.user.exception.InvalidUserTokenException;
import com.liemily.user.exception.UserAlreadyExistsException;
import com.liemily.user.repository.UserRepository;
import com.liemily.user.repository.UserTokenRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
    private UserTokenRepository userTokenRepository;

    public UserService(UserRepository userRepository, UserTokenRepository userTokenRepository) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
    }

    public User save(User user) throws UserAlreadyExistsException {
        if (user.getRole() == null) {
            user.setRole(UserRole.USER);
        }
        if (userRepository.exists(user.getUsername())) {
            logger.error("User " + user.getUsername() + " already exists");
            throw new UserAlreadyExistsException("User " + user.getUsername() + " already exists");
        }
        return userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public User getUser(String username) {
        return userRepository.findOne(username);
    }

    public void saveUserToken(String username, String token) {
        UserToken userToken = new UserToken(username, token);
        userTokenRepository.save(userToken);
    }

    public User activateUserWithToken(String token) throws InvalidUserTokenException {
        UserToken userToken = userTokenRepository.findByToken(token);
        if (userToken == null || userToken.getUser() == null) {
            throw new InvalidUserTokenException("Found no valid user token for token " + token);
        }
        User user = userToken.getUser();
        user.setEnabled(true);
        return userRepository.save(user);
    }
}
