
package com.zambo.zambo_mterminal100.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceData {

    @SerializedName("transactionType")
    @Expose
    private String transactionType;
    @SerializedName("merchantTranId")
    @Expose
    private String merchantTranId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("balanceAmount")
    @Expose
    private String balanceAmount;
    @SerializedName("adhaarNumber")
    @Expose
    private String adhaarNumber;
    @SerializedName("bankRRN")
    @Expose
    private String bankRRN;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("bcCode")
    @Expose
    private String bcCode;
    @SerializedName("bcName")
    @Expose
    private String bcName;
    @SerializedName("bcLocation")
    @Expose
    private String bcLocation;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("status")
    @Expose
    private String status;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMerchantTranId() {
        return merchantTranId;
    }

    public void setMerchantTranId(String merchantTranId) {
        this.merchantTranId = merchantTranId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(String balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(String adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public String getBankRRN() {
        return bankRRN;
    }

    public void setBankRRN(String bankRRN) {
        this.bankRRN = bankRRN;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBcCode() {
        return bcCode;
    }

    public void setBcCode(String bcCode) {
        this.bcCode = bcCode;
    }

    public String getBcName() {
        return bcName;
    }

    public void setBcName(String bcName) {
        this.bcName = bcName;
    }

    public String getBcLocation() {
        return bcLocation;
    }

    public void setBcLocation(String bcLocation) {
        this.bcLocation = bcLocation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
