package com.zambo.zambo_mterminal100.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.Activity.HistoryActivity;
import com.zambo.zambo_mterminal100.Activity.MainActivity;
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

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class HistoryFragment extends Fragment implements View.OnClickListener,
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
//    ImageView download;
    TextView start,end;
    ImageView startimg,endimg,downloadimg;
    Handler mHandler;
    private Bitmap bitmap;
    LinearLayout Slayout,Elayout;
    private String sharePath="no";
    ProgressDialog mProgressBar;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Button date,proceed;
    int mYear,mMonth,mDay;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        UserData userData = PrefManager.getInstance(getActivity()).getUserData();
//        download = view.findViewById(R.id.download);
        Slayout = view.findViewById(R.id.startLayout);
        Elayout = view.findViewById(R.id.endLayout);
        date=view.findViewById(R.id.date);
        start = view.findViewById(R.id.start);
        end = view.findViewById(R.id.end);
        startimg = view.findViewById(R.id.img_start);
        endimg = view.findViewById(R.id.img_end);
        proceed = view.findViewById(R.id.proceed);
      //Toast.makeText(getContext(), ""+formattedDate, Toast.LENGTH_SHORT).show();
        rlNoData = view.findViewById(R.id.rl_wl_trans_recharge);
        imgData = view.findViewById(R.id.img_wl_trans_recharge);
        txtData = view.findViewById(R.id.txt_wl_trans_recharge);
        imgBack = view.findViewById(R.id.img_back_history);
        lnTitle = view.findViewById(R.id.ln_title_recharge);
        downloadimg = view.findViewById(R.id.download);
        progressDialog = new ProgressDialog(getContext());
        requestListTransaction = view.findViewById(R.id.recyclerview_customer_history);
        imgBack.setOnClickListener(this);
        tabLayout = view.findViewById(R.id.tablayout_transaction);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        getCustomerHistory(userData.getuType(), userData.getUserId(),formattedDate);
       // viewPager = view.findViewById(R.id.viewpager_transaction);
        date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                rlNoData.setVisibility(View.GONE);
//                                imgData.setVisibility(View.GONE);
//                                txtData.setVisibility(View.GONE);
                                getCustomerHistory(userData.getuType(), userData.getUserId(),date.getText().toString());
                                //Toast.makeText(getContext(), ""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Slayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                start.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                rlNoData.setVisibility(View.GONE);
//                                imgData.setVisibility(View.GONE);
//                                txtData.setVisibility(View.GONE);
//                                getCustomerHistory(userData.getuType(), userData.getUserId(),date.getText().toString());
                                //Toast.makeText(getContext(), ""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        Elayout .setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                end.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                rlNoData.setVisibility(View.GONE);
//                                imgData.setVisibility(View.GONE);
//                                txtData.setVisibility(View.GONE);
//                                getCustomerHistory(userData.getuType(), userData.getUserId(),date.getText().toString());
                                //Toast.makeText(getContext(), ""+dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, Toast.LENGTH_SHORT).show();
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHistory(userData.getuType(), userData.getUserId(),start.getText().toString(),end.getText().toString());
            }
        });
        downloadimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop = new PopupMenu(getContext(),downloadimg);
                pop.getMenuInflater().inflate(R.menu.history_menu,pop.getMenu());

                //registering popup with OnMenuItemClickListener
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(TransactionReceipt.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        int id = item.getItemId();
                        switch (id){
                            case R.id.download_miniStatement:
//                                if (!checkPermission()) {
//                                    requestPermission();
//                                } else {
                                    notificationDialog();
                                    bitmap = loadBitmapFromView(requestListTransaction,requestListTransaction.getWidth(),requestListTransaction.getHeight());
                                    createPdf();
//                                }
                                return true;
                            case R.id.share_miniStatement:
//                                if (!checkPermission()) {
//                                    requestPermission();
//                                } else {
                                    bitmap = loadBitmapFromView(requestListTransaction,requestListTransaction.getWidth(),requestListTransaction.getHeight());
                                    createPdf();
                                    if (!sharePath.equals("no")) {
                                        openGeneratedPDF(sharePath);
                                    }
//                                }
                                return true;
                        }
                        return true;
                    }
                });
                pop.show();
            }
        });
        //UserData userData= PrefManager.getInstance(getActivity()).getUserData();
        if (!Configuration.hasNetworkConnection(getActivity())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Configuration.openPopupUpDownBack(getActivity(), R.style.Dialod_UpDown, "main", "internetError",
                        "No internet connectivity");
            }
        }

        if (userData.getuType().equalsIgnoreCase("CS")) {
            tabLayout.setVisibility(View.GONE);
           // viewPager.setVisibility(View.GONE);
            lnTitle.setVisibility(View.VISIBLE);
            requestListTransaction.setVisibility(View.VISIBLE);
            if (Configuration.hasNetworkConnection(getActivity())) {
              getCustomerHistory(userData.getuType(), userData.getUserId(),date.getText().toString());
            } else {
                rlNoData.setVisibility(View.VISIBLE);
                imgData.setImageResource(R.drawable.nointernet);
                txtData.setText(R.string.no_internet);
                requestListTransaction.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDownBack(getActivity(), R.style.Dialod_UpDown, "main", "internetError",
                            "No internet connectivity");
                }
            }
        } else {
            tabLayout.setVisibility(View.VISIBLE);
//            viewPager.setVisibility(View.VISIBLE);
            lnTitle.setVisibility(View.VISIBLE);
            requestListTransaction.setVisibility(View.VISIBLE);
           /* tabLayout.addTab(tabLayout.newTab().setText("Recharge History"));
            tabLayout.addTab(tabLayout.newTab().setText("Bill Payment History"));*/
            tabLayout.addTab(tabLayout.newTab().setText("Wallet Transaction"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
        listData = new ArrayList<>();
        loadListAdapter = new WalletHistoryAdapter(getActivity(), listData, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        requestListTransaction.setLayoutManager(mLayoutManager);
        requestListTransaction.setItemAnimator(new DefaultItemAnimator());
        requestListTransaction.setAdapter(loadListAdapter);
//        download.setOnClickListener(view1 -> {
//          //  download(userData.getuType(), userData.getUserId());
//            createExcelVendor(listData);
//        });
        return view;
    }

//    @Override
//    public void onTabSelected(TabLayout.Tab tab) {
//        viewPager.setCurrentItem(tab.getPosition());
//    }
//
//    @Override
//    public void onTabUnselected(TabLayout.Tab tab) {
//
//    }
//
//    @Override
//    public void onTabReselected(TabLayout.Tab tab) {
//
//    }

    @Override
    public void onClick(View v) {
        if (v == imgBack) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            //finish();

        }
    }

    public void onBackPressed() {
//        Fragment fragment=new ProfileFragment();
//        FragmentTransaction fragmentTransaction1=getChildFragmentManager().beginTransaction();
//        fragmentTransaction1.setCustomAnimations(R.anim.push_left_in,R.anim.push_left_out);
//        fragmentTransaction1.replace(R.id.frame_main,fragment);
//        fragmentTransaction1.commit();


        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

    }

    private void getCustomerHistory(String userType, String userId,String date) {
        String tag_string_req = "fund_req";
      //  Toast.makeText(getContext(), ""+date, Toast.LENGTH_SHORT).show();
        Configuration.showDialog("Please wait...", progressDialog);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.WALLET_STATEMENT,
                response -> {

                    Log.d(TAG, "Wallet RESPONSED" + response + " --->" + userType + " ---->" + userId);

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)) {
                                rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                              //  txtData.setText("No Transaction");
                                requestListTransaction.setVisibility(View.GONE);
                            } else {
                                rlNoData.setVisibility(View.GONE);
                                requestListTransaction.setVisibility(View.VISIBLE);
                                List<WalletHistory> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<WalletHistory>>() {
                                        }.getType());
                               // rl_wl_trans_recharge.set

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();
                                //  createExcelVendor(listData);
                            }
                        } else {
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
                        Configuration.openPopupUpDownBack(getActivity(), R.style.Dialod_UpDown, "main", "error",
                                "Something went wrong\n Try after sometime");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.Date,date);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }

    private void getHistory(String userType, String userId,String start,String end) {
        String tag_string_req = "fund_req";
        //  Toast.makeText(getContext(), ""+date, Toast.LENGTH_SHORT).show();
        Configuration.showDialog("Please wait...", progressDialog);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.WALLET_STATEMENT1,
                response -> {

                    Log.d(TAG, "Wallet RESPONSED" + response + " --->" + userType + " ---->" + userId);

                    try {

                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)) {
                                rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                //  txtData.setText("No Transaction");
                                requestListTransaction.setVisibility(View.GONE);
                            } else {
                                rlNoData.setVisibility(View.GONE);
                                requestListTransaction.setVisibility(View.VISIBLE);
                                List<WalletHistory> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<WalletHistory>>() {
                                        }.getType());
                                // rl_wl_trans_recharge.set

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();
                                //  createExcelVendor(listData);
                            }
                        } else {
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
                        Configuration.openPopupUpDownBack(getActivity(), R.style.Dialod_UpDown, "main", "error",
                                "Something went wrong\n Try after sometime");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.StartDate,start);
                params.put(Constant.EndDate,end);
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

    private void createExcelVendor(List<WalletHistory> day_sales) {
        //
        //   Toast.makeText(getContext(), "createexcelvender", Toast.LENGTH_SHORT).show();
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Transaction");

        HSSFRow row = sheet.createRow((short) 0);
        row.createCell((short) 0).setCellValue("Date");
        row.createCell((short) 1).setCellValue("Transaction Id");
        row.createCell((short) 2).setCellValue("Cr./Dr.");
        row.createCell((short) 3).setCellValue("Available Balance");
        row.createCell((short) 4).setCellValue("Status");

        int i = 1;
        for (WalletHistory user : day_sales) {
            HSSFRow row1 = sheet.createRow((short) i);
            row1.createCell((short) 0).setCellValue(user.getDate());
            row1.createCell((short) 1).setCellValue(user.getTransactionId());
            row1.createCell((short) 2).setCellValue(user.getType());
            row1.createCell((short) 3).setCellValue(user.getNewBalance());
            row1.createCell((short) 4).setCellValue(user.getStatus());

            i++;

        }


        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "transaction.xls");

        if (outputFile.exists()) {
            try {
                outputFile.delete();
            } catch (Exception e) {
            }
        }
        if (!outputFile.exists()) {
            try {
                outputFile.createNewFile();
            } catch (Exception e) {
            }
        }
        try {

            if (isStoragePermissionGranted()) {
              //  Toast.makeText(getContext(), "granted", Toast.LENGTH_SHORT).show();
                FileOutputStream fileOut = new FileOutputStream(outputFile);
                wb.write(fileOut);
                fileOut.close();
                Toast.makeText(getContext(), "Excel is downloaded in your phone internal memory as the name of Downloadtransaction.xls", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //  File file = new File("/storage/emulated/0/Samsung"+File.separator + race.getName()+".xls");
                Uri path = FileProvider.getUriForFile(getActivity(), "com.efficientindia.zambopay.fileprovider", outputFile);


                intent.setDataAndType(path, "application/vnd.ms-excel");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivity(intent);
            }
//
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    public void download(String userType, String userId,String date) {
        String tag_string_req = "fund_req";

        Configuration.showDialog("Please wait...", progressDialog);


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.WALLET_STATEMENT,
                response -> {

                    Log.d(TAG, "Wallet RESPONSED" + response + " --->" + userType + " ---->" + userId);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String responseCode = jsonObject.getString("status");
                        if (responseCode.equalsIgnoreCase("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)) {
                                rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");
                                requestListTransaction.setVisibility(View.GONE);
                            } else {
                                rlNoData.setVisibility(View.GONE);
                                requestListTransaction.setVisibility(View.VISIBLE);
                                List<WalletHistory> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<WalletHistory>>() {
                                        }.getType());
                                //  Toast.makeText(getContext(), "Download", Toast.LENGTH_SHORT).show();
                                //   listData.clear();
                                // listData.addAll(items);
                                List<WalletHistory> downloadlist = new ArrayList<>();
                                downloadlist.addAll(items);
                                //  loadListAdapter.notifyDataSetChanged();
                                createExcelVendor(downloadlist);
                            }
                        } else {
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
                        Configuration.openPopupUpDownBack(getActivity(), R.style.Dialod_UpDown, "main", "error",
                                "Something went wrong\n Try after sometime");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID, userId);
                params.put(Constant.Date,date);
                return params;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);

//        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://www.zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
//        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
//        Call<Example> call=apiinterface.download(userId);
//        call.enqueue(new Callback<Example>() {
//            @Override
//            public void onResponse(Call<Example> call, Response<Example> response) {
//                Example list=response.body();
//                WalletHistory walletHistory1=null;
//                if(list.getStatus()==0)
//                {
//                    for(int i=0;i<list.getData().size();i++)
//                    {
//                        walletHistory1=new WalletHistory();
//                        String date=response.body().getData().get(i).getId();
//                        String tranid=response.body().getData().get(i).getTransactionId();
//                        String amount=response.body().getData().get(i).getAmount();
//                        String bal=response.body().getData().get(i).getNewBalance();
//                        String status=response.body().getData().get(i).getStatus();
//                        Toast.makeText(getContext(), ""+tranid, Toast.LENGTH_SHORT).show();
//                        walletHistory1.setDate(date);
//                        walletHistory1.setAmount(amount);
//                        walletHistory1.setTransactionId(tranid);
//                        walletHistory1.setNewBalance(bal);
//                        walletHistory1.setStatus(status);
//
//                        listData.add(walletHistory1);
//                        createExcelVendor(listData);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Example> call, Throwable t) {
//
//            }
//        });
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getContext(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(getContext(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            //    Toast.makeText(getContext(), "Permission is granted", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
              //  Toast.makeText(getContext(), "Permission is revoked", Toast.LENGTH_SHORT).show();
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    public static Bitmap loadBitmapFromView(View view, int width, int height) {
        try {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            //Define a bitmap with the same size as the view
            Bitmap returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
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

    private void createPdf(){
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);
        File createFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"Zambo_Wallet");
        if(!createFolder.exists())
            createFolder.mkdir();
        File filePath = new File(createFolder,"Wallet_Statement_"+System.currentTimeMillis()+".pdf");
        // write the document content
//        String targetPdf = "/sdcard/pdffromlayout.pdf";
//        File filePath;
//        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
//        Toast.makeText(this, "PDF is created!!!", Toast.LENGTH_SHORT).show();

        String save = filePath.getPath();
        sharePath = save;


    }

    private void openGeneratedPDF(String sharePath){
        File file = new File(sharePath);
        if (file.exists())
        {
            Uri uri= FileProvider.getUriForFile(getContext(),"com.efficientindia.zambopay"+".provider",file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent .setType("application/pdf");
            intent .putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent );
//            Uri uri = FileProvider.getUriForFile(getApplicationContext(),"com.efficientindia.zambopay"+".provider",file);
//            Intent intent=new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setDataAndType(uri, "application/pdf");
//            try
//            {
//                getApplicationContext().startActivity(intent);
//            }
//            catch(ActivityNotFoundException e)
//            {
//                Toast.makeText(Mini_Statement.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
//            }
        }
    }

    private void notificationDialog() {
        mHandler=new Handler();
        mProgressBar= new ProgressDialog(getContext());
        mProgressBar.setTitle("Download");
        mProgressBar.setMessage("MiniStatement PDF Downloading...");
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
                                Toast.makeText(getContext(), "Wallet Statement PDF Download", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).start();
    }
}
