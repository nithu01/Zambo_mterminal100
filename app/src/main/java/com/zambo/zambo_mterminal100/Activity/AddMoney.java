package com.zambo.zambo_mterminal100.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Interface.updateBalance;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.UserData;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;



public class AddMoney extends AppCompatActivity implements View.OnClickListener, PaymentResultListener, updateBalance {

    public TextView txtBalance;
    EditText editTextAmount;
    private ImageView imgBack;
    private Button btnAddMoney;
    ProgressDialog progressDialog;
    String merchant_trxnId="",pin="";
    String adrs = "", address = "",city="",state="";
    public ProgressBar progressBar;
    String amot="";
    private static final String TAG = AddMoney.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private String country="";
    UserData userData;
    MediaPlayer englishaudio,hindiaudio;

    WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        englishaudio = MediaPlayer.create(this, R.raw.speech);
        hindiaudio=MediaPlayer.create(this,R.raw.hindispeech);
        progressBar=findViewById(R.id.prog_money);
        txtBalance=findViewById(R.id.txt_balance_add_money);
        imgBack=findViewById(R.id.imgback_add_money);
        imgBack.setOnClickListener(this);
        progressDialog=new ProgressDialog(AddMoney.this);
        editTextAmount=findViewById(R.id.edittext_amount_add_money);
    // openScratchPopup();
        btnAddMoney=findViewById(R.id.btn_add_money);
        btnAddMoney.setOnClickListener(this);
        ReadPhoneStatePermission();
        webService=new WebService((updateBalance) this);
//        if(SplashActivity.getPreferences("Hindi","").equalsIgnoreCase("show")){
//            Toast.makeText(this, "hindi", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
//        }

        userData=PrefManager.getInstance(AddMoney.this).getUserData();
        GPSTracker Location = new GPSTracker(this);
        getAddress(Location.getLatitude(),Location.getLongitude());
        if (Configuration.hasNetworkConnection(AddMoney.this)){
            webService.updateBalance(userData.getUserId());
        }else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
        try {
            amot=getIntent().getStringExtra(Constant.AMOUNT);
            editTextAmount.setText(amot);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String walletBalance, String comissionWallet, String aepsWallet,
                                String microAtmWallet, String accountNo, String bankName, String ifsc, String beneName) {
        txtBalance.setText("Available Balance: \u20B9" + walletBalance);
        progressBar.setVisibility(View.GONE);
        SplashActivity.savePreferences(Constant.BALANCE, walletBalance);
    }


    private void getAddress(double latitude, double longitude) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            address = addresses.get(0).getAddressLine(0);
            adrs = address    ;
            //lati= String.valueOf(lat);
            //lng= String.valueOf(lon);
            city=addresses.get(0).getLocality();
            state=addresses.get(0).getAdminArea();
            pin=addresses.get(0).getPostalCode();
            country=addresses.get(0).getCountryName();
            //Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
            Log.e("ADDRESS","ADDRESS-->"+address);
        } catch (Exception ex) {
            ex.printStackTrace();
            adrs = null;
        }
    }

    public void ReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddMoney.this, Manifest.permission.READ_PHONE_STATE)) {
            ActivityCompat.requestPermissions(AddMoney.this, PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(AddMoney.this, PERMISSIONS, REQUEST_PERMISSIONS);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
       /* if (requestCode == REQUEST_PERMISSIONS) {
           *//* if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(AddMoney.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                //  Toast.makeText(AddMoney.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }*//*
        }*/
    }

/*
    private void getEazeBuzzId() {
        String tag_string_req = "EAZE_BUZZ";
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.GET_EAZEBUSS_ID, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "EAZE_BUZZ Response: " + response);
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject=new JSONObject(response);
                    merchant_trxnId=jsonObject.getString("tid");
                    customers_unique_id=jsonObject.getString("customerId");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "EAZE_BUZZ Error: " + error.getMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }) */
/*{
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.UID,userId);
                return params;
            }
        }*//*
;
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/


    @Override
    public void onClick(View v) {
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        if (v==imgBack){
            String back=SplashActivity.getPreferences(Constant.HOME,"");
            if (back.equalsIgnoreCase("home")){
                Intent intent = new Intent(AddMoney.this, HomeActivity.class);
                intent.putExtra(Constant.RECHARGE,"rechar");
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                SplashActivity.getPreferences(Constant.HOME,"");
            }else {
                Intent intent = new Intent(AddMoney.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        }
        if (v==btnAddMoney){
            String amount=editTextAmount.getText().toString().trim();
            getRandomNumberString();
            if (amount.isEmpty()){
                editTextAmount.setError("Enter Amount");
                Log.e("DATA","DA-->"+merchant_trxnId+" "+pin);
            }else if(Integer.parseInt(editTextAmount.getText().toString())>5000) {
                Configuration.openPopupUpDown(AddMoney.this,R.style.Dialod_UpDown,"error","Amount should be less than 5000");
            }  else if (Configuration.hasNetworkConnection(AddMoney.this)&&!merchant_trxnId.isEmpty()){
                startPayment(userData.getName(),userData.getEmail(),userData.getMobile(),amount);
//                Log.e("DATA","DA-->"+merchant_trxnId+" "+pin);
//                Intent intentProceed = new Intent(AddMoney.this, PWECouponsActivity.class);
//                intentProceed.putExtra("trxn_id",merchant_trxnId);
//                intentProceed.putExtra("trxn_amount",Float.valueOf(amount));
//                intentProceed.putExtra("trxn_prod_info","Add Money");
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
//                intentProceed.putExtra("trxn_address2",address);
//                intentProceed.putExtra("trxn_city",city);
//                intentProceed.putExtra("trxn_state",state);
//                intentProceed.putExtra("trxn_country",country);
//                intentProceed.putExtra("trxn_zipcode",pin);
//                intentProceed.putExtra("trxn_is_coupon_enabled",0);
//                intentProceed.putExtra("trxn_salt",Constant.MERCH_SALT);
//                intentProceed.putExtra("unique_id",userData.getUserId());
//                intentProceed.putExtra("pay_mode",Constant.PAY_MODE);
//                startActivityForResult(intentProceed, StaticDataModel.PWE_REQUEST_CODE);
            }else {
                Toast.makeText(this, "Try again after 2 minutes", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        assert data != null;
        String result = data.getStringExtra("result");
        String response = data.getStringExtra("payment_response");
        Log.e("TOTELRES","RESPONSE--->"+response+"  \n"+result);
        WebService.sendResponse(response,userData.getUserId(),AddMoney.this,userData.getuType());
        webService.updateBalance(userData.getUserId());
        getRandomNumberString();

        try {
            JSONObject jsonObject=new JSONObject(response);
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            if (jsonObject.has("status")) {
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("success")) {
                    String back=SplashActivity.getPreferences(Constant.HOME,"");
                    if (back.equalsIgnoreCase("home")){
                        Intent intent = new Intent(AddMoney.this, HomeActivity.class);
                        intent.putExtra(Constant.RECHARGE,"rechar");
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        SplashActivity.getPreferences(Constant.HOME,"");
                    }else {
                        Intent intent = new Intent(AddMoney.this, MainActivity.class);
                        intent.putExtra(Constant.RESPONSE, response);
                        startActivity(intent);
                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
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
    @SuppressLint("DefaultLocale")
    public void getRandomNumberString() {
        UserData userData=PrefManager.getInstance(AddMoney.this).getUserData();
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999)+1000000;
        // this will convert any number sequence into 6 character.
        merchant_trxnId=userData.getUserId()+"TX"+String.format("%06d",number);
        Log.i("NUMBER",""+String.format("%06d", number));
    }

    @Override
    public void onBackPressed() {
        String back=SplashActivity.getPreferences(Constant.HOME,"");
        if (back.equalsIgnoreCase("home")){
            Intent intent = new Intent(AddMoney.this, HomeActivity.class);
            intent.putExtra(Constant.RECHARGE,"rechar");
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            SplashActivity.getPreferences(Constant.HOME,"");
        }else {
            Intent intent = new Intent(AddMoney.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
    }

    public void startPayment(String name, String email, String mobile,String amount) {

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name",name);
            options.put("description", "Demoing Charges");

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
            payment(userData.getToken(),userData.getUserId(),userData.getuType(),s);
//            Toast.makeText(this, "success"+s, Toast.LENGTH_SHORT).show();
            Log.d("TAG","successmessage"+s);
            if(SplashActivity.getPreferences("Hindi","").equalsIgnoreCase("show")){
             // Toast.makeText(this, "hindi", Toast.LENGTH_SHORT).show();
                hindiaudio.start();
            }else {
                englishaudio.start();
               //, Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
            }

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }
    private void openPopup() {
        final Dialog dialg=new Dialog(AddMoney.this);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.rateus_popup);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        RatingBar ratingBar=dialg.findViewById(R.id.ratingBar);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.efficientindia.zambopay"));

                startActivity(intent);

            }
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onPaymentError(int code ,String response) {

        try {
//            Toast.makeText(this, ""+response, Toast.LENGTH_SHORT).show();
//            payment(userData.getToken(),userData.getUserId(),userData.getuType(),response);

//            Intent intent=new Intent(AddMoney.this,MainActivity.class);
//            startActivity(intent);
//            Toast.makeText(this, "errror"+response, Toast.LENGTH_SHORT).show();
//            Log.d("TAG","patmenterror"+response);
//            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }
//    private void openScratchPopup()
//    {
//        final Dialog dialog=new Dialog(AddMoney.this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_scratch);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        ScratchTextView scratchImageView=dialog.findViewById(R.id.scratchimageview);
//        Button button=dialog.findViewById(R.id.btnok);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        dialog.show();
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//    }
    public void payment(String token,String userId,String type,String responses)
    {
        String tag_string_req = "register_res";

        Log.d("TAG","sucessresponse"+responses);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PAYMENT_RESPOSNE, response -> {
          //  Log.d(VolleyLog.TAG, "Profile Response1: " + response);
          //  Log.d(VolleyLog.TAG, "Profile Response1: " + responses);
            try {
                JSONObject jsonObject=new JSONObject(response);
                String status=jsonObject.getString("status");
                String message=jsonObject.getString("message");
                if (status.equals("0")){

                }else {
                    Toast.makeText(AddMoney.this,
                            message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {
            Log.e(VolleyLog.TAG, "Profile Error: " + error.getMessage());
            Toast.makeText(AddMoney.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("token",token);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.USER_TYPE,type);
                params.put(Constant.RESPONSE,responses);
                params.put(Constant.AMOUNT2,editTextAmount.getText().toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
