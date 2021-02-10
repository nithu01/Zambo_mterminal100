package com.zambo.zambo_mterminal100.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.Activity.LoginActivity;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentFgtPassword extends Fragment implements View.OnClickListener {
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    LinearLayout layoutOtp,layoutPhoneEmail,layoutMobile;
    private TextView txtLogin,txtRegister;
    Fragment currentFragment;
    FragmentTransaction ft;
    private ImageView imgBack;
    Button btnLogin,button1,button2;
    UserData userData;
    EditText username,otp,phone;
    TextView textView,resend;
    ProgressDialog progressDialog;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_forgot,container,false);
        userData = PrefManager.getInstance(getContext()).getUserData();
        progressDialog = new ProgressDialog(getContext());
        layoutOtp = view.findViewById(R.id.layoutOtp);
        layoutPhoneEmail = view.findViewById(R.id.mobile_email);
        layoutMobile = view.findViewById(R.id.layoutPhone);
        txtLogin=view.findViewById(R.id.login);
        username=view.findViewById(R.id.username);
        btnLogin=view.findViewById(R.id.btnLogin);
        txtRegister=view.findViewById(R.id.register_fgt);
        imgBack=view.findViewById(R.id.img_back_forgot);
        phone =view.findViewById(R.id.phone);
        otp=view.findViewById(R.id.otpin);
        textView = view.findViewById(R.id.mob);
        resend = view.findViewById(R.id.resend);
        resend.setOnClickListener(this);
        button1 = view.findViewById(R.id.submit1);
        button1.setOnClickListener(this);
        button2 = view.findViewById(R.id.submit2);
        button2.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        txtLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        startSmsUserConsent();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                  /*  ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                    currentFragment=new FragmentLogin();
                    ft.replace(R.id.frame_login,currentFragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();*/
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);

                    return true;
                }
                return true;
            }
        });
        return  view;
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(getContext());
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Toast.makeText(getContext(), "On Success", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getContext(), "On OnFailure", Toast.LENGTH_LONG).show();
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
//                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
        getContext().registerReceiver(smsBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().unregisterReceiver(smsBroadcastReceiver);
    }


    @Override
    public void onClick(View v) {
        if (v==imgBack){
           /* ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentLogin();
            ft.replace(R.id.frame_login,currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();*/
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);

        }
        if (v==txtLogin){
           /* ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentLogin();
            ft.replace(R.id.frame_login,currentFragment);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();*/
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }
        if (v==txtRegister){
            ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentRegistration();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if(v==btnLogin)
        {

            forgetPassword(username.getText().toString());
        }
        if (v==button1){
            if (otp.getText().toString().isEmpty()) {
                otp.setError("Enter OTP");
                otp.requestFocus();
            }else if(otp.getText().toString().length() < 6 ) {
                otp.setError("Enter Valid OTP");
                otp.requestFocus();
            }else {
                getPinOtp(phone.getText().toString(),otp.getText().toString());
            }
        }
        if (v==button2){
            if (phone.getText().toString().isEmpty()) {
                phone.setError("Enter Mobile Number");
                phone.requestFocus();
            }else if(phone.getText().toString().length() < 10 ) {
                phone.setError("Enter Valid Mobile Number");
                phone.requestFocus();
            }else {
                getOtp(phone.getText().toString());
            }
        }
        if (v==resend){
            getOtpresend(phone.getText().toString());
        }

    }
    public void forgetPassword(String username)
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        String tag_string_req = "forget_pass";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FORGET_PASSWORD, response -> {
            Log.d(VolleyLog.TAG, "forget Pass response: " + response);
                progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                String status=jsonObject.getString("status");
                String message=jsonObject.getString("message");
                if (status.equals("0")){

                    Configuration.openPopupUpDownBack(getContext(),R.style.Dialod_UpDown,"back","success",
                            jsonObject.getString("message"));

                }else {
                    Toast.makeText(getContext(),
                            message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Log.e(VolleyLog.TAG, "Profile Error: " + error.getMessage());
            Toast.makeText(getContext(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USERNAME,username);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getPinOtp(String username, String otp) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getverifyOtp(username,otp);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    forgetPassword(phone.getText().toString());
                }else{
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getContext(),""+t,Toast.LENGTH_SHORT).show();
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
                    layoutMobile.setVisibility(View.GONE);
                    layoutOtp.setVisibility(View.VISIBLE);
                    textView.setText("+91"+phone.getText().toString());
                }else{
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(getContext(),""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getOtpresend(String username) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getOtpin(username);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(getContext(),""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
