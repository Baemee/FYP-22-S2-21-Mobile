package com.example.fyp_22_s22_21_mobile.support;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyp_22_s22_21_mobile.BillDetails;
import com.example.fyp_22_s22_21_mobile.R;

import java.util.ArrayList;

public class BillDataAdapter extends RecyclerView.Adapter<BillDataAdapter.CustomViewHolder>{
    private ArrayList<BillData> arrayList;
    private Context context;
    private String[] BillId = new String[1000];

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

        String amount = arrayList.get(position).getVl_BillAmt();
        double amount_d = Double.parseDouble(amount);

        holder.vl_BillId.setText(arrayList.get(position).getBillTitle());
        holder.vl_BillDate.setText(arrayList.get(position).getVl_BillDue());
        holder.vl_BillAmount.setText("$" + String.format("%.2f", amount_d));
        BillId[position]=arrayList.get(position).getBillId();

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BillDetails.class);
                intent.putExtra("billId", BillId[position]);
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
        protected TextView vl_BillAmount;
        protected TextView vl_BillId;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vl_BillDate = (TextView) itemView.findViewById(R.id.vl_BillDate);
            this.vl_BillAmount = (TextView) itemView.findViewById(R.id.vl_BillAmount);
            this.vl_BillId = (TextView) itemView.findViewById(R.id.vl_BillId);
        }
    }
}
