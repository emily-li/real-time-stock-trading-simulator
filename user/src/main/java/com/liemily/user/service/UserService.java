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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

/**
 * Created by Emily Li on 01/10/2017.
 */
@Component
@Lazy
public class UserService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserRepository userRepository;
    private UserTokenRepository userTokenRepository;
    private BigDecimal userDefaultCredits;

    public UserService(UserRepository userRepository,
                       UserTokenRepository userTokenRepository,
                       @Value("${user.credits.default}") BigDecimal userDefaultCredits) {
        this.userRepository = userRepository;
        this.userTokenRepository = userTokenRepository;
        this.userDefaultCredits = userDefaultCredits;
    }

    public User save(User user) throws UserAlreadyExistsException {
        User savingUser = user;
        if (savingUser.getRole() == null) {
            savingUser.setRole(UserRole.USER);
        }
        if (userRepository.exists(savingUser.getUsername())) {
            logger.error("User " + savingUser.getUsername() + " already exists");
            throw new UserAlreadyExistsException("User " + savingUser.getUsername() + " already exists");
        }
        return userRepository.save(savingUser);
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

    public User activateUser(String token) throws InvalidUserTokenException {
        UserToken userToken = userTokenRepository.findByToken(token);
        if (userToken == null) {
            throw new InvalidUserTokenException("Invalid token " + token);
        }
        User user = userRepository.findOne(userToken.getUsername());
        if (user == null) {
            throw new InvalidUserTokenException("No valid user assigned to token " + token);
        }
        user.setEnabled(true);
        user.setCredits(userDefaultCredits);
        return userRepository.save(user);
    }
}
