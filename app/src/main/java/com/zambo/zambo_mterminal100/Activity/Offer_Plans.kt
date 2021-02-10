package com.zambo.zambo_mterminal100.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.Adapter.OfferListAdapter
import com.zambo.zambo_mterminal100.AppConfig.PrefManager
import com.zambo.zambo_mterminal100.Interface.Apiinterface
import com.zambo.zambo_mterminal100.R
import com.zambo.zambo_mterminal100.model.Record
import com.zambo.zambo_mterminal100.model.Roffer
import com.zambo.zambo_mterminal100.model.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Offer_Plans : AppCompatActivity() {
    var arrayList:ArrayList<Record>?=null
    var url:String?="https://www.zambo.in/mobileapi/"
    var progressDialog: ProgressDialog?=null
    var recyclerView :RecyclerView?=null
    var adapter: OfferListAdapter?=null
    var userData:UserData?=null
    var backlogo:ImageView?=null
    var back:ImageView?=null
    var Pnumber = SplashActivity.getPreferences("mnumber", null)
    var opraterid = SplashActivity.getPreferences("operateriid", null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer__plans)
        progressDialog= ProgressDialog(this)
        userData = PrefManager.getInstance(this@Offer_Plans).userData
        recyclerView = findViewById(R.id.offerRecycler) as RecyclerView
        backlogo = findViewById(R.id.backlogo) as ImageView
//        Toast.makeText(this@Offer_Plans,Pnumber+"\n"+opraterid,Toast.LENGTH_SHORT).show()
        getbanklist()

        back = findViewById(R.id.cancel)
        back!!.setOnClickListener(View.OnClickListener {
                intent = Intent(this@Offer_Plans, CustomerHomeActivity::class.java)
                startActivity(intent)
        })
    }

    fun getbanklist(){
        progressDialog!!.show();
        //   Toast.makeText(this@BankListActivity,"",Toast.LENGTH_SHORT).show()
        var retrofit= Retrofit.Builder().baseUrl(url!!).addConverterFactory(GsonConverterFactory.create()).build()
        var apiinterface=retrofit.create(Apiinterface::class.java)
        var callback=apiinterface.getofferlist(userData!!.token,Pnumber,opraterid,userData!!.userId)
        callback.enqueue(object: Callback<Roffer> {
            override fun onFailure(call: Call<Roffer>, t: Throwable) {
                progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<Roffer>, response: Response<Roffer>) {
                progressDialog!!.dismiss()
                arrayList=ArrayList()
                var bankList:Record
                if (response.body()!!.status.equals(0)) {
                for (i in response.body()!!.records.indices) {
                    bankList = Record()
                    bankList!!.setRs(response.body()!!.records.get(i).rs!!)
                    bankList!!.setDesc(response.body()!!.records.get(i).desc!!)
                    arrayList!!.add(bankList)

                }
                recyclerView!!.layoutManager = LinearLayoutManager(this@Offer_Plans, LinearLayoutManager.VERTICAL, false)
                adapter = OfferListAdapter(arrayList!!, this@Offer_Plans)
                recyclerView!!.adapter = adapter
                }else{
                    backlogo!!.visibility = View.VISIBLE
                    Toast.makeText(this@Offer_Plans,response.body()!!.message,Toast.LENGTH_SHORT).show()
                }
//                val jsonResponse: JSONResponse? = response.body()
//                arrayList = ArrayList(Arrays.asList(jsonResponse.getAndroid()))
//                adapter = BankListAdapter(arrayList!!,this@BankListActivity)
//                recyclerView!!.setAdapter(adapter)

            }
        })

    }
}