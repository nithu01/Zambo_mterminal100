
package com.zambo.zambo_mterminal100.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Complaint {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("service")
    @Expose
    private String service;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("complainId")
    @Expose
    private String complainId;
    @SerializedName("complainDate")
    @Expose
    private String complainDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("rtRemark")
    @Expose
    private String rtRemark;
    @SerializedName("staffRemark")
    @Expose
    private String staffRemark;
    @SerializedName("txnDate")
    @Expose
    private String txnDate;
    @SerializedName("userId")
    @Expose
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getComplainDate() {
        return complainDate;
    }

    public void setComplainDate(String complainDate) {
        this.complainDate = complainDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRtRemark() {
        return rtRemark;
    }

    public void setRtRemark(String rtRemark) {
        this.rtRemark = rtRemark;
    }

    public String getStaffRemark() {
        return staffRemark;
    }

    public void setStaffRemark(String staffRemark) {
        this.staffRemark = staffRemark;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
