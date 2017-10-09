package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.domain.UserProperty;
import com.liemily.user.domain.User;
import com.liemily.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Emily Li on 08/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegisterControllerIT {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegisterController registerController;
    @Autowired
    private UserService userService;

    private String username;
    private String password;
    private String email;
    private String forename;
    private String surname;

    private String url;
    private String successUrl;

    @Before
    public void setup() {
        String baseUrl = "http://localhost:" + port;
        url = baseUrl + "/" + RegisterController.getRegisterPage();
        successUrl = baseUrl + "/register/success";
        username = UUID.randomUUID().toString();
        username = username.substring(0, 15);
        password = "password123";
        email = "email@email.com";
        forename = "forename";
        surname = "surname";
    }

    /**
     * C.Reg01 A user should be able to register an account
     */
    @Test
    public void testUserCanBeRegistered() {
        assert username.length() > 3 && username.length() < 16;
        registerUser(username, password);
        assertNotNull(userService.getUser(username));
    }

    /**
     * C.Reg02 A username should be between 3 and 16 characters, exclusive
     */
    @Test
    public void testInvalidUsernameLessThan4Characters() {
        username = username.substring(0, 3);
        assert username.length() <= 3;
        registerUser(username, password);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg02 A username should be between 3 and 16 characters, exclusive
     */
    @Test
    public void testInvalidUsernameGreaterThan15Characters() {
        username = UUID.randomUUID().toString();
        assert username.length() >= 16;
        registerUser(username, password);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg03 A password must match in two field inputs
     */
    @Test
    public void testNonMatchingPasswords() {
        registerUser(username, password, password + password, email, email, forename, surname, null);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg04 A password must contain numbers and letters
     */
    @Test
    public void testPasswordHasNumbersAndLetters() {
        registerUser(username, "123456789");
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg05 A password must be greater than 7 characters
     */
    @Test
    public void testPasswordGreaterThan7Characters() {
        password = "123456a";
        assert password.length() <= 7;
        registerUser(username, password);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg06 An e-mail address must match in two field inputs
     */
    @Test
    public void testNonMatchingEmails() {
        registerUser(username, password, password, email, "1" + email, forename, surname, null);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg07 An e-mail address must be valid
     */
    @Test
    public void testInvalidEmail() {
        email = "1@1.1";
        registerUser(username, password, password, email, email, forename, surname, null);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg08 A forename must be given
     */
    @Test
    public void testMissingForename() {
        for (String forename : new String[]{"", null}) {
            registerUser(username, password, password, email, email, forename, surname, null);
            assertNull(userService.getUser(username));
        }
    }

    /**
     * C.Reg09 A surname must be given
     */
    @Test
    public void testMissingSurname() {
        for (String surname : new String[]{"", null}) {
            registerUser(username, password, password, email, email, forename, surname, null);
            assertNull(userService.getUser(username));
        }
    }

    /**
     * C.Reg10 A user should be able to register denoting their birth date
     */
    @Test
    public void testBirthDate() {
        registerUser(username, password, password, email, email, forename, surname, "01/01/1990");
        assertNotNull(userService.getUser(username));
    }

    /**
     * C.Reg11 A user should be able to register not denoting their birth date
     */
    @Test
    public void testMissingBirthDate() {
        registerUser(username, password, password, email, email, forename, surname, null);
        assertNotNull(userService.getUser(username));
    }

    /**
     * C.Reg12 A birth date must be a valid date
     */
    @Test
    public void testInvalidBirthDate() {
        registerUser(username, password, password, email, email, forename, surname, "01011990");
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg14 The user should be notified if their registration has been successfully submitted
     */
    @Test
    public void testUserNotifiedOnRegistrationSubmission() throws Exception {
        ResponseEntity<String> response = registerUser(username, password);
        assertNotNull(userService.getUser(username));
        assertEquals(successUrl, response.getHeaders().get("Location").get(0));
    }

    /**
     * C.Reg16 The confirmation e-mail should contain a link that activates the user account
     */
    @Test
    public void testConfirmationEmail() throws Exception {
        User user = new User(username, password);
        user = userService.save(user);
        String token = registerController.generateUserToken(user);
        assert !user.isEnabled();

        String response = restTemplate.getForObject(url + "/token/" + token, String.class);
        user = userService.getUser(username);
        assert user.isEnabled();
    }

    private ResponseEntity<String> registerUser(String username, String password) {
        return registerUser(username, password, password, email, email, forename, surname, null);
    }

    private ResponseEntity<String> registerUser(String username, String password, String matchingPassword, String email, String matchingEmail, String forename, String surname, String birthDate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(UserProperty.USERNAME.toString(), username);
        map.add(UserProperty.PASSWORD.toString(), password);
        map.add("password_confirmation", matchingPassword);
        map.add(UserProperty.EMAIL.toString(), email);
        map.add("email_confirmation", matchingEmail);
        map.add("forename", forename);
        map.add("surname", surname);
        if (birthDate != null) {
            map.add("birthDate", birthDate);
        }

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
