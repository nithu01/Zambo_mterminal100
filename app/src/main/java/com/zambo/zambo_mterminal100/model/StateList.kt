package com.zambo.zambo_mterminal100.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StateList {
    @SerializedName("id")
    @Expose
    private var id: String? = null

    @SerializedName("name")
    @Expose
    private var name: String? = null

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }
}