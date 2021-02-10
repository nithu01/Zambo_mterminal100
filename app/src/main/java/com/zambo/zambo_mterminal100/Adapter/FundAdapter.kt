package com.zambo.zambo_mterminal100.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.R
import com.zambo.zambo_mterminal100.model.Fund
import kotlin.collections.ArrayList


class FundAdapter(var list: ArrayList<Fund>, var context: Context): RecyclerView.Adapter<FundAdapter.Holder>(){

    var fund: List<Fund>? = null
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById(R.id.mode) as TextView
        var textView1 = itemView.findViewById(R.id.fundDate)as TextView
        var textView2 = itemView.findViewById(R.id.bName)as TextView
        var textView3 = itemView.findViewById(R.id.amount)as TextView
        var textView4 = itemView.findViewById(R.id.txnId) as TextView
        var textView5 = itemView.findViewById(R.id.status)as TextView
//        var textView6 = itemView.findViewById(R.id.staffRemark)as TextView
//        var textView7 = itemView.findViewById(R.id.txnDate)as TextView

    }

    init {
        this.fund = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FundAdapter.Holder {
        var layoutInflater=LayoutInflater.from(parent.context).inflate(R.layout.item_fund_list,parent,false)
        return Holder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return fund!!.size
    }

    override fun onBindViewHolder(holder: FundAdapter.Holder, position: Int) {
        holder.textView.text=fund!!.get(position).mode
        holder.textView1.text=fund!!.get(position).txnDate
        holder.textView2.text=fund!!.get(position).bName
        holder.textView3.text=fund!!.get(position).amount
        holder.textView4.text=fund!!.get(position).txnId
//        holder.textView5.text=fund!!.get(position).status
        if (fund!!.get(position).status.equals("A")){
            holder.textView5.setText("Accept")
            holder.textView5.setTextColor(Color.GREEN)
        }else if (fund!!.get(position).status.equals("P")){
            holder.textView5.setText("Pending")
            holder.textView5.setTextColor(Color.YELLOW)
        }else if (fund!!.get(position).status.equals("R")){
            holder.textView5.setText("Rejected")
            holder.textView5.setTextColor(Color.RED)
        }
//        holder.textView6.text=complaint!!.get(position).staffRemark
//        holder.textView7.text=complaint!!.get(position).txnDate
    }

}