package com.zambo.zambo_mterminal100.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.zambo.zambo_mterminal100.AppConfig.CustomProgressBar;
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

public class PinLogin extends AppCompatActivity {
    OtpView pin;
    Button button;
    UserData userData;
    TextView textView,name;
    CustomProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);
        progressDialog = new CustomProgressBar();
        userData = PrefManager.getInstance(PinLogin.this).getUserData();
        pin = findViewById(R.id.pin);
        name = findViewById(R.id.name);
        name.setText(userData.getName());
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
        textView = findViewById(R.id.change);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp(userData.getMobile());
            }
        });
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
        progressDialog.show(this,"Please wait...");
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getlockedscreen(userid,pin);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                if (response.body().getStatus().equals(1)){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            progressDialog.dialog.dismiss();
                        }
                    }, 5000);
                    SplashActivity.savePreferences("appPin",pin);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);

                }else {
                    progressDialog.dialog.dismiss();
                    Toast.makeText(PinLogin.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(PinLogin.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getOtp(String username) {
        progressDialog.show(this,"Please wait...");
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.getOtpin(username);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Intent intent = new Intent(PinLogin.this,pingenerate.class);
                    startActivity(intent);
//                    layoutPhone.setVisibility(View.GONE);
//                    layoutOtp.setVisibility(View.VISIBLE);
//                    layoutPin.setVisibility(View.GONE);
//                    textView.setText("+"+phone.getText().toString());
                }else{
                    Toast.makeText(PinLogin.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                progressDialog.dialog.dismiss();
                Toast.makeText(PinLogin.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(PinLogin.this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent launchNextActivity;
                        launchNextActivity = new Intent(Intent.ACTION_MAIN);
                        launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                        launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(launchNextActivity);
                        finish();
                    }
                }).create().show();
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
