package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // We were working on creating a second method that will request funds rather than initiate a send, however
    // we have realized that this initiateTransfer could be used in both situations. Therefore, we are keeping it
    // to a single method to handle initiated sends and requested transfers.
    @Override
    public boolean initiateTransfer(int senderId, int receiverId, BigDecimal amount) {
        String sql = "INSERT INTO transfer (transfer_status, sender_id, " +
                "receiver_id, amount, time_sent) VALUES ('Pending', ?, ?, ?, ?) RETURNING transfer_id";
        LocalDateTime timestamp = LocalDateTime.now();

        try {

            if(!isValid(senderId, receiverId, amount)) {
                return false;
            }

            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, senderId, receiverId, amount, timestamp);

        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean acceptTransfer(int transferId) {
        try {
            String sql = "UPDATE transfer SET transfer_status = 'Accepted' WHERE transfer_id = ?";
            jdbcTemplate.update(sql, transferId);
            return true;
        }
        catch (BadSqlGrammarException e) {
            System.out.println("Bad SQL grammar.");
        }
        catch (DataAccessException e) {
            System.out.println("Failed to accept transfer.");
        }
        return false;
    }

    @Override
    public boolean cancelTransfer(int transferId) {

        try {
            String sql = "UPDATE transfer SET transfer_status = 'Cancelled' WHERE transfer_id = ?";
            jdbcTemplate.update(sql, transferId);
            return true;
        }
        catch (DataAccessException e) {
            System.out.println("Failed to cancel transfer.");
        }
        return false;
    }

    @Override
    public List<Transfer> getTransfersListByAccountId(int accountId) {
        String sql = "SELECT * FROM transfer JOIN account ON transfer.sender_id = account.account_id" +
                " WHERE sender_id = ? OR receiver_id = ?";
        List<Transfer> transferList = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            while (results.next()) {
                transferList.add(mapRowToTransfer(results));
            }
        }
        catch (DataAccessException e) {
            System.out.println("Could not connect to database.");
        }

        return transferList;
    }

    @Override
    public Transfer getTransferDetailsById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
            if (rowSet.next()) {
                return mapRowToTransfer(rowSet);
            }
        }
        catch (DataAccessException e) {
            System.out.println("Cannot connect to database.");
        }

        return null;
    }

    @Override
    public List<Transfer> getPendingTransfersByAccountId(int accountId) {
        String sql = "SELECT * FROM transfer JOIN account ON transfer.sender_id = account.account_id" +
                " WHERE transfer_status LIKE ('Pending') AND (sender_id = ? OR receiver_id = ?)";
        List<Transfer> transferList = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, accountId);
            while (results.next()) {
                transferList.add(mapRowToTransfer(results));
            }
        }
        catch (DataAccessException e) {
            System.out.println("Could not connect to database.");
        }

        return transferList;
    }

    public boolean isValid(int senderId, int receiverId, BigDecimal amount) throws DataAccessException {

        if(senderId == receiverId) {
            return false;
        }

        if(amount.compareTo(new BigDecimal("0")) <= 0) {
            return false;
        }

        String sql = "SELECT balance FROM account WHERE account_id = ?";
        BigDecimal senderBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, senderId);
        if(amount.compareTo(senderBalance) >= 0) {
            return false;
        }

        return true;
    }

    public Transfer mapRowToTransfer (SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferStatus(result.getString("transfer_status"));
        transfer.setSenderId(result.getInt("sender_id"));
        transfer.setReceiverId(result.getInt("receiver_id"));
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setAmount(result.getBigDecimal("amount"));
        transfer.setTimeSent((result.getTimestamp("time_sent")).toLocalDateTime());
        return transfer;
    }
}
