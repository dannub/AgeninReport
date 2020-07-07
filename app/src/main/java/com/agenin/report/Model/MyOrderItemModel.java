package com.agenin.report.Model;

import com.google.gson.annotations.SerializedName;

public class MyOrderItemModel {

    @SerializedName("_id")
    private String _id;

    @SerializedName("user_id")
    private String user_id;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("handphone")
    private String handphone;

    @SerializedName("atas_nama")
    private String atas_nama;
    @SerializedName("bank")
    private String bank;
    @SerializedName("tgl_transfer")
    private String tgl_transfer;

    @SerializedName("total_ongkir")
    private String total_ongkir;
    @SerializedName("total_item_price")
    private String total_item_price;
    @SerializedName("total_amount")
    private String total_amount;
    @SerializedName("save_ongkir")
    private String save_ongkir;



    @SerializedName("full_address")
    private String full_address;
    @SerializedName("phone")
    private String phone;
    @SerializedName("detail_address")
    private String detail_address;
    @SerializedName("kode_pos")
    private String kode_pos;

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
    @SerializedName("date")
    private String date;

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




    @SerializedName("ket_kirim")
    private String ket_kirim;
    @SerializedName("metode_kirim")
    private String metode_kirim;


    public String getHandphone() {
        return handphone;
    }

    public void setHandphone(String handphone) {
        this.handphone = handphone;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAtas_nama(String atas_nama) {
        this.atas_nama = atas_nama;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setTgl_transfer(String tgl_transfer) {
        this.tgl_transfer = tgl_transfer;
    }

    public void setTotal_ongkir(String total_ongkir) {
        this.total_ongkir = total_ongkir;
    }

    public void setTotal_item_price(String total_item_price) {
        this.total_item_price = total_item_price;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public void setSave_ongkir(String save_ongkir) {
        this.save_ongkir = save_ongkir;
    }

    public void setFull_address(String full_address) {
        this.full_address = full_address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDetail_address(String detail_address) {
        this.detail_address = detail_address;
    }

    public void setKode_pos(String kode_pos) {
        this.kode_pos = kode_pos;
    }

    public void setOrdered_date(String ordered_date) {
        this.ordered_date = ordered_date;
    }

    public void setConfirmed_date(String confirmed_date) {
        this.confirmed_date = confirmed_date;
    }

    public void setPacked_date(String packed_date) {
        this.packed_date = packed_date;
    }

    public void setShipped_date(String shipped_date) {
        this.shipped_date = shipped_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public void setCanceled_date(String canceled_date) {
        this.canceled_date = canceled_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setPacked(Boolean packed) {
        this.packed = packed;
    }

    public void setShipped(Boolean shipped) {
        this.shipped = shipped;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public void setCanceled(Boolean canceled) {
        this.canceled = canceled;
    }

    public void setKet_kirim(String ket_kirim) {
        this.ket_kirim = ket_kirim;
    }

    public void setMetode_kirim(String metode_kirim) {
        this.metode_kirim = metode_kirim;
    }

    public String get_id() {
        return _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAtas_nama() {
        return atas_nama;
    }

    public String getBank() {
        return bank;
    }

    public String getTgl_transfer() {
        return tgl_transfer;
    }

    public String getTotal_ongkir() {
        return total_ongkir;
    }

    public String getTotal_item_price() {
        return total_item_price;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public String getSave_ongkir() {
        return save_ongkir;
    }

    public String getFull_address() {
        return full_address;
    }

    public String getPhone() {
        return phone;
    }

    public String getDetail_address() {
        return detail_address;
    }

    public String getKode_pos() {
        return kode_pos;
    }

    public String getOrdered_date() {
        return ordered_date;
    }

    public String getConfirmed_date() {
        return confirmed_date;
    }

    public String getPacked_date() {
        return packed_date;
    }

    public String getShipped_date() {
        return shipped_date;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public String getCanceled_date() {
        return canceled_date;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public Boolean getPacked() {
        return packed;
    }

    public Boolean getShipped() {
        return shipped;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public Boolean getCanceled() {
        return canceled;
    }

    public String getKet_kirim() {
        return ket_kirim;
    }

    public String getMetode_kirim() {
        return metode_kirim;
    }


}
