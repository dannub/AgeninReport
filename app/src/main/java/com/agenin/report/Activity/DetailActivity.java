package com.agenin.report.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.agenin.report.Adapter.CartPaymentAdapter;
import com.agenin.report.Adapter.PaymentAdapter;
import com.agenin.report.DBQueries;
import com.agenin.report.Fragment.PesananFragment;
import com.agenin.report.Interface.PaymentClient;
import com.agenin.report.Interface.UserClient;
import com.agenin.report.Model.API.OrderDetailsAPI;
import com.agenin.report.Model.API.OrderDetailsModel;
import com.agenin.report.Model.CartItemModel;
import com.agenin.report.Model.OrderListModel;
import com.agenin.report.Model.UserModel;
import com.agenin.report.Preference.UserPreference;
import com.agenin.report.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import info.hoang8f.widget.FButton;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    public static Dialog loadingDialog;
    private static String paymentID,userID;
    private Date tgl_pesan;
    private TextView idPayment;
    private TextView totalBayar;
    private TextView waktuPesan;
    private TextView alamat;
    private TextView bank;
    private TextView status;
    //tambahan
    public static TextView kirim_tv;
    public static TextView kirim;
    public static TextView ket_tv;
    public static TextView ket;
    public static EditText kirim_et;
    public static EditText ket_et;

    public static RecyclerView item_recyclerview;
    public  static CartPaymentAdapter cartAdapter;
    public static LinearLayoutManager linearLayoutManager;

    private FButton konfirm,callButton;

    public static int status_now;
    private OrderDetailsModel orderDetailsModel;
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idPayment = findViewById(R.id.id);
        totalBayar = findViewById(R.id.totalbayar);
        waktuPesan = findViewById(R.id.waktupesan);
        alamat = findViewById(R.id.alamat);
        bank = findViewById(R.id.bank);
        item_recyclerview = findViewById(R.id.item_recycleview);

        callButton = findViewById(R.id.call);
        status = findViewById(R.id.status);
        konfirm = findViewById(R.id.konfirm);


        callButton.setButtonColor(getResources().getColor(R.color.call));
        kirim_tv = findViewById(R.id.kirim);
        kirim = findViewById(R.id.metode_kirim);
        ket_tv = findViewById(R.id.ket);
        ket = findViewById(R.id.ket_kirim);

        ket_et = findViewById(R.id.ket_edit);
        kirim_et = findViewById(R.id.kirim_edit);


        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(this.getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        paymentID = getIntent().getStringExtra("paymentID").replace(" ","");
        userID = getIntent().getStringExtra("userID").replace(" ","");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout = findViewById(R.id.swipe);

        swipeRefreshLayout.setColorSchemeColors(this.getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary),getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
                swipeRefreshLayout.setRefreshing(false);

            }
        });


        reloadPage();


    }

    @Override
    protected void onStart() {
        super.onStart();
        reloadPage();
    }

    private void reloadPage (){


        loadingDialog.show();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
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
                    UserPreference userPreference = new UserPreference(DetailActivity.this);
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
                            .baseUrl(DBQueries.url)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();

                    PaymentClient paymentAPI = retrofit.create(PaymentClient.class);
                    Call<OrderDetailsAPI> call2 = paymentAPI.getMyOrderDetails(userID,paymentID);
                    call2.enqueue(new Callback<OrderDetailsAPI>() {
                        @Override
                        public void onResponse(Call<OrderDetailsAPI> call, Response<OrderDetailsAPI> response) {
                            if (!response.isSuccessful()) {
                                try {
                                    Toast.makeText(DetailActivity.this, String.valueOf(response.errorBody().string()), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                if (loadingDialog != null) {
                                    loadingDialog.dismiss();
                                }
                                return;
                            }


                            orderDetailsModel = response.body().getItems();
                            idPayment.setText( paymentID);
                            tgl_pesan = StringtoDate(orderDetailsModel.getMessage().getDate());

                            if (!orderDetailsModel.getMessage().getKet_kirim().isEmpty()){
                                //sedang-sudah dikirim
                                ket.setVisibility(View.VISIBLE);
                                ket_tv.setVisibility(View.VISIBLE);
                                kirim.setVisibility(View.VISIBLE);
                                kirim_tv.setVisibility(View.VISIBLE);
                                ket.setText(orderDetailsModel.getMessage().getKet_kirim());
                                kirim.setText(orderDetailsModel.getMessage().getMetode_kirim());
                            }else {
                                ket.setVisibility(View.GONE);
                                ket_tv.setVisibility(View.GONE);
                                kirim.setVisibility(View.GONE);
                                kirim_tv.setVisibility(View.GONE);
                            }

                            totalBayar.setText("Rp "+DBQueries.currencyFormatter(orderDetailsModel.getMessage().getTotal_amount()));

                            if (orderDetailsModel.getMessage().getConfirmed()) {
                                if (orderDetailsModel.getMessage().getPacked()) {
                                    if (orderDetailsModel.getMessage().getShipped()) {
                                        if (orderDetailsModel.getMessage().getDelivered()) {
                                            waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getDelivered_date())));
                                            status.setText("Telah Terkirim");
                                            konfirm.setVisibility(View.GONE);
                                            status_now=4;


                                        }else {
                                            waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getShipped_date())));
                                            status.setText("Sedang Dikirim");
                                            konfirm.setText("Terkirim");
                                            konfirm.setButtonColor(getResources().getColor(R.color.delivered));


                                            status_now=3;
                                        }
                                    }else {
                                        waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getPacked_date())));
                                        status.setText("Telah Dikemas");
                                        konfirm.setText("Dikirim");
                                        konfirm.setButtonColor(getResources().getColor(R.color.shipping));
                                        status_now=2;
                                    }
                                }else {
                                    if (!orderDetailsModel.getMessage().getCanceled()) {
                                        waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getConfirmed_date())));
                                        status.setText("Telah Dikonfirmasi");
                                        konfirm.setText("Dikemas");
                                        konfirm.setButtonColor(getResources().getColor(R.color.packed));
                                        status_now=1;
                                    }
                                    else {
                                        waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getCanceled_date())));
                                        status.setText("Order Dibatalkan Pembeli");
                                        konfirm.setVisibility(View.GONE);
                                        status_now = 0;
                                    }
                                }
                            }else {

                                if (!orderDetailsModel.getMessage().getCanceled()) {
                                    waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getOrdered_date())));
                                    status.setText("Belum Dikonfirmasi");
                                    konfirm.setText("Dikonfirmasi");
                                    konfirm.setButtonColor(getResources().getColor(R.color.ordered));
                                    status_now = 0;
                                }else {
                                    waktuPesan.setText(changeDate(StringtoDate(orderDetailsModel.getMessage().getCanceled_date())));
                                    status.setText("Order Dibatalkan Pembeli");
                                    konfirm.setVisibility(View.GONE);
                                    status_now = 0;
                                }
                            }

                            callButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String noWA = orderDetailsModel.getMessage().getHandphone();

                                    if (noWA.substring(0,1).equals("0")){
                                        noWA="+62"+noWA.substring(1,noWA.length());
                                    }

                                    String url = "https://api.whatsapp.com/send?phone=" + noWA;
                                    try {
                                        PackageManager pm = getPackageManager();
                                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    } catch (PackageManager.NameNotFoundException e) {
                                        Toast.makeText(DetailActivity.this, "Anda Belum menginstall WhatsApp", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    }
                                }
                            });

                            if (status_now ==2){
                                //akan-dikirim
                                ket_et.setVisibility(View.VISIBLE);
                                kirim_et.setVisibility(View.VISIBLE);
                            }else {
                                ket_et.setVisibility(View.GONE);
                                kirim_et.setVisibility(View.GONE);
                            }


                            konfirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new SweetAlertDialog(DetailActivity.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Konfirmasi")
                                            .setContentText("Anda akan mengubah status Akun ini?")
                                            .setConfirmText("Ya")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                                    if (status_now!=2||((status_now==2)&&(!kirim_et.getText().toString().equals("")))) {
                                                        if (status_now==2){
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
                                                                        UserPreference userPreference = new UserPreference(DetailActivity.this);
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
                                                                                .baseUrl(DBQueries.url)
                                                                                .client(okHttpClient)
                                                                                .addConverterFactory(GsonConverterFactory.create(gson))
                                                                                .build();

                                                                        PaymentClient paymentAPI = retrofit.create(PaymentClient.class);
                                                                        Call<ResponseBody> call2 = paymentAPI.updateShippedNota(userID,paymentID,kirim_et.getText().toString(),ket_et.getText().toString());
                                                                        call2.enqueue(new Callback<ResponseBody>() {
                                                                            @Override
                                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                if (!response.isSuccessful()) {
                                                                                    try {
                                                                                        Log.i("response", String.valueOf(response.errorBody().string()));
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    Toast.makeText(DetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                                                                    if (loadingDialog != null) {
                                                                                        loadingDialog.dismiss();
                                                                                    }
                                                                                    return;
                                                                                }

                                                                                if (loadingDialog != null) {
                                                                                    loadingDialog.dismiss();
                                                                                }
                                                                                sweetAlertDialog.dismiss();
                                                                                reloadPage();


                                                                            }
                                                                            @Override
                                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                sweetAlertDialog.dismiss();
                                                                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                                                if (loadingDialog!=null) {
                                                                                    loadingDialog.dismiss();
                                                                                }
                                                                            }
                                                                        });


                                                                    } else {

                                                                        UserPreference userPreference = new UserPreference(DetailActivity.this);
                                                                        userPreference.setUserPreference("user", null);
                                                                        sweetAlertDialog.dismiss();
                                                                        if (loadingDialog!=null) {
                                                                            loadingDialog.dismiss();
                                                                        }


                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<UserModel> call, Throwable t) {
                                                                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                                    sweetAlertDialog.dismiss();
                                                                    loadingDialog.dismiss();

                                                                }
                                                            });
                                                        }else {
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
                                                                        UserPreference userPreference = new UserPreference(DetailActivity.this);
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
                                                                                .baseUrl(DBQueries.url)
                                                                                .client(okHttpClient)
                                                                                .addConverterFactory(GsonConverterFactory.create(gson))
                                                                                .build();

                                                                        PaymentClient paymentAPI = retrofit.create(PaymentClient.class);
                                                                        Call<ResponseBody> call2 = paymentAPI.updateStatusNota(userID,paymentID);
                                                                        call2.enqueue(new Callback<ResponseBody>() {
                                                                            @Override
                                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                if (!response.isSuccessful()) {
                                                                                    try {
                                                                                        Log.i("response", String.valueOf(response.errorBody().string()));
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    Toast.makeText(DetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                                                                                    if (loadingDialog != null) {
                                                                                        loadingDialog.dismiss();
                                                                                    }
                                                                                    return;
                                                                                }
                                                                                sweetAlertDialog.dismiss();
                                                                                if (loadingDialog != null) {
                                                                                    loadingDialog.dismiss();
                                                                                }
                                                                               reloadPage();


                                                                            }
                                                                            @Override
                                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                sweetAlertDialog.dismiss();
                                                                                Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                                                if (loadingDialog!=null) {
                                                                                    loadingDialog.dismiss();
                                                                                }
                                                                            }
                                                                        });


                                                                    } else {

                                                                        UserPreference userPreference = new UserPreference(DetailActivity.this);
                                                                        userPreference.setUserPreference("user", null);
                                                                        sweetAlertDialog.dismiss();
                                                                        if (loadingDialog!=null) {
                                                                            loadingDialog.dismiss();
                                                                        }


                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<UserModel> call, Throwable t) {
                                                                    Log.e("debug", "onFailure: ERROR > " + t.toString());
                                                                    sweetAlertDialog.dismiss();
                                                                    sweetAlertDialog.dismiss();
                                                                    loadingDialog.dismiss();

                                                                }
                                                            });


                                                        }
                                                    }else {
                                                        sweetAlertDialog.dismiss();
                                                        Toast.makeText(DetailActivity.this, "Isikan metode kirim", Toast.LENGTH_SHORT).show();
                                                    }


                                                }

                                            }).show();




                                }

                            });


                            alamat.setText(orderDetailsModel.getMessage().getDetail_address());
                            bank.setText("Bank " + orderDetailsModel.getMessage().getBank());
                            int totalItem=0;
                            cartItemModelList = new ArrayList<>();
                            for (int i=0;i<orderDetailsModel.getNota().size();i++)
                            {
                                totalItem = totalItem+orderDetailsModel.getNota().get(i).getJumlah();
                                Double berat = Double.parseDouble(String.valueOf(orderDetailsModel.getNota().get(i).getProduct_ID().getBerat()));
                                int ongkir =  ((int)(8000*berat)*(int) orderDetailsModel.getNota().get(i).getJumlah());
                                cartItemModelList.add(new CartItemModel(
                                        CartItemModel.CART_ITEM,
                                        orderDetailsModel.getNota().get(i).getProduct_ID().get_id(),
                                        DBQueries.url +  orderDetailsModel.getNota().get(i).getProduct_ID().getImage().get(0)
                                        ,  orderDetailsModel.getNota().get(i).getProduct_ID().getTitle_product()
                                        ,  orderDetailsModel.getNota().get(i).getProduct_ID().getPrice()
                                        , (int)  orderDetailsModel.getNota().get(i).getJumlah()
                                        , true
                                        , orderDetailsModel.getNota().get(i).getProduct_ID().getAverage_rating()
                                        , orderDetailsModel.getNota().get(i).getProduct_ID().getSatuan()
                                        , berat
                                        , (int) orderDetailsModel.getNota().get(i).getJumlah()
                                        ,String.valueOf(ongkir)
                                        ,orderDetailsModel.getNota().get(i).getProduct_ID().getNo_pedagang()
                                ));
                            }
                            cartItemModelList.add(new CartItemModel(CartItemModel.TOTAL_AMOUNT));



                            cartAdapter = new CartPaymentAdapter(cartItemModelList,DetailActivity.this);
                            DetailActivity.item_recyclerview.setAdapter(DetailActivity.cartAdapter);

                            DetailActivity.linearLayoutManager = new LinearLayoutManager(DetailActivity.this);
                            DetailActivity.linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            DetailActivity.item_recyclerview.setLayoutManager(DetailActivity.linearLayoutManager);

                            DetailActivity.cartAdapter.notifyDataSetChanged();





                            if (loadingDialog != null) {
                                loadingDialog.dismiss();
                            }
                        }
                        @Override
                        public void onFailure(Call<OrderDetailsAPI> call, Throwable t) {
                            Log.e("debug", "onFailure: ERROR > " + t.toString());
                            if (loadingDialog!=null) {
                                loadingDialog.dismiss();
                            }
                        }
                    });


                } else {

                    UserPreference userPreference = new UserPreference(DetailActivity.this);
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

    public static Date StringtoDate(String dateString){

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" , Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC +7"));
        Date date = null;
        try {
            date = format.parse(dateString);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==android.R.id.home){

            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static String changeDate(Date date){

        final String NEW_FORMAT = "dd/MMMM/yyyy HH:mm:ss";
        String newDateString;
        SimpleDateFormat sdf = new SimpleDateFormat(NEW_FORMAT);
        newDateString = sdf.format(date);
        return newDateString;
    }
}