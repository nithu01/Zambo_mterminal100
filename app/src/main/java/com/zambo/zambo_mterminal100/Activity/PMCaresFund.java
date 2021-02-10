package com.zambo.zambo_mterminal100.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.UserData;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PMCaresFund extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    private static final String TAG = "TAG";
    EditText editTextAmount;
    Button btn1,btn2,btn3,contribute;
    UserData userData;
    ImageView imageView,imageView1;
    ProgressDialog progressDialog;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_m_cares_fund);
        progressDialog = new ProgressDialog(this);
        editTextAmount = findViewById(R.id.amount);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        btn2 = findViewById(R.id.btn2);
        btn2.setOnClickListener(this);
        btn3 = findViewById(R.id.btn3);
        userData=PrefManager.getInstance(PMCaresFund.this).getUserData();
        btn3.setOnClickListener(this);
        contribute = findViewById(R.id.contribute);
        contribute.setOnClickListener(this);
        imageView = findViewById(R.id.imgback);
        imageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v==btn1){
            editTextAmount.setText("500");
        }
        if (v==btn2){
            editTextAmount.setText("1000");
        }
        if (v==btn3){
            editTextAmount.setText("5000");
        }
        if (v==imageView){
            Intent intent = new Intent(PMCaresFund.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (v==contribute){
            String amount=editTextAmount.getText().toString().trim();
            if (amount.isEmpty()){
                editTextAmount.setError("Enter Amount");
            }  else if (Configuration.hasNetworkConnection(PMCaresFund.this)){
                startPayment(userData.getName(),userData.getEmail(),userData.getMobile(),amount);
            }else {
                Toast.makeText(PMCaresFund.this, "Try again after 2 minutes", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public void getSenderInfo() {
////        progressDialog.show();
////        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/axisdmt/pmcares/").addConverterFactory(GsonConverterFactory.create()).build();
////        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
//////        Log.d(TAG, "getSenderInfo: "+userData.getToken()+"\n"+userData.getMobile()+"\n"+userData.getUserId());
//////        Toast.makeText(this, ""+userData.getToken()+"\n"+userData.getMobile()+"\n"+userData.getUserId()+"\n\n", Toast.LENGTH_SHORT).show();
////        Call<com.efficientindia.zambopay.model.Response> call=apiinterface.SenderInfo(userData.getToken(),userData.getMobile(),userData.getUserId(),"APP");
////        call.enqueue(new Callback<com.efficientindia.zambopay.model.Response>() {
////            @Override
////            public void onResponse(Call<com.efficientindia.zambopay.model.Response> call, Response<com.efficientindia.zambopay.model.Response> response) {
////                progressDialog.dismiss();
////                if (response.body().getStatus().equals(0)){
////
////                }else if(response.body().getStatus().equals(2)){
////                    Intent intent = new Intent(PMCaresFund.this,KnowMore.class);
////                    intent.putExtra("OTP","KYC");
////                    startActivity(intent);
////                }else if(response.body().getStatus().equals(3)){
////                    Intent intent = new Intent(PMCaresFund.this,KnowMore.class);
////                    intent.putExtra("OTP","OTP");
////                    startActivity(intent);
////                }else{
////                    Toast.makeText(PMCaresFund.this,"Something went wrong...", Toast.LENGTH_LONG).show();
////                }
////            }
////
////            @Override
////            public void onFailure(Call<com.efficientindia.zambopay.model.Response> call, Throwable t) {
////                Toast.makeText(PMCaresFund.this,""+t,Toast.LENGTH_SHORT).show();
////            }
////        });
////
////    }

    public void startPayment(String name, String email, String mobile,String amount) {

        final Activity activity = this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name",name);
            options.put("description", "PM Cares Fund");

            options.put("currency", "INR");

            Double total=Double.parseDouble(amount);
            total=total*100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", mobile);
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        try {
            payment(userData.getUserId(),"APP",s,userData.getToken());
//            Toast.makeText(this, "success"+s, Toast.LENGTH_SHORT).show();
            Log.d("TAG","successmessage"+s);
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code ,String response) {

        try {
//            payment(userData.getUserId(),"APP",response,userData.getToken());
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    public void payment(String userId,String type,String responses,String token) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/axisdmt/pmcares/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.donation(token,userId,editTextAmount.getText().toString(),responses,type);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
                if (response.body().getStatus().equals(0)){
                    progressDialog.dismiss();
                    Intent intent=new Intent(getApplicationContext(),PM_Cares_Success.class);
                    intent.putExtra("amount",editTextAmount.getText().toString());
                    startActivity(intent);
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(PMCaresFund.this,"Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(PMCaresFund.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PMCaresFund.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
