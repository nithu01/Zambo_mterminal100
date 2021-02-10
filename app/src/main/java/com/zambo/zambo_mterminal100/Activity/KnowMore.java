package com.zambo.zambo_mterminal100.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.UserData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.hasnat.sweettoast.SweetToast;

public class KnowMore extends AppCompatActivity implements View.OnClickListener{
    EditText address,city,district,state,pincode,otp;
    UserData userData;
    ProgressDialog progressDialog;
    ScrollView scrollView;
    LinearLayout linearLayout;
    Button btn,btn2;
    String Otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);
        Otp = getIntent().getStringExtra("OTP");
        progressDialog = new ProgressDialog(this);
        userData= PrefManager.getInstance(KnowMore.this).getUserData();
        address =findViewById(R.id.edittext_address_fastag);
        city =findViewById(R.id.edittext_city_fastag);
        district =findViewById(R.id.edittext_district_fastag);
        state =findViewById(R.id.edittext_state_fastag);
        pincode =findViewById(R.id.edittext_pincode_fastag);
        scrollView =findViewById(R.id.scrollview_fastag);
        linearLayout = findViewById(R.id.verfyOtp);
        otp =findViewById(R.id.otpverify);
        btn = findViewById(R.id.btn_continue_fastag);
        btn.setOnClickListener(this);
        btn2 = findViewById(R.id.btn_verify_otp);
        btn2.setOnClickListener(this);
        if (Otp.equals("OTP")){
            getOTP();
            scrollView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }else if(Otp.equals("KYC")){
            scrollView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v==btn){
            String address1=address.getText().toString().trim();
            String city1=city.getText().toString().trim();
            String district1=district.getText().toString().trim();
            String state1=state.getText().toString().trim();
            String pincode1=pincode.getText().toString().trim();
            if (isValidate(address1, city1, district1, state1, pincode1)) {
                getcreateSender(userData.getToken(),userData.getMobile(),userData.getName(),address1, city1, district1, state1, pincode1,userData.getUserId(),"APP");
            }
        }
        if (v==btn2){
            String Otp=otp.getText().toString().trim();
            getverifyOTP(userData.getToken(),userData.getMobile(),Otp,userData.getUserId(),"APP");
        }

    }

    public void getcreateSender(String token,String mobile,String name,String address1, String city1, String district1,String state1, String pincode1,String userid,String source) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/axisdmt/pmcares/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.createSender(token,mobile,name,address1,city1,district1,state1,pincode1,userid,source);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                      getOTP();
                }else if(response.body().getStatus().equals(1)){
                    Toast.makeText(KnowMore.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(KnowMore.this,"Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(KnowMore.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getOTP() {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/axisdmt/pmcares/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.OTP(userData.getToken(),userData.getMobile(),userData.getUserId(),"APP");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    scrollView.setVisibility(View.GONE);
                    linearLayout.setVisibility(View.VISIBLE);
                }else if(response.body().getStatus().equals(1)){
                    Toast.makeText(KnowMore.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(KnowMore.this,"Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(KnowMore.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getverifyOTP(String token, String mobile, String otp, String userId, String s) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/axisdmt/pmcares/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.verifyOTP(token,mobile,otp,userId,s);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                     Intent intent = new Intent(KnowMore.this,PMCaresFund.class);
                     startActivity(intent);
                }else if(response.body().getStatus().equals(1)){
                    Toast.makeText(KnowMore.this,response.body().getMessage(), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(KnowMore.this,"Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(KnowMore.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isValidate(String address1, String city1, String district1, String state1, String pincode1) {
        if (TextUtils.isEmpty(address1)) {
            address.setError(getResources().getString(R.string.enter_address));
            address.requestFocus();
            SweetToast.error(KnowMore.this,"Please enter address");
            return false;
        }
        if (TextUtils.isEmpty(city1)) {
            city.setError(getResources().getString(R.string.enter_city));
            city.requestFocus();
            SweetToast.error(KnowMore.this,"Please enter city");
            return false;
        }
        if (TextUtils.isEmpty(district1)) {
            district.setError(getResources().getString(R.string.enter_address));
            district.requestFocus();
            SweetToast.error(KnowMore.this,"Please enter district");
            return false;
        }
        if (TextUtils.isEmpty(state1)) {
            state.setError(getResources().getString(R.string.enter_state));
            state.requestFocus();
            SweetToast.error(KnowMore.this,"Please enter state");
            return false;
        }
        if (TextUtils.isEmpty(pincode1)) {
            pincode.setError(getResources().getString(R.string.enter_pincode));
            pincode.requestFocus();
            SweetToast.error(KnowMore.this,"Please enter pincode");
            return false;
        }
        return true;
    }


}
