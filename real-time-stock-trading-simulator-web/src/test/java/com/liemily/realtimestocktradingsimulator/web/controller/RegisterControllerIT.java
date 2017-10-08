package com.liemily.realtimestocktradingsimulator.web.controller;

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
    private String url;

    @Before
    public void setup() {
        url = "http://localhost:" + port + "/" + RegisterController.getRegisterPage();
        username = UUID.randomUUID().toString();
    }

    /**
     * C.Reg01 A user should be able to register an account
     */
    @Test
    public void testUserCanBeRegistered() {
        username = username.substring(0, 15);
        assert username.length() > 3 && username.length() < 16;

        registerUser(username);

        assertNotNull(userService.getUser(username));
    }

    /**
     * C.Reg02 A username should be between 3 and 16 characters, exclusive
     */
    @Test
    public void testInvalidUsernameLessThan4Characters() {
        username = username.substring(0, 3);
        assert username.length() <= 3;

        registerUser(username);

        assertNull(userService.getUser(username));
    }

    /**
     * C.Reg02 A username should be between 3 and 16 characters, exclusive
     */
    @Test
    public void testInvalidUsernameGreaterThan15Characters() {
        assert username.length() >= 16;

        registerUser(username);

        assertNull(userService.getUser(username));
    }

    private void registerUser(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", "pwd");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        restTemplate.postForEntity(url, request, String.class);
    }
}
