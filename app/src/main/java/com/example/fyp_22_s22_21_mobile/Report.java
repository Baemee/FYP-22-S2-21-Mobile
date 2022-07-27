package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        Button btn_Resolve = (Button)findViewById(R.id.btn_Resolve);
        ImageView btn_Return = findViewById(R.id.img_back);

        btn_Resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportMain.class);
                startActivity(intent);
            }
        });
        requestReportDetails(url);
    }

    protected void requestReportDetails(String url)
    {
        TextView tv_reportTitle = findViewById(R.id.tv_ReportTitle);
        TextView tv_reportDate = findViewById(R.id.tv_reportDate);
        TextView tv_reportDescription = findViewById(R.id.tv_resolve);

        Intent intent = getIntent();
        reportId = intent.getStringExtra("reportId");

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));

        url = "http://10.0.2.2:5000/api/ReportTicket/MyInfo/" + reportId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    title = response.getString("title");
                    description = response.getString("description");
                    date = response.getString("createdAt");

                    //Convert UTC to SGT
                    DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date utcDate = utcFormat.parse(date);

                    DateFormat sgtFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                    date = sgtFormat.format(utcDate);

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                tv_reportTitle.setText(title);
                tv_reportDate.setText(date);
                tv_reportDescription.setText(description);
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