package com.liemily.realtimestocktradingsimulator.web.controller;

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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Emily Li on 17/07/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerIT {
    private final int AVG_RUN_COUNT = 100;
    private final long MAX_REQUEST_WAIT_MS = 1000;

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
        url = "http://localhost:" + port;
        username = UUID.randomUUID().toString();
        password = "pwd";
        User user = new User(username, password);
        user.setEnabled(true);
        userService.save(user);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        ResponseEntity<String> response = postForLogin(username, password);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo(url + "/");
    }

    @Test
    public void testUnsuccessfulLogin() throws Exception {
        ResponseEntity<String> response = postForLogin(username, null);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo(url + "/login?error");
    }

    private ResponseEntity<String> postForLogin(String user, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        return restTemplate.postForEntity(url + "/login", request, String.class);
    }
}
