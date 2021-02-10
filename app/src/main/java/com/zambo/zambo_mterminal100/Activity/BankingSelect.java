package com.zambo.zambo_mterminal100.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zambo.zambo_mterminal100.R;

public class BankingSelect extends AppCompatActivity {

    LinearLayout withdrawLayout ,balanceLayout;
//    static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banking_select);

        withdrawLayout = findViewById(R.id.CashWithdraw_layout);
        withdrawLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.savePreferences("Type","CW");
                Intent intent = new Intent(BankingSelect.this,CustomerMobile.class);
                startActivity(intent);
            }
        });
        balanceLayout = findViewById(R.id.BalanceInquiry_layout);
        balanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashActivity.savePreferences("Type","BE");
                Intent intent = new Intent(BankingSelect.this,CustomerMobile.class);
                startActivity(intent);
            }
        });
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
                Intent intent = new Intent(BankingSelect.this,MainActivity.class);
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
}
