package com.zambo.zambo_mterminal100.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.zambo.zambo_mterminal100.AppConfig.SessionManager;
import com.zambo.zambo_mterminal100.R;


public class SplashActivity extends AppCompatActivity {
    static String currentVersion;
    ProgressBar progressBar;
    static SharedPreferences sharedPreferences;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session=new SessionManager(SplashActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
//        try {
//            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }

        progressBar =  findViewById(R.id.progress_bar);
        // Start lengthy operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                doWork();
                startApp();

            }
        }).start();
//        GetVersionCode getVersionCode=new GetVersionCode();
//        getVersionCode.execute();
    }

    private void doWork() {
        for (int progress=0; progress<100; progress+=30) {
            try {
                Thread.sleep(1000);
                progressBar.setProgress(progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startApp() {

        if (session.isLoggedIn()){
            Intent intent =new Intent(SplashActivity.this, PinLogin.class);
            startActivity(intent);
        }else {
            Intent intent =new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
        }


    }

    public static void savelanguage(String key,String value){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.putString(key,value);
        editor.apply();
    }
    public static String getlanguage(String key,String value){
        return sharedPreferences.getString(key,value);
    }
    public static void savePreferences(String key, String value) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
       // editor.clear();
        editor.putString(key, value);
        editor.apply();
    }
    public static String getPreferences(String key, String val) {
        return sharedPreferences.getString(key, val);
    }


//    private class GetVersionCode extends AsyncTask<Void, String, String> {
//
//        @Override
//
//        protected String doInBackground(Void... voids) {
//
//            String newVersion = null;
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName() + "&hl=it")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select(".hAyfc .htlgb")
//                        .get(7)
//                        .ownText();
//                return newVersion;
//            } catch (Exception e) {
//                return newVersion;
//            }
//
//        }
//
//
//        @Override
//        protected void onPostExecute(String onlineVersion) {
//            super.onPostExecute(onlineVersion);
//
//            if (onlineVersion != null && !onlineVersion.isEmpty()) {
//
//                if (onlineVersion.equals(currentVersion)) {
//                    if (session.isLoggedIn()){
//                        Intent intent =new Intent(SplashActivity.this, FingerPrintLogin.class);
//                        startActivity(intent);
//                    }else {
//                        Intent intent =new Intent(SplashActivity.this, LoginActivity.class);
//                        startActivity(intent);
//                    }
//                } else {
//                    AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
//                    alertDialog.setTitle("Update");
//                    alertDialog.setIcon(R.mipmap.ic_launcher);
//                    alertDialog.setMessage("New Update is available");
//
//                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Update", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            try {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + SplashActivity.this.getPackageName())));
//                            } catch (android.content.ActivityNotFoundException anfe) {
//                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + SplashActivity.this.getPackageName())));
//                            }
//                        }
//                    });
//
//                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//
//                    alertDialog.show();
//                }
//
//            }
//
//            Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);
//
//        }
//
//        }
    }

