package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import com.example.fyp_22_s22_21_mobile.support.alertDataAdapter;
import com.example.fyp_22_s22_21_mobile.support.waterUsageData;
import com.example.fyp_22_s22_21_mobile.support.waterUsageDataAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WaterUsage extends AppCompatActivity {

    private SharedPreferences Token;
    DecimalFormat df = new DecimalFormat("00");
    DecimalFormat format2dp = new DecimalFormat("0.00");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    Calendar currentCalendar = Calendar.getInstance();
    Date dateNow, dateYesterday, dateHistory;
    String key, getDate_Start, getDate_End;

    private String url = "http://10.0.2.2:5000/api/WaterUsage";
    private int liter;

    private ArrayList<waterUsageData> arrayList;
    private waterUsageDataAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    private waterUsageData[] waterUsageData = new waterUsageData[100];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_usage);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        RecyclerView rv_waterUsageHistory = (RecyclerView)findViewById(R.id.rv_waterUsageHistory);

        layoutManager = new LinearLayoutManager(this);
        rv_waterUsageHistory.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        adapter = new waterUsageDataAdapter(arrayList, this);
        rv_waterUsageHistory.setAdapter(adapter);

        TextView tv_Cost = findViewById(R.id.tv_Cost);
        TextView tv_CalMonth = findViewById(R.id.tv_calmonth);
        TextView tv_TodayL = findViewById(R.id.tv_TodayL);
        TextView tv_YesterdayL = findViewById(R.id.tv_YesterdayL);
        TextView tv_Today = findViewById(R.id.tv_Today);
        TextView tv_HistoryBox = findViewById(R.id.tv_HistoryBox);

        EditText et_startDate = findViewById(R.id.et_startDate);
        EditText et_endDate = findViewById(R.id.et_endDate);

        // Get current month name
        String[] nameOfMonths = new DateFormatSymbols(Locale.getDefault()).getMonths();
        String month = nameOfMonths[currentCalendar.get(Calendar.MONTH)];
        tv_CalMonth .setText(month);

        double cost = 0.00; //later set get data from the DB
        String price = String.valueOf(cost);
        tv_Cost.setText(price);

        long now = System.currentTimeMillis();
        dateNow = new Date(now);
        String getDate = simpleDate.format(dateNow); //get current time and change to yyyy-mm-dd format
        String urlToday = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate + "&total=true";

        requestWaterUsageTotal(tv_TodayL, urlToday);

        dateYesterday = new Date(now - 24 * 60 * 60 * 1000);
        getDate = simpleDate.format(dateYesterday); //get current time and change to yyyy-mm-dd format
        String urlYesterday = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate + "&total=true";


        String[] date = new String[100];
        int[] value= new int[100];

        dateHistory = new Date(now - 32 * 24 * 60 * 60 * 1000);
        getDate_Start = simpleDate.format(dateHistory); //get current time and change to yyyy-mm-dd format
        dateNow = new Date(now - 2 * 24 * 60 * 60 * 1000);
        getDate_End = simpleDate.format(dateNow);

        et_startDate.setText(getDate);

        requestWaterUsageTotal(tv_YesterdayL, urlYesterday);
        requestWaterUsageHistory(getDate_Start, getDate_End); //(String Date to Start, String Date to end);
    }

    protected void requestWaterUsageTotal(TextView displayView, String url) {

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

    protected void requestWaterUsageHistory(String getDate, String getDate_now) {

        String[] date = new String[100];
        int[] value= new int[100];

        String url = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate_now + "&total=false";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                int value1, value2;
                JSONObject[] jsonObjects = new JSONObject[100];
                JSONObject[] data = new JSONObject[2];



                for(int i = 0; i < response.length(); i++) {

                    jsonObjects[i] = response.optJSONObject(i); //assign response array to JsonObjects of i
                    date[i] = jsonObjects[i].optString("date");
                    JSONArray dataArray = new JSONArray();
                    dataArray = jsonObjects[i].optJSONArray("data");

                    value1 = Integer.parseInt(dataArray.optJSONObject(0).optString("value"));
                    value2 = Integer.parseInt(dataArray.optJSONObject(1).optString("value"));

                    value[i] = value1 = value2;

                    waterUsageData[i] = new waterUsageData(date[i], value[i]);
                    arrayList.add(waterUsageData[i]);
                }
                adapter.notifyDataSetChanged();

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
        requestQueue.add(jsonArrayRequest);

    }


}