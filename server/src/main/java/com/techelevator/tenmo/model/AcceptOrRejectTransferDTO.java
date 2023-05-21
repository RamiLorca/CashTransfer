package com.techelevator.tenmo.model;

import javax.validation.constraints.NotEmpty;

public class AcceptOrRejectTransferDTO {

    @NotEmpty
    private int transferId;
    @NotEmpty
    private boolean isAccepted;

    public AcceptOrRejectTransferDTO(int transferId, boolean isAccepted) {
        this.transferId = transferId;
        this.isAccepted = isAccepted;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

}
