package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Bills extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url;;
    String []sgtDate = new String[100];
    String []sgtDue = new String[100];
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");

    private ArrayList<BillData> arrayList;
    private BillDataAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        ImageView img_back = findViewById(R.id.img_back);

        long now = System.currentTimeMillis();
        Date dateNow = new Date(now);
        String getDate = simpleDate.format(dateNow); //get current time and change to yyyy-mm-dd format

        url = getString(R.string.base_url) + "api/Bill/MyInfo?fromDate=2022-01-01&toDate=" + getDate;

        requestBills(url);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });

        // Bottom nav bar
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
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
        String [] billTitle = new String[100];
        BillData[] BillData = new BillData[100];
        String[] billD = new String[100];

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
                    for (int i = 0; i < jsonArray.length(); i++) {

                        jsonObject[i] = jsonArray.getJSONObject(i);

                        billAmt[i] = jsonObject[i].optString("amount");
                        billUsage[i] = jsonObject[i].optString("totalUsage");
                        createdAt[i] = jsonObject[i].optString("createdAt");
                        billId[i] = jsonObject[i].optString("billId");
                        billMth[i]=jsonObject[i].optString("month");
                        billYr[i]=jsonObject[i].optString("year");
                        billDue[i]=jsonObject[i].optString("deadline");
                        billTitle[i]=jsonObject[i].optString("title");

                        billD[i] = billDue[i].substring(0,10);

                        BillData[i] = new BillData(billTitle[i], billAmt[i], billD[i], billId[i]);
                        arrayList.add(BillData[i]);
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (JSONException e){
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
