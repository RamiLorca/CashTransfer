package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Transfer {
    private int transfer_id;
    private String transfer_status;
    private int sender_id;
    private int receiver_id;
    private BigDecimal amount;
    private LocalDateTime time_sent;

    public Transfer() {
    }

    public Transfer(int transfer_id, String transfer_status, int sender_id, int receiver_id, BigDecimal amount, LocalDateTime time_sent) {
        this.transfer_id = transfer_id;
        this.transfer_status = transfer_status;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.amount = amount;
        this.time_sent = time_sent;
    }

    public Transfer(String transfer_status, int sender_id, int receiver_id, BigDecimal amount, LocalDateTime time_sent) {
        this.transfer_status = transfer_status;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.amount = amount;
        this.time_sent = time_sent;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getTransfer_status() {
        return transfer_status;
    }

    public void setTransfer_status(String transfer_status) {
        this.transfer_status = transfer_status;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTime_sent() {
        return time_sent;
    }

    public void setTime_sent(LocalDateTime time_sent) {
        this.time_sent = time_sent;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transfer_id=" + transfer_id +
                ", transfer_status='" + transfer_status + '\'' +
                ", sender_id=" + sender_id +
                ", receiver_id=" + receiver_id +
                ", amount=" + amount +
                ", time_sent=" + time_sent +
                '}';
    }
}
