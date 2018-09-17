package com.crixal.interview.service;

import com.crixal.interview.dto.UserDTO;

import java.math.BigDecimal;
import java.util.Collection;

public interface UserService {
    Collection<UserDTO> getUsers();
    void transfer(Long fromId, Long toId, BigDecimal money);
}
