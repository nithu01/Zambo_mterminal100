package com.zambo.zambo_mterminal100.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.Activity.AadhaarPayMobile;
import com.zambo.zambo_mterminal100.Activity.ActivityFastag;
import com.zambo.zambo_mterminal100.Activity.AddMoney;
import com.zambo.zambo_mterminal100.Activity.AepsActivity;
import com.zambo.zambo_mterminal100.Activity.BankingSelect;
import com.zambo.zambo_mterminal100.Activity.BbpsActivity;
import com.zambo.zambo_mterminal100.Activity.DmtPin;
import com.zambo.zambo_mterminal100.Activity.Enddrawertoggle;
import com.zambo.zambo_mterminal100.Activity.GPSTracker;
import com.zambo.zambo_mterminal100.Activity.HistoryActivity;
import com.zambo.zambo_mterminal100.Activity.HomeActivity;
import com.zambo.zambo_mterminal100.Activity.KnowMore;
import com.zambo.zambo_mterminal100.Activity.MainActivity;
import com.zambo.zambo_mterminal100.Activity.Micro_Atm_Receipt;
import com.zambo.zambo_mterminal100.Activity.Micro_Atm_Receipt_P;
import com.zambo.zambo_mterminal100.Activity.Mini_Statement;
import com.zambo.zambo_mterminal100.Activity.PMCaresFund;
import com.zambo.zambo_mterminal100.Activity.SplashActivity;
import com.zambo.zambo_mterminal100.Activity.Transfer;
import com.zambo.zambo_mterminal100.Adapter.MenuItemHomeAdapter;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.ExpandableHeightGridView;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.Interface.updateBalance;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.ActiveServiceResponse;
import com.zambo.zambo_mterminal100.model.Fingpayseps;
import com.zambo.zambo_mterminal100.model.Notification;
import com.zambo.zambo_mterminal100.model.Res;
import com.zambo.zambo_mterminal100.model.UserData;
import com.zambo.zambo_mterminal100.viewmodel.HomeViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;

import org.egram.aepslib.DashboardActivity;
import org.egram.microatm.other.util;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.hasnat.sweettoast.SweetToast;

import static android.app.Activity.RESULT_OK;
import static org.egram.microatm.other.AllString.SnackBarBackGroundColor;
import static org.egram.microatm.other.util.isPackageInstalled;

public class HomeFragment extends Fragment implements View.OnClickListener,updateBalance {

    private static final int REQUEST_LOCATION = 123;
    private static String TAG=HomeFragment.class.getSimpleName();
    MediaPlayer mediaPlayer;
    private Button btnUpdateProfile, btnRedeemAeps;
    private TextView txtAmountAeps;
    private Double lat, lon;
    GPSTracker gpsTracker;
    private String stateId = "",
            accountNo = "", bankName = "", amountAeps;
    public TextView txtTitle;
    private ProgressDialog pDialog;
    Enddrawertoggle enddrawertoggle;
    private static final int INTENT_CODE = 1;
    private CardView cardViewAeps, cardViewMoney, cardViewMobile, cardViewDth, cardViewDatacard,
            cardViewElectricity, cardViewGas, cardViewLandline, cardViewInsuarance, cardViewAeps2;
    private ProgressDialog progressDialog;
    private static String phone = "", roundMobile = "", roundPassword = "";
    private static final int REQUEST_PERMISSIONS = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE
            , Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private String transType;
    private String BcName,BcId,BcLocation;

    private LinearLayout lnAeps;

    private ProgressBar progressBar;
    private Button wallet;
    private ExpandableHeightGridView /*recharges,*/ gridViewbillpayment;
    private RelativeLayout cardViewCustomer;
    private CardView /*cardViewCustomer,*/ cardViewBbps, cardViewRechargeThree, cardViewMoneytransferTwo, cardViewRechargeTwo, cardviewmicroatm;
    private Button transfer, addmoney;

    private String[] nameItem = {
            "Prime AEPS", "Micro Atm","Aadhaar Pay", "Mini Statement",
            "AEPS","Fastag","Mobile Prepaid", "DTH 1.0",
            "Mobile Postpaid ","DTH 2.0", "Broadband Postpaid", "Broadband Prepaid",
            "Electricity", "Landline", "Gas","Water",
            "PM CARES Fund"};

    private ExpandableHeightGridView gridViewBbps;
    private Integer[] mThumbIds = {
            R.drawable.aeps, R.drawable.micro_atm,R.drawable.aadhaar_pay,R.drawable.ministatement_circle,
            R.drawable.aeps,R.drawable.fastaglogo,R.drawable.mobile_prepaid, R.drawable.dth2new,
            R.drawable.mobile_postpaid,R.drawable.dth1new, R.drawable.broadband_prepaid, R.drawable.broadband_postpaid,
            R.drawable.electricitynew, R.drawable.landlinecolor, R.drawable.gas,R.drawable.waternew,
            R.drawable.pm_cares};

    private String[] billItem = {"PM CARES Fund","Mobile Prepaid", "DTH", "Gas",/* "Insurance", "Water Bill",
            "Landline", "Credit Card", "Broadband",
            "Electricity", "Postpaid", "Electricity", "Payphi"*/"Fastag"};
    private Integer[] billIcons = {R.drawable.pm_cares,
            R.drawable.mobile_postpaid, R.drawable.dth2new,
            R.drawable.gas,/* R.drawable.dthcolor, R.drawable.gascolor,
            R.drawable.insurancecolor, R.drawable.watercolor, R.drawable.landlinenew,
            R.drawable.creditcardcolor, R.drawable.broadbandpostpaidcolor,
            R.drawable.electricitycolor, R.drawable.postpaidcolor,*/R.drawable.fastaglogo};
    public TextView transaction;
    private TextView balance;
    private UserData userData;
   // TextView recharge2;
    private String service, serviceName;
    private TextView textViewl;
    private Handler handler=new Handler();
    View view;

    private WebService webService;

    private HomeViewModel homeViewModel;
    private GoogleApiClient googleApiClient;

    String requesttxn, bankremarks , refstan , cardno , date , amount, invoicenumber, mid , tid , clientrefid, vendorid , udf1 , udf2, udf3 , udf4 , txnamount, rrn ;
    //  private ProgressDialog progressDialog;

    @TargetApi(Build.VERSION_CODES.N)

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        userData=PrefManager.getInstance(getActivity()).getUserData();
//        Log.d("tag","token"+userData.getToken());
        homeViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(HomeViewModel.class);
        webService=new WebService(this);
        webService.updateBalance(userData.getUserId());
        gpsTracker = new GPSTracker(getContext());
        if (gpsTracker.canGetLocation) {
            lat = gpsTracker.getLatitude();
            lon = gpsTracker.getLongitude();
        }
        viewHome();
        addmoney.setOnClickListener(this);
        transfer.setOnClickListener(this);
        transaction.setOnClickListener(this);

        wallet.setOnClickListener(this);
        initCustomer();

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {

                new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(Intent.ACTION_MAIN);
                            launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchNextActivity);
                            getActivity().finish();
                        }).create().show();

                return true;
            }
            return true;
        });

        initApi();

        customerAdapter();

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onUpdateBalance(String walletBalance, String comissionWallet, String aepsWallet, String microAtmWallet,
                                String account, String bank, String ifsc, String beneName) {
        balance.setText("Bal. \u20B9" + walletBalance);
        progressBar.setVisibility(View.GONE);
        SplashActivity.savePreferences(Constant.BALANCE, walletBalance);
        wallet.setText("Redeem Aeps Wallet ( ₹"+aepsWallet+")");
        amountAeps=aepsWallet;
        accountNo =account;
        bankName =bank;
        Log.d("TAG","responseaeps"+ this.accountNo + this.bankName);
       // txtAmountAeps.setText("₹"+aepsWallet);
    }

    @SuppressLint("SetTextI18n")
    private void viewHome() {
        ReadPhoneStatePermission();
        textViewl = view.findViewById(R.id.text);
        //recharge2=view.findViewById(R.id.recharge2);
        gridViewBbps = view.findViewById(R.id.gridview_bbps);
        userData = PrefManager.getInstance(getActivity()).getUserData();
//        Toast.makeText(getContext(), ""+userData.getEmail(), Toast.LENGTH_SHORT).show();
        java.util.Calendar c = java.util.Calendar.getInstance();
        // java.text.SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        @SuppressLint("WrongConstant")
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            textViewl.setText("Good Morning " + userData.getName());
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            textViewl.setText("Good Afternoon " + userData.getName());
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            textViewl.setText("Good Evening " + userData.getName());
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            textViewl.setText("Good Night " + userData.getName());
        }

        bbps();
        gridViewbillpayment=view.findViewById(R.id.billpayment);
        lnAeps=view.findViewById(R.id.linear_aeps);
        progressBar=view.findViewById(R.id.progressbar);
        transaction=view.findViewById(R.id.transactions);

        cardViewBbps=view.findViewById(R.id.cardview_bbps);
        cardViewCustomer=view.findViewById(R.id.cardview_customer);
        transfer=view.findViewById(R.id.paymoney);
        addmoney=view.findViewById(R.id.addmoney);
      //  recharges=view.findViewById(R.id.recharge);
        wallet=view.findViewById(R.id.wallet);

        progressDialog = new ProgressDialog(getActivity());
        progressBar.setVisibility(View.GONE);

        balance=view.findViewById(R.id.balance);
        pDialog=new ProgressDialog(getContext());
    }

    private void initApi() {
        if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))) {

            final Handler handler = new Handler();
            handler.post(() -> {
                //getProfileData(userData.getUserId());
               // WebService.getBalance(userData.getUserId());
             //   userActiveService(userData.getUserId());
                getActiveService(userData.getUserId(),userData.getuType());

            });
            handler.postDelayed(() -> {
                WebService.getActiveService(userData.getUserId(), userData.getuType());
             //   userActiveService(userData.getUserId());
                getActiveService(userData.getUserId(),userData.getuType());
            }, 6000);
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }

    private void userActiveService(String userId, String serviceId) {
        pDialog.setMessage("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        homeViewModel.getActiveService(userId).observe(Objects.requireNonNull(getActivity()), activeServiceResponse -> {
            pDialog.dismiss();
            if (activeServiceResponse.getStatus() != null){
                Log.e(TAG, "LoginResponseError" + new Gson().toJson(activeServiceResponse));
                List<ActiveServiceResponse.ActiveService> list = activeServiceResponse.getStatus();
                Log.e("ASSIGNED SERVICE", "" + list.size() + "Data= " +
                        new Gson().toJson(activeServiceResponse.getStatus()));
                checkUser(list,serviceId);
            } else {
                Log.e(TAG, "LoginResponseError" + activeServiceResponse.getStatus()) ;
                //Toast.makeText(Ho.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void userActiveServiceFastag(String userId) {
        pDialog.setMessage("Please wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        homeViewModel.getActiveService(userId).observe(Objects.requireNonNull(getActivity()), activeServiceResponse -> {
            pDialog.dismiss();
            if (activeServiceResponse.getStatus() != null){
                Log.e(TAG, "LoginResponseError" + new Gson().toJson(activeServiceResponse));
                List<ActiveServiceResponse.ActiveService> list = activeServiceResponse.getStatus();
                Log.e("ASSIGNED SERVICE", "" + list.size() + "Data= " +
                        new Gson().toJson(activeServiceResponse.getStatus()));
                checkUserFastag(userId,list);
            } else {
                Log.e(TAG, "LoginResponseError" + activeServiceResponse.getStatus()) ;
                //Toast.makeText(Ho.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserFastag(String userId, List<ActiveServiceResponse.ActiveService> list) {
         String id,status;
        for (int i=0;i<list.size();i++){
            id=  list.get(i).getMainId();
            status=  list.get(i).getMainStatus();
            if (id != null && id.equalsIgnoreCase("29")) {
                if (status.equalsIgnoreCase("A")){
                    if (userData.getuType().equalsIgnoreCase("CS")) {
                        handler.post(() -> checkUserFastag(userData.getMobile(), userData.getUserId(), userData.getToken(), "CS"));
                    } else {
                        Intent intent = new Intent(getActivity(), ActivityFastag.class);
                        intent.putExtra(Constant.FASTAG_STATUS,false);
                        intent.putExtra(Constant.USER_TYPE,userData.getuType());
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                } else {
                    Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "maintenance",
                            "This service is not available, we working on it!\nplease try after sometime.");
                }
                Log.e(TAG, "SERVICES--->" + id + "\n" + status);
            }/*else {
                Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "maintenance",
                        "This service is not available, we working on it!\nplease try after sometime.");
            }*/
        }

    }

    private void checkUser(List<ActiveServiceResponse.ActiveService> list, String serviceId) {
     /*   Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "maintenance",
                "This service is not available, we working on it!\nplease try after sometime.");*/
        String id,status;
        for (int i=0;i<list.size();i++){
            id=  list.get(i).getMainId();
            status=  list.get(i).getMainStatus();
            if (id != null && id.equalsIgnoreCase(serviceId)) {
                if (status.equalsIgnoreCase("A")){
                    Intent intent =new Intent(getActivity(), DmtPin.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.right_in,R.anim.left_out);
                    //  overridePendingTransition(R.anim.left_in,R.anim.right_out);
                } else {
                    Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "maintenance",
                            "This service is not available, we working on it!\nplease try after sometime.");
                }
                Log.e(TAG, "SERVICES--->" + id + "\n" + status);
            }
        }
    }

    private void customerAdapter() {
        MenuItemHomeAdapter adapter1 = new MenuItemHomeAdapter(getActivity(),billItem,billIcons);
        gridViewbillpayment.setAdapter(adapter1);
        gridViewbillpayment.setExpanded(true);
      //  gridViewbillpayment.setNestedScrollingEnabled(true);
        gridViewbillpayment.setOnItemClickListener((parent, view, position, id) -> {
            if (position==1){
                if (SplashActivity.getPreferences(Constant.CUSTOMER_PREPAID,"")
                        .equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Prepaid mobile service not" +
                            " available due to some system problem\n" +
                            "please wait untill we start it asap"+
                            ", Thanks");
                }else {
                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePrepaid");
                    WebService.getOperatorList("Prepaid", (FragmentActivity) getContext(),progressDialog);
                    WebService.getActiveService(userData.getUserId(), userData.getuType());
                }
            }else if (position==2){
                if (SplashActivity.getPreferences(Constant.CUSTOMER_DTH,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Postpaid mobile bill service not" +
                            " available due to some system problem\n" +
                            "please wait untill we start it asap"+
                            ", Thanks");
                }else {
                    WebService.getOperatorList("DTH", getActivity(),progressDialog);
                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"dth");
                    WebService.getActiveService(userData.getUserId(), userData.getuType());
                }
            }else if (position==3) {
                if (SplashActivity.getPreferences(Constant.CUSTOMER_GAS, "").equalsIgnoreCase("hide")) {
                    Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "error", "Prepaid mobile service not" +
                            " available due to some system problem\n" +
                            "please wait untill we start it asap" +
                            ", Thanks");
                } else {
                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE, "gas");
                    WebService.getOperatorList("Gas", (FragmentActivity) getContext(), progressDialog);
                    WebService.getActiveService(userData.getUserId(), userData.getuType());
                }
            }else if (position==4){

                userActiveServiceFastag(userData.getUserId());
               /* if (SplashActivity.getPreferences(Constant.CUSTOMER_FASTAG, "").equalsIgnoreCase("hide")) {
                    Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "error", "Prepaid mobile service not" +
                            " available due to some system problem\n" +
                            "please wait untill we start it asap" +
                            ", Thanks");
                } else {*/

                    /*SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE, "gas");
                    WebService.getOperatorList("Gas", (FragmentActivity) getContext(), progressDialog);
                    WebService.getActiveService(userData.getUserId(), userData.getuType());*/
               // }
            }else if (position==0){
                getSenderInfo();
            }
//            }else if (position==3){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_INSURANCE,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Postpaid mobile bill service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("Insurance",getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"insurance");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==4){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_WATER,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","DTH recharge service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("WaterBill", getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"water");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==5){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_LANDLINE,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Broadband recharge service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("Landline",getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"landline");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==6){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_CREDITCARD,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Electricity bill payment service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("CreditCard",getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"creditcard");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==7){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_BROADBAND,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Landline bill payment service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("Broadband",getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"broadband");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==8){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_ELECTRICITY,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Gas bill payment service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("Electricity", getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"electricity");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==9){
//                if (SplashActivity.getPreferences(Constant.CUSTOMER_POSTPAID,"").equalsIgnoreCase("hide")){
//                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error","Water bill payment service not" +
//                            " available due to some system problem\n" +
//                            "please wait untill we start it asap"+
//                            ", Thanks");
//                }else {
//                    WebService.getOperatorList("postpaid",getActivity(),progressDialog);
//                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePostpaid");
//                    WebService.getActiveService(userData.getUserId(), userData.getuType());
//                }
//            }else if (position==12){


        });

    }



    private void initCustomer() {
        String userType = userData.getuType();

        if (userType.equalsIgnoreCase("CS")) {

            wallet.setVisibility(View.GONE);
            gridViewBbps.setVisibility(View.GONE);

            cardViewCustomer.setVisibility(View.VISIBLE);
        } else {
            if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
                assignService(userData.getUserId());
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (!userData.getuType().equalsIgnoreCase("CS")){
                        assignService1(userData.getUserId());
                    }
                }, 6000);
            }else {
                Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"internetError",
                        "No Internet Connectivity");
            }
            cardViewCustomer.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NewApi")
    private void bbps() {
        MenuItemHomeAdapter adapter = new MenuItemHomeAdapter(getActivity(), nameItem, mThumbIds);
        gridViewBbps.setAdapter(adapter);
        gridViewBbps.setExpanded(true);
        gridViewBbps.setOnItemClickListener((adapterView, view, position, l) -> {
            UserData userData = PrefManager.getInstance(getContext()).getUserData();
             if (position == 0) {
//                getfingpayaeps(userData.getUserId());
//                if (validateRecharge(lat,lon)) {

                 Intent intent = new Intent(getActivity(),BankingSelect.class);
                 startActivity(intent);
//                    getPrimeAepsDetails(userData.getUserId(),lat,lon);
//                }
            }else if (position == 1) {
                try {
                    if (SplashActivity.getPreferences(Constant.Micro_atm, "").equalsIgnoreCase("hide")) {
                        Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "error",
                                "This service is not available, we working on it\nplease try after sometime");
                    } else {
                        microatm(userData.getUserId());
//                        Intent intent = new Intent(getContext(),Micro_Atm_Receipt.class);
//                        startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

             }else if (position == 2) {

                 Intent intent = new Intent(getActivity(), AadhaarPayMobile.class);
                 startActivity(intent);

             }else if (position == 3) {
                 Intent intent = new Intent(getActivity(), Mini_Statement.class);
                 startActivity(intent);
             }else if (position == 4) {
                 try {
                     if (SplashActivity.getPreferences(Constant.AEPS_2, "").equalsIgnoreCase("hide")) {
                         Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "error",
                                 "This service is not available, we working on it\nplease try after sometime");
                     } else {
                         getAeps2(userData.getUserId());
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             } else if (position == 5){
                 if (userData.getuType().equalsIgnoreCase("CS")) {
                     handler.post(() -> checkUserFastag(userData.getMobile(),userData.getUserId(),userData.getToken(), userData.getuType()));
                 } else {
                     userActiveServiceFastag(userData.getUserId());
                    /*Intent intent = new Intent(getActivity(), ActivityFastag.class);
                    intent.putExtra(Constant.FASTAG_STATUS,false);
                    intent.putExtra(Constant.USER_TYPE,userData.getuType());
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);*/
                 }
            } else if (position == 6) {
                if (SplashActivity.getPreferences(Constant.CUSTOMER_SERVICE, "").equalsIgnoreCase("hide")) {
                    Configuration.openPopupUpDown(getContext(), R.style.Dialod_UpDown, "error", "Prepaid mobile service not" +
                            " available due to some system problem\n" +
                            "please wait untill we start it asap" +
                            ", Thanks");
                } else {
                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE, "mobilePrepaid");
                    WebService.getOperatorList("Prepaid", getActivity(), progressDialog);
                    WebService.getActiveService(userData.getUserId(), userData.getuType());
                }
            } else if (position == 7) {
                if (SplashActivity.getPreferences(Constant.CUSTOMER_SERVICE, "").equalsIgnoreCase("hide")) {
                    Configuration.openPopupUpDown(getContext(), R.style.Dialod_UpDown, "error", "DTH recharge service not" +
                            " available due to some system problem\n" +
                            "please wait untill we start it asap" +
                            ", Thanks");
                } else {
                    WebService.getOperatorList("DTH", getActivity(), progressDialog);
                    SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE, "dth");
                    WebService.getActiveService(userData.getUserId(), userData.getuType());
                }

            } else if (position == 8) {
                service = "6";
                serviceName = "Mobile Postpaid";
                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else if (position == 9) {
                service = "4";
                serviceName = "DTH";
                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else if (position == 10) {
                service = "7";
                serviceName = "Broadband Postpaid";
                // txtTitle.setText(R.string.pay_broadband);
                Intent intent = new Intent(getActivity(), BbpsActivity.class);

                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            } else if (position == 11) {
                service = "8";
                serviceName = "Broadband Prepaid";
                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else if (position == 12) {
                service = "3";
                serviceName = "Electricity";
                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else if (position == 13) {
                service = "5";
                serviceName = "Landline Postpaid";

                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            } else if (position == 14) {
                service = "2";
                serviceName = "Gas Utility";

                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);

                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            } else if (position == 15) {
                service = "9";
                serviceName = "Water";

                Intent intent = new Intent(getActivity(), BbpsActivity.class);
                intent.putExtra("service", service);
                intent.putExtra("serviceName", serviceName);

                startActivity(intent);
                Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                //      }

            }else if (position == 16) {
                getSenderInfo();
            }
        });
    }
    private boolean validateRecharge(Double lat, Double lon) {
        if (lat==null||lon==null){
            gpsCheck();
            return false;
        }
        return true;
    }
    private void gpsCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getActivity())) {
            Log.e("TAG","Gps already enabled");
            enableLoc();
        }else{
            Log.e("TAG","Gps already enabled");
        }
    }
    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }
                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener((ConnectionResult connectionResult) -> {
                        Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                    }).build();

            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1000);
            locationRequest.setFastestInterval(1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback((LocationSettingsResult result1) -> {
                final Status status = result1.getStatus();
                if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    try {
                        status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                }
            });
        }
    }

    private void ReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.READ_PHONE_STATE)) {

            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSIONS);
        }

    }
    private void checkUserFastag(String mobile, String userId, String token, String userType) {
        String tag_string_req = "user_info";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FASTTAG_USERINFO, response -> {
            Log.d(VolleyLog.TAG, "user_info Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                Log.d(VolleyLog.TAG,"status:"+status);
                if  (status.equals("0")) {
                    //success
                    Intent intent = new Intent(getActivity(), ActivityFastag.class);
                    intent.putExtra(Constant.FASTAG_STATUS,true);
                    intent.putExtra(Constant.USER_TYPE,userType);
                    intent.putExtra(Constant.REMITTER_ID, jsonObject1.getJSONObject("remitter").getString("remitterId"));
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else if (status.equals("2")){
                    //doCustomerKyc();
                    Intent intent = new Intent(getActivity(), ActivityFastag.class);
                    intent.putExtra(Constant.FASTAG_STATUS,false);
                    intent.putExtra(Constant.USER_TYPE,userType);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else if (status.equals("3")){
                      fastagKYCSendOtp(mobile,userId,token,userType);
                } else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(VolleyLog.TAG, "user_info Error: " + error.getMessage());
            Toast.makeText(getActivity(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.REM_MOBILE, mobile);
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SOURCE,"APP");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void fastagKYCSendOtp(String mobile, String userId, String token, String userType) {
        String tag_string_req = "fastag_otp_kyc";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FASTTAG_OTP_KYC, response -> {
            Log.d(VolleyLog.TAG, "fastag_otp_kyc Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                Log.d(VolleyLog.TAG,"status:"+status);
                if  (status.equals("0")) {
                    //success
                    final Dialog dialg=new Dialog(Objects.requireNonNull(getActivity()));
                    dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialg.setContentView(R.layout.layout_otp_bene);
                    dialg.setCanceledOnTouchOutside(false);
                    dialg.setCancelable(false);

                    RelativeLayout imgClose =  dialg.findViewById(R.id.rl_close);
                    ImageView imgFastag = dialg.findViewById(R.id.img_fastag);
                    imgFastag.setVisibility(View.VISIBLE);
                    TextView txtResendOtp=dialg.findViewById(R.id.txt_resend_sender_otp_bene);
                    TextView txtMessage=dialg.findViewById(R.id.txt_otp_sender_bene);
                    final EditText editTextOtp=dialg.findViewById(R.id.edittext_sender_otp_bene);
                    final Button btnVerify=dialg.findViewById(R.id.btn_verify_bene);

                    txtResendOtp.setOnClickListener(v -> {
                        if (Configuration.hasNetworkConnection(getActivity())){
                            resendOtpFastTag(mobile,userId,token);
                        }
                    });
                    btnVerify.setOnClickListener(v -> {
                        String otp=editTextOtp.getText().toString().trim();
                        if (otp.isEmpty()){
                            Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                                    "Enter otp sent to your mobile no");
                        }else {
                            otpKycFastagVerify(userId,mobile,otp,token,dialg,userType);
                        }
                    });

                    imgClose.setOnClickListener(v -> dialg.dismiss());

                    dialg.show();
                    Window window = dialg.getWindow();
                    assert window != null;
                    window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(VolleyLog.TAG, "fastag_otp_kyc Error: " + error.getMessage());
            Toast.makeText(getActivity(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.REM_MOBILE, mobile);
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SOURCE,"APP");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void otpKycFastagVerify(String userId, String mobile, String otp, String token,
                                    Dialog dialg, String userType) {
        String tag_string_req = "fastag_verify_kyc";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FASTTAG_OTP_VERIFY, response -> {
            Log.d(VolleyLog.TAG, "fastag_verify_kyc Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                Log.d(VolleyLog.TAG,"status:"+status);
                if  (status.equals("0")) {
                    //success
                    dialg.dismiss();
                    Intent intent = new Intent(getActivity(), ActivityFastag.class);
                    intent.putExtra(Constant.FASTAG_STATUS,true);
                    intent.putExtra(Constant.USER_TYPE,userType);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

                } else {
                    SweetToast.error(getActivity(),message);
//                    Toast.makeText(getActivity(),
//                            message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(VolleyLog.TAG, "user_info Error: " + error.getMessage());
            Toast.makeText(getActivity(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.REM_MOBILE, mobile);
                params.put(Constant.USER_ID,userId);
                params.put(Constant.REM_OTP,otp);
                params.put(Constant.SOURCE,"APP");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void resendOtpFastTag(String mobile, String userId, String token) {
        String tag_string_req = "fastag_verify_kyc";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.FASTTAG_OTP_KYC, response -> {
            Log.d(VolleyLog.TAG, "fastag_verify_kyc Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                Log.d(VolleyLog.TAG,"status:"+status);
                if  (status.equals("0")) {
                    //success
                    SweetToast.success(getActivity(),"New Otp Sent to your mobile number");
                } else {
                    Toast.makeText(getActivity(),
                            message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(VolleyLog.TAG, "user_info Error: " + error.getMessage());
            Toast.makeText(getActivity(),
                    error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.REM_MOBILE, mobile);
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SOURCE,"APP");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        //Make sure it's our original READ_CONTACTS request
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 3 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor"})

    public void onClick(View v) {

        if (v == wallet) {
            if (TextUtils.isEmpty(accountNo) || TextUtils.isEmpty(bankName)) {
                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle("Final submit?")
                        .setMessage("You can not transfer balance to  Bank account as your account details are not available." +
                                "Please continue to transfer balance to main wallet otherwise cancel.")
                        .setNegativeButton(R.string.cancel, null)
                        .setPositiveButton(R.string.continu, (arg0, arg1) ->
                                openDialog("hide")).create().show();
            } else {
                getaepsstatus();
            }
        }

        if (v == transaction) {
            Intent intent = new Intent(getContext(), HistoryActivity.class);
            startActivity(intent);

            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == addmoney) {
            Intent intent = new Intent(getContext(), AddMoney.class);
            startActivityForResult(intent,1);

        }
        if (v == transfer) {
            if (userData.getuType().equalsIgnoreCase("CS")){

            } else {
                userActiveService(userData.getUserId(),"27");
            }
           /* try {
                if (SplashActivity.getPreferences(Constant.DMT_4, "").equalsIgnoreCase("hide")) {
                    Configuration.openPopupUpDown(getActivity(), R.style.Dialod_UpDown, "error",
                            "This service is not available, we working on it\nplease try after sometime");
                } else {
                    Intent intent = new Intent(getContext(), Dmt_Transfer.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }

    if(v==cardviewmicroatm){
            try{
                if (SplashActivity.getPreferences(Constant.Micro_atm,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                   microatm(userData.getUserId());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (v==cardViewAeps2){
            try{
                if (SplashActivity.getPreferences(Constant.AEPS_2,"").equalsIgnoreCase("hide")){
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",
                            "This service is not available, we working on it\nplease try after sometime");
                }else {
                  getAeps2(userData.getUserId());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if (v == cardViewMoney) {
            SplashActivity.savePreferences("money", "transfer");
            Intent intent = new Intent(getActivity(), Transfer.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }

        if (v == cardViewElectricity) {
            SplashActivity.savePreferences("recharge", "electricity");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewGas) {
            SplashActivity.savePreferences("recharge", "gas");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewLandline) {
            SplashActivity.savePreferences("recharge", "landline");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
        if (v == cardViewInsuarance) {
            SplashActivity.savePreferences("recharge", "insurance");
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }
    /*user state*/
    public  void getUserState(final String userId) {
        String tag_string_req = "login_res";

        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.AEPS_OUTLET, response -> {
            Log.d(TAG, "AEPS Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                //  Log.d(TAG, "Response: " + jarray.toString());

                Log.d(TAG,"status:"+status);
                if  (status.equals("1")) {

                          /*  Toast.makeText(getActivity(),
                                    message, Toast.LENGTH_LONG).show();*/
                    if (jsonObject1.has("data")){
                        JSONObject jsonObject=jsonObject1.getJSONObject("data");
                        phone=jsonObject.getString("phone1");

                    }
                    if (jsonObject1.has("authKey")){
                        JSONObject jObj=jsonObject1.getJSONObject("authKey");
                        roundMobile =jObj.getString("mobile");
                        roundPassword=jObj.getString("password");
                    }


                          /*  if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(Objects.requireNonNull(getActivity()))) {
                               // Toast.makeText(getActivity(),"Gps already enabled",Toast.LENGTH_SHORT).show();
                                enableLoc();
                            }else {*/
                    Log.e("AEPS","AEPS-->"+phone+" "+roundMobile+" "+roundPassword);
//                    Intent i = new Intent(getActivity(), CALL_AEPS.class);
//                    i.putExtra("MOBILE",phone);// user mobile no
//                    i.putExtra("MERCHANT_USERID",roundMobile);// Roundpay Mobile no
//                    i.putExtra("MERCHANT_PASSWORD",roundPassword);
//                    startActivityForResult(i, 1);
                    //  }
                }else {
                    // Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "AEPS Error: " + error.getMessage());
            //  Toast.makeText(getActivity(),error.getMessage(), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_LOCATION&&resultCode==RESULT_OK){
            if (gpsTracker.canGetLocation) {
                lat = gpsTracker.getLatitude();
                lon = gpsTracker.getLongitude();
                if (validateRecharge(lat,lon)){
                    getPrimeAepsDetails(userData.getUserId(),lat,lon);
                }
            }
        }
        try {
            if (requestCode == INTENT_CODE) {
                //assert data != null;
                if (resultCode == RESULT_OK) {
                    String respCode=data.getStringExtra("respcode");
                    if(respCode.equals("999")){
                         requesttxn = data.getStringExtra("requesttxn");//Type of transaction
                         refstan = data.getStringExtra("refstan");// Mahagram Stan Numbe
                         txnamount = data.getStringExtra("txnamount");//Transaction amount (0 in case of balanceinquiry and transaction amount in cash withdrawal)
                         mid = data.getStringExtra("mid");//Mid
                         tid = data.getStringExtra("tid");//Tid
                         clientrefid = data.getStringExtra("clientrefid");//Your reference Id
                         vendorid = data.getStringExtra("vendorid");//Your define value
                         udf1 = data.getStringExtra("udf1");//Your define value
                         udf2 = data.getStringExtra("udf2");//Your define value
                         udf3 = data.getStringExtra("udf3");//Your define value
                         udf4 = data.getStringExtra("udf4");//Your define value
                         date = data.getStringExtra("date");//Date of transaction

                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("requesttxn",data.getStringExtra("requesttxn"));
                            jsonObject1.put("refstan",data.getStringExtra("refstan"));
                            jsonObject1.put("date",data.getStringExtra("date"));
                            jsonObject1.put("mid",data.getStringExtra("mid"));
                            jsonObject1.put("tid",data.getStringExtra("tid"));
                            jsonObject1.put("clientrefid",data.getStringExtra("clientrefid"));
                            jsonObject1.put("vendorid",data.getStringExtra("vendorid"));
                            jsonObject1.put("udf1",data.getStringExtra("udf1"));
                            jsonObject1.put("udf2",data.getStringExtra("udf2"));
                            jsonObject1.put("udf3",data.getStringExtra("udf3"));
                            jsonObject1.put("udf4",data.getStringExtra("udf4"));
                            jsonObject1.put("txnamount",data.getStringExtra("txnamount"));
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
//                    Toast.makeText(getContext(), requesttxn+"\n"+bankremarks+"\n"+refstan+"\n"+cardno+"\n"+date+"\n"+amount+"\n"+invoicenumber+"\n"+mid+"\n"+tid+"\n"+clientrefid+"\n"+vendorid+"\n"+udf1+"\n"+udf2+"\n"+udf3+"\n"+udf4+"\n"+txnamount+"\n"+rrn+"\n"+respcode, Toast.LENGTH_LONG).show();
                        getErrorStatus(userData.getUserId(),jsonObject1);

                        Intent intent= new Intent(getContext(), Micro_Atm_Receipt_P.class);
                        intent.putExtra("BcName",BcName);
                        intent.putExtra("BcId",BcId);
                        intent.putExtra("BcLocation",BcLocation);
                        intent.putExtra("refstan",refstan);
                        intent.putExtra("date",date);
                        intent.putExtra("amount",txnamount);
                        intent.putExtra("mid",mid);
                        intent.putExtra("tid",tid);
                        intent.putExtra("clientrefid",clientrefid);
                        intent.putExtra("txnamount",txnamount);
                        startActivityForResult(intent,1);

                    }else{
                         requesttxn = data.getStringExtra("requesttxn");//Type of transaction
                         bankremarks = data.getStringExtra("msg");//Bank message
                         refstan = data.getStringExtra("refstan");// Mahagram Stan Number
                         cardno = data.getStringExtra("cardno");//Atm card number
                         date = data.getStringExtra("date");//Date of transaction
                         amount = data.getStringExtra("amount");//Account Balance
                         invoicenumber = data.getStringExtra("invoicenumber");//Invoice Number
                         mid = data.getStringExtra("mid");//Mid
                         tid = data.getStringExtra("tid");//Tid
                         clientrefid = data.getStringExtra("clientrefid");//Your reference Id
                         vendorid = data.getStringExtra("vendorid");//Your define value
                         udf1 = data.getStringExtra("udf1");//Your define value
                         udf2 = data.getStringExtra("udf2");//Your define value
                         udf3 = data.getStringExtra("udf3");//Your define value
                         udf4 = data.getStringExtra("udf4");//Your define value
                         txnamount = data.getStringExtra("txnamount");//Transaction amount (0 in case of balanceinquiry and transaction amount in cash withdrawal)
                         rrn = data.getStringExtra("rrn");//Bank RRN number
//                    }
//                    assert data != null;
//                    String requesttxn = data.getStringExtra("requesttxn");//Type of transaction
//                    String bankremarks = data.getStringExtra("msg");//Bank message
//                    String refstan = data.getStringExtra("refstan");// Mahagram Stan Number
//                    String cardno = data.getStringExtra("cardno");//Atm card number
//                    String date = data.getStringExtra("date");//Date of transaction
//                    String amount = data.getStringExtra("amount");//Account Balance
//                    String invoicenumber = data.getStringExtra("invoicenumber");//Invoice Number
//                    String mid = data.getStringExtra("mid");//Mid
//                    String tid = data.getStringExtra("tid");//Tid
//                    String clientrefid = data.getStringExtra("clientrefid");//Your reference Id
//                    String vendorid = data.getStringExtra("vendorid");//Your define value
//                    String udf1 = data.getStringExtra("udf1");//Your define value
//                    String udf2 = data.getStringExtra("udf2");//Your define value
//                    String udf3 = data.getStringExtra("udf3");//Your define value
//                    String udf4 = data.getStringExtra("udf4");//Your define value
//                    String txnamount = data.getStringExtra("txnamount");//Transaction amount (0 in case of balance inquiry and transaction amount in cash withdrawal)
//                    String rrn = data.getStringExtra("rrn");//Bank RRN number
//                    String respcode = data.getStringExtra("respcode ");//Response code of bank( “00” for success transaction)
//                    data.getStringExtra("StatusCode");
//                    data.getStringExtra("Message");
//                    Log.e(TAG,"ok code"+ data.getStringExtra("StatusCode")+" "+data.getStringExtra("Message"));
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("requesttxn",data.getStringExtra("requesttxn"));
                        jsonObject.put("bankremarks",data.getStringExtra("msg"));
                        jsonObject.put("refstan",data.getStringExtra("refstan"));
                        jsonObject.put("cardno",data.getStringExtra("cardno"));
                        jsonObject.put("date",data.getStringExtra("date"));
                        jsonObject.put("amount",data.getStringExtra("amount"));
                        jsonObject.put("invoicenumber",data.getStringExtra("invoicenumber"));
                        jsonObject.put("mid",data.getStringExtra("mid"));
                        jsonObject.put("tid",data.getStringExtra("tid"));
                        jsonObject.put("clientrefid",data.getStringExtra("clientrefid"));
                        jsonObject.put("vendorid",data.getStringExtra("vendorid"));
                        jsonObject.put("udf1",data.getStringExtra("udf1"));
                        jsonObject.put("udf2",data.getStringExtra("udf2"));
                        jsonObject.put("udf3",data.getStringExtra("udf3"));
                        jsonObject.put("udf4",data.getStringExtra("udf4"));
                        jsonObject.put("txnamount",data.getStringExtra("txnamount"));
                        jsonObject.put("rrn",data.getStringExtra("rrn"));
                        jsonObject.put("respcode",data.getStringExtra("respcode"));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
//                    Toast.makeText(getContext(), requesttxn+"\n"+bankremarks+"\n"+refstan+"\n"+cardno+"\n"+date+"\n"+amount+"\n"+invoicenumber+"\n"+mid+"\n"+tid+"\n"+clientrefid+"\n"+vendorid+"\n"+udf1+"\n"+udf2+"\n"+udf3+"\n"+udf4+"\n"+txnamount+"\n"+rrn+"\n"+respcode, Toast.LENGTH_LONG).show();
                    getErrorStatus(userData.getUserId(),jsonObject);

                        Intent intent = new Intent(getContext(), Micro_Atm_Receipt.class);
                        intent.putExtra("BcName",BcName);
                        intent.putExtra("BcId",BcId);
                        intent.putExtra("BcLocation",BcLocation);
                        intent.putExtra("bankremarks",bankremarks);
                        intent.putExtra("refstan",refstan);
                        intent.putExtra("cardno",cardno);
                        intent.putExtra("date",date);
                        intent.putExtra("amount",amount);
                        intent.putExtra("invoicenumber",invoicenumber);
                        intent.putExtra("mid",mid);
                        intent.putExtra("tid",tid);
                        intent.putExtra("clientrefid",clientrefid);
                        intent.putExtra("txnamount",txnamount);
                        intent.putExtra("rrn",rrn);
                        startActivityForResult(intent,1);

                    }
                }else{
                    assert data != null;
                    data.getStringExtra("statuscode");
                    data.getStringExtra("message");
                    Log.e(TAG,"data code :"+ data.getStringExtra("statuscode")+"\n"+data.getStringExtra("message"));
                    if (data.getStringExtra("statuscode").equals("111")) {
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=org.egram.microatm")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        new util().snackBar(getView(), "" + data.getStringExtra("message"), SnackBarBackGroundColor);
                    }
                }
            }
            if (requestCode == 1) {
                if (resultCode == 0) {
                    Log.e("Message", " data0 " + requestCode + " " + resultCode + " " + data.getStringExtra("Message"));
                }
//                if (resultCode == 3) {
//                    try {
//                        String bankRrn = data.getStringExtra("bankRrn");
//                        int type = data.getIntExtra("TransactionType", 0); //to get transaction name
//                        String transAmount = data.getStringExtra("transAmount"); //to get response
//                        String balAmount = data.getStringExtra("balAmount");
//                        boolean status = data.getBooleanExtra("Status", false); //to get response message
//                        String response = data.getStringExtra("Message"); //to get response message
//                        Log.e("totalRes", " " + response);
//                        Log.e("totalRESPONSE", " " + data);
//
//                        try {
//                            switch (type) {
//
//                                case Constants.CASH_WITHDRAWAL:
//                                    transType = "Cash Withdrawal";
//                                    break;
//
//                                case Constants.BALANCE_ENQUIRY:
//                                    transType = "Balance Enquiry";
//                                    break;
//
//                                default:
//                                    break;
//                            }
//                            if (response != null) {
//                                // Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
//
//
//                                if (type == Constants.BALANCE_ENQUIRY) {
//                                  /*  Toast.makeText(getActivity(), "transType :" + transType + "\n"
//                                            +"Bank RNN Number :" + bankRrn + "\n"
//                                            +"Bank RNN Number :" + bankRrn + "\n"
//                                            +"Status :" + status + "\n"
//                                            +"balAmount :" + balAmount + "\n", Toast.LENGTH_LONG).show();*/
//                                    Intent intent =new Intent(getActivity(),AspsTransfer.class);
//                                    intent.putExtra(Constant.MESSAGE,response);
//                                    intent.putExtra(Constant.BANK_RRN,bankRrn);
//                                    intent.putExtra(Constant.TRANS_TYPE,transType);
//                                    intent.putExtra(Constant.AMOUNT,balAmount);
//                                    intent.putExtra(Constant.STATUS,status);
//                                    startActivity(intent);
//                                    Log.e("totalRes", " " + response+" "+ status);
//                                }
//                                if (type == Constants.CASH_WITHDRAWAL) {
//                                   /* Toast.makeText(getActivity(),"transType :" + transType + "\n"
//                                            + "Bank RNN Number :" + bankRrn + "\n"
//                                            +"Bank RNN Number :" + bankRrn + "\n"
//                                            +"Status :" + status + "\n"
//                                            +"transAmount :" + transAmount + "\n", Toast.LENGTH_LONG).show();*/
//                                    Intent intent =new Intent(getActivity(),AspsTransfer.class);
//                                    intent.putExtra(Constant.MESSAGE,response);
//                                    intent.putExtra(Constant.BANK_RRN,bankRrn);
//                                    intent.putExtra(Constant.TRANS_TYPE,transType);
//                                    intent.putExtra(Constant.AMOUNT,transAmount);
//                                    intent.putExtra(Constant.STATUS,status);
//                                    startActivity(intent);
//                                    Log.e("totalRes", " " + response+" "+ status);
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getActiveService(final String userId, final String userType) {
        String tag_string_req = "active_service";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ACTIVE_SERVICE, response -> {
           // Log.e(TAG, "Service Response: " + response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    JSONArray jsonArray=jsonObject.getJSONArray("status");
                    if (!jsonArray.isNull(0)){
                        for (int i = 0; i< jsonArray.length(); i++) {
                            JSONObject jobject = jsonArray.getJSONObject(i);

                            String id = jobject.getString("subServiceId");
                            String status = jobject.getString("subServicestatus");
                            String mainStatus = jobject.getString("mainStatus");
                            String mainId = jobject.getString("mainId");
                            //  String mainId = jobject.getString("mainId");

                            if (userType.equalsIgnoreCase("CS")) {
                                if (id.equalsIgnoreCase("19")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("20")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("21")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("22")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("23")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("24")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("25")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("26")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("28")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "unhide");
                                    }
                                }
                                if (id.equalsIgnoreCase("27")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "unhide");
                                    }
                                }
                                if (mainId.equalsIgnoreCase("29")){
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_FASTAG, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_FASTAG, "unhide");
                                    }
                                }
                            } else {
                                if (mainId.equalsIgnoreCase("19")) {
                                    if (!mainStatus.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.AEPS_2, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.AEPS_2, "unhide");
                                    }
//                                } else if (mainId.equalsIgnoreCase("13")) {
//                                    if (!mainStatus.equalsIgnoreCase("A")) {
//                                        SplashActivity.savePreferences(Constant.AEPS_1, "hide");
//                                    } else {
//                                        SplashActivity.savePreferences(Constant.AEPS_1, "unhide");
//                                    }
//                                } else if (mainId.equalsIgnoreCase("17")) {
//                                    if (!mainStatus.equalsIgnoreCase("A")) {
//                                        SplashActivity.savePreferences(Constant.BBPS, "hide");
//                                    } else {
//                                        SplashActivity.savePreferences(Constant.BBPS, "unhide");
//                                    }
//                                } else if (mainId.equalsIgnoreCase("18")) {
//                                    if (!mainStatus.equalsIgnoreCase("A")) {
//                                        SplashActivity.savePreferences(Constant.DMT_2, "hide");
//                                    } else {
//                                        SplashActivity.savePreferences(Constant.DMT_2, "unhide");
//                                    }
//                                } else if (mainId.equalsIgnoreCase("20")) {
//                                    if (!mainStatus.equalsIgnoreCase("A")) {
//                                        SplashActivity.savePreferences(Constant.RECHARGE_THREE, "hide");
//                                    } else {
//                                        SplashActivity.savePreferences(Constant.RECHARGE_THREE, "unhide");
//                                    }
//                                } else if (mainId.equalsIgnoreCase("9")) {
//                                    if (!mainStatus.equalsIgnoreCase("A")) {
//                                        Log.d("tag", "else ");
//                                        SplashActivity.savePreferences(Constant.RECHARGE_TWO, "hide");
//                                    } else {
//                                        Log.d("tag", "9serviceIdmain");
//                                        SplashActivity.savePreferences(Constant.RECHARGE_TWO, "unhide");
//                                    }
                                } else if (id.equalsIgnoreCase("19")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_PREPAID, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("20")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_DTH, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("21")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_POSTPAID, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("22")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_LANDLINE, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("23")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_BROADBAND, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("24")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_ELECTRICITY, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("25")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_GAS, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("26")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_WATER, "unhide");
                                    }
                                } else if (id.equalsIgnoreCase("28")) {
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_INSURANCE, "unhide");
                                    }
//                                } else if (id.equalsIgnoreCase("27")) {
//                                    if (!status.equalsIgnoreCase("A")) {
//                                        SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "hide");
//                                    } else {
//                                        SplashActivity.savePreferences(Constant.CUSTOMER_CREDITCARD, "unhide");
//                                    }
                                } else if (mainId.equalsIgnoreCase("24")) {
                                    Log.d("tag", "24statusmain" + mainStatus);
                                    if (!mainStatus.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.DMT_4, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.DMT_4, "unhide");
                                    }
                                }else if (mainId.equalsIgnoreCase("29")){
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_FASTAG, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.CUSTOMER_FASTAG, "unhide");
                                    }
                                }else if (mainId.equalsIgnoreCase("27")){
                                    if (!status.equalsIgnoreCase("A")) {
                                        SplashActivity.savePreferences(Constant.INSTANT_DMT, "hide");
                                    } else {
                                        SplashActivity.savePreferences(Constant.INSTANT_DMT, "unhide");
                                    }
                                }

                            }

                                   /* if (id.equalsIgnoreCase("1")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.PREPAID,status);
                                        if (!status.equalsIgnoreCase("A")){
                                            SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"hide");
                                        }else {
                                            SplashActivity.savePreferences(Constant.MOBILE_PREPAID,"unhide");
                                        }
                                    }
                                    if (id.equalsIgnoreCase("2")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.DTH,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewDth.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewDth.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("3")){
                                        SplashActivity.savePreferences(Constant.DATACARD,status);
                                       // Log.e("STATUS","STATUS-->"+status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewDatacard.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewDatacard.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("4")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.POSTPAID,status);
                                        if (!status.equalsIgnoreCase("A")){
                                            SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"hide");
                                        }else {
                                            SplashActivity.savePreferences(Constant.MOBILE_POSTPAID,"unhide");
                                        }
                                    }
                                    if (id.equalsIgnoreCase("5")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.ELECTRICITY,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewElectricity.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewElectricity.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("6")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.GAS,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewGas.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewGas.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("7")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.LANDLINE,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewLandline.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewLandline.setVisibility(View.GONE);
                                        }
                                    }
                                    if (id.equalsIgnoreCase("8")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.INSURANCE,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewInsuarance.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewInsuarance.setVisibility(View.GONE);
                                        }
                                    }
                                    if (mainId.equalsIgnoreCase("13")){
                                       // Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.AEPS,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewAeps.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewAeps.setVisibility(View.GONE);
                                        }
                                    }
                                    if (mainId.equalsIgnoreCase("14")){
                                      //  Log.e("STATUS","STATUS-->"+status);
                                        SplashActivity.savePreferences(Constant.MONEY,status);
                                        if (status.equalsIgnoreCase("A")){
                                            cardViewMoney.setVisibility(View.VISIBLE);
                                        }else {
                                            cardViewMoney.setVisibility(View.GONE);
                                        }
                                    }
                                    if (SplashActivity.getPreferences(Constant.MOBILE_PREPAID,"").equals("hide")&&
                                            SplashActivity.getPreferences(Constant.MOBILE_POSTPAID,"").equals("hide")){
                                        cardViewMobile.setVisibility(View.GONE);
                                    }else {
                                        cardViewMobile.setVisibility(View.VISIBLE);
                                    }
                                }*/
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e(TAG, "Active Service Error: " + error.getMessage())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*Micro atm*/
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void microatm(final String userId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "aeps_2";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.MICRO_ATM, response -> {
            Log.d(TAG, "Micro atm " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        UserData userData = PrefManager.getInstance(getActivity()).getUserData();
                        JSONObject jObj = jsonObject.getJSONObject("data");
                        JSONObject authObject = jsonObject.getJSONObject("authKey");

                        BcName=jObj.getString("bc_name");
                        BcId=jObj.getString("bc_id");
                        BcLocation=jObj.getString("bc_location");
//                        Toast.makeText(getContext(),BcName+"\n"+BcId+"\n"+BcLocation, Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "msg" +
                                authObject.getString("saltkey") + " " +
                                authObject.getString("secretkey") + " " +
                                jObj.getString("bc_id") + " " +
                                "ZAM"+jObj.getString("id") + " " +
                                jObj.getString("emailid") + " " +
                                jObj.getString("phone1") + " "+
                                jObj.getString("bc_name") + " "+
                                jObj.getString("bc_location") + " "+
                                authObject.getString("refId"));

                        PackageManager packageManager = getContext().getPackageManager();
                        if (isPackageInstalled("org.egram.microatm", packageManager)) {
                            Intent intent = new Intent();
                            intent.setComponent(new
                                    ComponentName("org.egram.microatm", "org.egram.microatm.BluetoothMacSearchActivity"));
                            intent.putExtra("saltkey", authObject.getString("saltkey"));
                            intent.putExtra("secretkey", authObject.getString("secretkey"));
                            intent.putExtra("bcid",  jObj.getString("bc_id"));
                            intent.putExtra("userid", "ZAM"+jObj.getString("id"));
                            intent.putExtra("bcemailid", jObj.getString("emailid"));
                            intent.putExtra("phone1", jObj.getString("phone1"));
                            intent.putExtra("clientrefid", authObject.getString("refId"));
                            intent.putExtra("vendorid", "");
                            intent.putExtra("udf1", "");
                            intent.putExtra("udf2", "");
                            intent.putExtra("udf3", "");
                            intent.putExtra("udf4", "");
                            startActivityForResult(intent, INTENT_CODE);
                        } else {
                            final android.app.AlertDialog.Builder alertDialog =
                                    new android.app.AlertDialog.Builder(getContext());
                            alertDialog.setTitle("Get Service");
                            alertDialog.setMessage("MicroATM Service not installed. Click OK to download .");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=org.egram.microatm")));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }


//                        Intent intent = new Intent(getContext(), BluetoothMacSearchActivity.class);
//                        intent.putExtra("saltKey", authObject.getString("saltkey"));
//                        intent.putExtra("secretKey", authObject.getString("secretkey"));
//                        intent.putExtra("BcId",  jObj.getString("bc_id"));
//                        intent.putExtra("UserId", userData.getUid().replace("ZAM",""));
//                        intent.putExtra("bcEmailId", jObj.getString("emailid"));
//                        intent.putExtra("Phone1", jObj.getString("phone1"));
//                        intent.putExtra("cpid","");//(If any)
//
//                        startActivityForResult(intent, INTENT_CODE);

                               /* Intent intent = new Intent(getActivity(),DashboardActivity.class);
                                intent.putExtra("saltKey", authObject.getString("saltkey"));
                                intent.putExtra("secretKey", authObject.getString("secretkey"));
                                intent.putExtra("BcId", jObj.getString("bc_id"));
                                intent.putExtra("UserId", userData.getUid());
                                intent.putExtra("bcEmailId", jObj.getString("emailid"));
                                intent.putExtra("Phone1", jObj.getString("phone1"));
                                intent.putExtra("cpid", "Your CP ID");//(If any)
                                startActivityForResult(intent, INTENT_CODE);*/


                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "AEPS Error: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void assignService(final String userId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "assigned_service";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ASSIGNED_SERVICE, response -> {
           // Log.d(TAG, "assigned_service Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("S")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        if (!jsonArray.isNull(0)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);

                                String id = jobject.getString("id");
                                String serviceSatus = jobject.getString("status");
//                                if (id.equalsIgnoreCase("20")){
//                                    SplashActivity.savePreferences(Constant.RECHARGE_3,status);
////                                    if (serviceSatus.equalsIgnoreCase("A")){
////                                        Log.d(TAG,"9");
////                                        cardViewRechargeThree.setVisibility(View.VISIBLE);
////                                    }else {
////                                        Log.d(TAG,"no service");
////                                        cardViewRechargeThree.setVisibility(View.GONE);
////                                    }
//                                }
//                                if (id.equalsIgnoreCase("9")) {
//                                    SplashActivity.savePreferences(Constant.DMT_2, status);
//                                    if (serviceSatus.equalsIgnoreCase("A")) {
//                                        cardViewMoneytransferTwo.setVisibility(View.VISIBLE);
//                                    } else {
//                                        cardViewMoneytransferTwo.setVisibility(View.GONE);
//                                    }
//                                }
//                                }if (id.equalsIgnoreCase("18")){
//                                    SplashActivity.savePreferences(Constant.RECHARGE_2,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewRechargeTwo.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewRechargeTwo.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("13")){
//                                    SplashActivity.savePreferences(Constant.AEPS,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewAeps.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewAeps.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("14")){
//                                    SplashActivity.savePreferences(Constant.MONEY,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewMoney.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewMoney.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("19")){
//                                    SplashActivity.savePreferences(Constant.AEPS_2,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewAeps2.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewAeps2.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("17")){
//                                    SplashActivity.savePreferences(Constant.BBPS,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewBbps.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewBbps.setVisibility(View.GONE);
//                                    }
//                                }
                            }
                        }

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "assigned_service Error: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void assignService1(String userId) {
        String tag_string_req = "assigned_service";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ASSIGNED_SERVICE, response -> {
          //  Log.d(TAG, "assigned_service Response: " + response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("S")) {
                        JSONArray jsonArray=jsonObject.getJSONArray("data");
                        if (!jsonArray.isNull(0)) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobject = jsonArray.getJSONObject(i);

                                String id = jobject.getString("id");
                                String serviceSatus = jobject.getString("status");
//                                if (id.equalsIgnoreCase("20")){
////                                    SplashActivity.savePreferences(Constant.RECHARGE_3,status);
////                                    if (serviceSatus.equalsIgnoreCase("A")){
////                                        cardViewRechargeThree.setVisibility(View.VISIBLE);
////                                    }else {
////                                        cardViewRechargeThree.setVisibility(View.GONE);
////                                    }
//                                }
//                                if (id.equalsIgnoreCase("18")){
//                                    SplashActivity.savePreferences(Constant.DMT_2,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewMoneytransferTwo.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewMoneytransferTwo.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("13")){
//                                    SplashActivity.savePreferences(Constant.AEPS,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewAeps.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewAeps.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("14")){
//                                    SplashActivity.savePreferences(Constant.MONEY,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewMoney.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewMoney.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("19")){
//                                    SplashActivity.savePreferences(Constant.AEPS_2,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewAeps2.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewAeps2.setVisibility(View.GONE);
//                                    }
//                                }
//                                if (id.equalsIgnoreCase("17")){
//                                    SplashActivity.savePreferences(Constant.BBPS,status);
//                                    if (serviceSatus.equalsIgnoreCase("A")){
//                                        cardViewBbps.setVisibility(View.VISIBLE);
//                                    }else {
//                                        cardViewBbps.setVisibility(View.GONE);
//                                    }
//                                }
                            }
                        }

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //  progressDialog.dismiss();
            Log.e(TAG, "assigned_service Error: " + error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getAeps2(final String userId) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "aeps_2";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.AEPS_2_SERVICE, response -> {
            Log.d(TAG, "AEPS Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        UserData userData=PrefManager.getInstance(getActivity()).getUserData();
                        JSONObject jObj = jsonObject.getJSONObject("data");
                        JSONObject authObject = jsonObject.getJSONObject("authKey");

                        Log.e(TAG,""+
                                authObject.getString("saltkey")+" "+
                                authObject.getString("secretkey")+" "+
                                jObj.getString("bc_id")+" "+
                                userId+" "+
                                jObj.getString("emailid")+" "+
                                jObj.getString("phone1"));

                        Intent intent = new Intent(getContext(), DashboardActivity.class);
                        intent.putExtra("saltKey", authObject.getString("saltkey"));
                        intent.putExtra("secretKey", authObject.getString("secretkey"));
                        intent.putExtra("BcId",  jObj.getString("bc_id"));
                        intent.putExtra("UserId", userData.getUid().replace("ZAM",""));
                        intent.putExtra("bcEmailId", jObj.getString("emailid"));
                        intent.putExtra("Phone1", jObj.getString("phone1"));
                        intent.putExtra("cpid","Your CP ID");//(If any)
                        startActivityForResult(intent, INTENT_CODE);

                               /* Intent intent = new Intent(getActivity(),DashboardActivity.class);
                                intent.putExtra("saltKey", authObject.getString("saltkey"));
                                intent.putExtra("secretKey", authObject.getString("secretkey"));
                                intent.putExtra("BcId", jObj.getString("bc_id"));
                                intent.putExtra("UserId", userData.getUid());
                                intent.putExtra("bcEmailId", jObj.getString("emailid"));
                                intent.putExtra("Phone1", jObj.getString("phone1"));
                                intent.putExtra("cpid", "Your CP ID");//(If any)
                                startActivityForResult(intent, INTENT_CODE);*/

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "AEPS Error: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*ERROR STATUS*/
    public void getErrorStatus(String userId, JSONObject data)
    {
        String tag_string_req = "sender_error";
        Log.d(TAG, "getErrorStatus: "+data);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        try{
            Configuration.hideKeyboardFrom(getActivity());
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.MICROATM, response -> {
            Log.d(VolleyLog.TAG, "RESPONSE " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                String status=jsonObject.getString("status");
                String message=jsonObject.getString("message");
                if(status.equals("0")){
//                    Intent intent=new Intent(getContext(), Micro_Atm_Receipt.class);
//                    intent.putExtra("BcName",BcName);
//                    intent.putExtra("BcId",BcId);
//                    intent.putExtra("BcLocation",BcLocation);
//                    intent.putExtra("bankremarks",bankremarks);
//                    intent.putExtra("refstan",refstan);
//                    intent.putExtra("cardno",cardno);
//                    intent.putExtra("date",date);
//                    intent.putExtra("amount",amount);
//                    intent.putExtra("invoicenumber",invoicenumber);
//                    intent.putExtra("mid",mid);
//                    intent.putExtra("tid",tid);
//                    intent.putExtra("clientrefid",clientrefid);
//                    intent.putExtra("txnamount",txnamount);
//                    intent.putExtra("rrn",rrn);
//                    startActivityForResult(intent,1);
                }else if(status.equals("2")){
//                    Intent intent=new Intent(getContext(), Micro_Atm_Receipt_P.class);
//                    intent.putExtra("BcName",BcName);
//                    intent.putExtra("BcId",BcId);
//                    intent.putExtra("BcLocation",BcLocation);
//                    intent.putExtra("refstan",refstan);
//                    intent.putExtra("date",date);
//                    intent.putExtra("amount",txnamount);
//                    intent.putExtra("mid",mid);
//                    intent.putExtra("tid",tid);
//                    intent.putExtra("clientrefid",clientrefid);
//                    intent.putExtra("txnamount",txnamount);
//                    startActivityForResult(intent,1);
                }else {
//                    Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("TAG","RESPONES"+response);

        }, error -> {
            Log.e(VolleyLog.TAG, "SENDER INFO Error: " + error.getMessage());
           /* Toast.makeText(Dmt_Transfer.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*/
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.DATA, String.valueOf(data));

             //   Log.e(VolleyLog.TAG,"SENDR INF--->"+Constant.SENDER_MOBILE+"=="+mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void getaepsstatus() {
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Res> call=apiinterface.getaepsstatus(userData.getUserId());
        call.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(@NotNull Call<Res> call, @NotNull Response<Res> response) {
                openDialog(response.body().getAepsStatus());
               //redeemBalance(mainWallet,bankAccount, Dialog dialog, String userId)
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void openDialog(String type) {
        UserData userData = PrefManager.getInstance(getContext()).getUserData();
        final Dialog dialog=new Dialog(Objects.requireNonNull(getContext()));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_redeem);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        LinearLayout layoutoption = dialog.findViewById(R.id.layoutOption);
        LinearLayout selectoption = dialog.findViewById(R.id.selectoption);
        Button towallet = dialog.findViewById(R.id.to_main_wallet);
        Button tobank = dialog.findViewById(R.id.to_bank);
        ImageView cancel = dialog.findViewById(R.id.cancel_image);
        txtAmountAeps=dialog.findViewById(R.id.txt_amount_aeps);
        TextView txtAccNumber=dialog.findViewById(R.id.txt_account_number);
        TextView txtBankName=dialog.findViewById(R.id.txt_bank_name);
        LinearLayout lnAccountNumber=dialog.findViewById(R.id.ln_account_number);
        LinearLayout lnBankName=dialog.findViewById(R.id.ln_bank_name);
        LinearLayout lnMainWallet=dialog.findViewById(R.id.ln_main_wallet);
        LinearLayout lnBankAccount=dialog.findViewById(R.id.ln_bank_account);
        EditText editTextMainWallet=dialog.findViewById(R.id.edittext_wallet_amount);
        EditText editTextBankAccount=dialog.findViewById(R.id.edittext_bank_amount);
        Button btnContinue=dialog.findViewById(R.id.btn_continue_redeem);
        Button btnCancel=dialog.findViewById(R.id.btn_no_redeem);
        cancel.setOnClickListener(v -> dialog.dismiss());
        towallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutoption.setVisibility(View.VISIBLE);
                selectoption.setVisibility(View.GONE);
                lnAccountNumber.setVisibility(View.GONE);
                lnBankName.setVisibility(View.GONE);
                editTextBankAccount.setText("0");
                lnMainWallet.setVisibility(View.VISIBLE);
            }
        });
        tobank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutoption.setVisibility(View.VISIBLE);
                selectoption.setVisibility(View.GONE);
                editTextMainWallet.setText("0");
                lnBankAccount.setVisibility(View.VISIBLE);
            }
        });
        txtAmountAeps.setText(": ₹"+amountAeps);
        txtAccNumber.setText(": "+accountNo);
        txtBankName.setText(": "+bankName);

       // Toast.makeText(getContext(), ""+type, Toast.LENGTH_SHORT).show();
        if (type.equals("Block")){
           tobank.setVisibility(View.GONE);
        }else if(type.equals("Active")){
            tobank.setVisibility(View.VISIBLE);
        }else{
////            lnBankAccount.setVisibility(View.GONE);
//            txtAccNumber.setText(": "+accountNo);
//            txtBankName.setText(": "+bankName);
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnContinue.setOnClickListener(v -> {

            String mainWallet=editTextMainWallet.getText().toString();
            String bankAccount=editTextBankAccount.getText().toString();
//            Toast.makeText(getContext(), editTextMainWallet.getText().toString()+"\n"+editTextBankAccount.getText().toString(), Toast.LENGTH_SHORT).show();
            if (editTextMainWallet.getText().toString().isEmpty()){
                editTextMainWallet.setError("Enter Amount");
                editTextMainWallet.requestFocus();
            }else if (Configuration.hasNetworkConnection(getContext())){
                redeemBalance(editTextMainWallet.getText().toString(),editTextBankAccount.getText().toString(),dialog,userData.getUserId());
            }else {
                Configuration.openPopupUpDown(getContext(), R.style.Dialod_UpDown,
                        "internetError", "No Internet Conectivity");
            }

        });

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void redeemBalance(String mainWallet, String bankAccount, Dialog dialog, String userId) {
        String tag_string_req = "redeem_balance";
        pDialog.setMessage("Please wait...");
        pDialog.show();
        try{
            Configuration.hideKeyboardFrom(getActivity());
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d(VolleyLog.TAG, "Redeem Response: " +mainWallet+" "+userId);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.REDEEM_BALANCE_AEPS, response -> {
            Log.d(VolleyLog.TAG, "Redeem Response: " + response+" "+bankAccount+" "+mainWallet+" "+userId);
            pDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                if  (status.equals("1")) {
                    dialog.dismiss();
                    openPopup(jsonObject1.getString("message"),"S");
                }else {
                    Configuration.openPopupUpDown(getContext(), R.style.Dialod_UpDown,
                            "error", jsonObject1.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(VolleyLog.TAG, "Redeem Error: " + error.getMessage());
            pDialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.TO_MAIN,mainWallet);
                params.put(Constant.TO_BANK,bankAccount);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void openPopup(String message, final String type) {
        final Dialog dialg=new Dialog(Objects.requireNonNull(getContext()));
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_recharge);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=dialg.findViewById(R.id.txt_status);
        if (type.equalsIgnoreCase("S")){
            imageView.setImageResource(R.drawable.success);
            txtStatus.setText("Status : Success");
            MediaPlayer  mediaPlayer = MediaPlayer.create(getContext(), R.raw.speech);
            mediaPlayer.start();
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
            Intent intent=new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }
/*
    private void getProfileData(String userId) {

        String tag_string_req = "register_res";

        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, response -> {
         //   Log.d(VolleyLog.TAG, "Profile Response1: " + response);

            try {
                JSONObject jsonObject=new JSONObject(response);
                String status=jsonObject.getString("status");
                String message=jsonObject.getString("message");
                if (status.equals("1")){
                    JSONObject jObj=jsonObject.getJSONObject("data");
                    wallet.setText("Redeem Aeps Wallet ( ₹"+jObj.getString("aepsWallet")+")");
                    amountAeps=jObj.getString("aepsWallet");
                    accountNo=jObj.getString("accountNo");
                    bankName=jObj.getString("bankName");
                    Log.d("TAG","responseaeps"+accountNo+bankName);
                    try{
                        txtAmountAeps.setText("₹"+jObj.getString("aepsWallet"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(jObj.getString("uPhoto"))){
                                Picasso.with(getContext()).load(jObj.getString("uPhoto")).fit().centerCrop()
                                        .placeholder(R.drawable.user)
                                        .error(R.drawable.user)
                                        .into(userImage);
                            }else if (jObj.getString("uPhoto").equalsIgnoreCase("http://www.zambo.in/uploads/userDocs/ZAM715269/")){
                                userImage.setImageResource(R.drawable.logo);
                            }
                            if (!TextUtils.isEmpty(jObj.getString("uCompany"))){
                                editTextCompany.setText(jObj.getString("uCompany"));
                                editTextCompany.setCursorVisible(false);
                            }else {
                                inputCompany.setHint(getResources().getString(R.string.enter_company));
                                editTextCompany.setCursorVisible(true);
                            }

                            if (!jObj.getString("addressLine1").equalsIgnoreCase("null")){
                                editTextAdd1.setText(jObj.getString("addressLine1"));
                            }else {
                                inputAdd1.setHint(getResources().getString(R.string.add_line_one));
                            }

                            if (!jObj.getString("addressLine2").equalsIgnoreCase("null")){
                                editTextAdd2.setText(jObj.getString("addressLine2"));
                            }else {
                                inputAdd2.setHint(getResources().getString(R.string.add_line_two));
                            }

                            if (!jObj.getString("city").equalsIgnoreCase("null")){
                                editTextcity.setText(jObj.getString("city"));
                            }else {
                                inputCity.setHint(getResources().getString(R.string.city));
                            }
                            if (!jObj.getString("stateName").equalsIgnoreCase("null")){
                                state.setText(jObj.getString("stateName"));
                            }else {
                                state.setHint(getResources().getString(R.string.state));
                            }

                            if (!jObj.getString("pin").equalsIgnoreCase("null")){
                                editTextPincode.setText(jObj.getString("pin"));
                            }else {
                                inputPincode.setHint(getResources().getString(R.string.pin));
                            }


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
                params.put(Constant.U_UID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/

   /* private void getProfile(final String userId) {
        String tag_string_req = "register_res";

        pDialog.setMessage("Loading Data...");
        pDialog.setIndeterminate(true);
        pDialog.show();


        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.d(VolleyLog.TAG, "Profile Response: " + response);

                pDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String message=jsonObject.getString("message");
                    if (status.equals("1")){
                        JSONObject jObj=jsonObject.getJSONObject("data");
                        btnRedeemAeps.setText("Redeem Aeps Wallet ( ₹"+jObj.getString("aepsWallet")+")");
                        amountAeps=jObj.getString("aepsWallet");
                        accountNo=jObj.getString("accountNo");
                        bankName=jObj.getString("bankName");
                        if (!TextUtils.isEmpty(jObj.getString("uPhoto"))){
                            Picasso.with(getContext()).load(jObj.getString("uPhoto")).fit().centerCrop()
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(userImage);
                        }else if (jObj.getString("uPhoto").equalsIgnoreCase("http://www.zambo.in/uploads/userDocs/ZAM715269/")){
                            userImage.setImageResource(R.drawable.logo);
                        }
                        if (!TextUtils.isEmpty(jObj.getString("uCompany"))){
                            editTextCompany.setText(jObj.getString("uCompany"));
                            editTextCompany.setCursorVisible(false);
                        }else {
                            inputCompany.setHint(getResources().getString(R.string.enter_company));
                            editTextCompany.setCursorVisible(true);
                        }

                        if (!jObj.getString("addressLine1").equalsIgnoreCase("null")){
                            editTextAdd1.setText(jObj.getString("addressLine1"));
                        }else {
                            inputAdd1.setHint(getResources().getString(R.string.add_line_one));
                        }

                        if (!jObj.getString("addressLine2").equalsIgnoreCase("null")){
                            editTextAdd2.setText(jObj.getString("addressLine2"));
                        }else {
                            inputAdd2.setHint(getResources().getString(R.string.add_line_two));
                        }

                        if (!jObj.getString("city").equalsIgnoreCase("null")){
                            editTextcity.setText(jObj.getString("city"));
                        }else {
                            inputCity.setHint(getResources().getString(R.string.city));
                        }
                        if (!jObj.getString("stateName").equalsIgnoreCase("null")){
                            state.setText(jObj.getString("stateName"));
                        }else {
                            state.setHint(getResources().getString(R.string.state));
                        }

                        if (!jObj.getString("pin").equalsIgnoreCase("null")){
                            editTextPincode.setText(jObj.getString("pin"));
                        }else {
                            inputPincode.setHint(getResources().getString(R.string.pin));
                        }

                    }else {
                        Toast.makeText(getContext(),
                                message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                params.put(Constant.U_UID,userId);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }*/
//   private void getProfile(final String userId) {
//       String tag_string_req = "register_res";
//
//       pDialog.setMessage("Loading Data...");
//       pDialog.setIndeterminate(true);
//       pDialog.show();
//
//
//       HttpsTrustManager.allowAllSSL();
//       StringRequest strReq = new StringRequest(Request.Method.POST,
//               AppConfig.PROFILE_DATA, new com.android.volley.Response.Listener<String>() {
//
//           @SuppressLint("SetTextI18n")
//           @Override
//           public void onResponse(String response) {
//               Log.d(VolleyLog.TAG, "Profile Response: " + response);
//
//               pDialog.dismiss();
//               try {
//                   JSONObject jsonObject=new JSONObject(response);
//                   String status=jsonObject.getString("status");
//                   String message=jsonObject.getString("message");
//                   if (status.equals("1")){
//                       JSONObject jObj=jsonObject.getJSONObject("data");
//                       btnRedeemAeps.setText("Redeem Aeps Wallet ( ₹"+jObj.getString("aepsWallet")+")");
//                       amountAeps=jObj.getString("aepsWallet");
//                       accountNo=jObj.getString("accountNo");
//                       bankName=jObj.getString("bankName");
//                       if (!TextUtils.isEmpty(jObj.getString("uPhoto"))){
//                           Picasso.with(getActivity()).load(jObj.getString("uPhoto")).fit().centerCrop()
//                                   .placeholder(R.drawable.user)
//                                   .error(R.drawable.user)
//                                   .into(userImage);
//                       }else if (jObj.getString("uPhoto").equalsIgnoreCase("http://www.zambo.in/uploads/userDocs/ZAM715269/")){
//                           userImage.setImageResource(R.drawable.logo);
//                       }
//                       if (!TextUtils.isEmpty(jObj.getString("uCompany"))){
//                           editTextCompany.setText(jObj.getString("uCompany"));
//                           editTextCompany.setCursorVisible(false);
//                       }else {
//                           inputCompany.setHint(getResources().getString(R.string.enter_company));
//                           editTextCompany.setCursorVisible(true);
//                       }
//
//                       if (!jObj.getString("addressLine1").equalsIgnoreCase("null")){
//                           editTextAdd1.setText(jObj.getString("addressLine1"));
//                       }else {
//                           inputAdd1.setHint(getResources().getString(R.string.add_line_one));
//                       }
//
//                       if (!jObj.getString("addressLine2").equalsIgnoreCase("null")){
//                           editTextAdd2.setText(jObj.getString("addressLine2"));
//                       }else {
//                           inputAdd2.setHint(getResources().getString(R.string.add_line_two));
//                       }
//
//                       if (!jObj.getString("city").equalsIgnoreCase("null")){
//                           editTextcity.setText(jObj.getString("city"));
//                       }else {
//                           inputCity.setHint(getResources().getString(R.string.city));
//                       }
//                       if (!jObj.getString("stateName").equalsIgnoreCase("null")){
//                           state.setText(jObj.getString("stateName"));
//                       }else {
//                           state.setHint(getResources().getString(R.string.state));
//                       }
//
//                       if (!jObj.getString("pin").equalsIgnoreCase("null")){
//                           editTextPincode.setText(jObj.getString("pin"));
//                       }else {
//                           inputPincode.setHint(getResources().getString(R.string.pin));
//                       }
//
//                   }else {
//                       Toast.makeText(getContext(),
//                               message, Toast.LENGTH_LONG).show();
//                   }
//               } catch (JSONException e) {
//                   e.printStackTrace();
//               }
//
//
//           }
//       }, error -> {
//           Log.e(VolleyLog.TAG, "Profile Error: " + error.getMessage());
//           Toast.makeText(getActivity(),
//                   error.getMessage(), Toast.LENGTH_LONG).show();
//       }) {
//           @Override
//           protected Map<String, String> getParams() {
//               // Posting params to register url
//               Map<String, String> params = new HashMap<>();
//               params.put(Constant.U_UID,userId);
//               return params;
//           }
//       };
//       strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
//               DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//       AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//   }
   public void storetoken(String token,String userid)
   {
     //  Toast.makeText(getContext(), ""+userid, Toast.LENGTH_SHORT).show();
       Retrofit retrofit=new Retrofit.Builder().baseUrl("http://zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
       Apiinterface apiinterface=retrofit.create(Apiinterface.class);
       Call<Notification> call=apiinterface.storetoken(token,userid);
       call.enqueue(new Callback<Notification>() {
           @Override
           public void onResponse(Call<Notification> call, Response<Notification> response) {
               String status=response.body().getStatus();
             //  Toast.makeText(getContext(), "Welcome to Zambo"+status, Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onFailure(Call<Notification> call, Throwable t) {

           }
       });
   }
    public static String hmacDigest(String msg, String keyString) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] bytes = mac.doFinal(msg.getBytes(StandardCharsets.US_ASCII));
            StringBuilder hash = new StringBuilder();
            for (byte aByte : bytes) {
                String hex = Integer.toHexString(0xFF & aByte);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }

    private void getPrimeAepsDetails(String userId, Double lat, Double lon) {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        String tag_string_req = "aeps_2";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PRIME_AEPS, response -> {
            Log.e(TAG, "PRIME AEPS Response: " + response+"\n"+userId+"\n"+lat+"\n"+lon+"\n"+userData.getToken());
            progressDialog.dismiss();
            try {
                JSONObject jsonObject=new JSONObject(response);
                if (jsonObject.has("status")){
                    String status=jsonObject.getString("status");
                    if (status.equalsIgnoreCase("0")) {
                        JSONObject jObj=jsonObject.getJSONObject("outlet");
                        String merchantUserId=jObj.getString("merchantUserId");
                        String merchantPassword=jObj.getString("merchantPassword");
                        String merchantId=jObj.getString("merchantId");
                        String merchantName=jObj.getString("merchantName");
                        String merchantContactNumber=jObj.getString("merchantContactNumber");
                        String merchantAddress1=jObj.getString("merchantAddress1");
                        String merchantAddress2=jObj.getString("merchantAddress2");
                        String merchantCity=jObj.getString("merchantCity");
                        String merchantState=jObj.getString("merchantState");
                        String merchantPinCode=jObj.getString("merchantPinCode");
                        String merchantCatCode=jObj.getString("merchantCatCode");
                        String merchantCountry=jObj.getString("merchantCountry");
                        String transId=jsonObject.getString("transId");
                        Intent intent = new Intent(getActivity(), AepsActivity.class);
                        intent.putExtra(Constant.MERCHANT_USERID,merchantUserId);
                        intent.putExtra(Constant.MERCHANT_PASSWORD,merchantPassword);
                        intent.putExtra(Constant.MERCHANT_ID,merchantId);
                        intent.putExtra(Constant.MERCHANT_NAME,merchantName);
                        intent.putExtra(Constant.MERCHANT_CONSTANT_NUMBER,merchantContactNumber);
                        intent.putExtra(Constant.MERCHANT_ADDRESS1,merchantAddress1);
                        intent.putExtra(Constant.MERCHANT_ADDRESS2,merchantAddress2);
                        intent.putExtra(Constant.MERCHANT_CITY,merchantCity);
                        intent.putExtra(Constant.MERCHANT_STATE,merchantState);
                        intent.putExtra(Constant.MERCHANT_PINCODE,merchantPinCode);
                        intent.putExtra(Constant.MERCHANT_CATCODE,merchantCatCode);
                        intent.putExtra(Constant.MERCHANT_COUNTRY,merchantCountry);
                        intent.putExtra(Constant.MERCHANT_TRANSID,transId);
                        startActivity(intent);
                        Objects.requireNonNull(getActivity()).overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

                    }else {
                        String message=jsonObject.getString("message");
                        Configuration.openPopupUpDown(getActivity(),R.style.Dialod_UpDown,"error",message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            progressDialog.dismiss();
            Log.e(TAG, "AEPS Error: " + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.LATTITUDE, String.valueOf(lat));
                params.put(Constant.LONGITUDE, String.valueOf(lon));
                params.put(Constant.SOURCE,"APP");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("token",userData.getToken());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getfingpayaeps(final String userId) {
     //   Toast.makeText(getContext(), ""+userData.getToken()+"\n"+userId+"\n"+lat+"\n"+lon, Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                okhttp3.Request request = chain.request().newBuilder().addHeader("token", userData.getToken()).build();
                return chain.proceed(request);
            }
        });
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://zambo.in/prime/aeps/").client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Fingpayseps> call=apiinterface.fingpayaeps(userId,lat,lon,"APP");
        call.enqueue(new Callback<Fingpayseps>() {
            @Override
            public void onResponse(@NotNull Call<Fingpayseps> call, @NotNull Response<Fingpayseps> response) {
              //Toast.makeText(getContext(), ""+response.body().getStatus()+""+response.body().getMessage()+"\n"+response.body().getOutlet().getMerchantUserId(), Toast.LENGTH_SHORT).show();

                if (response.body() != null) {
                    if (response.body().getStatus().equals(0)) {
                        progressDialog.dismiss();

                        SplashActivity.savePreferences("MERCHANT_ID_USER",response.body().getOutlet().getMerchantUserId());
                        SplashActivity.savePreferences("MERCHANT_PASSWORD",response.body().getOutlet().getMerchantPassword());
                        SplashActivity.savePreferences("END_MERCHANT_ID",response.body().getOutlet().getMerchantId());
                        SplashActivity.savePreferences("END_MERCHANT_NAME",response.body().getOutlet().getMerchantName());
                        SplashActivity.savePreferences("MERCHANT_CONTACT_NUMBER",response.body().getOutlet().getMerchantContactNumber());
                        SplashActivity.savePreferences("END_MERCHANT_CATEGORY_CODE",response.body().getOutlet().getMerchantCatCode());
                        SplashActivity.savePreferences("MERCHANT_ADDRESS1",response.body().getOutlet().getMerchantAddress1());
                        SplashActivity.savePreferences("MERCHANT_ADDRESS2",response.body().getOutlet().getMerchantAddress2());
                        SplashActivity.savePreferences("MERCHANT_CITY",response.body().getOutlet().getMerchantCity());
                        SplashActivity.savePreferences("MERCHANT_PINCODE",response.body().getOutlet().getMerchantPinCode());
                        SplashActivity.savePreferences("MERCHANT_STATE_CODE",response.body().getOutlet().getMerchantState());
                        SplashActivity.savePreferences("MERCHANT_COUNTRY_CODE",response.body().getOutlet().getMerchantCountry());
                        SplashActivity.savePreferences("TXN_ID",response.body().getTransId());

//                        Intent intent=new Intent(getContext(), FingPayAepsActivity.class);
//                        startActivity(intent);

    //                  intent.putExtra(Constants.IMEI, imei);
                    }else {

                        progressDialog.dismiss();
                        Configuration.openPopupUpDown(getContext(), R.style.Dialod_UpDown, "error",
                                response.body().getMessage());

                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<Fingpayseps> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSenderInfo() {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/axisdmt/pmcares/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
//        Log.d(TAG, "getSenderInfo: "+userData.getToken()+"\n"+userData.getMobile()+"\n"+userData.getUserId());
//        Toast.makeText(this, ""+userData.getToken()+"\n"+userData.getMobile()+"\n"+userData.getUserId()+"\n\n", Toast.LENGTH_SHORT).show();
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.SenderInfo(userData.getToken(),userData.getMobile(),userData.getUserId(),"APP");
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Intent intent = new Intent(getActivity(),PMCaresFund.class);
                    startActivity(intent);
                }else if(response.body().getStatus().equals(2)){
                    Intent intent = new Intent(getContext(), KnowMore.class);
                    intent.putExtra("OTP","KYC");
                    startActivity(intent);
                }else if(response.body().getStatus().equals(3)){
                    Intent intent = new Intent(getContext(),KnowMore.class);
                    intent.putExtra("OTP","OTP");
                    startActivity(intent);
                }else{
                    Toast.makeText(getContext(),"Something went wrong...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(getContext(),""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
