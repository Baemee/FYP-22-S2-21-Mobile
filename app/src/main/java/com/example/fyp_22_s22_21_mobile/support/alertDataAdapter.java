package com.example.fyp_22_s22_21_mobile.support;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_22_s22_21_mobile.AlertDetail;
import com.example.fyp_22_s22_21_mobile.R;

import java.util.ArrayList;

public class alertDataAdapter extends RecyclerView.Adapter<alertDataAdapter.CustomViewHolder>{

    private ArrayList<alertData> arrayList;
    private Context context;
    private String[] alertId = new String[1000];

    public alertDataAdapter(ArrayList<alertData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public alertDataAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alertviewlist, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull alertDataAdapter.CustomViewHolder holder, int position) {

        holder.vl_Date.setText(arrayList.get(position).getVl_Date());
        holder.vl_title.setText(arrayList.get(position).getVl_title());
        //holder.vl_description.setText(arrayList.get(position).getVl_description());
        alertId[position] = arrayList.get(position).getAlert_Id();

        //holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent intent = new Intent(context, AlertDetail.class);
                intent.putExtra("alertId", alertId[position]);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView vl_Date;
        protected TextView vl_title;
        protected TextView vl_description;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vl_Date = (TextView) itemView.findViewById(R.id.vl_Date);
            this.vl_title = (TextView) itemView.findViewById(R.id.vl_title);
            //this.vl_description = (TextView) itemView.findViewById(R.id.vl_description);
        }
    }


}
