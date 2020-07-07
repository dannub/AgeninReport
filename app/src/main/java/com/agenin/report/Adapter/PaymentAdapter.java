package com.agenin.report.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agenin.report.Activity.DetailActivity;
import com.agenin.report.DBQueries;
import com.agenin.report.Fragment.PesananFragment;
import com.agenin.report.Fragment.UserFragment;
import com.agenin.report.Interface.PaymentClient;
import com.agenin.report.Interface.UserClient;
import com.agenin.report.Model.OrderListModel;
import com.agenin.report.Model.TanggalModel;
import com.agenin.report.Model.UserAPIModel;
import com.agenin.report.Model.UserModel;
import com.agenin.report.Preference.UserPreference;
import com.agenin.report.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.Viewholder>  {


    private List<OrderListModel> uploadBuktiModelList;
    private Context context;
    private Dialog loadingDialog;

    public PaymentAdapter(List<OrderListModel> uploadBuktiModelList, Context context,Dialog loadingDialog) {
        this.uploadBuktiModelList = uploadBuktiModelList;
        this.loadingDialog = loadingDialog;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payment_item_layout,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentAdapter.Viewholder viewholder, int position) {



        String id = uploadBuktiModelList.get(position).get_id();
        String username = uploadBuktiModelList.get(position).getUsername();
        String id_user = uploadBuktiModelList.get(position).getUser_id();
        String name = uploadBuktiModelList.get(position).getAtas_nama();
        String imageUrl = uploadBuktiModelList.get(position).getBukti();
        int total = uploadBuktiModelList.get(position).getTotal_amount();
        String tglTransfer = uploadBuktiModelList.get(position).getTgl_transfer();
        String tglPesan = uploadBuktiModelList.get(position).getDate();
        String bank = uploadBuktiModelList.get(position).getBank();
        boolean ordered = uploadBuktiModelList.get(position).getConfirmed();
        boolean packed= uploadBuktiModelList.get(position).getPacked();
        boolean shipped= uploadBuktiModelList.get(position).getShipped();
        boolean delivered= uploadBuktiModelList.get(position).getDelivered();
        boolean canceled= uploadBuktiModelList.get(position).getCanceled();

        String ordered_date= uploadBuktiModelList.get(position).getConfirmed_date();
        String packed_date= uploadBuktiModelList.get(position).getPacked_date();
        String shipped_date= uploadBuktiModelList.get(position).getShipped_date();
        String delivered_date= uploadBuktiModelList.get(position).getDelivered_date();
        String canceled_date= uploadBuktiModelList.get(position).getCanceled_date();

        viewholder.setData(id,username,id_user,name,bank,tglTransfer,tglPesan,total,imageUrl, ordered,packed,shipped,delivered,canceled ,ordered_date,packed_date,shipped_date,delivered_date,canceled_date);
    }

    @Override
    public int getItemCount() {
        return uploadBuktiModelList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView total;
        private TextView tglTransfer;
        private TextView tglPesan;
        private TextView bank;
        private ImageView imageUrl;
        private Dialog zoom;
        private LinearLayout detail;
        private LinearLayout hapus;
        private TextView status;
        private TextView username;
        private TextView id;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.atas_nama);
            total = itemView.findViewById(R.id.total);
            tglTransfer = itemView.findViewById(R.id.tgl_tf);
            tglPesan = itemView.findViewById(R.id.tgl_upload);
            bank = itemView.findViewById(R.id.bank);
            imageUrl= itemView.findViewById(R.id.image_payment);
            detail = itemView.findViewById(R.id.detail);
            status = itemView.findViewById(R.id.status);
            username = itemView.findViewById(R.id.user_name);
            hapus = itemView.findViewById(R.id.remove_item_btn);
        }

        private void setData(final String  idText,String username,final String  id_user, String name, String bank, String tglTransfer, String tglPesan, int total, final String imageUrl, boolean ordered,boolean packed, boolean shipped, boolean delivered, boolean canceled, String ordered_date, String packed_date, String shipped_date,String delivered_date, String canceled_date){
            String atasnama = name;

            id.setText(idText);
            if(atasnama.length()>12)
            {
                atasnama=atasnama.substring(0,11)+"..";
            }

            String user = username;
            if(user.length()>12)
            {
                user=user.substring(0,11)+"..";
            }
            this.name.setText("a.n : "+atasnama);
            this.bank.setText("Bank : "+bank);
            this.username.setText("Username : "+user);

            this.tglTransfer.setText("Tgl-TF : "+tglTransfer);
            if (ordered) {
                if (packed) {
                    if (shipped) {
                        if (delivered) {
                            this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.delivered));
                            this.status.setTextColor(itemView.getResources().getColor(R.color.delivered));
                            this.status.setText("- Terkirim");
                            this.tglPesan.setText(getStringDate(delivered_date));
                        }else {
                            this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.shipping));
                            this.status.setTextColor(itemView.getResources().getColor(R.color.shipping));
                            this.status.setText("- Sedang Dikirim");
                            this.tglPesan.setText(getStringDate(shipped_date));
                        }
                    }else {
                        this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.packed));
                        this.status.setTextColor(itemView.getResources().getColor(R.color.packed));
                        this.status.setText("- Telah Dikemas");
                        this.tglPesan.setText(getStringDate(packed_date));
                    }
                }else {
                    if (!canceled) {
                        this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.ordered));
                        this.status.setTextColor(itemView.getResources().getColor(R.color.ordered));
                        this.status.setText("- Telah Dikonfirmasi");
                        this.tglPesan.setText(getStringDate(ordered_date));
                    }else {
                        this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                        this.status.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                        this.status.setText("- Canceled");
                        this.tglPesan.setText(getStringDate(canceled_date));
                    }
                }
            }else {
                if (!canceled) {
                    this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                    this.status.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                    this.status.setText("- Belum Dikonfirmasi");
                    this.tglPesan.setText(getStringDate(tglPesan));
                }else {
                    this.tglPesan.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                    this.status.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                    this.status.setText("- Canceled");
                    this.tglPesan.setText(getStringDate(canceled_date));
                }
            }

            this.total.setText("Total : Rp "+DBQueries.currencyFormatter(String.valueOf(total)));
            zoom = new Dialog(context);
            zoom.setContentView(R.layout.dialog_custom_layout);
            zoom.setCancelable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                zoom.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.slider_background));
            }
            zoom.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            ViewGroup.LayoutParams params = zoom.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            zoom.getWindow().setAttributes((WindowManager.LayoutParams) params);

            Glide.with(itemView.getContext()).load(DBQueries.url+imageUrl).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(this.imageUrl);
            this.imageUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoView photoView = zoom.findViewById(R.id.photo_view);
                    Glide.with(itemView.getContext()).load(DBQueries.url+imageUrl).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(photoView);
                    zoom.show();
                }
            });
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(), DetailActivity.class);
                    productDetailsIntent.putExtra("paymentID",idText);
                    productDetailsIntent.putExtra("userID",id_user);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });

            hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Hapus Data Pesanan!")
                        .setContentText("Apakah benar data pesanan ini akan dihapus?")
                        .setConfirmText("Ya, Hapus!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {

                                sDialog.dismiss();
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
                                            UserPreference userPreference = new UserPreference(itemView.getContext());
                                            userPreference.setUserPreference("user",user);
                                            // UserModel user = userPreference.getUserPreference("user");



                                            final OkHttpClient okHttpClient2 = new OkHttpClient.Builder()
                                                    .addInterceptor(new Interceptor() {
                                                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                                        @Override
                                                        public okhttp3.Response intercept(Chain chain) throws IOException {
                                                            Request newRequest  = chain.request().newBuilder()
                                                                    .addHeader("Authorization", "Bearer " + user.getToken())
                                                                    .build();
                                                            return chain.proceed(newRequest);
                                                        }
                                                    })
                                                    .readTimeout(3, TimeUnit.MINUTES)
                                                    .connectTimeout(3, TimeUnit.MINUTES)
                                                    .build();
                                            Retrofit retrofit2 = new Retrofit.Builder()
                                                    .baseUrl(DBQueries.url)
                                                    .client(okHttpClient2)
                                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                                    .build();


                                            PaymentClient paymentAPI = retrofit2.create(PaymentClient.class);
                                            Call<List<OrderListModel>> call2 = paymentAPI.deleteNota(id_user,idText,UserFragment.tgl_from.getText().toString(),UserFragment.tgl_to.getText().toString());


                                            call2.enqueue(new Callback<List<OrderListModel>>() {
                                                @Override
                                                public void onResponse(Call<List<OrderListModel>> call, Response<List<OrderListModel>> response) {

                                                    if (response.isSuccessful()) {
                                                        if (response.body().size()>0){

                                                            DBQueries.uploadBuktiModelList = response.body();
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
                                                    }else {
                                                        try {
                                                            Toast.makeText(itemView.getContext(), response.errorBody().string().toString(), Toast.LENGTH_SHORT).show();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        loadingDialog.dismiss();

                                                        return;

                                                    }


                                                    loadingDialog.dismiss();



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
                                            UserPreference userPreference = new UserPreference(itemView.getContext());
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
                        })
                        .show();



                }
            });



        }





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

    private String getStringDate (String dateString){
        Date date = StringtoDate(dateString);
        String format = "EEEE, dd MMMM yyyy HH:mm:ss";
        final SimpleDateFormat sdf = new SimpleDateFormat(format);
        String hasil = sdf.format(date);
        return hasil;
    }
}
