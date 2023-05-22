package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.AcceptOrRejectTransferDTO;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void initiateTransfer(@RequestBody Transfer transfer) {
        int senderId = transfer.getSenderId();
        int receiverId = transfer.getReceiverId();
        BigDecimal amount = transfer.getAmount();

        if(!transferDao.initiateTransfer(senderId, receiverId, amount)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transfer initialization failed.");
        }
    }

    @RequestMapping(path = "/transfers/{accountId}", method = RequestMethod.GET)
    public List<Transfer> getMyTransfers (@PathVariable Integer accountId) {
        List<Transfer> transfers = transferDao.getTransfersListByAccountId(accountId);

        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found.");
        }
        else {
            return transfers;
        }
    }

    @RequestMapping(path = "/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer getTransfer (@PathVariable Integer transferId) {
        Transfer transfer = transferDao.getTransferDetailsById(transferId);

        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        }
        else {
            return transfer;
        }
    }

    @RequestMapping(path = "/transfers/{accountId}/pending", method = RequestMethod.GET)
    public List<Transfer> getMyPendingTransfers (@PathVariable Integer accountId) {
        List<Transfer> transfers = transferDao.getPendingTransfersByAccountId(accountId);

        if (transfers == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transfers found.");
        }
        else {
            return transfers;
        }
    }
    //used to have OK response annotation here
    @RequestMapping(path = "/transfer", method = RequestMethod.PUT)
    public void acceptOrRejectTransfer (@RequestBody AcceptOrRejectTransferDTO transferDTO) {
        Transfer transfer = transferDao.getTransferDetailsById(transferDTO.getTransferId());

        if (transferDTO.isAccepted()) {
            BigDecimal newSenderBalance = accountDao.subtractFromBalance(transfer.getSenderId(), transfer.getAmount());
            BigDecimal newReceiverBalance = accountDao.addToBalance(transfer.getReceiverId(), transfer.getAmount());

            accountDao.updateAccountBalance(transfer.getSenderId(), newSenderBalance);
            accountDao.updateAccountBalance(transfer.getReceiverId(), newReceiverBalance);

            transferDao.acceptTransfer(transfer.getTransferId());
        }
        else {
            transferDao.cancelTransfer(transfer.getTransferId());
        }
    }

}
