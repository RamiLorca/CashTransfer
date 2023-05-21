package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.GetAllAccountsDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            if (rowSet.next()) {
                return mapRowToAccount(rowSet);
            }
        }
        catch (DataAccessException e) {
            System.out.println("Unable to access database.");
        }
        return null;
    }

    //used to subtract accepted transfer amount from sender's account balance
    @Override
    public BigDecimal subtractFromBalance(int accountId, BigDecimal amount) {
        BigDecimal updatedSenderBalance = new BigDecimal("0");
        String sql = "SELECT balance FROM account WHERE account_id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);

        try {
            if (result.next()) {
                BigDecimal senderBalance = result.getBigDecimal("balance");
                updatedSenderBalance = senderBalance.subtract(amount);
            }
        }
        catch (BadSqlGrammarException e) {
            System.out.println("Bad SQL grammar");
        }
        catch (DataAccessException e) {
            System.out.println("Unable to access database.");
        }
        return updatedSenderBalance;
    }

    //used to add accepted transfer amount to receiver's account balance
    @Override
    public BigDecimal addToBalance(int accountId, BigDecimal amount) {
        BigDecimal updatedReceiverBalance = new BigDecimal("0");
        String sql = "SELECT balance FROM account WHERE account_id = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);

        try {
            if (result.next()) {
                BigDecimal receiverBalance = result.getBigDecimal("balance");
                updatedReceiverBalance = receiverBalance.add(amount);
            }
        }
        catch (BadSqlGrammarException e) {
            System.out.println("Bad SQL grammar");
        }
        catch (DataAccessException e) {
            System.out.println("Unable to access database.");
        }

        return updatedReceiverBalance;
    }

    @Override
    public BigDecimal getAccountBalance(int accountId) {
        String sql = "SELECT balance FROM account WHERE account_id = ?";

        try {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
            return balance;
        }
        catch (DataAccessException e) {
            System.out.println("Unable to connect to database.");
        }
        return new BigDecimal("-1.00");
    }

    @Override
    public String getAccountUsername(int accountId) {
        String sql = "SELECT username FROM account WHERE account_id = ?";

        try {
            String username = jdbcTemplate.queryForObject(sql, String.class, accountId);
            return username;
        }
        catch (DataAccessException e) {
            System.out.println("Unable to connect to database.");
        }
        return null;
    }

    //updating balance method for database
    @Override
    public boolean updateAccountBalance(int accountId, BigDecimal newAmount) {

        try {
            String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
            jdbcTemplate.queryForRowSet(sql, newAmount, accountId);
            return true;
        }
        catch (DataAccessException e) {
            return false;
        }
    }

    @Override
    public List<GetAllAccountsDTO> getAllAccounts() {
        String sql = "SELECT username, account_id FROM account";
        List<GetAllAccountsDTO> accountsList = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()) {
                accountsList.add(mapRowToGetAllAccountsDTO(results));
            }
        }
        catch (DataAccessException e) {
            System.out.println("Could not connect to database.");
        }
        return accountsList;
    }

    @Override
    public boolean verifyAccount(int targetAccountId, String username) {
        String sql = "SELECT username FROM account WHERE account_id = ?";
        String nameToCompare = (String) jdbcTemplate.queryForObject(sql, new Object[] {targetAccountId}, String.class);

        if (username.equals(nameToCompare)) {
            return true;
        }
        return false;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccount_id(rs.getInt("account_id"));
        account.setUsername(rs.getString("username"));
        account.setBalance(rs.getBigDecimal("balance"));

        return account;
    }

    private GetAllAccountsDTO mapRowToGetAllAccountsDTO(SqlRowSet rs) {
        GetAllAccountsDTO dto = new GetAllAccountsDTO();
        dto.setUsername(rs.getString("username"));
        dto.setAccountId(rs.getInt("account_id"));

        return dto;
    }
}
