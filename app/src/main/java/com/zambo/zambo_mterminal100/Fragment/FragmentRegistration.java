package com.zambo.zambo_mterminal100.Fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.zambo.zambo_mterminal100.Activity.LoginActivity;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.States;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FragmentRegistration extends Fragment implements View.OnClickListener {

    private TextView txtLogin;
    private EditText name,email,mobile,password,address,city;
    private Spinner state;
    private Button btnRegister;
    private ProgressDialog pDialog;
    private ImageView imgShowPass,imgBack;
    private boolean showpass=false;
    ArrayAdapter<String> spinnerArrayAdapter;
    ArrayList<String> StateNames = new ArrayList<String>();
    ArrayList<String> StateId = new ArrayList<String>();
    String Sid;
    String value="";
    String value1="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_registration,container,false);
        txtLogin=view.findViewById(R.id.txt_login_reg);
        txtLogin.setOnClickListener(this);
        pDialog=new ProgressDialog(getActivity());
      //  sponsor=view.findViewById(R.id.sponsor);
        imgBack=view.findViewById(R.id.img_back_register);
        imgBack.setOnClickListener(this);
        imgShowPass=view.findViewById(R.id.img_show_pass_login);
        address=view.findViewById(R.id.address);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state);
        name= view.findViewById(R.id.name);
        email=view.findViewById(R.id.email);
        mobile=view.findViewById(R.id.mobile);
        password=view.findViewById(R.id.password);
        btnRegister=view.findViewById(R.id.btnregister);
        btnRegister.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        fetchJSON();
        StateNames.add("State");
        StateId.add("Select Bank");
        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String bankname = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < StateNames.size(); i++) {
                    if (bankname.equals(StateNames.get(i))) {
                        Sid = StateId.get(i);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        state.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SplashActivity.savePreferences("BankList","AEPS");
//                Intent intent = new Intent(getContext(), StateListActivity.class);
//                startActivity(intent);
//            }
//        });
//        try {
//            value = getActivity().getIntent().getStringExtra("State");
//            value1 = getActivity().getIntent().getStringExtra("StateId");
////            Toast.makeText(this,""+value1,Toast.LENGTH_SHORT).show();
//        }catch ( NullPointerException e)
//        {
//
//        }
//        try {
//            if (value.equals("")) {
//
//            } else {
//                state.setText(value);
//            }
//        }catch (NullPointerException e){
//
//        }
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK) {
                    /*ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
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
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v==imgBack){
          /*  ft=Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
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
        if (v==imgShowPass){
            int start, end;
            if (showpass) {
                start = password.getSelectionStart();
                end = password.getSelectionEnd();
                password.setTransformationMethod(new PasswordTransformationMethod());
                password.setSelection(start, end);
                showpass = false;
                imgShowPass.setImageResource(R.drawable.hide);

            } else {
                start = password.getSelectionStart();
                end = password.getSelectionEnd();
                password.setTransformationMethod(null);
                password.setSelection(start, end);
                showpass = true;
                imgShowPass.setImageResource(R.drawable.show);
            }
        }
        if (v==btnRegister){
         //   String spon=sponsor.getText().toString();
            String nam=name.getText().toString();
            String emai=email.getText().toString();
            String mob=mobile.getText().toString();
            String pass=password.getText().toString();
            String adres=address.getText().toString();
            String citty = city.getText().toString();
            Toast.makeText(getContext(), ""+adres, Toast.LENGTH_SHORT).show();
            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
                /*if (spon.isEmpty()){
                    sponsor.setError("Spomsor Id");
                }
                else */if (nam.isEmpty()){
                    name.setError("Name");
                }
                else if (emai.isEmpty()){
                    email.setError("Email");
                }
                else if (mob.isEmpty()){
                    mobile.setError("Mobile");
                }
                else if (pass.isEmpty()){
                    password.setError("Password");
                }else if (adres.isEmpty()){
                    address.setError("Address");
                }else if (citty.isEmpty()){
                    city.setError("City");
                } else if (state.getSelectedItem().equals("State")){
                    Toast.makeText(getActivity(),"Select State",Toast.LENGTH_LONG).show();
                }else {
                    //String regis="regis";
                    WebService.register(nam,emai,mob,pass,adres,citty,Sid,pDialog,getActivity());
                }
            }else {
                Toast.makeText(getActivity(),"check your internet connection",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void fetchJSON(){

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<States> call=apiinterface.getstatelist();
        call.enqueue(new Callback<States>() {
            @Override
            public void onResponse(Call<States> call, retrofit2.Response<States> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("onSuccess", response.body().toString());
                        for(int i=0;i<response.body().getData().size();i++) {
                            StateNames.add(response.body().getData().get(i).getName());
                            StateId.add(response.body().getData().get(i).getId());
                        }
                        spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item2, StateNames);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        state.setAdapter(spinnerArrayAdapter);
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<States> call, Throwable t) {
                Toast.makeText(getContext(),""+t,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
