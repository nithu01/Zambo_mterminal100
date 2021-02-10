package com.zambo.zambo_mterminal100.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zambo.zambo_mterminal100.Adapter.ComplaintAdapter;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.Complaint;
import com.zambo.zambo_mterminal100.model.ComplaintExample;
import com.zambo.zambo_mterminal100.model.UserData;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ComplaintStatus extends AppCompatActivity {
    LinearLayout Slayout,Elayout;
    TextView start,end;
    ImageView startimg,endimg;
    Button proceed;
    int mYear,mMonth,mDay;
    UserData userData;
    RelativeLayout relativeLayout;
    ProgressDialog progressDialog;
    ArrayList<Complaint> arrayList;
    RecyclerView recyclerView;
    ComplaintAdapter adapter;
    private ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_status);
        userData = PrefManager.getInstance(ComplaintStatus.this).getUserData();
        progressDialog = new ProgressDialog(this);
        Slayout = findViewById(R.id.startLayout);
        Elayout = findViewById(R.id.endLayout);
        recyclerView = findViewById(R.id.recyclerview_complaint);
        relativeLayout = findViewById(R.id.rl_wl_trans_recharge);
        relativeLayout.setVisibility(View.VISIBLE);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        startimg = findViewById(R.id.img_start);
        endimg = findViewById(R.id.img_end);
        proceed = findViewById(R.id.proceed);
        imgBack = findViewById(R.id.img_back_history);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComplaintStatus.this, MainActivity.class);
                startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
            }
        });
        Slayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ComplaintStatus.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                rlNoData.setVisibility(View.GONE);
//                                imgData.setVisibility(View.GONE);
//                                txtData.setVisibility(View.GONE);
//                                getCustomerHistory(userData.getuType(), userData.getUserId(),date.getText().toString());
                                //Toast.makeText(getContext(), ""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Elayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(ComplaintStatus.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                end.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                rlNoData.setVisibility(View.GONE);
//                                imgData.setVisibility(View.GONE);
//                                txtData.setVisibility(View.GONE);
//                                getCustomerHistory(userData.getuType(), userData.getUserId(),date.getText().toString());
                                //Toast.makeText(getContext(), ""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getComplaint(userData.getUserId(),start.getText().toString(),end.getText().toString());
            }
        });
    }
    public void getComplaint(String userid,String startdate, String enddate) {
        progressDialog.show();
//        Toast.makeText(this, userid+"\n"+service+"\n"+txnId+"\n"+amount+"\n"+date+"\n"+remarks, Toast.LENGTH_SHORT).show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<ComplaintExample> call=apiinterface.getComplaint(userid,startdate,enddate);
        call.enqueue(new Callback<ComplaintExample>() {
            @Override
            public void onResponse(Call<ComplaintExample> call, retrofit2.Response<ComplaintExample> response) {
                if (response.body().getStatus().equals(0)) {
                    progressDialog.dismiss();
                    relativeLayout.setVisibility(View.GONE);
                    arrayList = new ArrayList<>();
                    for(int i = 0; i < response.body().getData().size(); i++){
                        Complaint statement= new Complaint();
                        statement.setService(response.body().getData().get(i).getService());
                        statement.setAmount(response.body().getData().get(i).getAmount());
                        statement.setComplainId(response.body().getData().get(i).getComplainId());
                        statement.setComplainDate(response.body().getData().get(i).getComplainDate());
                        statement.setStatus(response.body().getData().get(i).getStatus());
                        statement.setRtRemark(response.body().getData().get(i).getRtRemark());
                        statement.setStaffRemark(response.body().getData().get(i).getStaffRemark());
                        statement.setTxnDate(response.body().getData().get(i).getTxnDate());
                        arrayList.add(statement);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ComplaintStatus.this,LinearLayoutManager.VERTICAL,false);
                    adapter=new ComplaintAdapter(arrayList,ComplaintStatus.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                }else {
                    progressDialog.dismiss();
                    relativeLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(ComplaintStatus.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ComplaintExample> call, Throwable t) {
                Toast.makeText(ComplaintStatus.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ComplaintStatus.this, MainActivity.class);
        startActivity(intent);
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }
}