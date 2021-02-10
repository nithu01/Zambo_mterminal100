package com.zambo.zambo_mterminal100.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.Activity.Dmt_Transfer;
import com.zambo.zambo_mterminal100.Activity.SplashActivity;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.Interface.Apiinterface;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.BeneListDmt;
import com.zambo.zambo_mterminal100.model.UserData;

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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.volley.VolleyLog.TAG;

public class BeneficiaryDmtAdapter extends RecyclerView.Adapter<BeneficiaryDmtAdapter.MyViewHolder>
        implements Filterable {

    private Context context;
    private List<BeneListDmt> loadList;
    private List<BeneListDmt> loadListFiltered;
    private BeneficiaryDmtAdapterListner listener;
    private ProgressDialog progressDialog;
    private String mobile;
    ImageView iv;
    private String sharePath="no";

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName,txtAccount,txtBank,txtIfsc;
        Button btnTransfer, btnVerify;
        CardView cardView;

        MyViewHolder(View view) {
            super(view);
            txtName=view.findViewById(R.id.txt_name_send);
            txtAccount=view.findViewById(R.id.txt_account);
            txtBank=view.findViewById(R.id.txt_bank);
            txtIfsc=view.findViewById(R.id.txt_ifsc);
            btnTransfer=view.findViewById(R.id.btn_transfer);
          //  btnVerify =view.findViewById(R.id.btn_verify_beneficiary);
            cardView=view.findViewById(R.id.cardview_u);
        }
    }


    public BeneficiaryDmtAdapter(Context context, List<BeneListDmt> contactList) {
        this.context = context;
        //this.listener = listener;
        this.loadList = contactList;
        this.loadListFiltered = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_beneficiary, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final BeneListDmt contact = loadListFiltered.get(position);
        UserData userData=PrefManager.getInstance(context).getUserData();
        progressDialog = new ProgressDialog(context,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        try{
            mobile= SplashActivity.getPreferences(Constant.MOBILE_SENDER,"");
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.txtName.setText(contact.getBenename());
        holder.txtAccount.setText("A/C No.: "+contact.getBeneaccno());
        holder.txtIfsc.setText("IFSC: "+contact.getIfsc());
        holder.txtBank.setText("Bank: "+contact.getBankname());

//        holder.btnVerify.setVisibility(View.VISIBLE);
        holder.btnTransfer.setVisibility(View.VISIBLE);
       /* if (contact.getStatus().equalsIgnoreCase("NV")){
            holder.btnVerify.setVisibility(View.VISIBLE);
            holder.btnTransfer.setVisibility(View.GONE);
        }else if (contact.getStatus().equalsIgnoreCase("V")){
            holder.btnVerify.setVisibility(View.GONE);
            holder.btnTransfer.setVisibility(View.VISIBLE);
        }else {
            holder.btnVerify.setVisibility(View.GONE);
            holder.btnTransfer.setVisibility(View.GONE);
        }*/
        if (position%2==0){
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.card_color));
        }
//        holder.btnVerify.setOnClickListener(v -> {
//            resendOtpOne(mobile,userData.getUserId(),contact.getBeneaccno());
//        });
        holder.btnTransfer.setOnClickListener(v -> {
            final Dialog dialog=new Dialog(context);
            dialog.setTitle("Send to "+contact.getBenename());
            dialog.setContentView(R.layout.layout_amount_tranfer);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
           // final UserData userData= PrefManager.getInstance(context).getUserData();
            final RadioGroup radioGroup;
           // final RadioButton rbImps,rbNeft;
            radioGroup=dialog.findViewById(R.id.rg_type);
            TextView txtAcc=dialog.findViewById(R.id.txt_acc);
            TextView txtDialog=dialog.findViewById(R.id.dialog);
            txtDialog.setVisibility(View.GONE);
            txtAcc.setText("A/C No: "+contact.getBeneaccno());
            final TextView txtCancel=dialog.findViewById(R.id.txt_cancel);
            final EditText editAmount=dialog.findViewById(R.id.edittext_amount_view);
            Button btnSend1=dialog.findViewById(R.id.btn_send);
            radioGroup.setVisibility(View.GONE);

            txtCancel.setOnClickListener(v1 -> dialog.dismiss());
            btnSend1.setOnClickListener(v12 -> {
                if (Configuration.hasNetworkConnection(context)){
                    String amount=editAmount.getText().toString().trim();
                    progressDialog = new ProgressDialog(context,
                            R.style.AppTheme_Dark_Dialog);
                    progressDialog.setIndeterminate(true);
                    String limit=Dmt_Transfer.limit;

                    if (amount.isEmpty()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                    "Enter amount");
                        }else {
                            Toast.makeText(context,"Enter amount",Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                     if (Integer.valueOf(amount)<100){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                    "Invalid amount");
                        }
                    }else if (Configuration.hasNetworkConnection(context)){
                   //      Toast.makeText(context, ""+userData.getuType(), Toast.LENGTH_SHORT).show();
                        openDialog(userData.getUserId(),mobile,contact.getBeneaccno(),contact.getBenename(),contact.getIfsc(),
                                dialog,contact.getBankname(),contact.getId(),amount);

                    }else
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"internetError",
                                    "No internet Connection");
                        }else {
                            Toast.makeText(context,"check your internet connection",Toast.LENGTH_LONG).show();
                        }
                    }
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"internetError",
                                "No internet Connection");
                    }else {
                        Toast.makeText(context, "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.show();
        });
    }

    private void openDialog(String userId, String mobile, String beneaccno, String benename,
                            String ifsc, Dialog dialog, String bankname,String id, String amount) {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.layout_dialog_confirmation);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);
        dialg.getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.dialog_background));

        TextView txtMessage=dialg.findViewById(R.id.txt_dialog);
        TextView btnCancel=dialg.findViewById(R.id.btn_cancel_dialog);
        TextView btnContinue=dialg.findViewById(R.id.btn_continue_dialog);
        txtMessage.setText("Do you want to Transfer amount of amount to "+ benename +" (A/C No." +beneaccno+")."+"?"+
                "please continue for transfer otherwise cancel.");
        btnCancel.setOnClickListener(v12 -> dialg.dismiss());
        btnContinue.setOnClickListener(v1 -> {
            dialg.dismiss();
            Log.d("TAG","DIALOGSHOW");
            sendMoney(userId,mobile,beneaccno,benename,ifsc, dialog,amount,bankname,id);
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public int getItemCount() {
        return loadListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    loadListFiltered = loadList;
                } else {
                    List<BeneListDmt> filteredList = new ArrayList<>();
                    for (BeneListDmt row : loadList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBenename().toLowerCase().contains(charString.toLowerCase())
                                || row.getBeneaccno().contains(charSequence)||row.getBankname().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    loadListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = loadListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                loadListFiltered = (ArrayList<BeneListDmt>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BeneficiaryDmtAdapterListner {
        void onBeneficiarySelcted(BeneListDmt beneficiaryList);
    }
//    private void openScratchPopup()
//    {
////        final Dialog dialog=new Dialog(context);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        dialog.setContentView(R.layout.popup_scratch);
////        dialog.setCanceledOnTouchOutside(false);
////        dialog.setCancelable(false);
////        ScratchImageView scratchImageView=dialog.findViewById(R.id.scratch_card);
////        Button button=dialog.findViewById(R.id.btnok);
////        button.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                dialog.dismiss();
////                Intent intent=new Intent(context,MainActivity.class);
////                context.startActivity(intent);
////            }
////        });
//    }
//openPopup(message,status,tid,beneaccno,bankname,ifsc,benename);

    @SuppressLint("SetTextI18n")
    private void openPopup(String message, String status, String tid, String amount,String benename,String beneacco,String bankname,String ifsc,String userid) {

        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.popup_dmt);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        LinearLayout linearLayout=dialg.findViewById(R.id.relative_popup_recharge);
        LinearLayout li_transa=dialg.findViewById(R.id.linear_trans);
        LinearLayout li_amt=dialg.findViewById(R.id.linear_amt);
        LinearLayout li_date=dialg.findViewById(R.id.linear_date);
        LinearLayout li_bname=dialg.findViewById(R.id.linear_bname);
        LinearLayout li_bacc=dialg.findViewById(R.id.linear_bacc);
        LinearLayout li_banknam=dialg.findViewById(R.id.linear_bankname);
        LinearLayout li_ifscs=dialg.findViewById(R.id.ifscs);

        ImageView imageView =  dialg.findViewById(R.id.img_status_recharge);
        TextView txtStatus=dialg.findViewById(R.id.txt_status);
        TextView transaction_id=dialg.findViewById(R.id.transaction_id);
        TextView amnt=dialg.findViewById(R.id.amount);
        TextView date=dialg.findViewById(R.id.date);
        TextView bene_name=dialg.findViewById(R.id.b_name);
        TextView bene_accou=dialg.findViewById(R.id.b_accou);
        TextView bank_name=dialg.findViewById(R.id.bank_name);
        TextView ifscc=dialg.findViewById(R.id.ifsc);

//        TextView txt_id=dialg.findViewById(R.id.txt_trans);
//        TextView txt_amnt=dialg.findViewById(R.id.txt_amt);
//        TextView txt_date=dialg.findViewById(R.id.txt_date);
//        TextView txt_bname=dialg.findViewById(R.id.txt_bname);
//        TextView txt_bacc=dialg.findViewById(R.id.txt_acc);
//        TextView txt_baname=dialg.findViewById(R.id.txt_baname);
//        TextView txt_ifsc=dialg.findViewById(R.id.txt_ifsc);

        bene_name.setText(benename);
        bene_accou.setText(beneacco);
        bank_name.setText(bankname);
        ifscc.setText(ifsc);

        Date dates=new Date();
        transaction_id.setText(tid);
        amnt.setText(amount);
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        Button btnOk= dialg.findViewById(R.id.btn_okay);
        // txtTransactionId.setVisibility(View.GONE);
        Button btshare=dialg.findViewById(R.id.btn_share);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        date.setText(formattedDate);
        TextView txtTransactionId=dialg.findViewById(R.id.txt_status_recharge);
        if (status.equalsIgnoreCase("0")&& message.equalsIgnoreCase("success")){
            imageView.setImageResource(R.drawable.success);
            txtStatus.setText("Status : Success");
            if (SplashActivity.getPreferences("Language","").equals("English")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.speech);
                mediaPlayer.start();
            }else if (SplashActivity.getPreferences("Language","").equals("Hindi")) {
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.hindispeech);
                mediaPlayer.start();
            }
        }else if (status.equalsIgnoreCase("P")){
            imageView.setImageResource(R.drawable.pending);
            txtStatus.setText("Status : Pending");
        }else if (status.equals("1")){
            imageView.setImageResource(R.drawable.failed);
            txtStatus.setText("Status : Failed");
        }else{
            imageView.setImageResource(R.drawable.failed);
            txtStatus.setText("Insufficient Balance");
            transaction_id.setVisibility(View.GONE);
            amnt.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            bene_name.setVisibility(View.GONE);
            bene_accou.setVisibility(View.GONE);
            ifscc.setVisibility(View.GONE);
            bank_name.setVisibility(View.GONE);

            li_transa.setVisibility(View.GONE);
            li_amt.setVisibility(View.GONE);
            li_bacc.setVisibility(View.GONE);
            li_banknam.setVisibility(View.GONE);
            li_bname.setVisibility(View.GONE);
            li_ifscs.setVisibility(View.GONE);
            li_date.setVisibility(View.GONE);

//            txt_id.setVisibility(View.GONE);
//            txt_amnt.setVisibility(View.GONE);
//            txt_bacc.setVisibility(View.GONE);
//            txt_baname.setVisibility(View.GONE);
//            txt_bname.setVisibility(View.GONE);
//            txt_ifsc.setVisibility(View.GONE);
//            txt_date.setVisibility(View.GONE);

            txtTransactionId.setVisibility(View.GONE);
        }

        iv=dialg.findViewById(R.id.iv);

        btshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshotForView(dialg);
                if (!sharePath.equals("no")) {
                    if(isStoragePermissionGranted())
                    share(sharePath);
                    else
                    {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_SETTINGS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    }
                }
            }
        });

        txtTransactionId.setText(message);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equalsIgnoreCase("0"))
                {
                    dialg.dismiss();
//                    if(SplashActivity.getPreferences(Constant.USER_TYPE,"").equals("CS"))
//                    cashbackupdate(tid,userid,amount);
//                    else{
//                    }
                }else{
                    dialg.dismiss();

                }

            }
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        //screenShot(linearLayout);
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void openRateus() {
        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.rateus_popup);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        RatingBar ratingBar=dialg.findViewById(R.id.ratingBar);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW);

                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.efficientindia.zambopay"));

                context.startActivity(intent);

            }
        });

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void openOtpBeneficiary(String userId, String mobile, String bankAccount) {

        final Dialog dialg=new Dialog(context);
        dialg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialg.setContentView(R.layout.layout_otp_bene);
        dialg.setCanceledOnTouchOutside(false);
        dialg.setCancelable(false);

        RelativeLayout imgClose =  dialg.findViewById(R.id.rl_close);
        TextView txtResendOtp=dialg.findViewById(R.id.txt_resend_sender_otp_bene);
        TextView txtMessage=dialg.findViewById(R.id.txt_otp_sender_bene);
        final EditText editTextOtp=dialg.findViewById(R.id.edittext_sender_otp_bene);
        final Button btnVerify=dialg.findViewById(R.id.btn_verify_bene);

        txtResendOtp.setOnClickListener(v -> {
            if (Configuration.hasNetworkConnection(context)){
                resendOtp(mobile,txtMessage,userId);
            }
        });
        btnVerify.setOnClickListener(v -> {
            String otp=editTextOtp.getText().toString();
            if (otp.isEmpty()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                            "Enter otp sent to your mobile no");
                }
            }else if (Configuration.hasNetworkConnection(context)){
                otpBeneVerify(userId,mobile,bankAccount,otp,dialg);
            }else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"internetError",
                            "Enter otp sent to your mobile no");
                }
            }
        });

        imgClose.setOnClickListener(v -> dialg.dismiss());

        dialg.show();
        Window window = dialg.getWindow();
        assert window != null;
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    }

    private void resendOtp(final String mob, final TextView txtMessage, final String userId) {
        String tag_string_req = "resend_otp";
        progressDialog.setMessage("Sending...");
        progressDialog.show();
        try{
            Configuration.hideKeyboardFrom((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESEND_OTP_DMT, response -> {
                    Log.d(TAG, "resend otp Response: " + response);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("1")) {
                            txtMessage.setText(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e(TAG, "resend otp Error: " + error.getMessage());
                    progressDialog.dismiss();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mob);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void otpBeneVerify(String userId, String mobile, String bankAccount, String otp, Dialog dialg) {
        String tag_string_req = "add_bene";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        try{
            Configuration.hideKeyboardFrom((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.OTP_VERIFY_BENE, response -> {
                    Log.d(TAG, "Add Benificiary Response: " + response+" "+bankAccount);
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        if  (status.equals("1")) {
                            dialg.dismiss();
                            //getBenificiaryList(mobile);
                            Dmt_Transfer.getSenderInfo(mobile,userId);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"",
                                        "Beneficiary successfully verified");
                            }
                            // openOtpBeneficiary(userId,mobile,bankAccount);
                        }else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                        message);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.e(TAG, "Add Benificiary Error: " + error.getMessage());
                    progressDialog.dismiss();
                }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                params.put(Constant.BANK_ACCOUNT,bankAccount);
                params.put(Constant.BENE_OTP,otp);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void resendOtpOne(String mobile, String userId, String beneaccno) {
        String tag_string_req = "resend_otp";
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        try{
            Configuration.hideKeyboardFrom((Activity) context);
        }catch (Exception e){
            e.printStackTrace();
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.RESEND_OTP_DMT, response -> {
            Log.d(TAG, "resend otp Response: " + response);
            progressDialog.dismiss();
            try {
                JSONObject jsonObject1=new JSONObject(response);
                String status = jsonObject1.getString("status");
                String message=jsonObject1.getString("message");
                if (status.equalsIgnoreCase("1")){
                    openOtpBeneficiary(userId,mobile,beneaccno);
                }else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Configuration.openPopupUpDown(context,R.style.Dialod_UpDown,"error",
                                message);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.e(TAG, "resend otp Error: " + error.getMessage());
            progressDialog.dismiss();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.USER_ID,userId);
                params.put(Constant.SENDER_MOBILE,mobile);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void sendMoney(String userId, String mobile, String beneaccno, String benename,
                           String ifsc, Dialog dialog, String amount, String bankname, String id) {

        String tag_string_req = "transfer_money";
        progressDialog=new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Sending");
        progressDialog.show();
        Log.d("TAG","response"+userId+""+ mobile +""+beneaccno+""+benename+""+ifsc+""+bankname+" "+" "+ifsc+"" +id+" "+bankname+" "+amount);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.TRANSFER_MONEY, response -> {

                    Log.d(TAG, "transfer_money Response: " + response);
                    progressDialog.dismiss();
                    dialog.dismiss();
                    try {
                       // webservice.updateBalance(userId);
                        JSONObject jsonObject1=new JSONObject(response);
                        String status = jsonObject1.getString("status");
                        String message=jsonObject1.getString("message");
                        String tid = null;
                        if  (status.equals("0")) {
                            if (jsonObject1.has("tid")){
                                tid=jsonObject1.getString("TransactionId");
                            }
                            Log.d("TAG","successresponse");
                            openPopup(message,status,tid,amount,benename,beneaccno,bankname,ifsc,userId);
                        }else {
                            Log.d("TAG","errorresponse");
                            openPopup(message, status, tid,amount,benename,beneaccno,bankname,ifsc,userId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                     Log.e(TAG, "transfer_money Error: " + error.getMessage());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(Constant.U_UID,userId);
                params.put(Constant.BENEFICARY_ID,id);
                params.put(Constant.SENDER_MOBILE, mobile);
                params.put(Constant.ACCOUNT,beneaccno);
                params.put(Constant.NAME,benename);
                params.put(Constant.TRANS_IFSC,ifsc);
                params.put(Constant.BANK_NAME_DMT,bankname);
                params.put(Constant.TRANS_AMOUNT,amount);
                params.put(Constant.SOURCE,"API");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void share(String sharePath){

        Log.d("ffff",sharePath);
        File file = new File(sharePath);
        //  Uri uri = Uri.fromFile(file);
        Uri uri= FileProvider.getUriForFile(context,"com.efficientindia.zambopay",file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent .setType("image/*");
        intent .putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent );

    }
    public Bitmap takeScreenshotForView(Dialog view) {
//        view.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(view.getHeight(), View.MeasureSpec.EXACTLY));
//        view.layout((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getMeasuredWidth(), (int) view.getY() + view.getMeasuredHeight());
        Date date=new Date();

        View view1 = view.getWindow().getDecorView().getRootView();
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + date + ".jpeg";
        view1.setDrawingCacheEnabled(true);
        view1.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());


        File imageFile = new File(mPath);


        try {
            FileOutputStream outputStream = null;
            outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setting screenshot in imageview
        String filePath = imageFile.getPath();

        Bitmap ssbitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        iv.setImageBitmap(ssbitmap);
        sharePath = filePath;

        iv.setImageBitmap(bitmap);
        sharePath=filePath;
        return bitmap;
    }
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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

                Toast.makeText(context, "Permission is granted", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "Permission is granted");
                return true;
            } else {
                Toast.makeText(context, "Permission is revoked", Toast.LENGTH_SHORT).show();
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }
    public void cashbackupdate(String txtid,String userid,String amount)
    {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.scratchcard(txtid,userid,amount,"0");
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
               progressDialog.dismiss();
//               openScratchPopup(response.body().getCashback(),txtid,userid,amount);
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
//    private void openScratchPopup(String commision, String txtid, String userid, String txt_amount)
//    {
//        final Dialog dialog=new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_scratch);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.setCancelable(false);
//        ScratchTextView scratchImageView=dialog.findViewById(R.id.scratchimageview);
//        TextView textView=dialog.findViewById(R.id.textView);
//        textView.setText(commision);
//        Button button=dialog.findViewById(R.id.btnok);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                if(scratchImageView.isRevealed())
//                {
//                    usercashback(commision,txtid,userid,txt_amount);
//                }
//                Intent intent=new Intent(context,MainActivity.class);
//                context.startActivity(intent);
//            }
//        });
//
//        dialog.show();
//        Window window = dialog.getWindow();
//        assert window != null;
//        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//
//    }

    public void usercashback(String commision, String txtid, String userid, String txt_amount)
    {
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.show();

        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://zambo.in/mobileapi/").addConverterFactory(GsonConverterFactory.create()).build();
        Apiinterface apiinterface=retrofit.create(Apiinterface.class);
        Call<com.zambo.zambo_mterminal100.model.Response> call=apiinterface.cashbackupdate(userid,txtid,txt_amount,commision);
        call.enqueue(new Callback<com.zambo.zambo_mterminal100.model.Response>() {
            @Override
            public void onResponse(Call<com.zambo.zambo_mterminal100.model.Response> call, Response<com.zambo.zambo_mterminal100.model.Response> response) {
                progressDialog.dismiss();
                Toast.makeText(context, ""+response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<com.zambo.zambo_mterminal100.model.Response> call, Throwable t) {

            }
        });
    }
}
