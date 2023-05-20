package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {
    private static final Transfer TEST_TRANSFER_1 = new Transfer(3001, "Pending", 2001, 2002,
                                                    new BigDecimal("150.00"), LocalDateTime.of(2023, 4,
                                         27, 12, 18, 41));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void initiateTransferTest() {
        boolean isInitiated = sut.initiateTransfer(2002, 2001, new BigDecimal("300.00"));
        Assert.assertTrue(isInitiated);
        Transfer transfer = sut.getTransferDetailsById(3002);
        Assert.assertEquals(3002, transfer.getTransfer_id());
    }

    // This test will fail due to an issue that appears to throw an exception after the UPDATE sql query is run and
    // the results saved to the database. We will need to track down what's triggering the data access exception, but
    // at the moment the program functions as expected even with the data access exception.
    @Test
    public void acceptTransferTest() {
        boolean isAccepted = sut.acceptTransfer(3001);
        Assert.assertTrue(isAccepted);
        Transfer transfer = sut.getTransferDetailsById(3001);
        Assert.assertEquals("Accepted", transfer.getTransfer_status());
    }

    // This test will fail due to an issue that appears to throw an exception after the UPDATE sql query is run and
    // the results saved to the database. We will need to track down what's triggering the data access exception, but
    // at the moment the program functions as expected even with the data access exception.
    @Test
    public void cancelTransferTest() {
        boolean isCancelled = sut.cancelTransfer(3001);
        Assert.assertTrue(isCancelled);
        Transfer transfer = sut.getTransferDetailsById(3001);
        Assert.assertEquals("Cancelled", transfer.getTransfer_status());
    }

    @Test
    public void getTransferListByAccountIdTest() {
        List<Transfer> returnedTransfers = sut.getTransfersListByAccountId(2001);
        int actualSize = returnedTransfers.size();
        Assert.assertEquals(1, actualSize);
    }

    @Test
    public void getTransferDetailsByIdTest() {
        Transfer transfer = sut.getTransferDetailsById(3001);
        assertTransfersMatch(TEST_TRANSFER_1, transfer);
    }

    @Test
    public void getPendingTransfersByAccountIdTest() {
        List<Transfer> returnedTransfers = sut.getPendingTransfersByAccountId(2001);
        int actualSize = returnedTransfers.size();
        Assert.assertEquals(1, actualSize);
    }

    @Test
    public void isValidTest() {
        boolean isValid = sut.isValid(2001, 2002, new BigDecimal("150.00"));
        Assert.assertTrue(isValid);
    }

    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransfer_id(), actual.getTransfer_id());
        Assert.assertEquals(expected.getTransfer_status(), actual.getTransfer_status());
        Assert.assertEquals(expected.getSender_id(), actual.getSender_id());
        Assert.assertEquals(expected.getReceiver_id(), actual.getReceiver_id());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
        Assert.assertEquals(expected.getTime_sent(), actual.getTime_sent());
    }
}
