package com.stratone.bmotion.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("NIP")
    @Expose
    private String nIP;
    @SerializedName("RoleId")
    @Expose
    private Integer roleId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Profession")
    @Expose
    private String profession;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("Password")
    @Expose
    private Object password;
    @SerializedName("Phone")
    @Expose
    private String phone;

    public String getNIP() {
        return nIP;
    }

    public void setNIP(String nIP) {
        this.nIP = nIP;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
