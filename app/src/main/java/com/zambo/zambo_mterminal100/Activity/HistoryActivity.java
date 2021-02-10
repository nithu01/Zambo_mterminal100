package com.zambo.zambo_mterminal100.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.Adapter.PagerAdapterTransaction;
import com.zambo.zambo_mterminal100.Adapter.WalletHistoryAdapter;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.UserData;
import com.zambo.zambo_mterminal100.model.WalletHistory;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener ,TabLayout.OnTabSelectedListener,
        WalletHistoryAdapter.WalletHistoryAdapterListener {

    private static final String TAG = HistoryActivity.class.getSimpleName();
    private ImageView imgBack;
    TabLayout tabLayout;
    @SuppressLint("StaticFieldLeak")
    public static ViewPager viewPager;
    RecyclerView requestListTransaction;
    private List<WalletHistory> listData;
    private WalletHistoryAdapter loadListAdapter;
    private ProgressDialog progressDialog;
    private RelativeLayout rlNoData;
    private ImageView imgData;
    private TextView txtData;
    LinearLayout lnTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        rlNoData=findViewById(R.id.rl_wl_trans_recharge);
        imgData=findViewById(R.id.img_wl_trans_recharge);
        txtData=findViewById(R.id.txt_wl_trans_recharge);
        imgBack=findViewById(R.id.img_back_history);
        lnTitle=findViewById(R.id.ln_title_recharge);
        progressDialog=new ProgressDialog(HistoryActivity.this);
        requestListTransaction=findViewById(R.id.recyclerview_customer_history);
        imgBack.setOnClickListener(this);
        tabLayout = findViewById(R.id.tablayout_transaction);
        viewPager = findViewById(R.id.viewpager_transaction);
        //isReadStoragePermissionGranted();
        //isWriteStoragePermissionGranted();
        UserData userData= PrefManager.getInstance(HistoryActivity.this).getUserData();

        if (!Configuration.hasNetworkConnection(HistoryActivity.this)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDownBack(HistoryActivity.this,R.style.Dialod_UpDown,"main","internetError",
                        "No internet connectivity");
            }
        }
        if (userData.getuType().equalsIgnoreCase("CS")){
            tabLayout.setVisibility(View.GONE);
            viewPager.setVisibility(View.GONE);
            lnTitle.setVisibility(View.VISIBLE);
            requestListTransaction.setVisibility(View.VISIBLE);
            if (Configuration.hasNetworkConnection(HistoryActivity.this)){
                getCustomerHistory(userData.getuType(),userData.getUserId());
            }else {
                rlNoData.setVisibility(View.VISIBLE);
                imgData.setImageResource(R.drawable.nointernet);
                txtData.setText(R.string.no_internet);
                requestListTransaction.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDownBack(HistoryActivity.this,R.style.Dialod_UpDown,"main","internetError",
                            "No internet connectivity");
                }
            }
        }else {
            tabLayout.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.VISIBLE);
            lnTitle.setVisibility(View.GONE);
            requestListTransaction.setVisibility(View.GONE);
           /* tabLayout.addTab(tabLayout.newTab().setText("Recharge History"));
            tabLayout.addTab(tabLayout.newTab().setText("Bill Payment History"));*/
            tabLayout.addTab(tabLayout.newTab().setText("Wallet Transaction"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
          //  tabLayout.setba

            PagerAdapterTransaction adapter = new PagerAdapterTransaction(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            tabLayout.setOnTabSelectedListener(this);
            tabLayout.setupWithViewPager(viewPager);
        }
        listData = new ArrayList<>();
        loadListAdapter = new WalletHistoryAdapter(HistoryActivity.this, listData, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(HistoryActivity.this);
        requestListTransaction.setLayoutManager(mLayoutManager);
        requestListTransaction.setItemAnimator(new DefaultItemAnimator());
        requestListTransaction.setAdapter(loadListAdapter);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent=new Intent(HistoryActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(HistoryActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        finish();
    }

    private void getCustomerHistory(String userType, String userId) {
        String tag_string_req = "fund_req";

        Configuration.showDialog("Please wait...",progressDialog);


        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.WALLET_STATEMENT,
                response -> {

                    Log.d(TAG,"Wallet RESPONSED"+response+" --->"+userType+" ---->"+userId);

                    try {

                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("1")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)){
                                rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");
                                requestListTransaction.setVisibility(View.GONE);
                            }else {
                                rlNoData.setVisibility(View.GONE);
                                requestListTransaction.setVisibility(View.VISIBLE);
                                List<WalletHistory> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<WalletHistory>>() {
                                        }.getType());

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();

                            }
                        }else {
                            rlNoData.setVisibility(View.VISIBLE);
                            imgData.setImageResource(R.drawable.norequest);
                            txtData.setText("No Transaction/Something went wrong");
                            requestListTransaction.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        rlNoData.setVisibility(View.VISIBLE);
                        imgData.setImageResource(R.drawable.norequest);
                        txtData.setText("No Transaction");
                        requestListTransaction.setVisibility(View.GONE);
                    }

                    progressDialog.dismiss();
                },
                error -> {

                    progressDialog.dismiss();
                   // Toast.makeText(getActivity(),"Try again",Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDownBack(HistoryActivity.this,R.style.Dialod_UpDown,"main","error",
                                "Something went wrong\n Try after sometime");
                    }
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    @Override
    public void onContactSelected(WalletHistory contact) {

    }
//    public  boolean isReadStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//             //   Log.v(TAG,"Permission is granted1");
//                Toast.makeText(getApplicationContext(),"writepermission is granted",Toast.LENGTH_SHORT).show();
//
//                return true;
//            } else {
//                Toast.makeText(getApplicationContext(),"Permission is revoked1",Toast.LENGTH_SHORT).show();
//             //   Log.v(TAG,"Permission is revoked1");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted1");
//            return true;
//        }
//    }
//
//    public  boolean isWriteStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getApplicationContext(),"writepermission is granted",Toast.LENGTH_SHORT).show();
//               // Log.v(TAG,"Permission is granted2");
//                return true;
//            } else {
//
//             //   Log.v(TAG,"Permission is revoked2");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted2");
//            return true;
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 2:
//                Log.d(TAG, "External storage2");
//                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
//                    //resume tasks needing this permission
//                  //  downloadPdfFile();
//                }else{
//                    progress.dismiss();
//                }
//                break;
//
//            case 3:
//                Log.d(TAG, "External storage1");
//                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
//                    //resume tasks needing this permission
//                   // SharePdfFile();
//                }else{
//    }
}
