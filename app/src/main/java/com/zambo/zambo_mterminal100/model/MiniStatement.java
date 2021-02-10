
package com.zambo.zambo_mterminal100.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MiniStatement {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("statements")
    @Expose
    private List<Statement> statements = null;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("aadhaar")
    @Expose
    private String aadhaar;
    @SerializedName("txndate")
    @Expose
    private String txndate;
    @SerializedName("txntime")
    @Expose
    private String txntime;
    @SerializedName("bccode")
    @Expose
    private String bccode;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("bcLocation")
    @Expose
    private String bcLocation;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getTxndate() {
        return txndate;
    }

    public void setTxndate(String txndate) {
        this.txndate = txndate;
    }

    public String getTxntime() {
        return txntime;
    }

    public void setTxntime(String txntime) {
        this.txntime = txntime;
    }

    public String getBccode() {
        return bccode;
    }

    public void setBccode(String bccode) {
        this.bccode = bccode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBcLocation() {
        return bcLocation;
    }

    public void setBcLocation(String bcLocation) {
        this.bcLocation = bcLocation;
    }

}
