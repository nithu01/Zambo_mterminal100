package com.zambo.zambo_mterminal100.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.DataAttributes;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.VerhoeffAlgorithm;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.Interface.updateBalance;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.BankList;
import com.zambo.zambo_mterminal100.model.DeviceInfo;
import com.zambo.zambo_mterminal100.model.Opts;
import com.zambo.zambo_mterminal100.model.PidData;
import com.zambo.zambo_mterminal100.model.PidOptions;
import com.zambo.zambo_mterminal100.model.UserData;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AadharPay extends AppCompatActivity implements View.OnClickListener, updateBalance {
    String uid,name,gender,yearOfBirth,careOf,house,street,location,villageTehsil,postOffice,district,state,postCode;
    String display = "",srno="",errCode="",errInfo="",fCount="",fType="",iCount="",iType="",nmPoints="",qScore="",dpID="",rdsID="",rdsVer="",dc="",mi="",mc="",sysid="",ci="",sesionKey="",hmac="",PidDatatype="",Piddata="";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 963;
    GPSTracker gpsTracker;
    Double lat = null, lon = null;
    private GoogleApiClient googleApiClient;
    private int REQUEST_LOCATION = 231;
    UserData userData;
    PidData pidData = null;
    JSONObject jsonObject=null;
    Spinner device;
    //    typee;
    EditText bank;
    Button capture,submit;
    TextView textView,textView1;
    private ArrayList<String> positions;
    public TextView txtBalance;
    public ImageView imgWallet, imgBack;
    public ProgressBar progressBarBalance;
    EditText amoutn,aadhar;
    ExpandableRelativeLayout expandableRelativeLayout;
    WebService webService;
    String IMEI="";
    Serializer serializer;
    String respp="";
    String bankidd="",bankinnoo="";
    List<String> bankname;
    List<String> bankinno;
    List<String> bankid;
    ImageView check;
    String url="https://www.zambo.in/prime/aeps/";
//    String type_val=SplashActivity.getPreferences("Type",null);
    String customer_no = SplashActivity.getPreferences("AadhaarPayMobileNo",null);
    String token="";
    String datat="";
    String[] data_array;
    JSONArray jsonArray=null;
    ProgressDialog progressDialog;
    LinearLayout linearLayout,LinearHide;
    String value="";
    String value1="";
    String irisvalue="";
    RadioGroup radioGroup;
    RadioButton radioButton;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aadhar_pay);

        Log.d("pidata",SplashActivity.getPreferences("pidata",""));
        linearLayout=findViewById(R.id.layout);
        textView1 = findViewById(R.id.CustomerNo);
        textView1.setText(customer_no);
        LinearHide = findViewById(R.id.LinearHide);
        textView=findViewById(R.id.textview);
        amoutn=findViewById(R.id.amount);
        check = findViewById(R.id.check);
        radioGroup = findViewById(R.id.radioG);
        aadhar=findViewById(R.id.aadhar_no);
        aadhar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==0){
                    aadhar.setPadding(0,0,0,0);
                    check.setVisibility(View.GONE);
                }else if (!validateAadharNumber(aadhar.getText().toString())){
                    aadhar.setPadding(55,0,0,0);
                    check.setImageResource(R.drawable.ic_cancel_red_24dp);
                    check.setVisibility(View.VISIBLE);
                }else if(validateAadharNumber(aadhar.getText().toString())){
                    aadhar.setPadding(55,0,0,0);
                    check.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    check.setVisibility(View.VISIBLE);
                }
            }
        });
        submit=findViewById(R.id.submit);
//            customer_no=findViewById(R.id.mobile);
        jsonArray=new JSONArray();
        progressDialog=new ProgressDialog(AadharPay.this);
        userData = PrefManager.getInstance(AadharPay.this).getUserData();
        Log.i("onSuccess", userData.getToken());
//        getbankdata();
//        try {
//
//        } catch (CertificateException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    progressDialog.show();
//
//                    final Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            progressDialog.dismiss();
//                            linearLayout.setVisibility(View.VISIBLE);
//                        }
//                    }, 5000);
//                }
//            });

        if (SplashActivity.getPreferences("deviceAadhaar","")!=null){
            if (SplashActivity.getPreferences("deviceAadhaar","").equals("Mantra")){
                radioGroup.check(R.id.mantra);
            }else if (SplashActivity.getPreferences("deviceAadhaar","").equals("Startek")){
                radioGroup.check(R.id.startek);
            }else if (SplashActivity.getPreferences("deviceAadhaar","").equals("Morpho")) {
                radioGroup.check(R.id.morpho);
            }else if (SplashActivity.getPreferences("deviceAadhaar","").equals("Mantra Iris")) {
                radioGroup.check(R.id.iris);
            }

        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (aadhar.getText().toString().isEmpty()) {
                    aadhar.setError("Enter Aadhaar No");
                    aadhar.requestFocus();
                } else if (aadhar.getText().toString().length() < 12 ) {
                    aadhar.setError("Enter Valid Aadhaar No");
                    aadhar.requestFocus();
                }else {
                    int selectedId=radioGroup.getCheckedRadioButtonId();
                    radioButton=findViewById(selectedId);
                    if (radioButton.getText().equals("Mantra")) {
                        SplashActivity.savePreferences("deviceAadhaar",radioButton.getText().toString());
                        irisvalue = "N";
                        getdevice();
//                     transaction();
                    } else if (radioButton.getText().equals("Startek")){
                        SplashActivity.savePreferences("deviceAadhaar",radioButton.getText().toString());
                        irisvalue = "N";
                        getdevice();
                    }else if (radioButton.getText().equals("Morpho")){
                        SplashActivity.savePreferences("deviceAadhaar",radioButton.getText().toString());
                        irisvalue = "N";
                        getdevice();
                    }else if (radioButton.getText().equals("Mantra Iris")){
                        SplashActivity.savePreferences("deviceAadhaar",radioButton.getText().toString());
                        irisvalue = "Y";
                        getdevice2();
                    }else {
                        Toast.makeText(AadharPay.this, "Select Device", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        bank=findViewById(R.id.bank);

        JSONObject jsonObject=new JSONObject();
        gpsTracker = new GPSTracker(AadharPay.this);
        if (gpsTracker.canGetLocation) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();

        }


//            typee=findViewById(R.id.type);
//            List<String> type = new ArrayList<String>();
//            type.add("Select type of transaction");
//            type.add("Cash withdrawal");
//            type.add("Balance Enquiry");
//            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, type);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            typee.setAdapter(dataAdapter);
//            typee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });

//            bank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
//                    Intent intent = new Intent(AepsActivity.this,BankListActivity.class);
//                      startActivity(intent);
////                    String item=adapterView.getItemAtPosition(pos).toString();
////                 //   Toast.makeText(getApplicationContext(),""+item,Toast.LENGTH_SHORT).show();
////                    for (int i = 0; i <bankname.size(); i++) {
////                        if (bankname.get(i).equals(item)) {
////                            bankinnoo=bankinno.get(i);
////                            bankidd = bankid.get(i);
////                            //    Toast.makeText(AepsActivity.this,""+bankinnoo,Toast.LENGTH_SHORT).show();
////                        }
////                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.savePreferences("BankList","AadharPay");
                Intent intent = new Intent(AadharPay.this,BankListActivity.class);
                startActivity(intent);
            }
        });
        try {
            value = getIntent().getStringExtra("value");
            value1 = getIntent().getStringExtra("value1");
//            Toast.makeText(this,""+value1,Toast.LENGTH_SHORT).show();
        }catch ( NullPointerException e)
        {

        }
        try {
            if (value.equals("")) {

            } else {
                bank.setText(value);
            }
        }catch (NullPointerException e){

        }
        positions = new ArrayList<>();
        bankid=new ArrayList<>();
        bankinno=new ArrayList<>();
//        device=findViewById(R.id.device);
//        device.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                    if (adapterView.getItemAtPosition(position).equals("Startek")){
//                    getdevice();
//                    }
//                    Toast.makeText(AepsActivity.this,""+adapterView.getItemAtPosition(position),Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

//        List<String> devices = new ArrayList<String>();
//        devices.add("Select Device");
//           devices.add("Mantra");
//            devices.add("Startek");
//            devices.add("Morpho");

//            bankname=new ArrayList<>();
//            bankname.add("Select Bank");
//            bankid.add("");
//            bankinno.add("");
//        ArrayAdapter<String> typeadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, devices);
//        typeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        device.setAdapter(typeadapter);

        capture=findViewById(R.id.capture);
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(getApplicationContext(),"butto",Toast.LENGTH_SHORT).show();
                getdevice();
                // capture();
            }
        });
        serializer=new Persister();


        init();
        //getIMEI();
        gpsTracker = new GPSTracker(AadharPay.this);
        if (gpsTracker.canGetLocation) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
        }
        //getdevice();
        //getData();
    }

    public static boolean validateAadharNumber(String aadharNumber){
        Pattern aadharPattern = Pattern.compile("\\d{12}");
        boolean isValidAadhar = aadharPattern.matcher(aadharNumber).matches();
        if(isValidAadhar){
            isValidAadhar = VerhoeffAlgorithm.validateVerhoeff(aadharNumber);
        }
        return isValidAadhar;
    }

    public void init() {
        imgWallet = findViewById(R.id.wallet_aeps);
        imgBack = findViewById(R.id.imgback_aeps);
        progressBarBalance = findViewById(R.id.prog_money);
        txtBalance = findViewById(R.id.txt_balance_bbps);
        expandableRelativeLayout = findViewById(R.id.expandablelayout_aeps);
        imgBack.setOnClickListener(this);
        imgWallet.setOnClickListener(this);
    }

//    public void getData() {
//        webService = new WebService((updateBalance) this);
//        webService.updateBalance(userData.getUserId());
//        if (expandableRelativeLayout.isExpanded()) {
//            expandableRelativeLayout.toggle();
//        }
////        if (validateLocation(lat, lon, IMEI)) {
////            String merchantUserId = getIntent().getStringExtra(Constant.MERCHANT_USERID);
////            String merchantPassword = getIntent().getStringExtra(Constant.MERCHANT_PASSWORD);
////            String merchantId = getIntent().getStringExtra(Constant.MERCHANT_ID);
////            String merchantName = getIntent().getStringExtra(Constant.MERCHANT_NAME);
////            String merchantContactNumber = getIntent().getStringExtra(Constant.MERCHANT_CONSTANT_NUMBER);
////            String merchantAddress1 = getIntent().getStringExtra(Constant.MERCHANT_ADDRESS1);
////            String merchantAddress2 = getIntent().getStringExtra(Constant.MERCHANT_ADDRESS2);
////            String merchantCity = getIntent().getStringExtra(Constant.MERCHANT_CITY);
////            String merchantState = getIntent().getStringExtra(Constant.MERCHANT_STATE);
////            String merchantPinCode = getIntent().getStringExtra(Constant.MERCHANT_PINCODE);
////            String merchantCatCode = getIntent().getStringExtra(Constant.MERCHANT_CATCODE);
////            String merchantCountry = getIntent().getStringExtra(Constant.MERCHANT_COUNTRY);
////            String transId = getIntent().getStringExtra(Constant.MERCHANT_TRANSID);
////        }
//    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String walletBalance, String comissionWallet, String aepsWallet, String microAtmWallet, String accountNo, String bankName, String ifsc, String beneName) {
        txtBalance.setText("Bal. \u20B9" + walletBalance);
        progressBarBalance.setVisibility(View.GONE);
        SplashActivity.savePreferences(Constant.BALANCE, walletBalance);
    }

//
//    public void getIMEI() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//            // READ_PHONE_STATE permission has not been granted.
//            //requestReadPhoneStatePermission();
//        } else {
//            // READ_PHONE_STATE permission is already been granted.
//           // doPermissionGrantedStuffs();
//        }
//    }



//    private void requestReadPhoneStatePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.READ_PHONE_STATE)) {
//            new AlertDialog.Builder(AepsActivity.this)
//                    .setTitle("Permission Request")
//                    .setMessage(getString(R.string.permission_read_phone_state_rationale))
//                    .setCancelable(false)
//                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                        //re-request
//                        ActivityCompat.requestPermissions(AepsActivity.this,
//                                new String[]{Manifest.permission.READ_PHONE_STATE},
//                                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//                    })
//                    .setIcon(R.drawable.warning)
//                    .show();
//        } else {
//            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
//                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//        }
//    }
//    private void alertAlert(String msg) {
//        new AlertDialog.Builder(AepsActivity.this)
//                .setTitle("Permission Request")
//                .setMessage(msg)
//                .setCancelable(false)
//                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                    // do somthing here
//                    ActivityCompat.requestPermissions(AepsActivity.this,
//                            new String[]{Manifest.permission.READ_PHONE_STATE},
//                            MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
//                })
//                .setNegativeButton(android.R.string.no, null)
//                .setIcon(R.drawable.warning)
//                .show();
//    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent = new Intent(AadharPay.this,AadhaarPayMobile.class);
            startActivity(intent);
        }
        if (v==imgWallet){
            if (expandableRelativeLayout.isExpanded())
                expandableRelativeLayout.toggle();
            else
                expandableRelativeLayout.toggle();
        }
//        if(v==bank)
//        {
////            Intent intent=new Intent(AepsActivity.this,BankListActivity.class);
////            startActivity(intent);
//        }
//        if(v==device){
////            Intent intent=new Intent(AepsActivity.this,DeviceActivity.class);
////            startActivityForResult(intent,1);
//        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure, you want to close?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.remove("Type");
//                editor.commit();
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(AadharPay.this, AadhaarPayMobile.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    private boolean validateLocation(Double lat, Double lon,String IMEI) {
//        if (lat==null||lon==null){
//            gpsCheck();
//            // SweetToast.error(ActivityFastag.this,"NO Location");
//            return false;
//        }
//        if (TextUtils.isEmpty(IMEI)){
//            SweetToast.error(AepsActivity.this,"To access service you shoul allow all permission");
//            return false;
//        }
//        return true;
//    }
//    private boolean hasGPSDevice(Context context) {
//        final LocationManager mgr = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);
//        if (mgr == null)
//            return false;
//        final List<String> providers = mgr.getAllProviders();
//        return providers.contains(LocationManager.GPS_PROVIDER);
//    }
//    private void gpsCheck() {
//        final LocationManager manager = (LocationManager) AepsActivity.this.getSystemService(Context.LOCATION_SERVICE);
//        assert manager != null;
//        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(AepsActivity.this)) {
//            Log.e("TAG","Gps already enabled");
//            enableLoc();
//        }else{
//            Log.e("TAG","Gps already enabled");
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_LOCATION) {
//            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(AepsActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(AepsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
//               // enableLoc();
//                finish();
//            }
//        }
//        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //doPermissionGrantedStuffs();
//            } else {
//           //     alertAlert(getString(R.string.permissions_not_granted_read_phone_state));
//            }
//        }
//    }
//fun detectdevice(){
//    try {
//        val intent = Intent()
//        intent.action = "in.gov.uidai.rdservice.fp.INFO"
//        startActivityForResult(intent, 1)
//    } catch (e: Exception) {
//        Log.e("Error", e.toStrin
//    private void enableLoc() {
//        if (googleApiClient == null) {
//            googleApiClient = new GoogleApiClient.Builder(AepsActivity.this)
//                    .addApi(LocationServices.API)
//                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                        @Override
//                        public void onConnected(Bundle bundle) {
//
//                        }
//                        @Override
//                        public void onConnectionSuspended(int i) {
//                            googleApiClient.connect();
//                        }
//                    })
//                    .addOnConnectionFailedListener((ConnectionResult connectionResult) -> Log.d("Location error", "Location error " + connectionResult.getErrorCode())).build();
//
//            googleApiClient.connect();
//            LocationRequest locationRequest = LocationRequest.create();
//            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//            locationRequest.setInterval(1000);
//            locationRequest.setFastestInterval(1000);
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(locationRequest);
//            builder.setAlwaysShow(true);
//            PendingResult<LocationSettingsResult> result =
//                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
//            result.setResultCallback((LocationSettingsResult result1) -> {
//                final Status status = result1.getStatus();
//                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                    try {
//                        status.startResolutionForResult(AepsActivity.this, REQUEST_LOCATION);
//                    } catch (IntentSender.SendIntentException e) {
//                        // Ignore the error.
//                    }
//                }
//            });
//        }
    //  }
    //}

    public void getdevice(){
        try {
            Intent intent = new Intent();
            intent.setAction("in.gov.uidai.rdservice.fp.INFO");
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    public void getdevice2(){
        try {
            Intent intent = new Intent();
            intent.setAction("in.gov.uidai.rdservice.iris.INFO");
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK)
        {
            Log.d("TAG","COULD NOT GET A GOOD RESULT.");
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(AadharPay.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d("TAG","Have scan result in your app activity :"+ result);
            processScannedData(result);
//            AlertDialog alertDialog = new AlertDialog.Builder(AepsActivity.this).create();
//            alertDialog.setTitle("Scan result");
//            alertDialog.setMessage(result);
//            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                    new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    });
//            alertDialog.show();

        }
        //retrieve scan result
//        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
//
//        if (scanningResult != null) {
//            //we have a result
//            String scanContent = scanningResult.getContents();
//            String scanFormat = scanningResult.getFormatName();
//
//            // process received data
//            if(scanContent != null && !scanContent.isEmpty()){
//                processScannedData(scanContent);
//            }else{
//                Toast toast = Toast.makeText(getApplicationContext(),"Scan Cancelled", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//
//        }else{
//            Toast toast = Toast.makeText(getApplicationContext(),"No scan data received!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    try {

                        String regis_info = data.getStringExtra("RD_SERVICE_INFO");
                        if (regis_info != null && regis_info.contains("NOTREADY")) {
                            Toast.makeText(this, "Device not connected", Toast.LENGTH_SHORT).show();
                        }else {

                            String result = data.getStringExtra("DEVICE_INFO");
                            String rdService = data.getStringExtra("RD_SERVICE_INFO");

                            if (radioButton.getText().equals("Mantra")) {
                                if (rdService != null) {
                                    display = "RD Service Info :\n" + rdService + "\n\n";
                                }
                                if (result != null) {

                                    DeviceInfo info = serializer.read(DeviceInfo.class, result);

                                    for (int i = 0; i < 1; i++) {

                                        srno = info.add_info.params.get(i).value;
//                                        Toast.makeText(this,""+srno,Toast.LENGTH_SHORT).show();
                                        dpID = info.dpId;
                                        rdsID = info.rdsId;
                                        rdsVer = info.rdsVer;
                                        mi = info.mi;
                                        mc = info.mc;
                                        dc = info.dc;

                                    }
                                    for (int i = 1; i < 2; i++) {
                                        sysid = info.add_info.params.get(i).value;
                                    }
                                    capture();
//                                    setText(result);
                                }
                            }
                            else if (radioButton.getText().equals("Startek")){

                                if (result != null) {
                                    //Toast.makeText(this,"Startek",Toast.LENGTH_SHORT).show();
                                    DeviceInfo info2 = serializer.read(DeviceInfo.class, result);
//                                    Toast.makeText(this,"dpID"+ info2,Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < 1; i++) {

                                        srno = info2.add_info.params.get(i).value;
//                                        Toast.makeText(this,""+srno,Toast.LENGTH_SHORT).show();
                                        dpID = info2.dpId;
                                        rdsID = info2.rdsId;
                                        rdsVer = info2.rdsVer;
                                        mi = info2.mi;
                                        mc = info2.mc;
                                        dc = info2.dc;

                                    }
                                    for (int i = 2; i < 3; i++) {
                                        sysid = info2.add_info.params.get(i).value;
                                    }
                                    capture();
//                                    setText();
                                }
                            }
                            else if (radioButton.getText().equals("Morpho")){

                                if (result != null) {
                                    //Toast.makeText(this,"Startek",Toast.LENGTH_SHORT).show();
                                    DeviceInfo info3 = serializer.read(DeviceInfo.class, result);
//                                    Toast.makeText(this,"dpID"+ info2,Toast.LENGTH_SHORT).show();

                                    for (int i = 0; i < 1; i++) {

                                        srno = info3.add_info.params.get(i).value;
//                                        Toast.makeText(this,""+srno,Toast.LENGTH_SHORT).show();
                                        dpID = info3.dpId;
                                        rdsID = info3.rdsId;
                                        rdsVer = info3.rdsVer;
                                        mi = info3.mi;
                                        mc = info3.mc;
                                        dc = info3.dc;

                                    }
//                                    for (int i = 2; i < 3; i++) {
//                                        sysid = info2.add_info.params.get(i).value;
//                                    }
                                    capture();
//                                    setText();
                                }
                            }
                            else if (radioButton.getText().equals("Mantra Iris")) {
                                if (rdService != null) {
                                    display = "RD Service Info :\n" + rdService + "\n\n";
                                }
                                if (result != null) {

                                    DeviceInfo info = serializer.read(DeviceInfo.class, result);

                                    for (int i = 0; i < 1; i++) {

                                        srno = info.add_info.params.get(i).value;
                                        Toast.makeText(this,""+srno,Toast.LENGTH_SHORT).show();
                                        dpID = info.dpId;
                                        rdsID = info.rdsId;
                                        rdsVer = info.rdsVer;
                                        mi = info.mi;
                                        mc = info.mc;
                                        dc = info.dc;

                                    }
                                    for (int i = 1; i < 2; i++) {
                                        sysid = info.add_info.params.get(i).value;
                                    }
                                    capture2();
//                                    setText(result);
                                }
                            }

                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze device info", e);
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        if (data != null) {

                            String result = data.getStringExtra("PID_DATA");
                            PidData pidData=serializer.read(PidData.class,result);

                            PidDatatype=pidData._Data.type;
                            errCode= pidData._Resp.errCode;
                            errInfo= pidData._Resp.errInfo;
                            fCount= pidData._Resp.fCount;
                            fType=pidData._Resp.fType;
                            iCount= pidData._Resp.iCount;
                            iType=pidData._Resp.iType;
                            nmPoints=pidData._Resp.nmPoints;
                            qScore=pidData._Resp.qScore;
                            hmac=pidData._Hmac;
                            Piddata=pidData._Data.value;
                            PidDatatype=pidData._Data.type;

                            ci=pidData._Skey.ci;
                            sesionKey=pidData._Skey.value;
                            if (result != null) {
                                pidData = serializer.read(PidData.class, result);
                                jsonArray=new JSONArray();
                                jsonObject=new JSONObject();
                                jsonObject.put("errCode",errCode);
                                jsonObject.put("errInfo",errInfo);
                                jsonObject.put("fCount",fCount);
                                jsonObject.put("fType",fType);
                                jsonObject.put("fCount",iCount);
                                jsonObject.put("fType",iType);
                                jsonObject.put("nmPoints",nmPoints);
                                jsonObject.put("qScore",qScore);
                                jsonObject.put("dpID",dpID);
                                jsonObject.put("rdsID",rdsID);
                                jsonObject.put("rdsVer",rdsVer);
                                jsonObject.put("dc",dc);
                                jsonObject.put("mi",mi);
                                jsonObject.put("mc",mc);
                                jsonObject.put("sysid",sysid);
                                jsonObject.put("ci",ci);
                                jsonObject.put("sessionKey",sesionKey);
                                jsonObject.put("hmac",hmac);
                                jsonObject.put("PidDatatype",PidDatatype);
                                jsonObject.put("Piddata",Piddata);

                                // jsonArray.put(jsonObject);
                                // Toast.makeText(this,"result"+jsonArray,Toast.LENGTH_SHORT).show();
                                //  setText(jsonObject.toString());
                                SplashActivity.savePreferences("pidata",result);
                                transaction();
                            }

                        }
                    } catch (Exception e) {
                        Log.e("Error", "Error while deserialze pid data", e);
                    }
                }
                break;
        }
    }
    public void capture(){
        try {
            String pidOption = getPIDOptions();
            //  Toast.makeText(getApplicationContext(),""+pidOption.length(),Toast.LENGTH_SHORT).show();
            if (pidOption != null) {
                Log.e("PidOptions", pidOption);
                Intent intent2 = new Intent();
                intent2.setAction("in.gov.uidai.rdservice.fp.CAPTURE");
                intent2.putExtra("PID_OPTIONS", pidOption);
                startActivityForResult(intent2, 2);
            }
        } catch (Exception e) {
            Log.e("Errorcapture", e.toString());
        }
    }
    public void capture2(){
        try {
            String pidOption = getPIDOptions2();
            //  Toast.makeText(getApplicationContext(),""+pidOption.length(),Toast.LENGTH_SHORT).show();
            if (pidOption != null) {
                Log.e("PidOptions", pidOption);
                Intent intent2 = new Intent();
                intent2.setAction("in.gov.uidai.rdservice.iris.CAPTURE");
                intent2.putExtra("PID_OPTIONS", pidOption);
                startActivityForResult(intent2, 2);
            }
        } catch (Exception e) {
            Log.e("Errorcapture", e.toString());
        }
    }
    private void setText(String message) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textView.setText(message.toString());
            }
        });
    }
    private String getPIDOptions() {
        try {

            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }

            Opts opts = new Opts();
            opts.fCount = "1";
            opts.fType = "0";
            opts.iCount = "0";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = "0";
            opts.pidVer = "2.0";
            opts.timeout = "10000";
//            opts.otp = "123456";
//            opts.wadh = "Hello";
            opts.posh = posh;
            opts.env = "P";

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }
    private String getPIDOptions2() {
        try {

            String posh = "UNKNOWN";
            if (positions.size() > 0) {
                posh = positions.toString().replace("[", "").replace("]", "").replaceAll("[\\s+]", "");
            }

            Opts opts = new Opts();
            opts.fCount = "0";
            opts.fType = "0";
            opts.iCount = "1";
            opts.iType = "0";
            opts.pCount = "0";
            opts.pType = "0";
            opts.format = "0";
            opts.pidVer = "2.0";
            opts.timeout = "10000";
//            opts.otp = "123456";
//            opts.wadh = "Hello";
            opts.posh = posh;
            opts.env = "P";

            PidOptions pidOptions = new PidOptions();
            pidOptions.ver = "1.0";
            pidOptions.Opts = opts;

            Serializer serializer = new Persister();
            StringWriter writer = new StringWriter();
            serializer.write(pidOptions, writer);
            return writer.toString();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return null;
    }
    public void getbankdata() {

        Retrofit retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<List<BankList>> call=apiinterface.getprimebanklist();
        call.enqueue(new Callback<List<BankList>>() {
            @Override
            public void onResponse(Call<List<BankList>> call, Response<List<BankList>> response) {
                BankList bankList=null;
//                Toast.makeText(AepsActivity.this,"toast",Toast.LENGTH_SHORT).show();
                for(int i=0;i<response.body().size();i++)
                {
                    Log.d("TAG","RESPONSEDATAEE"+response.body().get(i).getBankName()+"\n"+response.body().get(i).getIinno());
//                    Toast.makeText(AepsActivity.this,""+response.body().get(i).getBankName()+"\n"+response.body().get(i).getIinno(),Toast.LENGTH_SHORT).show();
                    bankid.add(response.body().get(i).getBankid());
                    bankinno.add(response.body().get(i).getIinno());
                    bankname.add(response.body().get(i).getBankName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(AadharPay.this, android.R.layout.simple_spinner_item, bankname);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                bank.setAdapter(dataAdapter);
            }

            @Override
            public void onFailure(Call<List<BankList>> call, Throwable t) {
                Toast.makeText(AadharPay.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void transaction()
    {
        String amt=amoutn.getText().toString();
        if(amt.length()==0)
            amt="0";
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        ProgressDialog progressDialog=new ProgressDialog(AadharPay.this);
        progressDialog.show();

        SplashActivity.savePreferences("request",jsonArray.toString());
//      Toast.makeText(AepsActivity.this,""+bankidd+"\n"+aadhar.getText().toString()+"\n"+type_val+"\n"+customer_no+"\n"+lat+"\n"+lon+"\n"+amoutn.getText().toString()+"\n"+srno+"\n"+userData.getUserId()+"\n"+value1,Toast.LENGTH_SHORT).show();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).readTimeout(100,TimeUnit.SECONDS);


        OkHttpClient builder = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200,TimeUnit.SECONDS).build();


        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/prime/aeps/").client(builder).addConverterFactory(GsonConverterFactory.create(gson)).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.primeaeps(userData.getToken(),jsonObject.toString(),aadhar.getText().toString(),"M",customer_no,lat,lon,amt,srno,userData.getUserId(),"APP",value1,irisvalue);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)) {
                    Log.i("onSuccess", response.body().toString());
//                    String jsonresponse = response.body().toString();
//                    parsejson(jsonresponse);
                    String status = response.body().getMessage();
                    Intent intent = new Intent(AadharPay.this,TransactionReceipt.class);
                    intent.putExtra("status",status);
                    intent.putExtra("txnId",response.body().getTxnid());
                    intent.putExtra("bank",response.body().getInvoiceData().getBankName());
                    intent.putExtra("aadhaar",response.body().getInvoiceData().getAdhaarNumber());
                    intent.putExtra("customerMobile",response.body().getInvoiceData().getMobileNumber());
                    intent.putExtra("bcCode",response.body().getInvoiceData().getBcCode());
                    intent.putExtra("bcName",response.body().getInvoiceData().getBcName());
                    intent.putExtra("bcLocation",response.body().getInvoiceData().getBcLocation());
                    intent.putExtra("rrn",response.body().getInvoiceData().getBankRRN());
                    intent.putExtra("txnAmount",response.body().getInvoiceData().getAmount());
                    intent.putExtra("balance",response.body().getInvoiceData().getBalanceAmount());
                    startActivity(intent);
                    if (SplashActivity.getPreferences("Language","").equals("English")) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(AadharPay.this, R.raw.speech);
                        mediaPlayer.start();
                    }else if (SplashActivity.getPreferences("Language","").equals("Hindi")) {
                        MediaPlayer mediaPlayer = MediaPlayer.create(AadharPay.this, R.raw.hindispeech);
                        mediaPlayer.start();
                    }
//                    Toast.makeText(AepsActivity.this,status,Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    String status = response.body().getMessage();
                    if (response.body().getInvoiceData() != null) {
                        Intent intent = new Intent(AadharPay.this, TransactionReceiptFailed.class);
                        intent.putExtra("status", status);
                        intent.putExtra("txnId", response.body().getTxnid());
                        intent.putExtra("bank", response.body().getInvoiceData().getBankName());
                        intent.putExtra("aadhaar",response.body().getInvoiceData().getAdhaarNumber());
                        intent.putExtra("customerMobile", response.body().getInvoiceData().getMobileNumber());
                        intent.putExtra("bcCode", response.body().getInvoiceData().getBcCode());
                        intent.putExtra("bcName", response.body().getInvoiceData().getBcName());
                        intent.putExtra("bcLocation", response.body().getInvoiceData().getBcLocation());
                        intent.putExtra("rrn", response.body().getInvoiceData().getBankRRN());
                        intent.putExtra("txnAmount", response.body().getInvoiceData().getAmount());
                        intent.putExtra("balance", response.body().getInvoiceData().getBalanceAmount());
                        startActivity(intent);
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(AadharPay.this, status, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AadharPay.this,"failure"+t,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void parsejson(String response){

        try {
            //getting th \e whole json object from the response
            JSONObject obj = new JSONObject(response);
            if(obj.optString("status").equals("true")){

                ArrayList<com.zambo.zambo_mterminal100.model.Response> retroModelArrayList = new ArrayList<>();

                Toast.makeText(AadharPay.this,"data"+obj.optString("message"),Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(AadharPay.this, obj.optString("message")+"", Toast.LENGTH_SHORT).show();
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * onclick handler for scan new card
     * @param view
     */
    public void scanNow( View view){
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return;
        }
        Intent i = new Intent(AadharPay.this, QrCodeActivity.class);
        startActivityForResult( i,REQUEST_CODE_QR_SCAN);
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setCaptureActivity(CaptureActivityPortrait.class);
//        integrator.setPrompt("Scan a Aadharcard QR Code");
//        integrator.setOrientationLocked(true);
//        integrator.setBeepEnabled(true);
//        integrator.setCameraId(0);// Use a specific camera of the device
//        integrator.initiateScan();
    }
    /**
     * process xml string received from aadhaar card QR code
     * @param scanData
     */
    protected void processScannedData(String scanData){
        Log.d("Rajdeol",scanData);
        XmlPullParserFactory pullParserFactory;

        try {
            // init the parserfactory
            pullParserFactory = XmlPullParserFactory.newInstance();
            // get the parser
            XmlPullParser parser = pullParserFactory.newPullParser();

            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(new StringReader(scanData));

            // parse the XML
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d("Rajdeol","Start document");
                } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                    // extract data from tag
                    //uid
                    uid = parser.getAttributeValue(null,DataAttributes.AADHAR_UID_ATTR);
                    //name
                    name = parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR);
                    //gender
                    gender = parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR);
                    // year of birth
                    yearOfBirth = parser.getAttributeValue(null,DataAttributes.AADHAR_YOB_ATTR);
                    // care of
                    careOf = parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR);
                    // house
                    house = parser.getAttributeValue(null,DataAttributes.AADHAR_HOUSE_ATTR);
                    // street
                    street = parser.getAttributeValue(null,DataAttributes.AADHAR_STREET_ATTR);
                    // location
                    location = parser.getAttributeValue(null,DataAttributes.AADHAR_LOC_ATTR);
                    // village Tehsil
                    villageTehsil = parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR);
                    // Post Office
                    postOffice = parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR);
                    // district
                    district = parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR);
                    // state
                    state = parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR);
                    // Post Code
                    postCode = parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR);

                } else if(eventType == XmlPullParser.END_TAG) {
                    Log.d("Rajdeol","End tag "+parser.getName());

                } else if(eventType == XmlPullParser.TEXT) {
                    Log.d("Rajdeol","Text "+parser.getText());

                }
                // update eventType
                eventType = parser.next();
            }

            // display the data on screen
            aadhar.setText(uid);
            final Dialog dialog=new Dialog(AadharPay.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_aadhaar);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background1));
            TextView textView=dialog.findViewById(R.id.Name);
            TextView textView2=dialog.findViewById(R.id.DOB);
            TextView textView3=dialog.findViewById(R.id.Gender);
            TextView textView4=dialog.findViewById(R.id.Address);
            TextView textView5=dialog.findViewById(R.id.AadhaarNo);
            ImageView imageView = dialog.findViewById(R.id.cancel);
            textView.setText(name);
            textView2.setText("DOB : "+yearOfBirth);
            textView3.setText("Gender : "+gender);
            textView4.setText("Address : "+house+","+street+","+location+","+villageTehsil+","+district+","+state+","+postCode);
            textView5.setText(uid);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            Window window = dialog.getWindow();
            assert window != null;
            window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }// EO function
}
