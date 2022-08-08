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

public class waterUsageDataAdapter extends RecyclerView.Adapter<waterUsageDataAdapter.CustomViewHolder> {

    private ArrayList<waterUsageData> arrayList;
    private Context context;
    private String alertId;

    public waterUsageDataAdapter(ArrayList<waterUsageData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public waterUsageDataAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waterusagehistorylist, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull waterUsageDataAdapter.CustomViewHolder holder, int position) {

        holder.vl_waterUsageHistoryDate.setText(arrayList.get(position).getVl_waterUsageHistoryDate());
        Double value = Double.valueOf(arrayList.get(position).getVl_waterUsageHistory());
        holder.vL_waterUsageHistory.setText(String.format("%.2f", value) + "L");
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //action
            }
        });

    }
        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView vl_waterUsageHistoryDate;
        protected TextView vL_waterUsageHistory;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vl_waterUsageHistoryDate = (TextView) itemView.findViewById(R.id.vl_waterUsageHistoryDate);
            this.vL_waterUsageHistory = (TextView) itemView.findViewById(R.id.vL_waterUsageHistory);
        }
    }
}
