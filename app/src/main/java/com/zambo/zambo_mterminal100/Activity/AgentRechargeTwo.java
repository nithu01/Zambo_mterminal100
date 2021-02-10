package com.zambo.zambo_mterminal100.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.zambo.zambo_mterminal100.Adapter.MenuItemHomeAdapter;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.AppConfig.WebService;
import com.zambo.zambo_mterminal100.model.UserData;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.zambo.zambo_mterminal100.R;

public class AgentRechargeTwo extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ImageView imgBack;
    GridView gridViewRechargeThree;
    public String[] nameItem={"Mobile Prepaid","DTH"};

    public Integer[] mThumbIds = {
            R.drawable.postpaidcolor,R.drawable.dthcolor};
    private ProgressDialog progressDialog;

    ExpandableRelativeLayout expandableRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_recharge_two);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        expandableRelativeLayout=findViewById(R.id.expandablelayout);
        progressDialog=new ProgressDialog(AgentRechargeTwo.this);
        imgBack=findViewById(R.id.imgback_agent);
        imgBack.setOnClickListener(this);
        gridViewRechargeThree=findViewById(R.id.gridview_recharge_three_agent);

        MenuItemHomeAdapter adapter = new MenuItemHomeAdapter(AgentRechargeTwo.this,nameItem,mThumbIds);
        gridViewRechargeThree.setAdapter(adapter);
        gridViewRechargeThree.setOnItemClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AgentRechargeTwo.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        UserData userData= PrefManager.getInstance(AgentRechargeTwo.this).getUserData();
        SplashActivity.savePreferences(Constant.AGENT,"agent");
        if (position==0){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_PREPAID,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeTwo.this,R.style.Dialod_UpDown,"error","Prepaid mobile service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"mobilePrepaid");
                WebService.getOperatorList("Prepaid",AgentRechargeTwo.this,progressDialog);
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }else if (position==1){
            if (SplashActivity.getPreferences(Constant.CUSTOMER_DTH,"").equalsIgnoreCase("hide")){
                Configuration.openPopupUpDown(AgentRechargeTwo.this,R.style.Dialod_UpDown,"error","DTH recharge service not" +
                        " available due to some system problem\n" +
                        "please wait untill we start it asap"+
                        ", Thanks");
            }else {
                WebService.getOperatorList("DTH",AgentRechargeTwo.this,progressDialog);
                SplashActivity.savePreferences(Constant.CUSTOMER_SERVICE,"dth");
                WebService.getActiveService(userData.getUserId(), userData.getuType());
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v==imgBack){
            Intent intent = new Intent(AgentRechargeTwo.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right);
        }
    }
}
