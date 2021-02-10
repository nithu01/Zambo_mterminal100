package com.zambo.zambo_mterminal100.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.Adapter.DeviceAdapter
import com.zambo.zambo_mterminal100.R

class DeviceActivity : AppCompatActivity() {


   var bank_list: RecyclerView?=null

    var recyclerView : RecyclerView?=null
    var adapter:DeviceAdapter?=null
    var arrayList:ArrayList<String>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)
        recyclerView=findViewById(R.id.device)
        arrayList=ArrayList()
        arrayList!!.add("Mantra")
        arrayList!!.add("Morpho")
        recyclerView!!.layoutManager=LinearLayoutManager(this@DeviceActivity,LinearLayoutManager.VERTICAL,false)
        adapter=DeviceAdapter(arrayList!!,this@DeviceActivity)
        recyclerView!!.adapter=adapter
    }

}
