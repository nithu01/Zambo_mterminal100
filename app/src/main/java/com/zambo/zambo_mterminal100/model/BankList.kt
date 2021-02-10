package com.zambo.zambo_mterminal100.model


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


class BankList {

    @SerializedName("id")
    @Expose
    private var id: String? = null
    @SerializedName("bankid")
    @Expose
    private var bankid: String? = null
    @SerializedName("activeFlag")
    @Expose
    private var activeFlag: String? = null
    @SerializedName("bankName")
    @Expose
    private var bankName: String? = null
    @SerializedName("details")
    @Expose
    private var details: String? = null
    @SerializedName("remarks")
    @Expose
    private var remarks: String? = null
    @SerializedName("timestamp")
    @Expose
    private var timestamp: String? = null
    @SerializedName("iinno")
    @Expose
    private var iinno: String? = null
    @SerializedName("pay")
    @Expose
    private var pay: String? = null
    @SerializedName("deposit")
    @Expose
    private var deposit: String? = null
    @SerializedName("lastupdate")
    @Expose
    private var lastupdate: String? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getBankid(): String? {
        return bankid
    }

    fun setBankid(bankid: String) {
        this.bankid = bankid
    }

    fun getActiveFlag(): String? {
        return activeFlag
    }

    fun setActiveFlag(activeFlag: String) {
        this.activeFlag = activeFlag
    }

    fun getBankName(): String? {
        return bankName
    }

    fun setBankName(bankName: String) {
        this.bankName = bankName
    }

    fun getDetails(): String? {
        return details
    }

    fun setDetails(details: String) {
        this.details = details
    }

    fun getRemarks(): String? {
        return remarks
    }

    fun setRemarks(remarks: String) {
        this.remarks = remarks
    }

    fun getTimestamp(): String? {
        return timestamp
    }

    fun setTimestamp(timestamp: String) {
        this.timestamp = timestamp
    }

    fun getIinno(): String? {
        return iinno
    }

    fun setIinno(iinno: String) {
        this.iinno = iinno
    }

    fun getPay(): String? {
        return pay
    }

    fun setPay(pay: String) {
        this.pay = pay
    }

    fun getDeposit(): String? {
        return deposit
    }

    fun setDeposit(deposit: String) {
        this.deposit = deposit
    }

    fun getLastupdate(): String? {
        return lastupdate
    }

    fun setLastupdate(lastupdate: String) {
        this.lastupdate = lastupdate
    }
}