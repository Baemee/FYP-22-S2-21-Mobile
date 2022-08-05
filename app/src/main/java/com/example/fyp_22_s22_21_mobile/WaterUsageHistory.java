package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WaterUsageHistory extends AppCompatActivity {

    SharedPreferences Token;
    DecimalFormat df = new DecimalFormat("00");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar currentCalendar = Calendar.getInstance();
    Date date;

    private String waterUsageId;
    private String customerId;
    private String getDate;
    private String getYdate;
    private String url = "http://10.0.2.2:5000/api/WaterUsage";
    private int liter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_usage_history);

        TextView tv_Cost = (TextView) findViewById(R.id.tv_Cost);
        TextView tv_calmonth = (TextView) findViewById(R.id.tv_calMonth);
        TextView tv_TodayL = (TextView) findViewById(R.id.tv_TodayL);
        TextView tv_YesterdayL = (TextView) findViewById(R.id.tv_YesterdayL);
        TextView tv_Today  = (TextView)findViewById(R.id.tv_Today);
        TextView tv_historybox = (TextView) findViewById(R.id.cl_HistoryBox);

        String month = df.format(currentCalendar.get(Calendar.MONTH) + 1);
        switch (month) {
            case "01" : month = "January";
                break;
            case "02" : month = "February";
                break;
            case "03" : month = "March";
                break;
            case "04" : month = "April";
                break;
            case "05" : month = "May";
                break;
            case "06" : month = "June";
                break;
            case "07" : month = "July";
                break;
            case "08" : month = "August";
                break;
            case "09" : month = "September";
                break;
            case "10" : month = "October";
                break;
            case "11" : month = "November";
                break;
            case "12" : month = "December";
                break;
        }


        /* JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String jsonKey = response.getString("token");
                    String type = response.getJSONObject("user").getString("type");
                    String lastMaintenance = response.getJSONObject("user").getString("lastMaintenance");
                    String userId = response.getJSONObject("user").getString("userId");
                    String username = response.getJSONObject("user").getString("username");
                    String createdAt = response.getJSONObject("user").getString("createdAt");
                    String fullName = response.getJSONObject("user").getString("fullName");

                    Token = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = Token.edit();
                    editor.putString("token", jsonKey);
                    editor.putString("username", username);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(MainActivity.this, test[0], Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), homepage.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Invalid username or password" , Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

        */

    }
}