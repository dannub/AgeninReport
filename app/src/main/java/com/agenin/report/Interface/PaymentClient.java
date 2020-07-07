package com.agenin.report.Interface;

import com.agenin.report.Model.API.OrderDetailsAPI;
import com.agenin.report.Model.OrderListModel;
import com.agenin.report.Model.TanggalModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentClient {
    @GET("api/user/{id}/mynota/{notaid}")
    Call<OrderDetailsAPI> getMyOrderDetails(
            @Path("id")String _id,
            @Path("notaid")String nota_id
    );

    @FormUrlEncoded
    @POST("api/user/{adminid}/mynota")
    Call<List<OrderListModel>> getAllOrder(
            @Path("adminid")String _id,
            @Field("from") String from,
            @Field("to")String to
    );

    @FormUrlEncoded
    @PATCH("api/user/{id}/mynota/delete/{notaid}/?admin=true")
    Call<List<OrderListModel>> deleteNota(
            @Path("id")String _id,
            @Path("notaid")String nota_id,
            @Field("from") String from,
            @Field("to")String to
    );

    @PATCH("api/user/{id}/mynota/update/{notaid}")
    Call<ResponseBody> updateStatusNota(
            @Path("id")String _id,
            @Path("notaid")String nota_id);

    @FormUrlEncoded
    @PATCH("api/user/{id}/mynota/update/{notaid}")
    Call<ResponseBody> updateShippedNota(
            @Path("id")String _id,
            @Path("notaid")String nota_id,
            @Field("metode_kirim") String metode_kirim,
            @Field("ket_kirim")String resi
    );
}
