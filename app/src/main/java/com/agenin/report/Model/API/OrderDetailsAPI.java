package com.agenin.report.Model.API;

import com.google.gson.annotations.SerializedName;

public class OrderDetailsAPI {
    @SerializedName("items")
    private OrderDetailsModel items;

    public OrderDetailsModel getItems() {
        return items;
    }
}
