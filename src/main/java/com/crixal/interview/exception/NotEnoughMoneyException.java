package com.crixal.interview.exception;

import java.math.BigDecimal;

public class NotEnoughMoneyException extends RuntimeException {
    private Long id;
    private BigDecimal balance;
    private BigDecimal operationMoney;

    public NotEnoughMoneyException(Long id, BigDecimal balance, BigDecimal operationMoney) {
        this.id = id;
        this.balance = balance;
        this.operationMoney = operationMoney;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getOperationMoney() {
        return operationMoney;
    }
}
