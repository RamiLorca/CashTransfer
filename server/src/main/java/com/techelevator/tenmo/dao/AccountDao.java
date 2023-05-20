package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.GetAllAccountsDTO;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getAccountBalance(int accountId);

    String getAccountUsername(int accountId);

    Account getAccountById(int accountId);

    BigDecimal subtractFromBalance(int accountId, BigDecimal amount);

    BigDecimal addToBalance(int accountId, BigDecimal amount);

    boolean updateAccountBalance(int accountId, BigDecimal newAmount);

    List<GetAllAccountsDTO> getAllAccounts();

    boolean verifyAccount(int targetAccountId, String username);
}
