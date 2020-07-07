package com.agenin.report.Adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
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

import com.agenin.report.DBQueries;
import com.agenin.report.Fragment.UserFragment;
import com.agenin.report.Interface.UserClient;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Dialog loadingDialog;
    private List<UserAPIModel> userModelList;
    public UserAdapter(List<UserAPIModel> userModelList,Dialog loadingDialog) {
        this.userModelList = userModelList;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {

        String id= userModelList.get(position).get_id();
        String username = userModelList.get(position).getName();
        String name_refferal = userModelList.get(position).getName_refferal();
        String bukti = userModelList.get(position).getBukti();
        String bukti_an = userModelList.get(position).getBukti_an();
        String bukti_tgl = userModelList.get(position).getBukti_tgl();
        String bukti_bank = userModelList.get(position).getBukti_bank();
        String email = userModelList.get(position).getEmail();
        String handphone = userModelList.get(position).getHandphone();
        String date = userModelList.get(position).getDate();
        Boolean status = userModelList.get(position).getStatus();



         viewholder.setData(id,username,name_refferal,bukti,bukti_an,bukti_tgl,bukti_bank,email,handphone,date,status);

    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView username;
        private TextView email;
        private TextView namerefferal;
        private TextView an;
        private TextView bank;
        private TextView tglTF;
        private TextView tgl;
        private TextView status;
        private ImageView dot;
        private ImageView imageUrl;
        private LinearLayout ubahStatusLayout;
        private LinearLayout whatsappLayout;
        private Dialog zoom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            an = itemView.findViewById(R.id.atas_nama);
            tglTF = itemView.findViewById(R.id.tgl_tf);
            tgl = itemView.findViewById(R.id.tgl_dibuat);
            namerefferal = itemView.findViewById(R.id.nama_reffeal);
            email = itemView.findViewById(R.id.email);
            bank = itemView.findViewById(R.id.bank);
            imageUrl = itemView.findViewById(R.id.image_payment);
            ubahStatusLayout = itemView.findViewById(R.id.ubah_status_layout);
            whatsappLayout = itemView.findViewById(R.id.whatsapplayout);
            status = itemView.findViewById(R.id.status);
            username = itemView.findViewById(R.id.user_name);
            dot = itemView.findViewById(R.id.dot);
        }

        private void setData(String idText, String usernameText, String name_refferalText, String buktiText, String bukti_anText, String bukti_tglText, String bukti_bankText, String emailText, String handphoneText, String dateText, Boolean statusInput) {
            id.setText(idText);
            if(bukti_anText.length()>12)
            {
                bukti_anText=bukti_anText.substring(0,11)+"..";
            }


            if(usernameText.length()>12)
            {
                usernameText=usernameText.substring(0,11)+"..";
            }
            username.setText("Username : "+usernameText);
            email.setText("Email : "+emailText);
            namerefferal.setText("Nama Refferal : "+name_refferalText);
            tgl.setText("Tgl : "+getStringDate(dateText));
            an.setText("a.n : "+bukti_anText);
            bank.setText("Bank : "+bukti_bankText);
            tglTF.setText("Tgl-TF : "+bukti_tglText);
            if (statusInput){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dot.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorSuccess)));
                }
                status.setText("Sudah Dikonfirmasi");
                status.setTextColor(itemView.getResources().getColor(R.color.colorSuccess));
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    dot.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorPrimary)));
                }
                status.setText("Belum Dikonfirmasi");
                status.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
            }

            zoom = new Dialog(itemView.getContext());
            zoom.setContentView(R.layout.dialog_custom_layout);
            zoom.setCancelable(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                zoom.getWindow().setBackgroundDrawable(itemView.getContext().getDrawable(R.drawable.slider_background));
            }
            zoom.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            ViewGroup.LayoutParams params = zoom.getWindow().getAttributes();
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            zoom.getWindow().setAttributes((WindowManager.LayoutParams) params);

            Glide.with(itemView.getContext()).load(DBQueries.url+buktiText).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(this.imageUrl);
            this.imageUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhotoView photoView = zoom.findViewById(R.id.photo_view);
                    Glide.with(itemView.getContext()).load(DBQueries.url+buktiText).apply(new RequestOptions().placeholder(R.drawable.load_icon)).into(photoView);
                    zoom.show();
                }
            });
            whatsappLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String noWA = handphoneText;

                    if (noWA.substring(0,1).equals("0")){
                        noWA="+62"+noWA.substring(1,noWA.length());
                    }
//                    String contact = "+62"; // use country code with your phone number
                    String url = "https://api.whatsapp.com/send?phone=" + noWA;
                    try {
                        PackageManager pm = itemView.getContext().getPackageManager();
                        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        itemView.getContext().startActivity(i);
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(itemView.getContext(), "Anda Belum menginstall WhatsApp", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            });

            ubahStatusLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(itemView.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Konfirmasi")
                            .setContentText("Anda akan mengubah status Akun ini?")
                            .setConfirmText("Ya")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(final SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
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

                                                UserClient userAPI = retrofit2.create(UserClient.class);
                                                Call<List<UserAPIModel>> call2 = userAPI.updateStatus(idText,UserFragment.tgl_from.getText().toString(),UserFragment.tgl_to.getText().toString());


                                                call2.enqueue(new Callback<List<UserAPIModel>>() {
                                                    @Override
                                                    public void onResponse(Call<List<UserAPIModel>> call, Response<List<UserAPIModel>> response) {

                                                        if (response.isSuccessful()) {
                                                            if (statusInput){
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                    dot.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorPrimary)));
                                                                }
                                                                status.setText("Belum Dikonfirmasi");
                                                                status.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));

                                                            }else {
                                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                                    dot.setImageTintList(ColorStateList.valueOf(itemView.getResources().getColor(R.color.colorSuccess)));
                                                                }
                                                                status.setText("Sudah Dikonfirmasi");
                                                                status.setTextColor(itemView.getResources().getColor(R.color.colorSuccess));
                                                            }
                                                            if (response.body().size()>0){

                                                                DBQueries.userAPIModelArrayList.clear();
                                                                UserFragment.recyclerView.setVisibility(View.VISIBLE);
                                                                UserFragment.kosong.setVisibility(View.INVISIBLE);
                                                                DBQueries.userAPIModelArrayList = response.body();
                                                                UserFragment.userAdapter = new UserAdapter(DBQueries.userAPIModelArrayList,loadingDialog);
                                                                UserFragment.recyclerView.setAdapter( UserFragment.userAdapter);

                                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
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
                                                    public void onFailure(Call<List<UserAPIModel>> call, Throwable t) {
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

                            }).show();



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
