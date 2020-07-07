package com.agenin.report.Model;

import com.google.gson.annotations.SerializedName;

public class UserAPIModel {
    @SerializedName("_id")
    private  String _id;
    @SerializedName("name")
    private  String name;
    @SerializedName("email")
    private  String email;
    @SerializedName("name_refferal")
    private  String name_refferal;
    @SerializedName("bukti")
    private  String bukti;
    @SerializedName("bukti_tgl")
    private  String bukti_tgl;
    @SerializedName("bukti_bank")
    private  String bukti_bank;
    @SerializedName("bukti_an")
    private  String bukti_an;
    @SerializedName("status")
    private  Boolean status;
    @SerializedName("handphone")
    private   String handphone;
    @SerializedName("lastseen")
    private  String lastseen;
    @SerializedName("date")
    private  String date;

    public String get_id() {
        return _id;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName_refferal() {
        return name_refferal;
    }

    public void setName_refferal(String name_refferal) {
        this.name_refferal = name_refferal;
    }

    public String getBukti_tgl() {
        return bukti_tgl;
    }

    public void setBukti_tgl(String bukti_tgl) {
        this.bukti_tgl = bukti_tgl;
    }

    public String getBukti_bank() {
        return bukti_bank;
    }

    public void setBukti_bank(String bukti_bank) {
        this.bukti_bank = bukti_bank;
    }

    public String getBukti_an() {
        return bukti_an;
    }

    public void setBukti_an(String bukti_an) {
        this.bukti_an = bukti_an;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public String getLastseen() {
        return lastseen;
    }

    public void setLastseen(String lastseen) {
        this.lastseen = lastseen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
