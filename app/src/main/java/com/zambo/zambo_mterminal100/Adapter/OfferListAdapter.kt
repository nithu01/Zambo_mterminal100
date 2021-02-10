package com.zambo.zambo_mterminal100.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.Activity.*
import com.zambo.zambo_mterminal100.model.Record
import kotlin.collections.ArrayList


class OfferListAdapter(var list: ArrayList<Record>, var context: Context): RecyclerView.Adapter<OfferListAdapter.Holder>(){

   // var filteredListt:ArrayList<BankList>?=null
    var loadListFiltered: List<Record>? = null
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById(com.zambo.zambo_mterminal100.R.id.textview) as TextView
        var textView1 = itemView.findViewById(com.zambo.zambo_mterminal100.R.id.textview1)as TextView
        var button =itemView.findViewById(com.zambo.zambo_mterminal100.R.id.selectbutton) as Button

}

    init {
        this.loadListFiltered = list
       // this.filteredListt = ArrayList(list)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferListAdapter.Holder {
        var layoutInflater=LayoutInflater.from(parent.context).inflate(com.zambo.zambo_mterminal100.R.layout.row_offerlist,parent,false)
        return Holder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return loadListFiltered!!.size
    }

    override fun onBindViewHolder(holder: OfferListAdapter.Holder, position: Int) {
        Log.d("TAG","valuesdata"+list.get(position).rs)
        holder.textView.text=loadListFiltered!!.get(position).rs
        holder.textView1.text=loadListFiltered!!.get(position).desc
        holder.button.setOnClickListener(View.OnClickListener {

//            if (SplashActivity.getPreferences("BankList","").equals("AEPS")) {
                val intent = Intent(context, CustomerHomeActivity::class.java)
                intent.putExtra("RS", holder.textView.text.toString())
                context.startActivity(intent)
//            }else if(SplashActivity.getPreferences("BankList","").equals("AadharPay")) {
//                val intent = Intent(context, AadharPay::class.java)
//                intent.putExtra("value", holder.textView.text.toString())
//                intent.putExtra("value1", holder.textView1.text.toString())
//                context.startActivity(intent)
//            }
//            else if(SplashActivity.getPreferences("BankList","").equals("Mini")) {
//                val intent = Intent(context, Mini_Statement::class.java)
//                intent.putExtra("value", holder.textView.text.toString())
//                intent.putExtra("value1", holder.textView1.text.toString())
//                context.startActivity(intent)
//            }
        })
    }

//    fun updateList(mylist:List<BankList>) {
//        loadListFiltered = ArrayList()
//        loadListFiltered!!.addAll(mylist)
//        notifyDataSetChanged()
//    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val charString = constraint.toString();
//                val resultList = ArrayList<BankList>()
//                if (charString.isEmpty()) {
//                    //resultList.addAll(loadListFiltered!!)
//                    loadListFiltered=list
//                } else {
//                      val result = ArrayList<BankList>()
//                    for (row in list!!) {
//                        if (row.getBankName()!!.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(Locale.ROOT)))
//                            result.add(row)
//                    }
//                    loadListFiltered=result
//                }
//                val filterResults: FilterResults = FilterResults()
//                filterResults.values = loadListFiltered
//                return filterResults
//            }
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                loadListFiltered = results!!.values as List<BankList>
//                notifyDataSetChanged()
//            }
//        }
//    }

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