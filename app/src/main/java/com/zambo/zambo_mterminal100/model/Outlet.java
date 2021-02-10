
package com.zambo.zambo_mterminal100.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Outlet {

    @SerializedName("merchantUserId")
    @Expose
    private String merchantUserId;
    @SerializedName("merchantPassword")
    @Expose
    private String merchantPassword;
    @SerializedName("merchantId")
    @Expose
    private String merchantId;
    @SerializedName("merchantName")
    @Expose
    private String merchantName;
    @SerializedName("merchantContactNumber")
    @Expose
    private String merchantContactNumber;
    @SerializedName("merchantAddress1")
    @Expose
    private String merchantAddress1;
    @SerializedName("merchantAddress2")
    @Expose
    private String merchantAddress2;
    @SerializedName("merchantCity")
    @Expose
    private String merchantCity;
    @SerializedName("merchantState")
    @Expose
    private String merchantState;
    @SerializedName("merchantPinCode")
    @Expose
    private String merchantPinCode;
    @SerializedName("merchantCatCode")
    @Expose
    private String merchantCatCode;
    @SerializedName("merchantCountry")
    @Expose
    private String merchantCountry;

    public String getMerchantUserId() {
        return merchantUserId;
    }

    public void setMerchantUserId(String merchantUserId) {
        this.merchantUserId = merchantUserId;
    }

    public String getMerchantPassword() {
        return merchantPassword;
    }

    public void setMerchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantContactNumber() {
        return merchantContactNumber;
    }

    public void setMerchantContactNumber(String merchantContactNumber) {
        this.merchantContactNumber = merchantContactNumber;
    }

    public String getMerchantAddress1() {
        return merchantAddress1;
    }

    public void setMerchantAddress1(String merchantAddress1) {
        this.merchantAddress1 = merchantAddress1;
    }

    public String getMerchantAddress2() {
        return merchantAddress2;
    }

    public void setMerchantAddress2(String merchantAddress2) {
        this.merchantAddress2 = merchantAddress2;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getMerchantState() {
        return merchantState;
    }

    public void setMerchantState(String merchantState) {
        this.merchantState = merchantState;
    }

    public String getMerchantPinCode() {
        return merchantPinCode;
    }

    public void setMerchantPinCode(String merchantPinCode) {
        this.merchantPinCode = merchantPinCode;
    }

    public String getMerchantCatCode() {
        return merchantCatCode;
    }

    public void setMerchantCatCode(String merchantCatCode) {
        this.merchantCatCode = merchantCatCode;
    }

    public String getMerchantCountry() {
        return merchantCountry;
    }

    public void setMerchantCountry(String merchantCountry) {
        this.merchantCountry = merchantCountry;
    }

}
