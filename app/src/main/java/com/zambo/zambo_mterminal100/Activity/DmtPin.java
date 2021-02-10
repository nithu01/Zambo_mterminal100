package com.zambo.zambo_mterminal100.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.UserData;
import com.mukesh.OtpView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DmtPin extends AppCompatActivity {
    OtpView pin;
    Button button;
    UserData userData;
    TextView textView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmt_pin);
        progressDialog = new ProgressDialog(this);
        userData = PrefManager.getInstance(DmtPin.this).getUserData();
        pin = findViewById(R.id.pindmt);
        pin.requestFocus();
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==6){
                    hideKeyboard(pin);
                }

            }
        });
//        pin.setOtpCompletionListener(new OnOtpCompletionListener() {
//            @Override
//            public void onOtpCompleted(String otp) {
//                if (pin.getText().toString().isEmpty()) {
//                    pin.setError("Enter Pin");
//                    pin.requestFocus();
//                } else if (pin.getText().toString().length() < 6 ) {
//                    pin.setError("Enter Valid Pin");
//                    pin.requestFocus();
//                }else{
//                    getpin(userData.getUserId(),pin.getText().toString());
//                    pin.getText().clear();
//                }
//
//            }
//        });
//        textView = findViewById(R.id.change);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(DmtPin.this,pingenerate.class);
//                intent.putExtra("Type","PHONE");
//                startActivity(intent);
//            }
//        });
        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pin.getText().toString().isEmpty()) {
                    pin.setError("Enter Pin");
                    pin.requestFocus();
                } else if (pin.getText().toString().length() < 6 ) {
                    pin.setError("Enter Valid Pin");
                    pin.requestFocus();
                }else{
                     getpin(userData.getUserId(),pin.getText().toString());
                     pin.getText().clear();
                }
            }
        });
    }

    public void getpin(String userid, String pin) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getlockedscreen(userid,pin);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                if (response.body().getStatus().equals(1)){
                    progressDialog.dismiss();
                    Intent intent = new Intent(DmtPin.this,MoneyTransfer.class);
                    startActivity(intent);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(DmtPin.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(DmtPin.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DmtPin.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
