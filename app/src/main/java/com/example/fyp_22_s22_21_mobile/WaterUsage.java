package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WaterUsage extends AppCompatActivity {

    SharedPreferences Token;
    DecimalFormat df = new DecimalFormat("00");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar currentCalendar = Calendar.getInstance();
    Date date;
    private String waterUsageId;
    private String customerId;
    private String getDate;
    private String url = "http://10.0.2.2:5000/api/WaterUsage";
    private int liter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_usage);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));

        Log.i("key", key);

        TextView tv_Cost = (TextView) findViewById(R.id.tv_Cost);
        TextView tv_calmonth = (TextView) findViewById(R.id.tv_calmonth);
        TextView tv_TodayL = (TextView) findViewById(R.id.tv_TodayL);
        TextView tv_YesterdayL = (TextView) findViewById(R.id.tv_YesterdayL);
        TextView tv_Today  = (TextView)findViewById(R.id.tv_Today);
        TextView tv_historybox = (TextView) findViewById(R.id.tv_HistoryBox);

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

        tv_calmonth.setText(month);


        double cost = 0.00; //later set get data from the DB
        String price = String.valueOf(cost);
        tv_Cost.setText(price);

        long now = System.currentTimeMillis();
        date = new Date(now);
        getDate = simpleDate.format(date); //get current time and change to yyyy-mm-dd format
        String urlToday = "http://10.0.2.2:5000/api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate + "&total=true";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Authorization", key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String test_2 = jsonObject.toString();
        tv_Today.setText(getDate);
        tv_historybox.setText(urlToday);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlToday, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Double tLiter = response.getDouble("totalUsage");
                    String tLiter_s = tLiter.toString();
                    tv_TodayL.setText(tLiter_s + "L");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_TodayL.setText("Not working");
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