package com.liemily.realtimestocktradingsimulator.web.validator;

import com.liemily.realtimestocktradingsimulator.web.domain.UserProperty;
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
    private final int passwordLengthMin;

    @Autowired
    public RegisterValidator(@Value("${validation.user.username.min}") int usernameLengthMin,
                             @Value("${validation.user.username.max}") int usernameLengthMax,
                             @Value("${validation.user.password.min}") int passwordLengthMin) {
        this.usernameLengthMin = usernameLengthMin;
        this.usernameLengthMax = usernameLengthMax;
        this.passwordLengthMin = passwordLengthMin;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        validateUsername(user.getUsername(), errors);
        validatePassword(user.getPassword(), errors);
        validateEmail(user.getEmail(), errors);
    }

    private void validateUsername(String username, Errors errors) {
        if (username.length() < usernameLengthMin || username.length() > usernameLengthMax) {
            errors.reject(UserProperty.USERNAME.toString(), String.format("Username must be between %d and %d characters in length", usernameLengthMin, usernameLengthMax));
        }
    }

    private void validatePassword(String password, Errors errors) {
        boolean containsNumbers = password.matches(".*[0-9].*");
        boolean containsLetters = password.matches(".*[A-Z].*") || password.matches(".*[a-z].*");

        if (!(containsNumbers && containsLetters)) {
            errors.reject(UserProperty.PASSWORD.toString(), "Password must contain both letters and numbers");
        }
        if (password.length() < passwordLengthMin) {
            errors.reject("Password must have at least " + passwordLengthMin + " characters");
        }
    }

    private void validateEmail(String email, Errors errors) {
        final String emailRegEx =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegEx)) {
            errors.reject(UserProperty.EMAIL.toString(), "Email " + email + " is invalid");
        }
    }

    public void validatePasswordsMatch(User user, String matchingPassword, Errors errors) {
        if (!user.getPassword().equals(matchingPassword)) {
            errors.reject("password_confirmation", "Passwords do not match");
        }
    }

    public void validateEmailsMatch(User user, String emailConfirmation, Errors errors) {
        if (!user.getEmail().equals(emailConfirmation)) {
            errors.reject("email_confirmation", "Email addresses do not match");
        }
    }
}
