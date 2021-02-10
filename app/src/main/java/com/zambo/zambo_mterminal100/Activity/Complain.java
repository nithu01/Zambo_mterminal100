package com.zambo.zambo_mterminal100.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.Service;
import com.zambo.zambo_mterminal100.model.UserData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Complain extends AppCompatActivity {
    Spinner services;
    EditText transaction_Id,amountt,transaction_date,remarkss;
    Button button;
    private ArrayList<String> playerNames = new ArrayList<String>();
    ArrayAdapter<String> spinnerArrayAdapter;
    UserData userData;
    int mYear,mMonth,mDay;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain);
        userData= PrefManager.getInstance(Complain.this).getUserData();
        progressDialog = new ProgressDialog(this);
        services =findViewById(R.id.service);
        playerNames.add("Select Service");
        fetchJSON();
//        "₹"
        transaction_Id = findViewById(R.id.transaction_Id);
        amountt = findViewById(R.id.amount);
        transaction_date =findViewById(R.id.transaction_date);
        transaction_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                hideKeyboard(transaction_date);
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Complain.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                transaction_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
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
        remarkss = findViewById(R.id.remarks);
        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (services.getSelectedItem().toString().equals("Select Service")){
                    Toast.makeText(Complain.this, "Select Service", Toast.LENGTH_SHORT).show();
                    services.requestFocus();
                }else  if (amountt.getText().toString().isEmpty()){
                    amountt.setError("Enter Amount");
                    amountt.requestFocus();
                }else  if (transaction_date.getText().toString().isEmpty()){
                    transaction_date.setError("Enter Transaction Date");
                    transaction_date.requestFocus();
                }else  if (remarkss.getText().toString().isEmpty()){
                    remarkss.setError("Enter Remarks");
                    remarkss.requestFocus();
                }else {
                getComplain(userData.getUserId(),services.getSelectedItem().toString(),transaction_Id.getText().toString(),amountt.getText().toString(),transaction_date.getText().toString(),remarkss.getText().toString());
                }
            }
        });

    }

    private void fetchJSON(){

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<List<Service>> call=apiinterface.getServices();
        call.enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        for(int i=0;i<response.body().size();i++) {
                            playerNames.add(response.body().get(i).getName());
                        }
                        spinnerArrayAdapter = new ArrayAdapter<String>(Complain.this, android.R.layout.simple_spinner_item, playerNames);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        services.setAdapter(spinnerArrayAdapter);
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(Complain.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getComplain(String userid, String service,String txnId, String amount,String date, String remarks) {
        progressDialog.show();
//        Toast.makeText(this, userid+"\n"+service+"\n"+txnId+"\n"+amount+"\n"+date+"\n"+remarks, Toast.LENGTH_SHORT).show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.getcomplain(userid,service,txnId,amount,date,remarks);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
                if (response.body().getStatus().equals(0)) {
                    progressDialog.dismiss();
                    Configuration.openPopupUpDownBack(Complain.this,R.style.Dialod_UpDown,"main","success",
                            response.body().getMessage());
                    spinnerArrayAdapter.notifyDataSetChanged();
                    transaction_Id.getText().clear();
                    amountt.getText().clear();
                    transaction_date.getText().clear();
                    remarkss.getText().clear();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(Complain.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(Complain.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Complain.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
