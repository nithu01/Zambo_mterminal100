package com.zambo.zambo_mterminal100.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.zambo.zambo_mterminal100.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Random;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TransactionReceipt extends AppCompatActivity {
    String type_val=SplashActivity.getPreferences("Type","");
    TextView mobile,bank,tid,time,status,successful,bcCode,bcName,bcLocation,Rrn,txnAmount,Balance,Aadhaar;
    String stus,bnk,txn,mob,bccode,bcname,bclocation,rrn,txnamount,balance,aadhaar;
    Random invoice;
    int tidd;
    Button button;
    ImageView menu;
    private String sharePath="no";
    LinearLayout linearLayout;
    Handler mHandler;
    ProgressDialog mProgressBar;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_receipt);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        successful = findViewById(R.id.successful);
        if(SplashActivity.getPreferences("BankList","").equals("AadharPay")){
            successful.setText("Transaction Successful");
        }else if(SplashActivity.getPreferences("BankList","").equals("AEPS")){
            if (type_val.equals("CW")) {
                successful.setText("Cash Withdrawal Successful");
            } else if (type_val.equals("BE")) {
                successful.setText("Balance Enquiry Successful");
            }
        }
        aadhaar = getIntent().getStringExtra("aadhaar");
        stus = getIntent().getStringExtra("status");
        txn = getIntent().getStringExtra("txnId");
        bnk  = getIntent().getStringExtra("bank");
        mob = getIntent().getStringExtra("customerMobile");
        bccode = getIntent().getStringExtra("bcCode");
        bcname = getIntent().getStringExtra("bcName");
        bclocation = getIntent().getStringExtra("bcLocation");
        rrn = getIntent().getStringExtra("rrn");
        txnamount = getIntent().getStringExtra("txnAmount");
        balance = getIntent().getStringExtra("balance");

        invoice=new Random();
        tidd=invoice.nextInt(9999999)+1000;
        Aadhaar = findViewById(R.id.aadhaar);
        Aadhaar.setText(aadhaar);
        mobile = findViewById(R.id.mobile);
        mobile.setText(SplashActivity.getPreferences("MobileNo",null));
        bank = findViewById(R.id.bank);
        bank.setText(bnk);
        tid = findViewById(R.id.tid);
        tid.setText(txn);
        time = findViewById(R.id.time);
        time.setText(currentDateTimeString);
        status = findViewById(R.id.status);
        status.setText(stus);
        bcCode = findViewById(R.id.bcCode);
        bcCode.setText(bccode);
        bcName = findViewById(R.id.bcName);
        bcName.setText(bcname);
        bcLocation = findViewById(R.id.bcLocation);
        bcLocation.setText(bclocation);
        Rrn = findViewById(R.id.rrn);
        Rrn.setText(rrn);
        txnAmount = findViewById(R.id.txnAmount);
        txnAmount.setText(txnamount);
        Balance = findViewById(R.id.balance);
        Balance.setText(balance);
        linearLayout = findViewById(R.id.transaction_layout);
        button = findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.setImageBitmap(getBitmapFromView(linearLayout));
                if(SplashActivity.getPreferences("BankList","").equals("AadharPay")) {
                    Intent intent = new Intent(TransactionReceipt.this, AadhaarPayMobile.class);
                    startActivity(intent);
                }else if(SplashActivity.getPreferences("BankList","").equals("AEPS")){
                    Intent intent = new Intent(TransactionReceipt.this, BankingSelect.class);
                    startActivity(intent);
                }
            }
        });

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(TransactionReceipt.this,menu);
                pop.getMenuInflater().inflate(R.menu.transaction_menu,pop.getMenu());

                //registering popup with OnMenuItemClickListener
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(TransactionReceipt.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        int id = item.getItemId();
                        switch (id){
                            case R.id.download:
                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    notificationDialog();
                                    saveImageToExternal(getBitmapFromView(linearLayout));
                                }
                                return true;
                            case R.id.share:
                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    saveImageToExternal(getBitmapFromView(linearLayout));
                                    if (!sharePath.equals("no")) {
                                            share(sharePath);
                                    }
                                }
                                return true;
                        }
                        return true;
                    }
                });
                pop.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
                if(SplashActivity.getPreferences("BankList","").equals("AadharPay")) {
                    Intent intent = new Intent(TransactionReceipt.this, AadhaarPayMobile.class);
                    startActivity(intent);
                }else if(SplashActivity.getPreferences("BankList","").equals("AEPS")){
                    Intent intent = new Intent(TransactionReceipt.this, BankingSelect.class);
                    startActivity(intent);
                }
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


    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmapFromView(LinearLayout view) {
        try {

            view.setDrawingCacheEnabled(true);

            view.buildDrawingCache();
            //Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888);
            //Bind a canvas to it
            Canvas canvas = new Canvas(returnedBitmap);
            //Get the view's background
            Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null) {
                //has background drawable, then draw it on the canvas
                bgDrawable.draw(canvas);
            } else {
                //does not have background drawable, then draw white background on the canvas
                canvas.drawColor(Color.WHITE);
            }
            // draw the view on the canvas
            view.draw(canvas);
            //return the bitmap
            return returnedBitmap;
        }catch (Exception e){
            // Global.logError("getBitmapFromView", e);
        }
        return null;
    }
    public void saveImageToExternal(Bitmap bm) {
        //Create Path to save Image
        File createFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Zambo");
        if(!createFolder.exists())
            createFolder.mkdir();
        File saveImage = new File(createFolder,System.currentTimeMillis()+".png");
        try {
            OutputStream outputStream = new FileOutputStream(saveImage);
            bm.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setting screenshot in imageview
        String filePath = saveImage.getPath();
        sharePath = filePath;

    }
    private void share(String sharePath){

        Log.d("ffff",sharePath);
        File file = new File(sharePath);
        //  Uri uri = Uri.fromFile(file);
        Uri uri= FileProvider.getUriForFile(getApplicationContext(),"com.efficientindia.zambopay"+".provider",file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent .setType("image/*");
        intent .putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getApplicationContext().startActivity(intent );

    }

//    public boolean isStoragePermissionGranted() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//
//                try {
//                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                    m.invoke(null);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                }
//
////                Toast.makeText(getApplicationContext(), "Permission is granted", Toast.LENGTH_SHORT).show();
//                Log.v("TAG", "Permission is granted");
//                return true;
//            } else {
//                Toast.makeText(getApplicationContext(), "Permission is revoked", Toast.LENGTH_SHORT).show();
//                Log.v("TAG", "Permission is revoked");
//                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                return false;
//            }
//        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("TAG", "Permission is granted");
//            return true;
//        }
//    }

    private void notificationDialog() {
        mHandler=new Handler();
        mProgressBar= new ProgressDialog(TransactionReceipt.this);
        mProgressBar.setTitle("Download");
        mProgressBar.setMessage("Receipt Downloading...");
        mProgressBar.setMax(100);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    final int currentProgressCount = i;
                    try {
                            Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Update the value background thread to UI thread
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(currentProgressCount);
                            if(mProgressBar.getProgress() == mProgressBar.getMax()){
                                mProgressBar.dismiss();
                                Toast.makeText(TransactionReceipt.this, "Receipt Download", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED  ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean writePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writePermission) {
                        Toast.makeText(this, "Permission Granted, Now you can access storage.", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Permission Denied, You cannot access storage.", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to All the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(TransactionReceipt.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
