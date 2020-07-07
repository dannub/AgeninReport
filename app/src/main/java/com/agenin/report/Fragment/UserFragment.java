package com.agenin.report.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.agenin.report.Activity.MainActivity;
import com.agenin.report.Adapter.UserAdapter;
import com.agenin.report.DBQueries;
import com.agenin.report.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class UserFragment extends Fragment {

    public static TextInputEditText tgl_from;
    public static TextInputEditText tgl_to;
    private long time;
    public  static RecyclerView recyclerView;
    public  static UserAdapter userAdapter;
    private DatePickerDialog picker;
    public static Dialog loadingDialog;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private NetworkInfo networkInfo;
    private ConnectivityManager connectivityManager;
    private ImageView noInternetConnection;
    private Button retryBtn;
    public static ConstraintLayout kosong;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =inflater.inflate(R.layout.fragment_user, container, false);
        tgl_from = itemView.findViewById(R.id.tgl_now_from);
        tgl_to = itemView.findViewById(R.id.tgl_now_to);
        recyclerView = itemView.findViewById(R.id.user_recycleview);
        swipeRefreshLayout = itemView.findViewById(R.id.refresh_layout);
        kosong = itemView.findViewById(R.id.kosong);
        noInternetConnection = itemView.findViewById(R.id.no_internet_connection);
        retryBtn = itemView.findViewById(R.id.retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimary));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadPage();
            }
        });

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tgl_to.getText().toString().isEmpty()){
                    reloadPage();
                }else {
                    Toast.makeText(getContext(), "Isi Tanggal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        }
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        tgl_to.setEnabled(false);
        tgl_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tgl_from.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);


                                String someDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = null;
                                try {
                                    date = sdf.parse(someDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                time = date.getTime();
                                tgl_to.setEnabled(true);
                                tgl_to.setText("");

                            }
                        }, year, month, day);

                picker.show();
            }
        });
        tgl_from.setFocusable(false);
        tgl_from.setFocusableInTouchMode(false);
        tgl_to.setFocusable(true);
        tgl_to.setFocusableInTouchMode(false);


        tgl_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                tgl_to.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                reloadPage();
//                                DBQueries.loadUsers(getContext(),loadingDialog, tgl_from.getText().toString(),tgl_to.getText().toString());

                            }
                        }, year, month, day);
                picker.getDatePicker().setMinDate(time);
                picker.show();

            }
        });

        return itemView;
    }


        @Override
    public void onStart() {
        super.onStart();


        if (!tgl_to.getText().toString().isEmpty()){
            reloadPage();
        }


//        if (tgl.getText().toString().isEmpty()) {
//            DBqueries.loadPayment(this, loadingDialog, "");
//
//
//        } else {
//            DBqueries.loadPayment(this, loadingDialog, tgl.getText().toString());
//        }


        userAdapter = new UserAdapter(DBQueries.userAPIModelArrayList,loadingDialog);
        recyclerView.setAdapter(userAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        userAdapter.notifyDataSetChanged();
    }

    private void reloadPage() {
        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);
            retryBtn.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(false);
            DBQueries.loadUsers(getContext(),loadingDialog, tgl_from.getText().toString(),tgl_to.getText().toString());


            recyclerView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(getContext(), "Tidak Ada Jaringan Internet", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.INVISIBLE);
            Glide.with(getContext()).load(R.drawable.no_internet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            retryBtn.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}