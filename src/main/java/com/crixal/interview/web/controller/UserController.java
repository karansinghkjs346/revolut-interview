package com.crixal.interview.web.controller;

import com.crixal.interview.dto.EmptyResponseDTO;
import com.crixal.interview.dto.UserDTO;
import com.crixal.interview.exception.BadInputDataException;
import com.crixal.interview.service.UserService;
import org.rapidoid.http.Req;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Collection;

class UserController extends WebController {
    private final UserService userService;
    private final EmptyResponseDTO emptyResponseDTO = new EmptyResponseDTO();

    @Inject
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void startAsync() {
        onGetJson("/api/users", req -> getUsers());
        onPutJson("/api/users/{id}/transfer", this::transfer);
    }

    private Collection<UserDTO> getUsers() {
        return userService.getUsers();
    }

    private Object transfer(Req req) {
        Long fromId;
        Long toId;
        BigDecimal money;
        try {
            fromId = Long.parseLong(req.param("id"));
            toId = Long.parseLong(req.param("to_id"));
            money = new BigDecimal(req.param("money"));
        } catch (NumberFormatException e) {
            throw new BadInputDataException(e);
        }
        userService.transfer(fromId, toId, money);
        return emptyResponseDTO;
    }
}
