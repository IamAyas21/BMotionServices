package com.stratone.bmotion.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails {
    @SerializedName("OrderDetailId")
    @Expose
    private Integer orderDetailId;
    @SerializedName("FuelId")
    @Expose
    private Integer fuelId;
    @SerializedName("OrderNo")
    @Expose
    private Object orderNo;
    @SerializedName("Liter")
    @Expose
    private Integer liter;

    public Integer getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getFuelId() {
        return fuelId;
    }

    public void setFuelId(Integer fuelId) {
        this.fuelId = fuelId;
    }

    public Object getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Object orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getLiter() {
        return liter;
    }

    public void setLiter(Integer liter) {
        this.liter = liter;
    }
}
