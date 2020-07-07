package com.agenin.report.Model.API;

import com.agenin.report.Model.OrderListModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderUserAPI {
    @SerializedName("items")
    private List<OrderListModel> orderListModels;

    public List<OrderListModel> getOrderListModels() {
        return orderListModels;
    }

    public void setOrderListModels(List<OrderListModel> orderListModels) {
        this.orderListModels = orderListModels;
    }
}
