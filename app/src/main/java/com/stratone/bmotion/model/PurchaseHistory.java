package com.stratone.bmotion.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseHistory {
    @SerializedName("NIP")
    @Expose
    private Object nIP;
    @SerializedName("OrderNo")
    @Expose
    private String orderNo;
    @SerializedName("TransactionDate")
    @Expose
    private String transactionDate;
    @SerializedName("OutletNo")
    @Expose
    private String outletNo;
    @SerializedName("Liter")
    @Expose
    private String liter;

    public Object getNIP() {
        return nIP;
    }

    public void setNIP(Object nIP) {
        this.nIP = nIP;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getOutletNo() {
        return outletNo;
    }

    public void setOutletNo(String outletNo) {
        this.outletNo = outletNo;
    }

    public String getLiter() {
        return liter;
    }

    public void setLiter(String liter) {
        this.liter = liter;
    }
}
