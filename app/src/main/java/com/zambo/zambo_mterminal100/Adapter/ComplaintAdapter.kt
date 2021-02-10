package com.zambo.zambo_mterminal100.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.R
import com.zambo.zambo_mterminal100.model.Complaint
import kotlin.collections.ArrayList


class ComplaintAdapter(var list: ArrayList<Complaint>, var context: Context): RecyclerView.Adapter<ComplaintAdapter.Holder>(){

    var complaint: List<Complaint>? = null
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById(R.id.service) as TextView
        var textView1 = itemView.findViewById(R.id.amount)as TextView
        var textView2 = itemView.findViewById(R.id.complainId)as TextView
        var textView3 = itemView.findViewById(R.id.complainDate)as TextView
        var textView4 = itemView.findViewById(R.id.status) as TextView
        var textView5 = itemView.findViewById(R.id.rtRemark)as TextView
        var textView6 = itemView.findViewById(R.id.staffRemark)as TextView
        var textView7 = itemView.findViewById(R.id.txnDate)as TextView

    }

    init {
        this.complaint = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintAdapter.Holder {
        var layoutInflater=LayoutInflater.from(parent.context).inflate(R.layout.item_complaint_list,parent,false)
        return Holder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return complaint!!.size
    }

    override fun onBindViewHolder(holder: ComplaintAdapter.Holder, position: Int) {
        holder.textView.text=complaint!!.get(position).service
        holder.textView1.text=complaint!!.get(position).amount
        holder.textView2.text=complaint!!.get(position).complainId
        holder.textView3.text=complaint!!.get(position).complainDate
        holder.textView4.text=complaint!!.get(position).status
        holder.textView5.text=complaint!!.get(position).rtRemark
        holder.textView6.text=complaint!!.get(position).staffRemark
        holder.textView7.text=complaint!!.get(position).txnDate
    }

}