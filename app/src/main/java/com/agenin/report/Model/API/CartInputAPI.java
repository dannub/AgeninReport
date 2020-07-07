package com.agenin.report.Model.API;

import com.agenin.report.Model.CartInputModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartInputAPI {
    @SerializedName("carts")
    private List<CartInputModel> carts;

    public CartInputAPI(List<CartInputModel> carts) {

        this.carts = carts;
    }


    public List<CartInputModel> getCarts() {
        return carts;
    }

    public void setCarts(List<CartInputModel> carts) {
        this.carts = carts;
    }
}
