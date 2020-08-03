package com.stratone.bmotion.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stratone.bmotion.model.PurchaseHistory;

import java.util.List;

public class ResponsePurchaseHistory {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("Data")
    @Expose
    private List<PurchaseHistory> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<PurchaseHistory> getData() {
        return data;
    }

    public void setData(List<PurchaseHistory> data) {
        this.data = data;
    }
}
