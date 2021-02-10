package com.zambo.zambo_mterminal100.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zambo.zambo_mterminal100.Activity.AepsActivity;
import com.zambo.zambo_mterminal100.R;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {
    List<String> list;
    Context context;
    public DeviceAdapter(List<String> list,Context context) {
        this.list = list;
        this.context=context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtName,txtAccount,txtBank,txtIfsc;
        Button btnTransfer, btnDelete;
        CardView cardView;

        MyViewHolder(View view) {
            super(view);
            txtName=view.findViewById(R.id.textview);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_banklist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtName.setText(list.get(position));
        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,AepsActivity.class);
                intent.putExtra("device",holder.txtName.getText().toString());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
