package com.agenin.report;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.agenin.report.Adapter.PaymentAdapter;
import com.agenin.report.Adapter.UserAdapter;
import com.agenin.report.Fragment.PesananFragment;
import com.agenin.report.Fragment.UserFragment;
import com.agenin.report.Interface.PaymentClient;
import com.agenin.report.Interface.UserClient;
import com.agenin.report.Model.OrderListModel;
import com.agenin.report.Model.UserAPIModel;
import com.agenin.report.Model.UserModel;
import com.agenin.report.Preference.UserPreference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import info.hoang8f.widget.FButton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DBQueries {
    public static String url = "https://apk.agenin.id/";

    //public static String url = "http://192.168.100.9:8000/";
    public static List<UserAPIModel> userAPIModelArrayList = new ArrayList<>();
    public static List<OrderListModel> uploadBuktiModelList = new ArrayList<>();


    public static void requestLogin(Context context,@Nullable Dialog dialog){


            if (dialog!=null) {
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {

                    }
                });
            }


        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DBQueries.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);

        Call<UserModel> call =  client.login("apk.agenin.id@gmail.com", "agenin12345");

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()){

                    UserModel userModel = response.body();
                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", userModel);

                    if (dialog!=null) {
                        dialog.dismiss();
                    }



                } else {

                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", null);

                        if (dialog!=null) {
                            dialog.dismiss();
                        }


                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());

                    dialog.dismiss();

            }
        });
    }


    public static void loadPayment(final Context context, final Dialog loadingDialog,final String from,final String to) {
        uploadBuktiModelList.clear();
        PesananFragment.recyclerView.setVisibility(View.INVISIBLE);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (loadingDialog != null) {
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DBQueries.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);

        Call<UserModel> call =    client.login("apk.agenin.id@gmail.com", "Agenin12345");

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()){

                    UserModel user = response.body();
                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", user);


                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request newRequest  = chain.request().newBuilder()
                                            .addHeader("Authorization", "Bearer " + user.getToken())
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                            })
                            .readTimeout(2, TimeUnit.MINUTES)
                            .connectTimeout(2, TimeUnit.MINUTES)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    PaymentClient paymentAPI = retrofit.create(PaymentClient.class);
                    Call<List<OrderListModel>> call2 = paymentAPI.getAllOrder(user.getId(),from,to);
                    call2.enqueue(new Callback<List<OrderListModel>>() {
                        @Override
                        public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {
                            if (!response.isSuccessful()) {
                                try {
                                    Log.i("response", String.valueOf(response.errorBody().string()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                                return;
                            }
                          if (response.body().size()>0){



                                uploadBuktiModelList = response.body();


                                PesananFragment.recyclerView.setVisibility(View.VISIBLE);
                                PesananFragment.kosong.setVisibility(View.INVISIBLE);

                                PesananFragment.paymentAdapter = new PaymentAdapter(DBQueries.uploadBuktiModelList,context,loadingDialog);
                                PesananFragment.recyclerView.setAdapter( PesananFragment.paymentAdapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                PesananFragment.recyclerView.setLayoutManager(linearLayoutManager);

                                PesananFragment.paymentAdapter.notifyDataSetChanged();
                            }else {
                                PesananFragment.recyclerView.setVisibility(View.INVISIBLE);
                                PesananFragment.kosong.setVisibility(View.VISIBLE);
                            }
                            if (loadingDialog != null) {
                                loadingDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<List<OrderListModel>> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            if (loadingDialog!=null) {
                                loadingDialog.dismiss();
                            }
                        }
                    });


                } else {

                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", null);

                    if (loadingDialog!=null) {
                        loadingDialog.dismiss();
                    }


                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());

                loadingDialog.dismiss();

            }
        });





}





    public static void loadUsers(final Context context, final Dialog loadingDialog,final String from,final String to) {
        userAPIModelArrayList.clear();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (loadingDialog!=null) {
            loadingDialog.show();
            loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        }

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(DBQueries.url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        UserClient client = retrofit.create(UserClient.class);

        Call<UserModel> call =    client.login("apk.agenin.id@gmail.com", "Agenin12345");

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()){

                    UserModel user = response.body();
                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", user);


                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new Interceptor() {
                                @Override
                                public okhttp3.Response intercept(Chain chain) throws IOException {
                                    Request newRequest  = chain.request().newBuilder()
                                            .addHeader("Authorization", "Bearer " + user.getToken())
                                            .build();
                                    return chain.proceed(newRequest);
                                }
                            })
                            .readTimeout(2, TimeUnit.MINUTES)
                            .connectTimeout(2, TimeUnit.MINUTES)
                            .build();
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    UserClient userAPI = retrofit.create(UserClient.class);
                    Call<List<UserAPIModel>> call2 = userAPI.getAllUser(from,to);
                    call2.enqueue(new Callback<List<UserAPIModel>>() {
                        @Override
                        public void onResponse(Call<List<UserAPIModel>> call, Response<List<UserAPIModel>> response) {
                            if (!response.isSuccessful()) {
                                try {
                                    Log.i("response", String.valueOf(response.errorBody().string()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                                return;
                            }
                            if (response.body().size()>0){

                                UserFragment.recyclerView.setVisibility(View.VISIBLE);
                                UserFragment.kosong.setVisibility(View.INVISIBLE);
                                userAPIModelArrayList = response.body();
                                UserFragment.userAdapter = new UserAdapter(DBQueries.userAPIModelArrayList,loadingDialog);
                                UserFragment.recyclerView.setAdapter( UserFragment.userAdapter);

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                UserFragment.recyclerView.setLayoutManager(linearLayoutManager);

                                UserFragment.userAdapter.notifyDataSetChanged();
                            }else {
                                UserFragment.recyclerView.setVisibility(View.INVISIBLE);
                                UserFragment.kosong.setVisibility(View.VISIBLE);
                            }
                            if (loadingDialog != null) {
                                loadingDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<List<UserAPIModel>> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            if (loadingDialog!=null) {
                                loadingDialog.dismiss();
                            }
                        }
                    });



                } else {

                    UserPreference userPreference = new UserPreference(context);
                    userPreference.setUserPreference("user", null);

                    if (loadingDialog!=null) {
                        loadingDialog.dismiss();
                    }


                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("debug", "onFailure: ERROR > " + t.toString());

                loadingDialog.dismiss();

            }
        });



    }

    public static String currencyFormatter(String num) {
        double m = Double.parseDouble(num);
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(m);
    }

}