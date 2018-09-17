package com.crixal.interview.dto;

import java.math.BigDecimal;

public class UserDTO {
    private Long id;
    private String name;
    private BigDecimal balance;
    private Long version;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, BigDecimal balance, Long version) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getVersion() {
        return version;
    }
}
