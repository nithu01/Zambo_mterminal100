package com.zambo.zambo_mterminal100.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.zambo.zambo_mterminal100.Adapter.FundAdapter;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.BankMode;
import com.zambo.zambo_mterminal100.model.Fund;
import com.zambo.zambo_mterminal100.model.FundExample;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.UserData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fund_Request extends AppCompatActivity {
    Spinner mode,bank_account;
    EditText txnid,amt,remark;
    Button sumbit;
    ProgressDialog progressDialog;
    UserData userData;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayList<String> bankNames = new ArrayList<String>();
    ArrayList<String> accountNumber = new ArrayList<String>();
    String acc_no;
    ImageView imageView;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    ArrayList<Fund> arrayList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FundAdapter adapter;
    String S,E;
    int mYear,mMonth,mmMonth,mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund__request);
        userData= PrefManager.getInstance(Fund_Request.this).getUserData();
        progressDialog = new ProgressDialog(this);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        recyclerView = findViewById(R.id.recyclerview_Fund);
        relativeLayout = findViewById(R.id.rl_wl_trans_recharge);
        relativeLayout.setVisibility(View.VISIBLE);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mmMonth = c.get(Calendar.MONTH)+1;
        mDay = c.get(Calendar.DAY_OF_MONTH);
        S = mDay+ "-"+ mMonth+"-" +mYear;
        E = mDay+ "-"+ mmMonth+"-" +mYear;
        getComplaint(userData.getUserId(),S,E);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
                getComplaint(userData.getUserId(),S,E);
            }
        });
        mode = findViewById(R.id.mode);
        bank_account = findViewById(R.id.fund_bank);
        txnid = findViewById(R.id.transaction_id);
        amt = findViewById(R.id.amount);
        remark = findViewById(R.id.Remarks);
        sumbit =findViewById(R.id.submit);
        fetchJSON();
        bankNames.add("Select Bank");
        accountNumber.add("Select Bank");
        imageView = findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fund_Request.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bank_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String bankname = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < bankNames.size(); i++) {
                    if (bankname.equals(bankNames.get(i))) {
                         acc_no = accountNumber.get(i);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        List<String> devices = new ArrayList<String>();
        devices.add("Select Mode of Payment");
        devices.add("IMPS");
        devices.add("NEFT");
        devices.add("RTGS");
        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, devices);
        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mode.setAdapter(typeadapter);


        sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.getSelectedItem().toString().equals("Select Mode of Payment")){
                    Toast.makeText(Fund_Request.this, "Select Mode of Payment", Toast.LENGTH_SHORT).show();
                    mode.requestFocus();
                }else if (bank_account.getSelectedItem().toString().equals("Select Bank")){
                    Toast.makeText(Fund_Request.this, "Select Bank", Toast.LENGTH_SHORT).show();
                    bank_account.requestFocus();
                }else  if (txnid.getText().toString().isEmpty()){
                    txnid.setError("Enter Transaction Id");
                    txnid.requestFocus();
                }else  if (amt.getText().toString().isEmpty()){
                    amt.setError("Enter Amount");
                    amt.requestFocus();
                }else  if (remark.getText().toString().isEmpty()){
                    remark.setError("Enter Remark");
                    remark.requestFocus();
                }else {
                     getFundRequest(userData.getUserId(),acc_no,mode.getSelectedItem().toString(),amt.getText().toString(),remark.getText().toString(),txnid.getText().toString());
                }
            }
        });

    }

    public void getFundRequest(String userid, String account, String mode, String amount, String remarks, String txnId) {
        progressDialog.show();
//        Toast.makeText(this, userid+"\n"+account+"\n"+mode+"\n"+amount+"\n"+remarks+"\n"+txnId, Toast.LENGTH_SHORT).show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getfundRequest(userid,account,mode,amount,remarks,txnId);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                if (response.body().getStatus().equals(0)) {
                    progressDialog.dismiss();
                    Configuration.openPopupUpDownBack(Fund_Request.this,R.style.Dialod_UpDown,"main","success",
                            response.body().getMessage());
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Fund_Request.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(Fund_Request.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fetchJSON(){

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<List<BankMode>> call=apiinterface.getmode();
        call.enqueue(new Callback<List<BankMode>>() {
            @Override
            public void onResponse(Call<List<BankMode>> call, retrofit2.Response<List<BankMode>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        for(int i=0;i<response.body().size();i++) {
                            bankNames.add(response.body().get(i).getBankName());
                            accountNumber.add(response.body().get(i).getAccountNo());
                        }
                        spinnerArrayAdapter = new ArrayAdapter<String>(Fund_Request.this, android.R.layout.simple_spinner_item, bankNames);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        bank_account.setAdapter(spinnerArrayAdapter);
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BankMode>> call, Throwable t) {
                Toast.makeText(Fund_Request.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getComplaint(String userid,String startdate, String enddate) {
        progressDialog.show();
//        Toast.makeText(this, userid+"\n"+service+"\n"+txnId+"\n"+amount+"\n"+date+"\n"+remarks, Toast.LENGTH_SHORT).show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<FundExample> call=apiinterface.getFund(userid,startdate,enddate);
        call.enqueue(new Callback<FundExample>() {
            @Override
            public void onResponse(Call<FundExample> call, retrofit2.Response<FundExample> response) {
                if (response.body().getStatus().equals(0)) {
                    progressDialog.dismiss();
                    relativeLayout.setVisibility(View.GONE);
                    arrayList = new ArrayList<>();
                    for(int i = 0; i < response.body().getData().size(); i++){
                        Fund statement= new Fund();
                        statement.setMode(response.body().getData().get(i).getMode());
                        statement.setBName(response.body().getData().get(i).getBName());
                        statement.setAmount(response.body().getData().get(i).getAmount());
                        statement.setTxnId(response.body().getData().get(i).getTxnId());
                        statement.setStatus(response.body().getData().get(i).getStatus());
                        statement.setTxnDate(response.body().getData().get(i).getTxnDate());
                        arrayList.add(statement);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Fund_Request.this,LinearLayoutManager.VERTICAL,false);
                    adapter=new FundAdapter(arrayList,Fund_Request.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                }else {
                    progressDialog.dismiss();
                    relativeLayout.setVisibility(View.VISIBLE);
                    Toast.makeText(Fund_Request.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FundExample> call, Throwable t) {
                Toast.makeText(Fund_Request.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Fund_Request.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
