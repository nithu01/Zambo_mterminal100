package com.zambo.zambo_mterminal100.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.donbrody.customkeyboard.components.keyboard.CustomKeyboardView;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.CustomProgressBar;
import com.zambo.zambo_mterminal100.AppConfig.SessionManager;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Fragment.FragmentFgtPassword;
import com.zambo.zambo_mterminal100.Fragment.FragmentRegistration;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout frameLayout;
    private LinearLayout scrollView;
    private TextView txtForgot, txtRegister;
    private EditText editTextUsername,editTextPassword;
    private ImageView imgShowPass;
    private boolean showpass=false;
    private Button btnLogin;
    private CustomProgressBar pDialog;
    SessionManager session;
    Fragment currentFragment;
    FragmentTransaction ft;
    CustomKeyboardView keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        keyboard = findViewById(R.id.customerKeyboardView);
        frameLayout=findViewById(R.id.frame_login);
        scrollView=findViewById(R.id.scrollview_login);
        txtForgot=findViewById(R.id.forgot);
        txtRegister=findViewById(R.id.register);
        session=new SessionManager(LoginActivity.this);
        editTextPassword=findViewById(R.id.password_login);
        editTextUsername=findViewById(R.id.username_login);
        imgShowPass=findViewById(R.id.img_showpass);
        pDialog=new CustomProgressBar();
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        txtForgot.setOnClickListener(this);
        keyboard.registerEditText(CustomKeyboardView.KeyboardType.NUMBER,editTextUsername);
        keyboard.registerEditText(CustomKeyboardView.KeyboardType.QWERTY,editTextPassword);

    }

    @Override
    public void onClick(View v) {
        if (v==txtForgot){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentFgtPassword();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if (v==imgShowPass){
            int start, end;
            if (showpass) {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                editTextPassword.setSelection(start, end);
                showpass = false;
                imgShowPass.setImageResource(R.drawable.hide);
            } else {
                start = editTextPassword.getSelectionStart();
                end = editTextPassword.getSelectionEnd();
                editTextPassword.setTransformationMethod(null);
                editTextPassword.setSelection(start, end);
                showpass = true;
                imgShowPass.setImageResource(R.drawable.show);
            }
        }
        if (v==txtRegister){
            frameLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            ft=getSupportFragmentManager().beginTransaction();
            currentFragment=new FragmentRegistration();
            ft.replace(R.id.frame_login,currentFragment);
            ft.commit();
        }
        if (v==btnLogin){
            String username=editTextUsername.getText().toString();
            String password=editTextPassword.getText().toString();
            if (Configuration.hasNetworkConnection(LoginActivity.this)){
                if (username.isEmpty()||password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter details",Toast.LENGTH_LONG).show();
                } else {
//                    getOtp(username,password);
                    WebService.login(username,password,pDialog,LoginActivity.this,session);
                }
            }else {
                Toast.makeText(LoginActivity.this,"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void getOtp(String username, String password) {

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.getOtpin(username);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
                 if (response.body().getStatus().equals(0)){
                     WebService.login(username,password,pDialog,LoginActivity.this,session);
                 }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(LoginActivity.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(LoginActivity.this)
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
}
