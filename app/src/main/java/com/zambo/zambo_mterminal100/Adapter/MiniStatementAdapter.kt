package com.zambo.zambo_mterminal100.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zambo.zambo_mterminal100.R
import com.zambo.zambo_mterminal100.model.Statement
import kotlin.collections.ArrayList


class MiniStatementAdapter(var list: ArrayList<Statement>, var context: Context): RecyclerView.Adapter<MiniStatementAdapter.Holder>(){

    var statement: List<Statement>? = null
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView = itemView.findViewById(R.id.datetime) as TextView
        var textView1 = itemView.findViewById(R.id.narration)as TextView
        var textView2 = itemView.findViewById(R.id.amount)as TextView
        var textView3 = itemView.findViewById(R.id.type)as TextView

    }

    init {
        this.statement = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniStatementAdapter.Holder {
        var layoutInflater=LayoutInflater.from(parent.context).inflate(R.layout.ministatement_items,parent,false)
        return Holder(layoutInflater)
    }

    override fun getItemCount(): Int {
        return statement!!.size
    }

    override fun onBindViewHolder(holder: MiniStatementAdapter.Holder, position: Int) {
        holder.textView.text=statement!!.get(position).date
        holder.textView1.text=statement!!.get(position).narration
//        if (statement!!.get(position).txnType.equals("Dr")){
//            holder.textView2.setTextColor(Color.RED)
            holder.textView2.text=statement!!.get(position).amount
//        }else if (statement!!.get(position).txnType.equals("Cr")){
//            holder.textView2.setTextColor(Color.parseColor("#388e3c"))
            holder.textView3.text="("+statement!!.get(position).txnType+")"
//        }


    }

}