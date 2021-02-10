package com.zambo.zambo_mterminal100.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.SessionManager;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.Fragment.HistoryFragment;
import com.zambo.zambo_mterminal100.Fragment.HomeFragment;
import com.zambo.zambo_mterminal100.BuildConfig;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.Interface.updateBalance;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.Response;
import com.zambo.zambo_mterminal100.model.UserData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener, updateBalance {

    Enddrawertoggle enddrawertoggle;
    ImageView imageView;
    Animation animationup,animationdown;
    //LinearLayout linearLayout;
   // BottomSheetBehavior bottomSheetBehavior;
    FragmentTransaction ft;
    public static UserData userData;
    Fragment currentFragment = null;
    SessionManager session;
    TextView txtName, txtEmail,txtVersion;
    ProgressDialog progressDialog;
    CircleImageView profileImage;
    @SuppressLint("StaticFieldLeak")
    public static TextView ui_hot;
    BottomNavigationView bottomNavigationView;
    protected static final String TAG = "LocationOnOff";
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    NavigationView navigationView;
    DrawerLayout drawer;
    Toolbar toolbar;
    String message="";
    WebService webService;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webService=new WebService((updateBalance) this);
        userData = PrefManager.getInstance(MainActivity.this).getUserData();
        Log.d("TAG","tokendata"+ userData.getToken());

        message=getIntent().getStringExtra("message");
        imageView=findViewById(R.id.imageView);
        txtVersion=findViewById(R.id.txt_version);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView=findViewById(R.id.bottomnavigation);
        animationup= AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_up);
        animationdown=AnimationUtils.loadAnimation(MainActivity.this,R.anim.slide_down);

        gpsCheck();
        if (!Configuration.hasNetworkConnection(MainActivity.this)){
            Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                    "No internet connectivity");
        }

        progressDialog = new ProgressDialog(MainActivity.this);
        session = new SessionManager(MainActivity.this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setTitle(Html.fromHtml("<font color='#FFFFFF'><b> " + "ZAM" + "</b></font>" + "<font color='#FFFFFF'>" + "BO" + "</font>"));

        UserData userData = PrefManager.getInstance(MainActivity.this).getUserData();
        String name = userData.getName();
        String email = userData.getEmail();
        String profilePhoto = userData.getPhoto();
        webService.updateBalance(userData.getUserId());
        String version= BuildConfig.VERSION_NAME;
        txtVersion.setText(version);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            Fragment fragment;
            switch (item.getItemId()) {

                case R.id.home:
                    fragment=new HomeFragment();
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_main,fragment);
                    overridePendingTransition(R.anim.push_right_out,R.anim.push_right_in);
                    fragmentTransaction.commit();
                    toolbar.setVisibility(View.VISIBLE);
                    break;

               case R.id.profile:

                   Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                   startActivity(intent);
                    break;

                case R.id.history:

                    fragment=new HistoryFragment();
                    FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction2.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
                    fragmentTransaction2.replace(R.id.frame_main,fragment);
                    fragmentTransaction2.commit();
                    toolbar.setVisibility(View.GONE);
                    break;
            }
            return false;
        });

      if (Configuration.hasNetworkConnection(MainActivity.this)) {
          // getProfile(userData.getUserId());
          final Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                // getProfile(userData.getUserId());
                  handler.postDelayed(this, 6000);
              }
          }, 6000);
      }else {
          Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                  "No Internet Connectivity"+
                          ", Thanks");
      }
        ft = getSupportFragmentManager().beginTransaction();
            currentFragment = new HomeFragment();
            ft.replace(R.id.frame_main, currentFragment);
            ft.commit();

        /*Navigation Icon*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setToolbarNavigationClickListener(view -> {
            if (drawer.isDrawerOpen(GravityCompat.END)) {

                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
        });
        toggle.syncState();

/*details on navigation header*/
        initNavigationDrawer();

        txtName = navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.text_email);
        profileImage = navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        txtName.setText(name);
        txtEmail.setText(email);

        navigationView.setNavigationItemSelectedListener(this);
//        Picasso.with(MainActivity.this).load(profilePhoto).fit().centerCrop()
//                .placeholder(R.drawable.user)
//                .error(R.drawable.user)
//                .into(profileImage);
        Glide.with(MainActivity.this)
                .load(profilePhoto)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(profileImage);
      //  WebService.getBalance(userData.getUserId());

    }
    @Override
    public void onUpdateBalance(String walletBalance, String comissionWallet, String aepsWallet, String microAtmWallet, String accountNo, String bankName, String ifsc, String beneName) {

    }
    private void openPopup() {
        final Dialog dialg=new Dialog(MainActivity.this);
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
    private void gpsCheck() {
        // Todo Location Already on  ... start
        final LocationManager manager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(MainActivity.this)) {
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
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }
    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
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
                        status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        // Ignore the error.
                    }
                }
            });
        }
    }
    public void initNavigationDrawer() {
        enddrawertoggle = new Enddrawertoggle(this,
                drawer,
                toolbar,
                "open",
                "close");

        drawer.addDrawerListener(enddrawertoggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        enddrawertoggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
       // BottomNavigationView bottomNavigationView=findViewById(R.id.nav_terms)
                int selectedid=bottomNavigationView.getSelectedItemId();

        if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else if(selectedid!=0){
         Intent intent=new Intent(MainActivity.this,MainActivity.class);
         startActivity(intent);
        } else {
            super.onBackPressed();
            if (session.isLoggedIn()){
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Really Exit?")
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Intent launchNextActivity;
                            launchNextActivity = new Intent(Intent.ACTION_MAIN);
                            launchNextActivity.addCategory(Intent.CATEGORY_HOME);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            launchNextActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(launchNextActivity);
                            finish();
                        }).create().show();
            }

        }
        }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
    if(id==R.id.home) {
            Fragment fragment=new HomeFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_main,fragment).commit();
    }else
    if (id==R.id.nav_logout){
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Logging out...");
            try {
                if (progressDialog!=null&&!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }catch (Exception e){e.printStackTrace();}
            final Handler handler = new Handler();
            handler.postDelayed(this::logout,2000);

        }

//            if (id == R.id.scratch_card) {
//                Intent intent = new Intent(MainActivity.this, AddMoney.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//            }
        else if(id==R.id.nav_terms)
        {
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.WEBSITE_ADDRESS, "https://zambo.in/terms");
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);

        }else if(id==R.id.nav_refund) {
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.WEBSITE_ADDRESS, "https://zambo.in/refund");
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }else if(id==R.id.nav_support)
        {
            Intent i = new Intent(MainActivity.this, WebActivity.class);
            i.putExtra(WebActivity.WEBSITE_ADDRESS, "https://zambo.in/contact");
            startActivity(i);
            overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
        }else if(id==R.id.notification) {
            Intent intent=new Intent(getApplicationContext(),NotificationActivity.class);
            startActivity(intent);
    }else if(id==R.id.rate_us) {
       Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

        //Copy App URL from Google Play Store.
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.efficientindia.zambopay"));

        startActivity(intent);
    }else if(id==R.id.change_pin) {
                getOtp(userData.getMobile());
            }
            else if(id==R.id.forgot_password) {
                getOtpPass(userData.getMobile());
            }
            else if(id==R.id.Complaint) {
                Intent intent = new Intent(MainActivity.this,Complain.class);
                startActivity(intent);
            }
            else if(id==R.id.ComplaintStatus) {
                Intent intent = new Intent(MainActivity.this,ComplaintStatus.class);
                startActivity(intent);
            }
            else if(id==R.id.fund_request) {
                Intent intent = new Intent(MainActivity.this,Fund_Request.class);
                startActivity(intent);
            }
//            else if(id==R.id.fund_request_status) {
//                Intent intent = new Intent(MainActivity.this,FundStatus.class);
//                startActivity(intent);
//            }
        else if(id==R.id.language)
    {
        Dialog dialog=new Dialog(MainActivity.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_language);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView txtenglish=dialog.findViewById(R.id.english);
        TextView txthindi=dialog.findViewById(R.id.hindi);

        txtenglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.savelanguage("Language","English");
                Toast.makeText(MainActivity.this, "English Selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        txthindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SplashActivity.savePreferences("Language","Hindi");
                Toast.makeText(MainActivity.this, "Hindi Selected", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
  //     txtenglish.setOnClickListener(view -> SplashActivity.savelanguage("language","English"));
//        hindi.setOnClickListener(view->SplashActivity.savelanguage("language","Hindi"));

        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void getOtp(String username) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<Response> call=apiinterface.getOtpin(username);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Intent intent = new Intent(MainActivity.this,pingenerate.class);
                    intent.putExtra("Type","CHANGE");
                    startActivity(intent);
//                    layoutPhone.setVisibility(View.GONE);
//                    layoutOtp.setVisibility(View.VISIBLE);
//                    layoutPin.setVisibility(View.GONE);
//                    textView.setText("+"+phone.getText().toString());
                }else{
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(MainActivity.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void logout() {
        PrefManager.getInstance(MainActivity.this).logout();
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
/*
    private void getProfile(final String userId) {
        String tag_string_req = "register_res";

        HttpsTrustManager.allowAllSSL();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.PROFILE_DATA, response -> {
                   // Log.d(TAG, "Profile Response: " + response);

                   // pDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String status=jsonObject.getString("status");
                        if (status.equals("1")){
                            JSONObject jObj=jsonObject.getJSONObject("data");
                            if (!jObj.getString("uStatus").equalsIgnoreCase("A")){
                                logout();
                                Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                                        "Session Expired\n Please Login again"+
                                                ", Thanks");
                            }
                        }else {
                            Configuration.openPopupUpDown(MainActivity.this,R.style.Dialod_UpDown,"internetError",
                                    "Something went wrong\nTry after sometime"+
                                            ", Thanks");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
          //  Log.e(TAG, "Profile Error: " + error.getMessage());
          */
/*  Toast.makeText(ProfileActivity.this,
                    error.getMessage(), Toast.LENGTH_LONG).show();*//*

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
/*Bottom Navigation*/
/*
    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                   item.setShifting(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }
    }
*/

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//       switch (requestCode)
//       {
//           case 1:
//               if(resultCode==RESULT_OK)
//               {
//                   Toast.makeText(this, "resultok", Toast.LENGTH_SHORT).show();
//                   openPopup();
//               }else{
//                   Toast.makeText(this, "nook", Toast.LENGTH_SHORT).show();
//               }
//       }
//    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                     //   Toast.makeText(MainActivity.this, "Gps enabled", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                       finish();
                        break;
                    default:
                        break;
                }
                break;
        }
        try {
            if (requestCode == 1) {
                if (resultCode == 0) {
                    Log.e("Mess", " data " + requestCode + " " + resultCode + " " + data.getStringExtra("Message"));
                }
                if (resultCode == 3) {
                    try {
                        assert data != null;
                        String bankRrn = data.getStringExtra("bankRrn");
                        int type = data.getIntExtra("TransactionType", 0); //to get transaction name
                        String transAmount = data.getStringExtra("transAmount"); //to get response
                        String balAmount = data.getStringExtra("balAmount");
                        boolean status = data.getBooleanExtra("Status", false); //to get response message
                        String response = data.getStringExtra("Message"); //to get response message
                        Log.e("totalRes", " " + response);

                        try {
                            String transType = null;
                            switch (type) {

                                case Constants.CASH_WITHDRAWAL:
                                    transType = "Cash Withdrawal";
                                    break;

                                case Constants.BALANCE_ENQUIRY:
                                    transType = "Balance Enquiry";
                                    break;

                                default:
                                    break;
                            }
                            if (response != null) {
                                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();

                                if (type == Constants.BALANCE_ENQUIRY) {
                                    Toast.makeText(MainActivity.this, "transType :" + transType + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Status :" + status + "\n"
                                            +"balAmount :" + balAmount + "\n", Toast.LENGTH_LONG).show();
                                    Intent intent =new Intent(MainActivity.this,AspsTransfer.class);
                                    intent.putExtra(Constant.RESPONSE,response);
                                    startActivity(intent);
                                    Log.e("totalRes", " " + response);
                                }
                                if (type == Constants.CASH_WITHDRAWAL) {
                                    Toast.makeText(MainActivity.this,"transType :" + transType + "\n"
                                            + "Bank RNN Number :" + bankRrn + "\n"
                                            +"Bank RNN Number :" + bankRrn + "\n"
                                            +"Status :" + status + "\n"
                                            +"transAmount :" + transAmount + "\n", Toast.LENGTH_LONG).show();
                                    Intent intent =new Intent(MainActivity.this,AspsTransfer.class);
                                    intent.putExtra(Constant.RESPONSE,response);
                                    startActivity(intent);
                                    Log.e("totalRes", " " + response);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LOCATION","LOCATION--->"+location.getLatitude()+" "+location.getLongitude());
    }

    public void getOtpPass(String username) {
        progressDialog.show();
        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.getOtpin(username);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.body().getStatus().equals(0)){
                    Intent intent = new Intent(MainActivity.this,Forget_Password.class);
                    startActivity(intent);
//                    layoutPhone.setVisibility(View.GONE);
//                    layoutOtp.setVisibility(View.VISIBLE);
//                    layoutPin.setVisibility(View.GONE);
//                    textView.setText("+"+phone.getText().toString());
                }else{
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                Toast.makeText(MainActivity.this,""+t,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
