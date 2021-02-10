package com.zambo.zambo_mterminal100.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zambo.zambo_mterminal100.R;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    String title,body;
    Button send;
    TextView ettitle,etbody,message;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ettitle=findViewById(R.id.titles);
        etbody=findViewById(R.id.messages);
        imgBack=findViewById(R.id.imgback_add_money);
        imgBack.setOnClickListener(this);
        title=getIntent().getStringExtra("title");
        body=getIntent().getStringExtra("message");
        message=findViewById(R.id.messages);
        if(title!=null)
        {
           // ettitle.setText("You don't have notification right now");
            imgBack.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {
        if(view==imgBack)
        {

            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(NotificationActivity.this,MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        super.onBackPressed();
    }
}
