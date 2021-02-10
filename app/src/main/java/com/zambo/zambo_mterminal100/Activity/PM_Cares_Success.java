package com.zambo.zambo_mterminal100.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zambo.zambo_mterminal100.R;

public class PM_Cares_Success extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_m__cares__success);
        imageView = findViewById(R.id.cancel);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PM_Cares_Success.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textView = findViewById(R.id.amt);
        textView.setText(getIntent().getStringExtra("amount"));
        button = findViewById(R.id.close);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PM_Cares_Success.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
