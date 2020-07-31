package com.stratone.bmotion.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fuel {
    @SerializedName("FuelId")
    @Expose
    private Integer fuelId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("IsSubsidy")
    @Expose
    private String isSubsidy;
    @SerializedName("BackgroundColor")
    @Expose
    private String backgroundColor;
    @SerializedName("TextColor")
    @Expose
    private String textColor;
    @SerializedName("Liter")
    @Expose
    private int liter;

    public Integer getFuelId() {
        return fuelId;
    }

    public void setFuelId(Integer fuelId) {
        this.fuelId = fuelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsSubsidy() {
        return isSubsidy;
    }

    public void setIsSubsidy(String isSubsidy) {
        this.isSubsidy = isSubsidy;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getLiter() {
        return liter;
    }

    public void setLiter(int liter) {
        this.liter = liter;
    }
}
