package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp_22_s22_21_mobile.support.BillData;
import com.example.fyp_22_s22_21_mobile.support.BillDataAdapter;
import com.example.fyp_22_s22_21_mobile.support.reportData;
import com.example.fyp_22_s22_21_mobile.support.reportDataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Bills extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url = "http://10.0.2.2:5000/api/Bill/MyInfo";
    String sgtDate;
    String sgtDue;
    double amount;
    double usage;

    private ArrayList<BillData> arrayList;
    private BillDataAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        Button btn_back = (Button) findViewById(R.id.btn_back);

        requestBills(url);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void requestBills(String url) {
        RecyclerView rv_bills = findViewById(R.id.rv_bills);
        rv_bills.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rv_bills.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        adapter = new BillDataAdapter(arrayList, this);
        rv_bills.setAdapter(adapter);

        String[] billAmt = new String[100];
        String[] billUsage = new String[100];
        String[] createdAt = new String[100];
        String[] billId = new String[100];
        String [] billMth = new String[100];
        String [] billYr = new String[100];
        String [] billDue = new String [100];
        BillData[] BillData = new BillData[100];

        billAmt[0] = "";
        billUsage[0] = "";
        createdAt[0] = "";
        billId[0] = "";
        billMth[0]="";
        billYr[0]="";
        billDue[0]="";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                try {
                    JSONObject[] jsonObject = new JSONObject[100];
                    //JSONArray jsonArray = new JSONArray();
                    //jsonArray = response.optJSONArray("");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject[i] = jsonArray.getJSONObject(i);

                        billAmt[i] = jsonObject[i].optString("amount");
                        billUsage[i] = jsonObject[i].optString("totalUsage");
                        createdAt[i] = jsonObject[i].optString("createdAt");
                        billId[i] = jsonObject[i].optString("billId");
                        billMth[i]=jsonObject[i].optString("month");
                        billYr[i]=jsonObject[i].optString("year");
                        billDue[i]=jsonObject[i].optString("deadline");

                        sgtDate = createdAt[i];
                        sgtDue = billDue[i];
                        //Convert UTC to SGT
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date utcDate = utcFormat.parse(sgtDate);
                        utcDate = utcFormat.parse(sgtDue);

                        DateFormat sgtFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                        sgtDate = sgtFormat.format(utcDate);
                        sgtDue = sgtFormat.format(utcDate);
                        createdAt[i] = sgtDate;
                        billDue[i] = sgtDue;

                        BillData[i] = new BillData(billMth[i],billYr[i], billAmt[i], billId[i], billDue[i]);
                        arrayList.add(BillData[i]);

                        adapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException | ParseException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Bills.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonArrayRequest);

    }
}
/*
        TextView tv_billDate = findViewById(R.id.tv_billDate);
        TextView tv_billAmount = findViewById(R.id.tv_billAmount);
        TextView tv_billUsage = findViewById(R.id.tv_billUsage);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int length = response.length();

                            for(int i = 0; i<length; i++)
                            {
                                date = response.getJSONObject(i).getString("title");
                                amount = response.getJSONObject(i).getDouble("amount");
                                usage = response.getJSONObject(i).getDouble("totalUsage");
                                //status = response.getJSONObject(i).getString("Payment");

                                tv_billDate.append(date+"\n");
                                tv_billAmount.append(String.format("%.2f",amount)+"\n");
                                tv_billUsage.append(String.format("%.2f",usage)+"\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(Bills.this,
                                "An error occurred.",
                                Toast.LENGTH_LONG)
                        .show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }
}*/