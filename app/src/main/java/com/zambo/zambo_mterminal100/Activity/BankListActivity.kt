package com.zambo.zambo_mterminal100.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.Adapter.BankListAdapter
import com.zambo.zambo_mterminal100.Interface.Apiinterface
import com.zambo.zambo_mterminal100.R
import com.zambo.zambo_mterminal100.model.BankList
import com.zambo.zambo_mterminal100.model.DeviceInfo
import kotlinx.android.synthetic.main.app_bar_main.*
import org.simpleframework.xml.Serializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BankListActivity : AppCompatActivity(){
    var arrayList:ArrayList<BankList>?=null
    var bankinno:ArrayList<BankList>?=null
    var serializer: Serializer? = null
    var url:String?="https://www.zambo.in/prime/aeps/"
    var bank_list:RecyclerView?=null
    var adapter:BankListAdapter?=null
    var recyclerView :RecyclerView?=null
    var searchView: SearchView?=null
    var progressDialog: ProgressDialog?=null
    var value:String?=null
    var back:ImageView?=null
    var searchBank:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_list)
        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
////        supportActionBar!!.title="Search Bank"
        progressDialog= ProgressDialog(this)
        back = findViewById(R.id.back)
        back!!.setOnClickListener(View.OnClickListener {
            if (SplashActivity.getPreferences("BankList","").equals("AEPS")) {
                intent = Intent(this@BankListActivity, AepsActivity::class.java)
                startActivity(intent)
            }else if (SplashActivity.getPreferences("BankList","").equals("AadharPay")) {
                intent = Intent(this@BankListActivity, AadharPay::class.java)
                startActivity(intent)
            }else if (SplashActivity.getPreferences("BankList","").equals("Mini")) {
                intent = Intent(this@BankListActivity, Mini_Statement::class.java)
                startActivity(intent)
            }
        })
        searchBank = findViewById(R.id.searchBank)
        searchBank!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                //after the change calling the method and passing the search input
                adapter!!.getFilter()!!.filter(editable.toString())
            }
        })


        recyclerView = findViewById(R.id.bank_list) as RecyclerView
//        recyclerView!!.layoutManager = LinearLayoutManager(this@BankListActivity, RecyclerView.HORIZONTAL, false)
//        bank_list=findViewById(R.id.bank_list)
//        searchView=findViewById(R.id.search_view)
        getbanklist()
//        serializer = Persister()


//        searchView!!.setOnQueryTextListener(androidx.appcompat.widget.SearchView.OnQueryTextListener(){
//
//        })

//        detectdevice()

    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.main_menu, menu)
//        val search = menu.findItem(R.id.search)
//        searchView = MenuItemCompat.getActionView(search) as SearchView
//        search(searchView!!)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun search(searchView: SearchView): Boolean {
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                adapter!!.getFilter()!!.filter(query)
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                adapter!!.getFilter()!!.filter(newText)
//                return true
//            }
//        })
//        return true
//    }
//    override fun onBackPressed() {
//        // close search view on back button pressed
//        if (!searchView!!.isIconified) {
//            searchView!!.isIconified = true
//            return
//        }
//
//    }
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.main_menu,menu)
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        searchView = menu!!.findItem(R.id.action_search).actionView as SearchView
//        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//        searchView!!.maxWidth = Int.MAX_VALUE
//
//        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
////                        if (arrayList!!.contains(query)) {
//                adapter!!.filter!!.filter(query)
////                        } else {
////                            Toast.makeText(this@BankListActivity, "No Match found", Toast.LENGTH_LONG).show()
////                        }
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                adapter!!.filter!!.filter(newText)
//                //    adapter.getFilter().filter(newText);
//                return true
//            }
//        })
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id=item!!.itemId
//        return if (id == R.id.action_search){
//            true
//        }else super.onOptionsItemSelected(item)
//    }
//
//        override fun onBackPressed() {
//        if (!searchView!!.isIconified()){
//            searchView!!.isIconified=true
//            return
//        }
//        super.onBackPressed()
//    }

    fun getbanklist(){
        progressDialog!!.show();
     //   Toast.makeText(this@BankListActivity,"",Toast.LENGTH_SHORT).show()
        var retrofit= Retrofit.Builder().baseUrl(url!!).addConverterFactory(GsonConverterFactory.create()).build()
        var apiinterface=retrofit.create(Apiinterface::class.java)
        var callback=apiinterface.getprimebanklist()
        callback.enqueue(object: Callback<List<BankList>> {
            override fun onFailure(call: Call<List<BankList>>, t: Throwable) {
                progressDialog!!.dismiss()
            }

            override fun onResponse(call: Call<List<BankList>>, response: Response<List<BankList>>) {
                progressDialog!!.dismiss()
                arrayList=ArrayList()
                var bankList:BankList

                for(i in response.body()!!.indices){
                    bankList=BankList()
                    bankList!!.setBankName(response.body()!!.get(i).getBankName()!!)
                    bankList!!.setIinno(response.body()!!.get(i).getIinno()!!)
                    arrayList!!.add(bankList)

                }
                recyclerView!!.layoutManager= LinearLayoutManager(this@BankListActivity,LinearLayoutManager.VERTICAL,false)
                adapter=BankListAdapter(arrayList!!,this@BankListActivity)
                recyclerView!!.adapter=adapter

//                val jsonResponse: JSONResponse? = response.body()
//                arrayList = ArrayList(Arrays.asList(jsonResponse.getAndroid()))
//                adapter = BankListAdapter(arrayList!!,this@BankListActivity)
//                recyclerView!!.setAdapter(adapter)

            }
        })

    }

    fun detectdevice(){
        try {
            val intent = Intent()
            intent.action = "in.gov.uidai.rdservice.fp.INFO"
            startActivityForResult(intent, 1)
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
//                        Toast.makeText(this@BankListActivity,"not null",Toast.LENGTH_SHORT).show()
                        val result = data.getStringExtra("DEVICE_INFO")
                        val rdService = data.getStringExtra("RD_SERVICE_INFO")
                        var display = ""
                        if (rdService != null) {
                            display = "RD Service Info :\n$rdService\n\n"
                        }
                        if (result != null) {
                            val info = serializer!!.read(DeviceInfo::class.java, result)
//                            display = (display + "Device Code: " + info.dc + "\n\n"
////                                    + "Serial No: " + info.srno + "\n\n"
//                                    + "dpId: " + info.dpId + "\n\n"
//                                    + "MC: " + info.mc + "\n\n"
//                                    + "MI: " + info.mi + "\n\n"
//                                    + "rdsId: " + info.rdsId + "\n\n"
//                                    + "rdsVer: " + info.rdsVer)
//                            display += "Device Info :\n$result"
                            // setText(display)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Error while deserialze device info", e)
                }

            }
            2 -> if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        val result = data.getStringExtra("PID_DATA")
                        if (result != null) {
//                            pidData = serializer.read(PidData::class.java, result)
//                            setText(result)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Error", "Error while deserialze pid data", e)
                }

            }
        }
    }

//    override fun onQueryTextSubmit(query: String?): Boolean {
//        return false
//    }
//
//    override fun onQueryTextChange(newText: String?): Boolean {
//        val userInput = newText!!.toLowerCase()
//        val newList = ArrayList<BankList>()
//        for (name in arrayList!!){
//            if (name.getBankName()!!.toLowerCase().contains(userInput)){
//                newList.add(name)
//            }
//        }
//        adapter!!.updateList(newList)
//        return true
//    }
}
