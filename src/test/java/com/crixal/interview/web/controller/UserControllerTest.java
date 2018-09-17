package com.crixal.interview.web.controller;

import com.crixal.interview.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.BaseControllerTest;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UserControllerTest extends BaseControllerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void startApp() {
        clearTestData();
        startControllers();
    }

    @Test
    public void getUsersTest() throws IOException {
        saveUser("u1", new BigDecimal(10));
        saveUser("u2", new BigDecimal(1));

        Collection<UserDTO> users = getUsers();

        Assert.assertEquals(2, users.size());
        Map<String, UserDTO> name2User = users.stream().collect(Collectors.toMap(UserDTO::getName, u -> u));

        Assert.assertEquals("u1", name2User.get("u1").getName());
        Assert.assertEquals(new BigDecimal(10), name2User.get("u1").getBalance());

        Assert.assertEquals("u2", name2User.get("u2").getName());
        Assert.assertEquals(new BigDecimal(1), name2User.get("u2").getBalance());
    }

    @Test
    public void transferTest() throws IOException {
        saveUser("u1", new BigDecimal(10));
        saveUser("u2", new BigDecimal(1));

        Collection<UserDTO> users = getUsers();
        Map<String, UserDTO> name2User = users.stream().collect(Collectors.toMap(UserDTO::getName, u -> u));

        transfer(name2User.get("u1").getId(), name2User.get("u2").getId(), new BigDecimal(5));

        Collection<UserDTO> updatedUsers = getUsers();
        Map<String, UserDTO> name2UpdatedUser = updatedUsers.stream().collect(Collectors.toMap(UserDTO::getName, u -> u));

        Assert.assertEquals(new BigDecimal(5), name2UpdatedUser.get("u1").getBalance());
        Assert.assertEquals(new BigDecimal(6), name2UpdatedUser.get("u2").getBalance());
    }

    @Test
    public void concurrentTransferTest() throws IOException {
        saveUser("u1", new BigDecimal(10));
        saveUser("u2", new BigDecimal(6));

        Collection<UserDTO> users = getUsers();
        Map<String, UserDTO> name2User = users.stream().collect(Collectors.toMap(UserDTO::getName, u -> u));

        CompletableFuture.allOf(CompletableFuture.runAsync(() -> transfer(name2User.get("u1").getId(), name2User.get("u2").getId(), new BigDecimal(5))),
                CompletableFuture.runAsync(() -> transfer(name2User.get("u1").getId(), name2User.get("u2").getId(), new BigDecimal(2))),
                CompletableFuture.runAsync(() -> transfer(name2User.get("u2").getId(), name2User.get("u1").getId(), new BigDecimal(3))))
                .join();

        Collection<UserDTO> updatedUsers = getUsers();
        Map<String, UserDTO> name2UpdatedUser = updatedUsers.stream().collect(Collectors.toMap(UserDTO::getName, u -> u));

        Assert.assertEquals(new BigDecimal(6), name2UpdatedUser.get("u1").getBalance());
        Assert.assertEquals(new BigDecimal(10), name2UpdatedUser.get("u2").getBalance());
    }

    @Test()
    public void userDoesNotExistsTest() {
        RestAssured
                .given()
                .param("to_id", 2L)
                .param("money", BigDecimal.ONE)
                .when()
                .put("/api/users/" + 1L + "/transfer")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    private Collection<UserDTO> getUsers() throws IOException {
        return objectMapper.readValue(
                RestAssured
                        .when()
                        .get("/api/users")
                        .then()
                        .log()
                        .ifValidationFails()
                        .statusCode(HttpStatus.SC_OK)
                        .extract()
                        .asInputStream(),
                objectMapper.getTypeFactory().constructCollectionType(Collection.class, UserDTO.class));
    }

    private void transfer(Long fromId, Long toId, BigDecimal money) {
        RestAssured
                .given()
                .param("to_id", toId)
                .param("money", money)
                .when()
                .put("/api/users/" + fromId + "/transfer")
                .then()
                .log()
                .ifValidationFails()
                .statusCode(HttpStatus.SC_OK)
                .extract();
    }
}