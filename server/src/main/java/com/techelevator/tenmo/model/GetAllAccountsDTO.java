package com.techelevator.tenmo.model;

public class GetAllAccountsDTO {

    private String username;
    private int accountId;

    public GetAllAccountsDTO() {
    }

    public GetAllAccountsDTO(String username, int accountId) {
        this.username = username;
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

}
