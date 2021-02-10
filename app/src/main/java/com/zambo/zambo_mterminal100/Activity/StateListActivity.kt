package com.zambo.zambo_mterminal100.Activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.Adapter.StateListAdapter
import com.zambo.zambo_mterminal100.Fragment.FragmentRegistration
import com.zambo.zambo_mterminal100.Interface.Apiinterface
import com.zambo.zambo_mterminal100.R
import com.zambo.zambo_mterminal100.model.StateList
import com.zambo.zambo_mterminal100.model.States
import kotlinx.android.synthetic.main.app_bar_main.*
import org.simpleframework.xml.Serializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StateListActivity : AppCompatActivity() {
    var arrayList:ArrayList<StateList>?=null
    var bankinno:ArrayList<StateList>?=null
    var serializer: Serializer? = null
    var url:String?="https://www.zambo.in/mobileapi/"
    var bank_list: RecyclerView?=null
    var adapter: StateListAdapter?=null
    var recyclerView : RecyclerView?=null
    var searchView: SearchView?=null
    var progressDialog: ProgressDialog?=null
    var value:String?=null
    var back: ImageView?=null
    var searchState: EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_state_list)
        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
////        supportActionBar!!.title="Search Bank"
        progressDialog= ProgressDialog(this)
        back = findViewById(R.id.back)
        back!!.setOnClickListener(View.OnClickListener {
                intent = Intent(this@StateListActivity, FragmentRegistration::class.java)
                startActivity(intent)

        })
        searchState = findViewById(R.id.searchState)
        searchState!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                adapter!!.getFilter()!!.filter(editable.toString())
            }
        })


        recyclerView = findViewById(R.id.state_list) as RecyclerView
//        recyclerView!!.layoutManager = LinearLayoutManager(this@BankListActivity, RecyclerView.HORIZONTAL, false)
//        bank_list=findViewById(R.id.bank_list)
//        searchView=findViewById(R.id.search_view)
        getStatelist()
//        serializer = Persister()


//        searchView!!.setOnQueryTextListener(androidx.appcompat.widget.SearchView.OnQueryTextListener(){
//
//        })

//        detectdevice()

    }

    fun getStatelist(){
        progressDialog!!.show();
        //   Toast.makeText(this@BankListActivity,"",Toast.LENGTH_SHORT).show()
        var retrofit= Retrofit.Builder().baseUrl(url!!).addConverterFactory(GsonConverterFactory.create()).build()
        var apiinterface=retrofit.create(Apiinterface::class.java)
        var callback=apiinterface.getstatelist()
        callback.enqueue(object: Callback<States> {
            override fun onFailure(call: Call<States>, t: Throwable) {
                progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<States>, response: Response<States>) {
                progressDialog!!.dismiss()
                arrayList=ArrayList()
                var bankList:StateList

                for(i in response.body()!!.data.indices){
                    bankList=StateList()
                    bankList!!.setName(response.body()!!.data.get(i).getName()!!)
                    bankList!!.setId(response.body()!!.data.get(i).getId()!!)
                    arrayList!!.add(bankList)

                }
                recyclerView!!.layoutManager= LinearLayoutManager(this@StateListActivity, LinearLayoutManager.VERTICAL,false)
                adapter=StateListAdapter(arrayList!!,this@StateListActivity)
                recyclerView!!.adapter=adapter

//                val jsonResponse: JSONResponse? = response.body()
//                arrayList = ArrayList(Arrays.asList(jsonResponse.getAndroid()))
//                adapter = BankListAdapter(arrayList!!,this@BankListActivity)
//                recyclerView!!.setAdapter(adapter)

            }
        })

    }

}
