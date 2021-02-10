package com.zambo.zambo_mterminal100.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.SmsBroadcastReceiver;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.UserData;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forget_Password extends AppCompatActivity  {
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    EditText otp,upin,cpin,opin;
    LinearLayout layoutOtp,layoutPin,layoutPhone;
    Button button,button1,button2;
    UserData userData;
    ProgressDialog progressDialog;
    TextView textView,resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);
        progressDialog = new ProgressDialog(this);
        userData = PrefManager.getInstance(Forget_Password.this).getUserData();
        layoutOtp = findViewById(R.id.layoutOtp);
        layoutPin = findViewById(R.id.layoutPassword);
//        layoutPhone = findViewById(R.id.layoutPhone);
//        if (getIntent().getStringExtra("Type").equals("PHONE")){
//            layoutPhone.setVisibility(View.VISIBLE);
//            layoutOtp.setVisibility(View.GONE);
//            layoutPin.setVisibility(View.GONE);
//        }else if (getIntent().getStringExtra("Type").equals("CHANGE")){
//            layoutPhone.setVisibility(View.VISIBLE);
//            layoutOtp.setVisibility(View.GONE);
//            layoutPin.setVisibility(View.GONE);
//        } else if (getIntent().getStringExtra("Type").equals("PIN")){
//            layoutPhone.setVisibility(View.GONE);
//            layoutOtp.setVisibility(View.VISIBLE);
//            layoutPin.setVisibility(View.GONE);
//        }
        otp = findViewById(R.id.otpin);
        upin = findViewById(R.id.upass);
        cpin = findViewById(R.id.cpass);
        opin =findViewById(R.id.opass);
        textView = findViewById(R.id.mob);
        textView.setText("+91"+userData.getMobile());
        startSmsUserConsent();
        resend = findViewById(R.id.resend);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOtp(userData.getMobile());
            }
        });
//        button2 = findViewById(R.id.submit2);
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (phone.getText().toString().isEmpty()) {
//                    phone.setError("Enter Mobile No");
//                    phone.requestFocus();
//                }else if(phone.getText().toString().length() < 10 ) {
//                    phone.setError("Enter Valid Mobile Number");
//                    phone.requestFocus();
//                }else {
//                    getOtp(phone.getText().toString());
//                }
//            }
//        });
        button1 = findViewById(R.id.submit1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otp.getText().toString().isEmpty()) {
                    otp.setError("Enter OTP");
                    otp.requestFocus();
                }else if(otp.getText().toString().length() < 6 ) {
                    otp.setError("Enter Valid OTP");
                    otp.requestFocus();
                }else {
                    getPinOtp(userData.getUserId(),otp.getText().toString());
                }

            }
        });
        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (opin.getText().toString().isEmpty()) {
                    opin.setError("Enter Old Password");
                    opin.requestFocus();
                } else if (upin.getText().toString().isEmpty()) {
                    upin.setError("Enter New Password");
                    upin.requestFocus();
                } else if (cpin.getText().toString().isEmpty()) {
                    cpin.setError("Enter Confirm New Password");
                    cpin.requestFocus();
                }else if (upin.getText().toString()==cpin.getText().toString()) {
                    cpin.setError("Password doesn't match");
                    cpin.requestFocus();
                }else {
                    getpass(userData.getUserId(),opin.getText().toString(),upin.getText().toString(),cpin.getText().toString());
                }
            }
        });

    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(Forget_Password.this, "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Forget_Password.this, "On OnFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
//                Toast.makeText(Forget_Password.this, message, Toast.LENGTH_LONG).show();
//                textViewMessage.setText(
//                        String.format("%s - %s", getString(R.string.received_message), message));

                getOtpFromMessage(message);
            }
        }
    }

    public void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            otp.setText(matcher.group(0));
        }
    }

    public void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }

                    @Override
                    public void onFailure() {

                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

    public void getPinOtp(String userId, String otp) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getPinOtp(userId,otp);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    layoutOtp.setVisibility(View.GONE);
                    layoutPin.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(Forget_Password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Forget_Password.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getOtp(String username) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.getOtpin(username);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Toast.makeText(Forget_Password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Forget_Password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(Forget_Password.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getpass(String userid,String opin, String pin, String cpin) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
//        Toast.makeText(this, ""+userData.getUserId()+"\n"+otp+"\n"+pin+"\n"+cpin, Toast.LENGTH_LONG).show();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.getpassword(userid,opin,pin,cpin);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Toast.makeText(Forget_Password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    PrefManager.getInstance(Forget_Password.this).logout();
                    PreferenceManager.getDefaultSharedPreferences(Forget_Password.this).edit().clear().apply();
                    Intent intent = new Intent(Forget_Password.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(Forget_Password.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(Forget_Password.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

}
