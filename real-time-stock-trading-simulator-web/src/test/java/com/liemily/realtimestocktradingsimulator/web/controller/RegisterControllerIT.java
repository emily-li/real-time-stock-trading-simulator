package com.liemily.realtimestocktradingsimulator.web.controller;

import com.liemily.realtimestocktradingsimulator.web.domain.UserProperty;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    private UserService userService;

    private String username;
    private String password;
    private String url;

    @Before
    public void setup() {
        url = "http://localhost:" + port + "/" + RegisterController.getRegisterPage();
        username = UUID.randomUUID().toString();
        username = username.substring(0, 15);
        password = "1234asdf";
    }

    /**
     * C.Reg01 A user should be able to register an account
     */
    @Test
    public void testUserCanBeRegistered() {
        assert username.length() > 3 && username.length() < 16;
        registerUser(username, password, password);
        assertNotNull(userService.getUser(username));
    }

    /**
     * C.Reg02 A username should be between 3 and 16 characters, exclusive
     */
    @Test
    public void testInvalidUsernameLessThan4Characters() {
        username = username.substring(0, 3);
        assert username.length() <= 3;
        registerUser(username, password, password);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg02 A username should be between 3 and 16 characters, exclusive
     */
    @Test
    public void testInvalidUsernameGreaterThan15Characters() {
        username = UUID.randomUUID().toString();
        assert username.length() >= 16;
        registerUser(username, password, password);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg03 A password must match in two field inputs
     */
    @Test
    public void testNonMatchingPasswords() {
        registerUser(username, password, password + password);
        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg04 A password must contain numbers and letters
     */
    @Test
    public void testInvalidPassword() {
        registerUser(username, "123456789", "123456789");
        assertNull(userService.getUser(username));
    }

    private void registerUser(String username, String password, String matchingPassword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add(UserProperty.USERNAME.toString(), username);
        map.add(UserProperty.PASSWORD.toString(), password);
        map.add("password_confirmation", matchingPassword);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}
