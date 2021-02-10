package com.zambo.zambo_mterminal100.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.zambo.zambo_mterminal100.AppConfig.PrefManager
import com.zambo.zambo_mterminal100.Interface.Apiinterface
import com.zambo.zambo_mterminal100.model.Response
import com.zambo.zambo_mterminal100.model.UserData
import com.zambo.zambo_mterminal100.R
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FasttagActivity : AppCompatActivity() {
    var userData: UserData?=null
    var mobile: EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fasttag)
        mobile=findViewById(R.id.mobile)
        userData = PrefManager.getInstance(this@FasttagActivity).userData
        mobile!!.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(view: Editable?) {
                if(mobile!!.text.toString().length==10){
                    checkkyc()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }
    fun checkkyc() {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder().addHeader("token", userData!!.getToken()).build()
            chain.proceed(request)
        }
        var retrofit=Retrofit.Builder().baseUrl("https://zambo.in/axisdmt/fastag/").client(httpClient.build()).addConverterFactory(GsonConverterFactory.create()).build()
            var apiinterface=retrofit.create(Apiinterface::class.java)
            var call=apiinterface.checkkyc(userData!!.uid,mobile!!.text.toString(),"APP")
            call.enqueue(object:Callback<Response>{
                override fun onFailure(call: retrofit2.Call<Response>, t: Throwable) {

                }

                override fun onResponse(call: retrofit2.Call<Response>, response: retrofit2.Response<Response>) {

                    Toast.makeText(this@FasttagActivity,""+response.body()!!.message+"\n"+response.body()!!.status,Toast.LENGTH_SHORT).show()
                }

            })

    }
}
