package com.zambo.zambo_mterminal100.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zambo.zambo_mterminal100.R;

public class CustomerMobile extends AppCompatActivity {

    EditText editText;
    Button button;
    String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mobile);
        editText = findViewById(R.id.customer_mobile_no);
//        phoneNo = editText.getText().toString();
        button = findViewById(R.id.proceed);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty()) {
                    editText.setError("Enter Mobile No");
                    editText.requestFocus();
                } else if (editText.getText().toString().length() < 10) {
                    editText.setError("Enter Valid Mobile No");
                    editText.requestFocus();
                } else {
                    SplashActivity.savePreferences("MobileNo", editText.getText().toString());
                    Intent intent = new Intent(CustomerMobile.this, AepsActivity.class);
                    startActivity(intent);
                }
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
                Intent intent = new Intent(CustomerMobile.this, MainActivity.class);
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
