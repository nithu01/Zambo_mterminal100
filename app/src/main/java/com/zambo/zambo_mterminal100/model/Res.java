
package com.zambo.zambo_mterminal100.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Res {




    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("ErrorCode")
    @Expose
    private String errorCode;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("SenderMobileNo")
    @Expose
    private String senderMobileNo;
    @SerializedName("Account_Detail")
    @Expose
    private List<AccountDetail_> accountDetail = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderMobileNo() {
        return senderMobileNo;
    }

    public void setSenderMobileNo(String senderMobileNo) {
        this.senderMobileNo = senderMobileNo;
    }

    public List<AccountDetail_> getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(List<AccountDetail_> accountDetail) {
        this.accountDetail = accountDetail;
    }
    @SerializedName("aepsStatus")
    @Expose
    private String aepsStatus;

    public String getAepsStatus() {
        return aepsStatus;
    }

    public void setAepsStatus(String aepsStatus) {
        this.aepsStatus = aepsStatus;
    }
}
