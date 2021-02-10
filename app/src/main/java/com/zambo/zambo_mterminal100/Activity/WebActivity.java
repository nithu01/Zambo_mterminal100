package com.zambo.zambo_mterminal100.Activity;

import android.app.ProgressDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.zambo.zambo_mterminal100.R;

public class WebActivity extends AppCompatActivity {

    public static final String WEBSITE_ADDRESS = "website_address";
    ProgressDialog pd;
    WebView webView;
    TextView textView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webview);
        textView = findViewById(R.id.txt_home_title);
        imageView = findViewById(R.id.imgback_add_money);
        pd = new ProgressDialog(WebActivity.this);
        pd.setTitle("Loading...");
        pd.setMessage("Please wait.");
        pd.setCancelable(false);
        pd.show();
        webView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new Myappwebclient());
        webSettings.setDomStorageEnabled(true);
        String url = getIntent().getStringExtra(WEBSITE_ADDRESS);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                // prDialog = ProgressDialog.show(MainActivity.this, null, "loading, please wait...");
                super.onPageStarted(view, url, favicon);
                // prDialog.dismiss();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                pd.dismiss();
                //prDialog.dismiss();
                super.onPageFinished(view, url);

            }

        });

        //loading webpage
        webView.loadUrl(url);


    }


    public class Myappwebclient extends WebViewClient{

    }
}
