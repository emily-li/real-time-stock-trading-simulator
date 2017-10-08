package com.liemily.realtimestocktradingsimulator.web.controller;

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
    private String user;
    private String password;
    private String url;

    @Before
    public void setup() {
        url = "http://localhost:" + port;
        restTemplate.getForObject(url, String.class); // First server request initialises server components which takes longer, so run this before performance checks
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        ResponseEntity<String> response = postForLogin(user, password);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo(url + "/");
    }

    @Test
    public void testUnsuccessfulLogin() throws Exception {
        ResponseEntity<String> response = postForLogin(user, null);
        assertThat(response.getHeaders().get("Location").get(0)).isEqualTo(url + "/login?error");
    }

    private ResponseEntity<String> postForLogin(String user, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", user);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        return restTemplate.postForEntity(url + "/login", request, String.class);
    }
}
