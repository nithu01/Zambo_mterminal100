
package com.zambo.zambo_mterminal100.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statement {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("txnType")
    @Expose
    private String txnType;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("narration")
    @Expose
    private String narration;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

}
