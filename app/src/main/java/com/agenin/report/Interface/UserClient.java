package com.agenin.report.Interface;

import com.agenin.report.Model.UserAPIModel;
import com.agenin.report.Model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserClient {
    @FormUrlEncoded
    @POST("api/user/")
    Call<List<UserAPIModel>> getAllUser(@Field("from") String from, @Field("to")String to);

    @FormUrlEncoded
    @POST("api/user/login/?role=admin")
    Call<UserModel> login(@Field("email") String email, @Field("password")String password);

    @FormUrlEncoded
    @POST("api/user/status/update/{id}")
    Call<List<UserAPIModel>> updateStatus(@Path("id") String userId,@Field("from") String from, @Field("to")String to);
}
