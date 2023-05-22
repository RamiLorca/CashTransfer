package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {
    @JsonProperty("account_id")
    private int accountId;
    private String username;
    private BigDecimal balance;

    public Account() {
    }

    public Account(int accountId, String username, BigDecimal balance) {
        this.accountId = accountId;
        this.username = username;
        this.balance = balance;
    }

    public Account(String username, BigDecimal balance) {
        this.username = username;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + accountId +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
