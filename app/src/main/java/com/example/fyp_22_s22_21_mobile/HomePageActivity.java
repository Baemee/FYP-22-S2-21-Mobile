package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp_22_s22_21_mobile.support.notificationData;
import com.example.fyp_22_s22_21_mobile.support.notificationDataAdapter;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class HomePageActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    SharedPreferences Token;

    private ArrayList<notificationData> arrayList;
    private notificationDataAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayout;
    String false_request, true_request;

    int x = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        false_request = "false";
        true_request = "true";

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));

        Button btn_WaterUsage = (Button) findViewById(R.id.btn_WaterUsage);
        ImageView btn_Alerts = findViewById(R.id.img_alert);
        Button btn_Bills = (Button) findViewById(R.id.btn_Bills);
        Button btn_Report = (Button) findViewById(R.id.btn_Report);
        Button btn_broadcastAlerts = (Button) findViewById(R.id.btn_Alerts);
        ImageView btn_Logout = findViewById(R.id.img_logout);
        ImageView img_alert = findViewById(R.id.img_alert);
        ImageView iv_Exit =  findViewById(R.id.iv_exit);
        TextView tv_userID = (TextView) findViewById(R.id.tv_userID);
        tv_userID.setText(username);

        ConstraintLayout cl_notification = (ConstraintLayout) findViewById(R.id.cl_notification);

        btn_WaterUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterUsage.class);
                startActivity(intent);
            }
        });

        btn_Alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);

            }
        });

        btn_Bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bills.class);
                startActivity(intent);

            }
        });

        btn_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportPage.class);
                startActivity(intent);

            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_broadcastAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);

            }
        });

        // Bottom nav bar
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Home:
                        return true;
                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //notification start

        recyclerView = (RecyclerView)findViewById(R.id.rv_notification);
        linearLayout = new LinearLayoutManager(this);
        recyclerView .setLayoutManager(linearLayout);

        arrayList = new ArrayList<>();

        adapter = new notificationDataAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        iv_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_notification.setVisibility(View.GONE);
                btn_Bills.setVisibility(View.VISIBLE);
                btn_Report.setVisibility(View.VISIBLE);
                btn_WaterUsage.setVisibility(View.VISIBLE);
                btn_broadcastAlerts.setVisibility(View.VISIBLE);

            }
        });

        img_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_Bills.setVisibility(View.GONE);
                btn_Report.setVisibility(View.GONE);
                btn_broadcastAlerts.setVisibility(View.GONE);
                btn_WaterUsage.setVisibility(View.GONE);
                cl_notification.setVisibility(View.VISIBLE);

                requestNotification(false_request, 1);
                requestNotification(true_request,1);

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    x++;
                    requestNotification(false_request, x);
                    requestNotification(true_request, x);
                }
            }
        });
        //Notification
    }

    protected void requestNotification(String mark, int x) {

        recyclerView = (RecyclerView)findViewById(R.id.rv_notification);
        linearLayout = new LinearLayoutManager(this);
        recyclerView .setLayoutManager(linearLayout);

        arrayList = new ArrayList<>();

        adapter = new notificationDataAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));


        String[] vl_notificationId = new String[100];
        String[] vl_recipientId = new String[100];
        String[] vl_date = new String[100];
        String[] vl_notificationContent = new String[100];
        notificationData[] notificationData = new notificationData[100];

        String url = getString(R.string.base_url) + "api/Notification?isRead=" + mark + "&page=" + x + "&pageSize=32";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArray = response.optJSONArray("result");
                JSONObject[] jsonObjects = new JSONObject[100];

                int count = response.optJSONObject("metadata").optInt("count");

                for(int i = 0; i < count; i++) {
                    jsonObjects[i] = jsonArray.optJSONObject(i);
                    vl_notificationId[i] = jsonObjects[i].optString("notificationId");
                    vl_recipientId[i] = jsonObjects[i].optString("recipientId");
                    vl_date[i] = jsonObjects[i].optString("createdAt");
                    vl_notificationContent[i] = jsonObjects[i].optString("content");

                    try{
                        //Convert UTC to SGT
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date utcDate = utcFormat.parse(vl_date[i]);

                        DateFormat sgtFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                        vl_date[i] = sgtFormat.format(utcDate);
                    }
                    catch (ParseException e){
                        e.printStackTrace();
                    }

                    notificationData[i] = new notificationData(vl_notificationId[i], vl_recipientId[i], vl_date[i], vl_notificationContent[i]);
                    arrayList.add(notificationData[i]);
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(HomePageActivity.this,
                                "Error_Daily.",
                                Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+ key);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

}
