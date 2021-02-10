package com.zambo.zambo_mterminal100.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.Activity.AadharPay
import com.zambo.zambo_mterminal100.Activity.AepsActivity
import com.zambo.zambo_mterminal100.Activity.Mini_Statement
import com.zambo.zambo_mterminal100.Activity.SplashActivity
import com.zambo.zambo_mterminal100.model.BankList
import java.util.*
import kotlin.collections.ArrayList


class BankListAdapter(var list: ArrayList<BankList>,var context: Context): RecyclerView.Adapter<BankListAdapter.Holder>(),Filterable{

   // var filteredListt:ArrayList<BankList>?=null
    var loadListFiltered: List<BankList>? = null
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById(com.zambo.zambo_mterminal100.R.id.textview) as TextView
        var textView1 = itemView.findViewById(com.zambo.zambo_mterminal100.R.id.textview1)as TextView

}

    init {
        this.loadListFiltered = list
       // this.filteredListt = ArrayList(list)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListAdapter.Holder {
        var layoutInflater=LayoutInflater.from(parent.context).inflate(com.zambo.zambo_mterminal100.R.layout.row_banklist,parent,false)
        return Holder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return loadListFiltered!!.size
    }

    override fun onBindViewHolder(holder: BankListAdapter.Holder, position: Int) {
        Log.d("TAG","valuesdata"+list.get(position).getBankName())
        holder.textView.text=loadListFiltered!!.get(position).getBankName()
        holder.textView1.text=loadListFiltered!!.get(position).getIinno();
        holder.textView.setOnClickListener(View.OnClickListener {
            if (SplashActivity.getPreferences("BankList","").equals("AEPS")) {
                val intent = Intent(context, AepsActivity::class.java)
                intent.putExtra("value", holder.textView.text.toString())
                intent.putExtra("value1", holder.textView1.text.toString())
                context.startActivity(intent)
            }else if(SplashActivity.getPreferences("BankList","").equals("AadharPay")) {
                val intent = Intent(context, AadharPay::class.java)
                intent.putExtra("value", holder.textView.text.toString())
                intent.putExtra("value1", holder.textView1.text.toString())
                context.startActivity(intent)
            }
            else if(SplashActivity.getPreferences("BankList","").equals("Mini")) {
                val intent = Intent(context, Mini_Statement::class.java)
                intent.putExtra("value", holder.textView.text.toString())
                intent.putExtra("value1", holder.textView1.text.toString())
                context.startActivity(intent)
            }
        })
    }

//    fun updateList(mylist:List<BankList>) {
//        loadListFiltered = ArrayList()
//        loadListFiltered!!.addAll(mylist)
//        notifyDataSetChanged()
//    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString();
                val resultList = ArrayList<BankList>()
                if (charString.isEmpty()) {
                    //resultList.addAll(loadListFiltered!!)
                    loadListFiltered=list
                } else {
                      val result = ArrayList<BankList>()
                    for (row in list!!) {
                        if (row.getBankName()!!.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT)))
                            result.add(row)
                    }
                    loadListFiltered=result
                }
                val filterResults: FilterResults = FilterResults()
                filterResults.values = loadListFiltered
                return filterResults
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                loadListFiltered = results!!.values as List<BankList>
                notifyDataSetChanged()
            }
        }
    }

//    override fun getFilter(): Filter? {
//        return object : Filter() {
//            override fun performFiltering(charSequence: CharSequence): FilterResults {
//                val charString = charSequence.toString()
//                if (charString.isEmpty()) {
//                    filteredListt = loadListFiltered as ArrayList<BankList>?
//                } else {
//                    val filteredList: ArrayList<BankList> = ArrayList()
//                    for (androidVersion in loadListFiltered!!) {
//                        if (
////                                androidVersion.getApi().toLowerCase().contains(charString) ||
//                                androidVersion.getBankName()!!.toLowerCase().contains(charString)
////                                || androidVersion.getVer().toLowerCase().contains(charString)
//                        ){
//                            filteredList.add(androidVersion)
//                        }
//                    }
//                    filteredListt = filteredList
//                }
//                val filterResults = FilterResults()
//                filterResults.values = filteredListt
//                return filterResults
//            }
//
//            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
//                filteredListt = filterResults.values as ArrayList<BankList>
//                notifyDataSetChanged()
//            }
//        }
//    }


}