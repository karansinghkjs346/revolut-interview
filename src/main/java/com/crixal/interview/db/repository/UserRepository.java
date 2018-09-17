package com.crixal.interview.db.repository;

import com.crixal.interview.dto.UserDTO;
import com.crixal.interview.exception.NotEnoughMoneyException;

import java.math.BigDecimal;
import java.util.Collection;

public interface UserRepository extends AutoCloseable {
    Collection<UserDTO> getUsers();
    boolean transfer(Long fromId, Long toId, BigDecimal money) throws NotEnoughMoneyException;
    void commit();
}
