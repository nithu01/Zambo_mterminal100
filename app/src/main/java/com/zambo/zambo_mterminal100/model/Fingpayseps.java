
package com.zambo.zambo_mterminal100.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fingpayseps {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("outlet")
    @Expose
    private Outlet outlet;
    @SerializedName("transId")
    @Expose
    private String transId;

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

    public Outlet getOutlet() {
        return outlet;
    }

    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

}
