package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WaterUsage extends AppCompatActivity {

    SharedPreferences Token;
    DecimalFormat df = new DecimalFormat("00");
    DecimalFormat format2dp = new DecimalFormat("0.00");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar currentCalendar = Calendar.getInstance();
    Date dateNow, dateYesterday;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_usage);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        TextView tv_Cost = findViewById(R.id.tv_Cost);
        TextView tv_CalMonth = findViewById(R.id.tv_CalMonth);
        TextView tv_TodayL = findViewById(R.id.tv_TodayL);
        TextView tv_YesterdayL = findViewById(R.id.tv_YesterdayL);
        TextView tv_Today = findViewById(R.id.tv_Today);
        TextView tv_HistoryBox = findViewById(R.id.tv_HistoryBox);

        // Get current month name
        String[] nameOfMonths = new DateFormatSymbols(Locale.getDefault()).getMonths();
        String month = nameOfMonths[currentCalendar.get(Calendar.MONTH)];
        tv_CalMonth.setText(month);

        double cost = 0.00; //later set get data from the DB
        String price = String.valueOf(cost);
        tv_Cost.setText(price);

        long now = System.currentTimeMillis();
        dateNow = new Date(now);
        String getDate = simpleDate.format(dateNow); //get current time and change to yyyy-mm-dd format
        String urlToday = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate + "&total=true";

        tv_Today.setText(getDate);
        tv_HistoryBox.setText(urlToday); // testing

        RequestWaterUsageTotal(tv_TodayL, urlToday);

        dateYesterday = new Date(now - -24 * 60 * 60 * 1000);
        getDate = simpleDate.format(dateYesterday); //get current time and change to yyyy-mm-dd format
        String urlYesterday = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate + "&total=true";

        RequestWaterUsageTotal(tv_YesterdayL, urlYesterday);
    }

    protected void RequestWaterUsageTotal(TextView displayView, String url) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Double totalUsage = response.getDouble("totalUsage");
                    String totalUsage_formatted = format2dp.format(totalUsage) + "L";
                    displayView.setText(totalUsage_formatted);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(WaterUsage.this,
                                "An error occurred.",
                                Toast.LENGTH_LONG)
                        .show();
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
        requestQueue.add(jsonObjectRequest);
    }

}