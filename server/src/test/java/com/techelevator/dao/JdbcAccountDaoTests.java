package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.GetAllAccountsDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests {
    private static final Account TEST_ERIC_CAMERON_1 = new Account(2001, "eric_cameron_1", new BigDecimal("1000.00"));
    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAccountByIdTest() {
        Account returnedAccount = sut.getAccountById(2001);
        assertAccountsMatch(TEST_ERIC_CAMERON_1, returnedAccount);
    }

    @Test
    public void subtractFromBalanceTest() {
        BigDecimal updatedSenderBalance = sut.subtractFromBalance(2001, new BigDecimal("100.00"));
        Assert.assertEquals(new BigDecimal("900.00"), updatedSenderBalance);
    }

    @Test
    public void addToBalanceTest() {
        BigDecimal updatedReceiverBalance = sut.addToBalance(2001, new BigDecimal("100.00"));
        Assert.assertEquals(new BigDecimal("1100.00"), updatedReceiverBalance);
    }

    @Test
    public void getAccountBalanceTest() {
        BigDecimal returnedAmount = sut.getAccountBalance(2001);
        Assert.assertEquals(new BigDecimal("1000.00"), returnedAmount);
    }

    @Test
    public void getAccountUsernameTest() {
        String returnedUsername = sut.getAccountUsername(2001);
        Assert.assertEquals("eric_cameron_1", returnedUsername);
    }

    // This test will fail due to an issue that appears to throw an exception after the UPDATE sql query is run and
    // the results saved to the database. We will need to track down what's triggering the data access exception, but
    // at the moment the program functions as expected even with the data access exception.
    @Test
    public void updateAccountBalanceTest() {
        boolean updatedBalance = sut.updateAccountBalance(2001, new BigDecimal("5000.00"));
        Assert.assertTrue(updatedBalance);
    }

    @Test
    public void getAllAccountsTest() {
        List<GetAllAccountsDTO> returnedAccounts = sut.getAllAccounts();
        int actualSize = returnedAccounts.size();
        Assert.assertEquals(4, actualSize);
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccount_id(), actual.getAccount_id());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
}
