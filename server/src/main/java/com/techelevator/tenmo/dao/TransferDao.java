package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    public boolean initiateTransfer(int senderId, int receiverId, BigDecimal amount);

    public boolean acceptTransfer(int transferId);

    public boolean cancelTransfer(int transferId);

   // public boolean acceptOrReject(int transferId, boolean isAccepted);

    public List<Transfer> getTransfersListByAccountId(int accountId);

    public Transfer getTransferDetailsById(int transferId);

    public List<Transfer> getPendingTransfersByAccountId(int accountId);
}
