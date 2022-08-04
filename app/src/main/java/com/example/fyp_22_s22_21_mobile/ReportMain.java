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
import android.widget.SearchView;
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
import com.example.fyp_22_s22_21_mobile.support.alertData;
import com.example.fyp_22_s22_21_mobile.support.alertDataAdapter;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ReportMain extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url;
    String sgtDate;

    private ArrayList <reportData> arrayList;
    private reportDataAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    int x = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_main);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        ImageView img_back = findViewById(R.id.img_back);
        RecyclerView rv_report = findViewById(R.id.rv_report);
        rv_report.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rv_report.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        adapter = new reportDataAdapter(arrayList, this);
        rv_report.setAdapter(adapter);

        loadReport(1);

        rv_report.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!rv_report.canScrollVertically(1)) {
                    x++;
                    loadReport(x);
                }
            }
        });

        //Search

        SearchView searchView = (SearchView)findViewById(R.id.searchView);
        final String[] search = new String[1];

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //search
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search[0] = s;
                return true;
            }
        });


        //Seach end



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

    protected void loadReport(int i) {

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = Token.getString("token", String.valueOf(1));

        String[] reportTitle = new String[100];
        String[] reportDescription = new String[100];
        String[] createdAt = new String[100];
        String[] status = new String[100];
        String [] reportId = new String[100];
        reportData[] reportData = new reportData[100];


        url = getString(R.string.base_url) + "api/ReportTicket/MyInfo?page=" + i + "&pageSize="+ 20;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONObject[] jsonObject = new JSONObject[1000];
                JSONArray jsonArray = new JSONArray();
                jsonArray = response.optJSONArray("result");
                for(int i = 0; i < jsonArray.length(); i++) {


                    jsonObject[i] = jsonArray.optJSONObject(i);

                    reportTitle[i] = jsonObject[i].optString("title");
                    status[i] = jsonObject[i].optString("status");
                    reportDescription[i] = jsonObject[i].optString("description");
                    createdAt[i] = jsonObject[i].optString("createdAt");
                    reportId[i]=jsonObject[i].optString("reportId");

                    sgtDate = createdAt[i];
                    try{
                        //Convert UTC to SGT
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date utcDate = utcFormat.parse(sgtDate);

                        DateFormat sgtFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                        sgtDate = sgtFormat.format(utcDate);
                        createdAt[i] = sgtDate;
                    }
                    catch (ParseException e){
                        e.printStackTrace();
                    }

                    reportData[i] = new reportData(createdAt[i], reportTitle[i], reportDescription[i], status[i], reportId[i]);
                    arrayList.add(reportData[i]);

                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReportMain.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();
                headers.put("Authorization","Bearer " + key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }
}