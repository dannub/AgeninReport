package com.agenin.report.Model;

import com.agenin.report.Model.ProductModel;
import com.google.gson.annotations.SerializedName;

public class OrderListModel {
    @SerializedName("ordered_date")
    private String ordered_date;
    @SerializedName("confirmed_date")
    private String confirmed_date;
    @SerializedName("packed_date")
    private String packed_date;
    @SerializedName("shipped_date")
    private String shipped_date;
    @SerializedName("delivered_date")
    private String delivered_date;
    @SerializedName("canceled_date")
    private String canceled_date;
    @SerializedName("confirmed")
    private Boolean confirmed;
    @SerializedName("packed")
    private Boolean packed;
    @SerializedName("shipped")
    private Boolean shipped;
    @SerializedName("delivered")
    private Boolean delivered;
    @SerializedName("canceled")
    private Boolean canceled;
    @SerializedName("_id")
    private String _id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("username")
    private String username;
    @SerializedName("bukti")
    private String bukti;
    @SerializedName("atas_nama")
    private String atas_nama;
    @SerializedName("bank")
    private String bank;
    @SerializedName("tgl_transfer")
    private String tgl_transfer;
    @SerializedName("total_amount")
    private Integer total_amount;
    @SerializedName("date")
    private String date;

    public String getTgl_transfer() {
        return tgl_transfer;
    }

    public void setTgl_transfer(String tgl_transfer) {
        this.tgl_transfer = tgl_transfer;
    }

    public Integer getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(Integer total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public String getConfirmed_date() {
        return confirmed_date;
    }

    public void setConfirmed_date(String confirmed_date) {
        this.confirmed_date = confirmed_date;
    }

    public String getPacked_date() {
        return packed_date;
    }

    public void setPacked_date(String packed_date) {
        this.packed_date = packed_date;
    }

    public String getShipped_date() {
        return shipped_date;
    }

    public void setShipped_date(String shipped_date) {
        this.shipped_date = shipped_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public String getCanceled_date() {
        return canceled_date;
    }

    public void setCanceled_date(String canceled_date) {
        this.canceled_date = canceled_date;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean getPacked() {
        return packed;
    }

    public void setPacked(Boolean packed) {
        this.packed = packed;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }

    public String getAtas_nama() {
        return atas_nama;
    }

    public void setAtas_nama(String atas_nama) {
        this.atas_nama = atas_nama;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
