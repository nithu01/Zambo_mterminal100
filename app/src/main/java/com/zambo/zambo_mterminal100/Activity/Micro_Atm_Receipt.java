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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Micro_Atm_Receipt extends AppCompatActivity {
    TextView receipt,date,BcName,BcId,BcLocation,bank,invoicenumber,tid,mid,refstan,rrn,clientrefid,cardno,txnamount,amount,bankremarks;
    ImageView menu,statusimg;
    Button button;
    private String sharePath="no";
    LinearLayout linearLayout,txnlayout;
    Handler mHandler;
    ProgressDialog mProgressBar;
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro__atm__receipt);
        receipt = findViewById(R.id.successful);
        txnlayout = findViewById(R.id.txnlayout);
        if (getIntent().getStringExtra("txnamount").equals("0")){
            receipt.setText("Balance Inquiry");
            txnlayout.setVisibility(View.GONE);
        }else {
            receipt.setText("Cash Withdrawal");
            txnlayout.setVisibility(View.VISIBLE);
        }
        statusimg = findViewById(R.id.statusimg);
        date = findViewById(R.id.time);
        date.setText(getIntent().getStringExtra("date"));
        BcName = findViewById(R.id.bc_name);
        BcName.setText(getIntent().getStringExtra("BcName"));
        BcId = findViewById(R.id.bc_id);
        BcId.setText(getIntent().getStringExtra("BcId"));
        BcLocation = findViewById(R.id.bc_location);
        BcLocation.setText(getIntent().getStringExtra("BcLocation"));
//        bank =findViewById(R.id.bank);
        invoicenumber = findViewById(R.id.invoice_number);
        invoicenumber.setText(getIntent().getStringExtra("invoicenumber"));
        tid = findViewById(R.id.terminal_id);
        tid.setText(getIntent().getStringExtra("tid"));
        mid = findViewById(R.id.mid);
        mid.setText(getIntent().getStringExtra("mid"));
        refstan = findViewById(R.id.ref_id);
        refstan.setText(getIntent().getStringExtra("refstan"));
        rrn = findViewById(R.id.rrn);
        rrn.setText(getIntent().getStringExtra("rrn"));
        clientrefid = findViewById(R.id.clientrefid);
        clientrefid.setText(getIntent().getStringExtra("clientrefid"));
        cardno = findViewById(R.id.card_number);
        cardno.setText(getIntent().getStringExtra("cardno"));
        txnamount = findViewById(R.id.txnamount);
        txnamount.setText(getIntent().getStringExtra("txnamount"));
        amount = findViewById(R.id.account_balance);
        amount.setText(getIntent().getStringExtra("amount"));
        bankremarks = findViewById(R.id.status);
        if (getIntent().getStringExtra("bankremarks").equals("Successful")) {
            bankremarks.setTextColor(getResources().getColor(R.color.green_text));
            statusimg.setImageDrawable(getDrawable(R.drawable.ic_check_circle_black_24dp));
        }else {
            bankremarks.setTextColor(getResources().getColor(R.color.red));
            statusimg.setImageDrawable(getDrawable(R.drawable.ic_cancel_red_24dp));
        }
        bankremarks.setText(getIntent().getStringExtra("bankremarks"));
        linearLayout = findViewById(R.id.transaction_layout);
        button = findViewById(R.id.ok);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Micro_Atm_Receipt.this,MainActivity.class);
                startActivity(intent);
            }
        });

        menu = findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(Micro_Atm_Receipt.this,menu);
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
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure, you want to close?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Micro_Atm_Receipt.this, MainActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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

    private void notificationDialog() {
        mHandler=new Handler();
        mProgressBar= new ProgressDialog(Micro_Atm_Receipt.this);
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
                                Toast.makeText(Micro_Atm_Receipt.this, "Receipt Download", Toast.LENGTH_SHORT).show();
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
        new android.app.AlertDialog.Builder(Micro_Atm_Receipt.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
