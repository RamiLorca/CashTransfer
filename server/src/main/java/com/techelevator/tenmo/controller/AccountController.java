package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.GetAllAccountsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    private AccountDao accountDao;

    public AccountController (AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @RequestMapping(path = "/accounts/{accountId}", method = RequestMethod.GET)
    public Account getBalance(@PathVariable int accountId, Principal principal) {
        if (accountDao.verifyAccount(accountId, principal.getName())) {
            return accountDao.getAccountById(accountId);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username did not match");
        }
    }

    @RequestMapping(path = "/get-users", method = RequestMethod.GET)
    public List<GetAllAccountsDTO> getAllAccounts() {
        return accountDao.getAllAccounts();
    }
}

