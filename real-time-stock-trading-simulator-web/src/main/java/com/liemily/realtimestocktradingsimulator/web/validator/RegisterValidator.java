package com.liemily.realtimestocktradingsimulator.web.validator;

import com.liemily.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Emily Li on 08/10/2017.
 */
@Component
@Lazy
public class RegisterValidator implements Validator {
    private final int usernameLengthMin;
    private final int usernameLengthMax;

    @Autowired
    public RegisterValidator(@Value("${validation.user.username.min}") int usernameLengthMin,
                             @Value("${validation.user.username.max}") int usernameLengthMax) {
        this.usernameLengthMin = usernameLengthMin;
        this.usernameLengthMax = usernameLengthMax;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (user.getUsername().length() < usernameLengthMin || user.getUsername().length() > usernameLengthMax) {
            errors.reject("username", String.format("Username must be between %d and %d characters in length", usernameLengthMin, usernameLengthMax));
        }
    }

    public void validatePasswords(Object target, String matchingPassword, Errors errors) {
        User user = (User) target;
        if (!user.getPassword().equals(matchingPassword)) {
            errors.reject("password_confirmation", "Passwords do not match");
        }
    }
}
