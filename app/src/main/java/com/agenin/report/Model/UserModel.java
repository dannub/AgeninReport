package com.agenin.report.Model;

import com.google.gson.annotations.SerializedName;

public class UserModel {
    @SerializedName("_id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("email")
    String email;

    @SerializedName("status")
    Boolean status;

    @SerializedName("token")
    String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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



    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
