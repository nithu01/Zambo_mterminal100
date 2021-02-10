package com.zambo.zambo_mterminal100.Fragment;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.zambo.zambo_mterminal100.Adapter.WalletHistoryAdapter;
import com.zambo.zambo_mterminal100.AppConfig.AppConfig;
import com.zambo.zambo_mterminal100.AppConfig.AppController;
import com.zambo.zambo_mterminal100.AppConfig.Configuration;
import com.zambo.zambo_mterminal100.AppConfig.Constant;
import com.zambo.zambo_mterminal100.AppConfig.PrefManager;
import com.zambo.zambo_mterminal100.R;
import com.zambo.zambo_mterminal100.model.UserData;
import com.zambo.zambo_mterminal100.model.WalletHistory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FragmentWalletHistory extends Fragment implements WalletHistoryAdapter.WalletHistoryAdapterListener {
    private static final String TAG = FragmentRechargeHistory.class.getSimpleName();
    private RecyclerView requestListTransaction;
    private RelativeLayout rlNoData;
    private ImageView imgData;
    private TextView txtData;
    private ProgressDialog progressDialog;
    EditText searchView;

    private List<WalletHistory> listData;
    private WalletHistoryAdapter loadListAdapter;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recharge_history,container,false);
        rlNoData=view.findViewById(R.id.rl_wl_trans_recharge);
        imgData=view.findViewById(R.id.img_wl_trans_recharge);
        txtData=view.findViewById(R.id.txt_wl_trans_recharge);
        searchView=view.findViewById(R.id.recharge_search);
        searchView.setVisibility(View.GONE);
        progressDialog=new ProgressDialog(getActivity());
        requestListTransaction=view.findViewById(R.id.recyclerview_transaction_recharge);
        UserData userData= PrefManager.getInstance(getActivity()).getUserData();
        String uid=userData.getUserId();
        if (Configuration.hasNetworkConnection(Objects.requireNonNull(getActivity()))){
            getTransaction(uid);
        }else {
            rlNoData.setVisibility(View.VISIBLE);
            imgData.setImageResource(R.drawable.nointernet);
            txtData.setText(R.string.no_internet);
            requestListTransaction.setVisibility(View.GONE);
            // Toast.makeText(getActivity(),"check your internet connection",Toast.LENGTH_LONG).show();
        }
        listData = new ArrayList<>();
        loadListAdapter = new WalletHistoryAdapter(getActivity(), listData, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        requestListTransaction.setLayoutManager(mLayoutManager);
        requestListTransaction.setItemAnimator(new DefaultItemAnimator());
        requestListTransaction.setAdapter(loadListAdapter);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                loadListAdapter.getFilter().filter(s);
            }
        });
        return view;
    }

    private void getTransaction(final String uid) {
        String tag_string_req = "fund_req";

        Configuration.showDialog("Please wait...",progressDialog);


        StringRequest stringRequest=new StringRequest(Request.Method.POST,
                AppConfig.WALLET_STATEMENT,
                response -> {

                    Log.d(TAG,"Wallet RESPONSED"+response);

                    try {

                        JSONObject jsonObject=new JSONObject(response);
                        String responseCode=jsonObject.getString("status");

                        if (responseCode.equalsIgnoreCase("1")){
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            if (jsonArray.isNull(0)){
                                rlNoData.setVisibility(View.VISIBLE);
                                imgData.setImageResource(R.drawable.norequest);
                                txtData.setText("No Transaction");
                                requestListTransaction.setVisibility(View.GONE);
                            }else {
                                rlNoData.setVisibility(View.GONE);
                                requestListTransaction.setVisibility(View.VISIBLE);
                                List<WalletHistory> items = new Gson().fromJson(jsonArray.toString(),
                                        new TypeToken<List<WalletHistory>>() {
                                        }.getType());

                                listData.clear();
                                listData.addAll(items);
                                loadListAdapter.notifyDataSetChanged();

                            }
                        }else {
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
                    Toast.makeText(getActivity(),"Try again",Toast.LENGTH_SHORT).show();
                })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put(Constant.U_UID,uid);
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
}
