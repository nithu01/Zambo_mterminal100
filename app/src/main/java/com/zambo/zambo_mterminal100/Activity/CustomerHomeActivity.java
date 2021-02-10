package com.zambo.zambo_mterminal100.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.anupkumarpanwar.scratchview.ScratchView;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.CustomProgressBar;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.Interface.updateBalance;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.RechargeModal;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.UserData;
import com.zambo.zambo_mterminal100.model.rchage;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerHomeActivity extends AppCompatActivity implements View.OnClickListener, updateBalance {

    private ImageView imgBack;
    TextView txtTitle,plans;
    public TextView txtBalance;
    public ProgressBar progressBar;
    TextInputLayout inputNumber,inputSecond,inputAmount,inputThree;
    private EditText editTextNumber,editTextAmount,editTextSecond,editTextThree;
    @SuppressLint("StaticFieldLeak")
    public static Spinner spinnerOperatotList;
    private Button btnSubmit;
    CustomProgressBar progressDialog;
    WebService webService;
    String operatorId,service,serviceProvider;
    private String merchant_trxnId="";
    String adrs = "", address = "",city="",state="",pin="",country="";
    ImageView imageView;
    ExpandableRelativeLayout expandableRelativeLayout;
    UserData userData;
    GPSTracker gpsTracker;
    Double lat = null, lon = null;
    String value="";
    String AppPin = SplashActivity.getPreferences("appPin",null);
    String Pnumber = SplashActivity.getPreferences("mnumber", null);
    String language = SplashActivity.getPreferences("operater", null);
    ArrayAdapter<String> counryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        expandableRelativeLayout=findViewById(R.id.relativelayout);
        imageView=findViewById(R.id.wallet);
        if(expandableRelativeLayout.isExpanded())
            expandableRelativeLayout.toggle();
        inputSecond=findViewById(R.id.input_second);
        inputAmount=findViewById(R.id.input_amount);
        editTextSecond=findViewById(R.id.edittext_second);
        inputThree=findViewById(R.id.input_three);
        editTextThree=findViewById(R.id.edittext_three);
        progressDialog=new CustomProgressBar();
        editTextNumber=findViewById(R.id.edittext_number);

        plans = findViewById(R.id.plans);
        plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextNumber.getText().toString().isEmpty()){
                    editTextNumber.setError("Enter number");
                }else {
                    SplashActivity.savePreferences("mnumber", editTextNumber.getText().toString());
                    startActivity(new Intent(CustomerHomeActivity.this, Offer_Plans.class));
                }
            }
        });

        progressBar=findViewById(R.id.prog_bar_customer);
        progressBar.setVisibility(View.VISIBLE);
        txtBalance=findViewById(R.id.txt_balance_customer);
        spinnerOperatotList =findViewById(R.id.operator_list_customer);
        editTextAmount=findViewById(R.id.edittext_amount_customer);
        btnSubmit=findViewById(R.id.btn_customer);
        inputNumber=findViewById(R.id.input_mobile);
        imgBack=findViewById(R.id.imgback_customer);
        txtTitle=findViewById(R.id.txt_title_customer);
        webService=new WebService((updateBalance) this);
        imgBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        imageView.setOnClickListener(this);
        inputAmount.setHint("Enter Amount (\u20B9)");
        gpsTracker = new GPSTracker(CustomerHomeActivity.this);
        if (gpsTracker.canGetLocation) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
        }

        GPSTracker Location = new GPSTracker(this);
        getAddress(Location.getLatitude(),Location.getLongitude());
        userData= PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        if (Configuration.hasNetworkConnection(CustomerHomeActivity.this)){
            webService.updateBalance(userData.getUserId());
        }else {
            Configuration.openPopupUpDownBack(CustomerHomeActivity.this,R.style.Dialod_UpDown,
                    "main","internetError","No Internet connectivity"+
                    ", Thanks");
        }
        try{
            service=SplashActivity.getPreferences(Constant.CUSTOMER_SERVICE,"");

            if (service.equalsIgnoreCase("mobilePrepaid")){
                serviceProvider="Prepaid";
                txtTitle.setText("Mobile Recharge");
                btnSubmit.setText("Continue to Recharge");
                editTextNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                inputNumber.setHint("Enter 10 digit mobile number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("mobilePostpaid")){
                serviceProvider="postpaid";
                txtTitle.setText("Postpaid Bill Payment");
                btnSubmit.setText("Continue to Pay Bill");
                editTextNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(10) });
                inputNumber.setHint("Enter 10 digit mobile number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("dth")){
                serviceProvider="DTH";
                txtTitle.setText("DTH Recharge");
                btnSubmit.setText("Continue to DTH Recharge");
                editTextNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter  Number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
                plans.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("broadband")){
                serviceProvider="Broadband";
                txtTitle.setText("Broadband Recharge");
                btnSubmit.setText("Continue to Recharge");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter your subscribing id");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);

            }else if (service.equalsIgnoreCase("electricity")){
                serviceProvider="Electricity";
                txtTitle.setText("Electricity Bill Payment");
                btnSubmit.setText("Continue to pay bill");
                editTextNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter Number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Account Number");
                inputSecond.setVisibility(View.GONE);
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("landline")){
                serviceProvider="Landline";
                txtTitle.setText("Landline Bill pay");
                btnSubmit.setText("Continue to pay bill");
                editTextNumber.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter phone number without std code");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.VISIBLE);
                inputThree.setHint("Enter STD Code");
            }else if (service.equalsIgnoreCase("gas")){
                serviceProvider="Gas";
                txtTitle.setText("Gas Bill payments");
                btnSubmit.setText("Continue to pay bill");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter Number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("water")){
                serviceProvider="WaterBill";
                txtTitle.setText("Water Bill Payment");
                btnSubmit.setText("Continue to pay bills");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter Number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }else if (service.equalsIgnoreCase("creditCard")){
                serviceProvider="CreditCard";
                txtTitle.setText("Credit card Bill Payment");
                btnSubmit.setText("Continue to pay bills");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter your credit card number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }else {
                Log.d("TAG","MSG"+service);
                serviceProvider="Insurance";
                txtTitle.setText("Insurance premium");
                btnSubmit.setText("Continue to pay premium");
                editTextNumber.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                editTextNumber.setFilters(new InputFilter[] { new InputFilter.LengthFilter(20) });
                inputNumber.setHint("Enter your policy number");
                inputSecond.setVisibility(View.VISIBLE);
                inputSecond.setHint("Enter Account Number");
                inputThree.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
         counryAdapter = new ArrayAdapter<>(CustomerHomeActivity.this, R.layout.spinner_item,
                R.id.spinner_text, WebService.operatorName);
        spinnerOperatotList.setAdapter(counryAdapter);
        spinnerOperatotList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String operator = parent.getItemAtPosition(position).toString();
                SplashActivity.savePreferences("operater",operator);
                for (int i = 0; i < WebService.operatorName.size(); i++) {
                    if (WebService.operatorName.get(i).equals(operator)){
                        operatorId = WebService.operatorId.get(i);
                        SplashActivity.savePreferences("operateriid",operatorId);
                    }
                }
                System.out.println("Operator code-->" + operatorId + ", Operator Name---->" + operator);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            value = getIntent().getStringExtra("RS");
//            Toast.makeText(this,""+value1,Toast.LENGTH_SHORT).show();
        }catch ( NullPointerException e)
        {

        }
        try {
            if (value.equals("")) {

            } else {
                editTextNumber.setText(Pnumber);
                editTextAmount.setText(value);
                if(!language.equalsIgnoreCase(""))
                {
                    int spinnerPosition = counryAdapter.getPosition(language);
                    spinnerOperatotList.setSelection(spinnerPosition);

                }
            }
        }catch (NullPointerException e){

        }

        editTextNumber.setOnClickListener(v -> {
            if (spinnerOperatotList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
                try {
                    Configuration.hideKeyboardFrom(CustomerHomeActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        editTextAmount.setOnClickListener(v -> {
            if (spinnerOperatotList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
                try {
                    Configuration.hideKeyboardFrom(CustomerHomeActivity.this);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String walletBalance, String comissionWallet, String aepsWallet, String microAtmWallet, String accountNo, String bankName, String ifsc, String beneName) {
        txtBalance.setText("Wallet Balance : \u20B9" + walletBalance);
        progressBar.setVisibility(View.GONE);
        SplashActivity.savePreferences(Constant.BALANCE,walletBalance);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        final UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        if(v==imageView) {
            if(expandableRelativeLayout.isExpanded())
                expandableRelativeLayout.toggle();
            else
                expandableRelativeLayout.toggle();
        }
        if (v==imgBack){
//            progressDialog.setMessage("Loading...");
            progressDialog.show(this,"Loading...");
//            progressDialog.setCancelable(false);
//            progressDialog.setCanceledOnTouchOutside(false);
            SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"");
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                WebService.getActiveService(userData.getUserId(),userData.getuType());
                if (progressDialog.dialog.isShowing()){
                    progressDialog.dialog.dismiss();
                }
                if (SplashActivity.getPreferences(Constant.AGENT,"").equalsIgnoreCase("agent")){
                    Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }else {
                    Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                }
            }, 1000);
        }
        if (v==btnSubmit){
            String number=editTextNumber.getText().toString().trim();
            String amount=editTextAmount.getText().toString().trim();
            String second=editTextSecond.getText().toString().trim();
            String three=editTextThree.getText().toString().trim();
            getRandomNumberString();
            final String balance=SplashActivity.getPreferences(Constant.BALANCE,"");
            //Toast.makeText(this, ""+balance, Toast.LENGTH_SHORT).show();
            if (spinnerOperatotList.getSelectedItemPosition()==0){
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please select service provider name");
            }else if (number.isEmpty()){
                editTextNumber.setError("Enter number");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter number");
            }else if (inputSecond.isShown()&&second.isEmpty()){
                editTextSecond.setError("Enter account number");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter account number");
            }else if (inputThree.isShown()&&three.isEmpty()){
                editTextThree.setError("Enter STD Code");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter std code");
            }else if (amount.isEmpty()){
                editTextAmount.setError("Enter amount");
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Please enter amount");
//            }else if (Float.valueOf(amount)>Float.valueOf(balance)){
//                Float amt;
//                if (Float.valueOf(balance)<Float.valueOf(amount)){
//                    amt=Float.valueOf(amount)-Float.valueOf(balance);
//                }else {
//                    amt=Float.valueOf(amount);
//                }
//                Intent intentProceed = new Intent(CustomerHomeActivity.this, PWECouponsActivity.class);
//                intentProceed.putExtra("trxn_id",merchant_trxnId);
//                intentProceed.putExtra("trxn_amount",amt);
//                intentProceed.putExtra("trxn_prod_info","Recharge");
//                intentProceed.putExtra("trxn_firstname",userData.getName());
//                intentProceed.putExtra("trxn_email_id",userData.getEmail());
//                intentProceed.putExtra("trxn_phone",userData.getMobile());
//                intentProceed.putExtra("trxn_key",Constant.MERCHANT_ID);
//                intentProceed.putExtra("trxn_udf1","");
//                intentProceed.putExtra("trxn_udf2","");
//                intentProceed.putExtra("trxn_udf3","");
//                intentProceed.putExtra("trxn_udf4","");
//                intentProceed.putExtra("trxn_udf5","");
//                intentProceed.putExtra("trxn_address1",address);
//                intentProceed.putExtra("trxn_address2","");
//                intentProceed.putExtra("trxn_city",city);
//                intentProceed.putExtra("trxn_state",state);
//                intentProceed.putExtra("trxn_country",country);
//                intentProceed.putExtra("trxn_zipcode",pin);
//                intentProceed.putExtra("trxn_is_coupon_enabled",0);
//                intentProceed.putExtra("trxn_salt",Constant.MERCH_SALT);
//                intentProceed.putExtra("unique_id",userData.getUserId());
//                intentProceed.putExtra("pay_mode",Constant.PAY_MODE);
//                startActivityForResult(intentProceed, StaticDataModel.PWE_REQUEST_CODE);
            }else if (Configuration.hasNetworkConnection(CustomerHomeActivity.this)){
                ViewDialog(userData.getUserId(),operatorId, number,second,three,amount);
//                openScratchPopup( "12","","","");
               // rechargeProcess(userData.getUserId(),operatorId,number,second,three,amount);
            }else {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"internetError",
                        "No internet connectivity");
            }
        }
    }

    private void getAddress(double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            adrs = address    ;
            //  lati= String.valueOf(lat);
            // lng= String.valueOf(lon);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
            country=addresses.get(0).getCountryName();
            pin=addresses.get(0).getPostalCode();

            //  Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            Log.e("ADDRESS","ADDRESS-->"+address);

        } catch (Exception ex) {
            ex.printStackTrace();
            adrs = null;
        }
    }

    @Override
    public void onBackPressed() {
        final UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
//        progressDialog.setMessage("Loading...");
        progressDialog.show(this,"Loading...");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
        SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"");
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            WebService.getActiveService(userData.getUserId(), userData.getuType());
            if (progressDialog.dialog.isShowing()){
                progressDialog.dialog.dismiss();
            }
            if (SplashActivity.getPreferences(Constant.AGENT,"").equalsIgnoreCase("agent")){
                Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }else {
                Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        }, 1000);
    }

    @SuppressLint("SetTextI18n")
    private void ViewDialog(final String userId,final String operatorId,final String  number,
                            final String second,final String three,final String amount) {

        getRandomNumberString();
        final UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        final Dialog dialog=new Dialog(CustomerHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background1));
        dialog.setCancelable(false);
        Button btnNo,btnYes;
        TextView textView = dialog.findViewById(R.id.txt_operater);
        TextView txtMsg=dialog.findViewById(R.id.txt_msg);
        final String balance=SplashActivity.getPreferences(Constant.BALANCE,"");
       // txtMsg.setText("Do you want to proceed for payment of Rs 10 for "+number+"?");
       //   Html.fromHtml("<font color='#FFFFFF'><b> " + "ZAM" + "</b></font>"+"<font color='#0093dd'>" + "BO" + "</font>");
        textView.setText(spinnerOperatotList.getSelectedItem().toString());
        txtMsg.setText(Html.fromHtml("Do you want to proceed for payment of" +
                " <font color='#f54600'>₹ "+amount+"</font> for <font color='#f54600'>"+
                number + "</font> ?"));
        btnNo=dialog.findViewById(R.id.btn_no);
        btnYes=dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            if (Float.valueOf(amount)<10){
                editTextAmount.setError("Minimum Amount is Rs.10");
//            }else if (Float.valueOf(amount)>Float.valueOf(balance)){
//                Float amt;
//                if (Float.valueOf(balance)<Float.valueOf(amount)){
//                    amt=Float.valueOf(amount)-Float.valueOf(balance);
//                }else {
//                    amt=Float.valueOf(amount);
//                }
//                Intent intentProceed = new Intent(CustomerHomeActivity.this, PWECouponsActivity.class);
//                intentProceed.putExtra("trxn_id",merchant_trxnId);
//                intentProceed.putExtra("trxn_amount",amt);
//                intentProceed.putExtra("trxn_prod_info","Recharge");
//                intentProceed.putExtra("trxn_firstname",userData.getName());
//                intentProceed.putExtra("trxn_email_id",userData.getEmail());
//                intentProceed.putExtra("trxn_phone",userData.getMobile());
//                intentProceed.putExtra("trxn_key",Constant.MERCHANT_ID);
//                intentProceed.putExtra("trxn_udf1","");
//                intentProceed.putExtra("trxn_udf2","");
//                intentProceed.putExtra("trxn_udf3","");
//                intentProceed.putExtra("trxn_udf4","");
//                intentProceed.putExtra("trxn_udf5","");
//                intentProceed.putExtra("trxn_address1",address);
//                intentProceed.putExtra("trxn_address2","");
//                intentProceed.putExtra("trxn_city",city);
//                intentProceed.putExtra("trxn_state",state);
//                intentProceed.putExtra("trxn_country",country);
//                intentProceed.putExtra("trxn_zipcode",pin);
//                intentProceed.putExtra("trxn_is_coupon_enabled",0);
//                intentProceed.putExtra("trxn_salt",Constant.MERCH_SALT);
//                intentProceed.putExtra("unique_id",userId);
//                intentProceed.putExtra("pay_mode",Constant.PAY_MODE);
//                startActivityForResult(intentProceed, StaticDataModel.PWE_REQUEST_CODE);
            }else {
                if(operatorId.equals("3000")) {
                    recharge2AirtelProcess(userData.getUserId(), operatorId, number, second, three, amount);
                } else {
//                    recharge2Process(userData.getUserId(),operatorId,number,second,three,amount);
                    rechargemobile(number,operatorId,amount,userData.getUserId());
                  //  rechargeProcess(userData.getUserId(),operatorId,number,second,three,amount);
                }
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
      //02  window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void rechargemobile(String number, String operatorId, String amount, String userId) {
        progressDialog.show(this,"Please wait...");
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient builder = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200,TimeUnit.SECONDS).build();
//        Log.d("TAG", "rechargemobile: "+userData.getToken()+"\n"+AppPin+"\n"+number+"\n"+operatorId+"\n"+amount+"\n"+serviceProvider+"\n"+lat+"\n"+lon+"\n"+userId);
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").client(builder).addConverterFactory(GsonConverterFactory.create(gson)).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<RechargeModal> call=apiinterface.rechargemobile(userData.getToken(),AppPin,number,operatorId,amount,serviceProvider,lat,lon,userId);
        call.enqueue(new Callback<RechargeModal>() {
            @Override
            public void onResponse(Call<RechargeModal> call, retrofit2.Response<RechargeModal> response) {
                Log.d("TAG", "onResponse: "+response.body().getStatus());
                if (response.body().getStatus().equals(0)){
                    progressDialog.dialog.dismiss();
                    openPopup(response.body().getMessage(),"S",response.body().getTxnId(),amount);
                }else if (response.body().getStatus().equals(2)) {
                    progressDialog.dialog.dismiss();
                    String txtId = "NA";
                    openPopup(response.body().getMessage(),"P",txtId,amount);
                }else if (response.body().getStatus().equals(1)){
                    progressDialog.dialog.dismiss();
                    Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                            response.body().getMessage());
                }else {
                    progressDialog.dialog.dismiss();
                    Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                            response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<RechargeModal> call, Throwable t) {
                progressDialog.dialog.dismiss();
                Log.e("TAG", "RECHARGE_PROCESS3 Error: " + t.getMessage());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                            "Try again after sometime,please wait untill you get clear response");
                }
            }
        });
    }

//    private void rechargeProcess(final String userId, final String operatorId, final String number, final String second, final String three, final String amount) {
//
//        String tag_string_req = "recharge_process";
//
//        progressDialog.setMessage("Processing...");
//        progressDialog.show();
//        HttpsTrustManager.allowAllSSL();
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.RECHARGE2_PROCESS, response -> {
//                    Log.d("TAG", "RECHARGE_PROCESS2 Response: " + response+" "+userId+" "+operatorId+" "+number+" "+second+" "+three+" "+amount);
//                    progressDialog.dismiss();
//                    try {
//                        JSONObject jsonObject1=new JSONObject(response);
//                        String status = jsonObject1.getString("status");
//                        String message=jsonObject1.getString("message");
//
//                        Log.d("TAG","status:"+status);
//                        if (status.equals("S")){
//                            String txtid=jsonObject1.getString("txnId");
//                            WebService.getBalance(userId);
//                            openPopup(message,"S");
////                            if(SplashActivity.getPreferences(Constant.USER_TYPE,"").equals("CS")) {
////                                Toast.makeText(this, ""+txtid+userId+amount, Toast.LENGTH_SHORT).show();
////                                cashbackupdate(txtid, userId, amount);
////                            }
////                            else{
////                            }
//                        }else if (status.equalsIgnoreCase("P")){
//                            WebService.getBalance(userId);
//                            openPopup(message,"P");
//                        }else {
//                            WebService.getBalance(userId);
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
//                                        message);
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }, error -> {
//                    Log.e("TAG", "RECHARGE_PROCESS3 Error: " + error.getMessage());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
//                        "Try again after sometime,please wait untill you get clear response");
//            }
//            progressDialog.dismiss();
//                }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put(Constant.U_UID, userId);
//                params.put(Constant.MOBILE, number);
//                params.put(Constant.OPERATOR, operatorId);
//                params.put(Constant.AMOUNT, amount);
//                params.put(Constant.SERVICE, serviceProvider);
//                params.put(Constant.ACCOUNT_NUMBER, second);
//                params.put(Constant.STD_CODE, three);
//                return params;
//            }
//        };
//        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//
//    }

    

    private void recharge2Process(final String userId, final String operatorId, final String number,
                                  final String second, final String three, final String amount) {

        String tag_string_req = "recharge_process";

//        progressDialog.setMessage("Processing...");
        progressDialog.show(this,"Please wait...");


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RECHARGE2_PROCESS, response -> {
            Log.d("TAG", "RECHARGE_PROCESS3 Response: " + response+" "+userId+" "+operatorId+" "+number+" "+second+" "+three+" "+amount);
            progressDialog.dialog.dismiss();
            try {

                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
             //   String txtidd=jsonObject1.getString("txnId");
                Log.d("TAG","status:"+status);
                webService.updateBalance(userId);
              //  Toast.makeText(this, "outerstatus"+status, Toast.LENGTH_SHORT).show();
                if (status.equals("S")){
                    String txtid=jsonObject1.getString("txnId");
                    Log.d("TAG", "recharge2ProcesstxnId: "+txtid);
                    openPopup(message,"S",txtid,amount);

                }else if (status.equalsIgnoreCase("P")){

                    String txtid=jsonObject1.getString("txnId");
                    getCheckRecharge(txtid,amount);

                }else {
                    Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                            message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("TAG", "RECHARGE_PROCESS3 Error: " + error.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Try again after sometime,please wait untill you get clear response");
            }
            progressDialog.dialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.MOBILE, number);
                params.put(Constant.OPERATOR, operatorId);
                params.put(Constant.AMOUNT, amount);
                params.put(Constant.SERVICE, serviceProvider);
                params.put(Constant.ACCOUNT_NUMBER, second);
                params.put(Constant.STD_CODE, three);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    public void getCheckRecharge(String txnId, String amount) {
        progressDialog.show(this,"Please wait...");
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<rchage> call=apiinterface.checkRecharge(txnId);
        call.enqueue(new Callback<rchage>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.rchage> call, retrofit2.Response<rchage> response) {
                Log.d("TAG", "onResponse: "+response.body());
                if (response.body().getStatus().equals("S")){
                    progressDialog.dialog.dismiss();
                    openPopup(response.body().getMessage(),"S",txnId,amount);

                }else if (response.body().getStatus().equals("P")) {
                    progressDialog.dialog.dismiss();
                    String txtId = "NA";
                    openPopup(response.body().getMessage(),"P",txtId,amount);
                }else if (response.body().getStatus().equals("F")){
                    progressDialog.dialog.dismiss();
                    Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                            response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.rchage> call, Throwable t) {
                Log.d("tag", "onFailure: "+t);
                progressDialog.dialog.dismiss();
                Toast.makeText(CustomerHomeActivity.this,""+t,Toast.LENGTH_SHORT).show();
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        ""+t);
            }
        });

    }

    private void recharge2AirtelProcess(final String userId, final String operatorId, final String number,
                                        final String second, final String three, final String amount) {

        String tag_string_req = "recharge_process";

//        progressDialog.setMessage("Processing...");
        progressDialog.show(this,"Processing...");


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RECHARGE2_AIRTELPROCESS, response -> {
            Log.d("TAG", "RECHARGE_PROCESS3 Response: " + response+" "+userId+" "+operatorId+" "+number+" "+second+" "+three+" "+amount);
            progressDialog.dialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                webService.updateBalance(userId);
                Log.d("TAG","status:"+status);
                if (status.equals("S")){
                    openPopup(message, "S", message,"S");
//                    if(SplashActivity.getPreferences(Constant.USER_TYPE,"").equals("CS"))
//                        cashbackupdate(tid,userid,amount);
//                    else{
//                    }
                }else if (status.equalsIgnoreCase("P")){
                    openPopup(message, "S", message,"P");
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                                message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e("TAG", "RECHARGE_PROCESS3 Error: " + error.getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                        "Try again after sometime,please wait untill you get clear response");
            }
            progressDialog.dialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.MOBILE, number);
                params.put(Constant.OPERATOR, operatorId);
                params.put(Constant.AMOUNT, amount);
                params.put(Constant.SERVICE, serviceProvider);
                params.put(Constant.ACCOUNT_NUMBER, second);
                params.put(Constant.STD_CODE, three);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    @SuppressLint("DefaultLocale")
    public void getRandomNumberString() {
        UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999)+1000000;
        // this will convert any number sequence into 6 character.
        merchant_trxnId=userData.getUserId()+"BO"+String.format("%06d",number);
        Log.i("NUMBER",""+String.format("%06d", number));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserData userData=PrefManager.getInstance(CustomerHomeActivity.this).getUserData();
        String account=editTextNumber.getText().toString();
        String amount=editTextAmount.getText().toString();
        String second=editTextSecond.getText().toString();
        String three=editTextThree.getText().toString();
        assert data != null;
        String result = data.getStringExtra("result");
        String response = data.getStringExtra("payment_response");
        Log.e("TOTELRES","RESPONSE--->"+response+"  \n"+result);
        sendResponse(response,userData.getUserId(),account,amount,userData.getuType(),second,three);
        webService.updateBalance(userData.getUserId());
        getRandomNumberString();

        try {
            JSONObject jsonObject=new JSONObject(response);
            if (jsonObject.has("status")) {
                String status=jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    Log.e("STATUS","STATUS--->"+status);
                    Intent intent=new Intent(CustomerHomeActivity.this,MainActivity.class);
                    startActivity(intent);
                } else {
                    if (jsonObject.has("error_msg")) {
                        Toast.makeText(this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                Toast.makeText(this, "You have cancelled your transaction", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(final String response, final String userId, final String account, final String amount, final String type,
                              final String second, final String three) {
        String tag_string_req = "transaction_recharge";
//        progressDialog.setMessage("Please wait...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.setCancelable(false);
//        progressDialog.setIndeterminate(true);
        progressDialog.show(this,"Please wait...");
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYMENT_TRANSACTION, response1 -> {
                    Log.d("TAG", "transaction recharge Response: " + response1);
                    if (progressDialog.dialog.isShowing()){
                        progressDialog.dialog.dismiss();
                    }
                    try {
                        webService.updateBalance(userId);
                        JSONObject jsonObject=new JSONObject(response1);
                        String status=jsonObject.getString("status");
                        if (status.equals("0")){
                            recharge2Process(userId,operatorId,account,second,three,amount);
                        }else {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(CustomerHomeActivity.this,R.style.Dialod_UpDown,"error",
                                        jsonObject.getString("message"));
                            }
                            //  Toast.makeText(CustomerHomeActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    if (progressDialog.dialog.isShowing()) {
                        progressDialog.dialog.dismiss();
                    }
                    Log.e("TAG", "transaction recharge Error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.RESPONSE,response);
                params.put(Constant.USER_TYPE,type);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @SuppressLint("SetTextI18n")
    private void openPopup( String message, final String type, String txtid,String amount) {
        final Dialog dialg=new Dialog(CustomerHomeActivity.this);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_recharge);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=dialg.findViewById(R.id.txt_status);
        if (type.equalsIgnoreCase("S")){
            imageView.setImageResource(R.drawable.success);
            txtStatus.setText("Status : Success");
            if (SplashActivity.getPreferences("Language","").equals("English")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(CustomerHomeActivity.this, R.raw.speech);
                mediaPlayer.start();
            }else if (SplashActivity.getPreferences("Language","").equals("Hindi")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(CustomerHomeActivity.this, R.raw.hindispeech);
                mediaPlayer.start();
            }
        }else if (type.equalsIgnoreCase("P")){
            imageView.setImageResource(R.drawable.pending);
            txtStatus.setText("Status : Pending");
        }else {
            imageView.setImageResource(R.drawable.failed);
            txtStatus.setText("Status : Failed");
        }
        TextView txtTransactionId=dialg.findViewById(R.id.txt_status_recharge);
        Button btnOk= dialg.findViewById(R.id.btn_okay);
        txtTransactionId.setText(message);

        btnOk.setOnClickListener(v -> {
            dialg.dismiss();
            if(userData.getuType().equals("CS")) {
                cashbackupdate(txtid,userData.getUserId(),amount);
            }else{
                editTextAmount.setText("");
                editTextNumber.setText("");
                spinnerOperatotList.setSelection(0);
            }
           // Toast.makeText(this, ""+SplashActivity.getPreferences(Constant.USER_TYPE,""), Toast.LENGTH_SHORT).show();

//            Intent intent  = new Intent(CustomerHomeActivity.this,MainActivity.class);
//            startActivity(intent);
//            overridePendingTransition(0,0);

        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public void cashbackupdate(String txtid,String userid,String amount)
    {
        ProgressDialog progressDialog=new ProgressDialog(CustomerHomeActivity.this);
        progressDialog.show();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").client(client).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.scratchcard(txtid,userid,amount,"0");
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
            //  Toast.makeText(CustomerHomeActivity.this, ""+response.body().getCashback()+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                assert response.body() != null;
                   if (response.body().getStatus().equals(0)){
                       openScratchPopup1(response.body().getMessage(), txtid, userid, amount);
                   }else if(response.body().getStatus().equals(1)) {
                       openScratchPopup(response.body().getCashback(), txtid, userid, amount);
                   }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(CustomerHomeActivity.this, "Error"+t, Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        });
    }

    private void openScratchPopup(String commision, String txtid, String userid, String txt_amount)
    {
//      Toast.makeText(this, ""+commision, Toast.LENGTH_SHORT).show();
        final Dialog dialog=new Dialog(CustomerHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_scratch);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background1));
        ScratchView scratchView =dialog.findViewById(R.id.scratch_view);
        ImageView imageView = dialog.findViewById(R.id.imagecashback);
        imageView.setImageDrawable(getDrawable(R.drawable.offer_box));
        TextView textView=dialog.findViewById(R.id.textView);
        textView.setText(Html.fromHtml("You've won <br /><b><font color='#f54600'>₹"+commision+"</font></b>"));
        scratchView.setRevealListener(new ScratchView .IRevealListener() {
            @Override
            public void onRevealed(ScratchView  scratchView) {
//                Toast.makeText(getApplicationContext(), "Reveled", Toast.LENGTH_LONG).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        openScratchPopupMessage(commision, txtid, userid, txt_amount);
                        dialog.dismiss();
                    }
                }, 2000);
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView  scratchView, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    private void openScratchPopup1(String commision, String txtid, String userid, String txt_amount)
    {
        final Dialog dialog=new Dialog(CustomerHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_scratch);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background1));
        ScratchView  scratchView =dialog.findViewById(R.id.scratch_view);
        ImageView imageView = dialog.findViewById(R.id.imagecashback);
        imageView.setImageDrawable(getDrawable(R.drawable.betterluck));
        TextView textView=dialog.findViewById(R.id.textView);
        textView.setText(commision);
        scratchView.setRevealListener(new ScratchView .IRevealListener() {
            @Override
            public void onRevealed(ScratchView  scratchView) {
//                Toast.makeText(getApplicationContext(), "Reveled", Toast.LENGTH_LONG).show();
                openScratchPopupMessage1(commision);
                dialog.dismiss();
            }

            @Override
            public void onRevealPercentChangedListener(ScratchView  scratchView, float percent) {
                if (percent>=0.5) {
                    Log.d("Reveal Percentage", "onRevealPercentChangedListener: " + String.valueOf(percent));
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    private void openScratchPopupMessage(String commision, String txtid, String userid, String txt_amount)
    {
//      Toast.makeText(this, ""+commision, Toast.LENGTH_SHORT).show();
        final Dialog dialog=new Dialog(CustomerHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background1));
        TextView textView=dialog.findViewById(R.id.txt_msg);
        textView.setText(Html.fromHtml("You've won <br /><b><font color='#f54600'>₹"+commision+"</font></b>"));
        Button button=dialog.findViewById(R.id.btn_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usercashback(commision, txtid, userid, txt_amount);
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    private void openScratchPopupMessage1(String commision)
    {
//      Toast.makeText(this, ""+commision, Toast.LENGTH_SHORT).show();
        final Dialog dialog=new Dialog(CustomerHomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background1));
        TextView textView=dialog.findViewById(R.id.txt_msg);
        textView.setText(commision);
        Button button=dialog.findViewById(R.id.btn_yes);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    public void usercashback(String commision, String txtid, String userid, String txt_amount)
    {
        ProgressDialog progressDialog=new ProgressDialog(CustomerHomeActivity.this);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.cashbackupdate(userid,txtid,txt_amount,commision);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                Toast.makeText(CustomerHomeActivity.this, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerHomeActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {

            }
        });
    }
}
