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

public class BillDataAdapter extends RecyclerView.Adapter<BillDataAdapter.CustomViewHolder>{
    private ArrayList<BillData> arrayList;
    private Context context;
    private String BillId;

    public BillDataAdapter(ArrayList<BillData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public BillDataAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.billviewlist, parent, false);
        BillDataAdapter.CustomViewHolder holder = new BillDataAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillDataAdapter.CustomViewHolder holder, int position) {

        holder.vl_BillDate.setText(arrayList.get(position).getvl_BillDate());
        holder.vl_BillAmt.setText(arrayList.get(position).getvl_BillAmt());
        holder.vl_BillUsage.setText(arrayList.get(position).getvl_BillUsage());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BillDetails.class);
                intent.putExtra("billId", BillId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView vl_BillDate;
        protected TextView vl_BillAmt;
        protected TextView vl_BillUsage;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vl_BillDate = (TextView) itemView.findViewById(R.id.vl_BillDate);
            this.vl_BillAmt = (TextView) itemView.findViewById(R.id.vl_BillAmt);
            this.vl_BillUsage = (TextView) itemView.findViewById(R.id.vl_Usage);
        }
    }
}
