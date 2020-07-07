package com.agenin.report.Model;

import com.google.gson.annotations.SerializedName;

public class TanggalModel {

    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;

    public TanggalModel(String from, String to) {
        this.from = from;
        this.to = to;
    }
}
