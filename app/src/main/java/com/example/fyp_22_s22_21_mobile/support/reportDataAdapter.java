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
import com.example.fyp_22_s22_21_mobile.BillDetails;
import com.example.fyp_22_s22_21_mobile.R;
import com.example.fyp_22_s22_21_mobile.Report;

import java.util.ArrayList;

public class reportDataAdapter extends RecyclerView.Adapter<reportDataAdapter.CustomViewHolder>{
    private ArrayList<reportData> arrayList;
    private Context context;
    private String status;

    public reportDataAdapter(ArrayList<reportData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public reportDataAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportviewlist, parent, false);
        reportDataAdapter.CustomViewHolder holder = new reportDataAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull reportDataAdapter.CustomViewHolder holder, int position) {

        holder.vl_reportDate.setText(arrayList.get(position).getVl_reportDate());
        holder.vl_reporttitle.setText(arrayList.get(position).getVl_reporttitle());
        holder.vl_reportdescription.setText(arrayList.get(position).getVl_reportdescription());
        status = arrayList.get(position).getStatus();

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Report.class);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView vl_reportDate;
        protected TextView vl_reporttitle;
        protected TextView vl_reportdescription;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vl_reportDate = (TextView) itemView.findViewById(R.id.vl_reportDate);
            this.vl_reporttitle = (TextView) itemView.findViewById(R.id.vl_reporttitle);
            this.vl_reportdescription = (TextView) itemView.findViewById(R.id.vl_reportdescription);
        }
    }
}
