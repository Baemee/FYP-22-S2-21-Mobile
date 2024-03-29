package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.example.fyp_22_s22_21_mobile.support.alertData;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class AlertDetail extends AppCompatActivity {

    SharedPreferences Token;
    private String url;

    private String alertId;
    private String alertTitle;
    private String alertDate;
    private String alertDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_detail);

        ImageView img_back = findViewById(R.id.img_back);
        TextView tv_AlertTitle = (TextView) findViewById(R.id.tv_AlertTitle);
        TextView tv_AlertDate = (TextView) findViewById(R.id.tv_AlertDate);
        TextView tv_AlertDetail = (TextView) findViewById(R.id.tv_AlertDetail);

        Intent intent = getIntent();
        alertId = intent.getStringExtra("alertId");

        //Shared Preference + Json
        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));

        url = getString(R.string.base_url) +"api/BroadcastAlert/" + alertId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    alertTitle = response.getString("alertTitle");
                    alertDescription = response.getString("alertDescription");
                    alertDate = response.getString("createdAt");

                    //Convert UTC to SGT
                    DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    Date utcDate = utcFormat.parse(alertDate);

                    DateFormat sgtFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                    alertDate = sgtFormat.format(utcDate);

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

                tv_AlertTitle.setText(alertTitle);
                tv_AlertDate.setText(alertDate);
                tv_AlertDetail.setText(alertDescription);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AlertDetail.this, "Error", Toast.LENGTH_LONG).show();
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

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
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
}