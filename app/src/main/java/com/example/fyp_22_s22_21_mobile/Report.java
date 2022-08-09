package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Report extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url;
    String title;
    String date;
    String description;
    String reportId;
    String customerId;
    String createdAt;
    String status;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));
        userId = Token.getString("userId", String.valueOf(1));

        ImageView img_Return = findViewById(R.id.img_back);

        img_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportMain.class);
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

        Intent intent = getIntent();
        reportId = intent.getStringExtra("reportId");
        url = "http://10.0.2.2:5000/" + "api/ReportTicket/MyInfo/" + reportId;
        requestReportDetails(url);

        String url_update = getString(R.string.base_url) + "api/ReportTicket";
        Button btn_Resolve = (Button) findViewById(R.id.btn_Resolve);
        Button btn_Update = (Button) findViewById(R.id.btn_Update);

        btn_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestUpdate(url_update, url);
            }
        });

        btn_Resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestResolve(url_update, url);
            }
        });



    }

    protected void requestReportDetails(String url)    {
        EditText tv_reportTitle = findViewById(R.id.tv_ReportTitle);
        TextView tv_reportDate = findViewById(R.id.tv_reportDate);
        EditText tv_reportDescription = findViewById(R.id.tv_resolve);
        TextView tv_status = findViewById(R.id.tv_Status);
        Button btn_Resolve = (Button) findViewById(R.id.btn_Resolve);
        Button btn_Update = (Button) findViewById(R.id.btn_Update);

        Intent intent = getIntent();
        reportId = intent.getStringExtra("reportId");

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    title = response.getString("title");
                    description = response.getString("description");
                    date = response.getString("createdAt");
                    status = response.getString("status");

                    //Convert UTC to SGT
                    DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd");
                    utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date utcDate = utcFormat.parse(date);

                    DateFormat sgtFormat = new SimpleDateFormat("yyyy-MM-dd");
                    sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                    date = sgtFormat.format(utcDate);


                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                if(status.contains("ed")) {
                    btn_Update.setVisibility(View.GONE);
                    btn_Resolve.setVisibility(View.GONE);
                }

                tv_reportTitle.setText(title);
                tv_reportDate.setText(date);
                tv_reportDescription.setText(description);
                tv_status.setText(status);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Report.this, "Error", Toast.LENGTH_LONG).show();
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

    protected void requestUpdate(String url_update, String url) {

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));
        Intent intent = getIntent();
        reportId = intent.getStringExtra("reportId");

        EditText tv_reportTitle = findViewById(R.id.tv_ReportTitle);
        TextView tv_reportDate = findViewById(R.id.tv_reportDate);
        EditText tv_reportDescription = findViewById(R.id.tv_resolve);
        TextView tv_Status = findViewById(R.id.tv_Status);

        String title_update = tv_reportTitle.getText().toString();
        String description_update = tv_reportDescription.getText().toString();
        String date_update = tv_reportDate.getText().toString();
        String status = tv_Status.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reportId", reportId);
            jsonObject.put("customerId", userId);
            jsonObject.put("title", title_update);
            jsonObject.put("description", description_update);
            jsonObject.put("createdAt", date_update);
            jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url_update, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestReportDetails(url);
                Toast.makeText(Report.this, "Update has been successfully updated", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),ReportMain.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Report.this, "Error", Toast.LENGTH_LONG).show();
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

    protected void requestResolve(String url_update, String url) {
        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));
        Intent intent = getIntent();
        reportId = intent.getStringExtra("reportId");

        EditText tv_reportTitle = findViewById(R.id.tv_ReportTitle);
        TextView tv_reportDate = findViewById(R.id.tv_reportDate);
        EditText tv_reportDescription = findViewById(R.id.tv_resolve);
        TextView tv_Status = findViewById(R.id.tv_Status);

        String title_update = tv_reportTitle.getText().toString();
        String description_update = tv_reportDescription.getText().toString();
        String date_update = tv_reportDate.getText().toString();
        String status = tv_Status.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("reportId", reportId);
            jsonObject.put("customerId", userId);
            jsonObject.put("title", title_update);
            jsonObject.put("description", description_update);
            jsonObject.put("createdAt", date_update);
            jsonObject.put("status", "Closed");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url_update, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                requestReportDetails(url);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Report.this, "Error", Toast.LENGTH_LONG).show();
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