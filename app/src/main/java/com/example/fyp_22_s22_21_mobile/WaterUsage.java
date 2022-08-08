package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp_22_s22_21_mobile.support.barChartData;
import com.example.fyp_22_s22_21_mobile.support.waterUsageData;
import com.example.fyp_22_s22_21_mobile.support.waterUsageDataAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class WaterUsage extends AppCompatActivity {

    private SharedPreferences Token;
    private SharedPreferences usageValue;
    DecimalFormat df = new DecimalFormat("00");
    DecimalFormat format2dp = new DecimalFormat("0.00");
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat simpleYear = new SimpleDateFormat("yyyy");
    SimpleDateFormat simpleMonth = new SimpleDateFormat("MM");
    SimpleDateFormat simpleDay = new SimpleDateFormat("dd");
    SimpleDateFormat simpleMonth_cal = new SimpleDateFormat("yyyy-MM");
    Calendar currentCalendar = Calendar.getInstance();
    Date dateNow, dateYesterday, dateHistory;
    Date date_start, date_end, date_daily;
    String key, getDate_Start, getDate_End;
    String date_s, date_e, date_d;
    ArrayList<BarEntry> chartData;
    int year_s, month_s, day_s;
    int year_e, month_e, day_e;

    double test_value = 0.00;

    private int liter;

    private ArrayList<waterUsageData> arrayList;
    private waterUsageDataAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    waterUsageData waterUsageData;

    private BarChart barChart;

    ConstraintLayout rl_start, rl_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_usage);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        ConstraintLayout rl_Daily = findViewById(R.id.rl_Daily);
        DatePicker dp_Daily = findViewById(R.id.dp_Daily);
        Button btn_dailyConfirm = findViewById(R.id.btn_dailyConfirm);

        TextView tv_showdaily = findViewById(R.id.tv_showdaily);

        barChart = (BarChart) findViewById(R.id.chart);

        RecyclerView rv_waterUsageHistory = (RecyclerView) findViewById(R.id.rv_waterUsageHistory);

        layoutManager = new LinearLayoutManager(this);
        rv_waterUsageHistory.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        adapter = new waterUsageDataAdapter(arrayList, this);
        rv_waterUsageHistory.setAdapter(adapter);

        TextView tv_Cost = findViewById(R.id.tv_Cost);
        TextView tv_CalMonth = findViewById(R.id.tv_calMonth);
        TextView tv_TodayL = findViewById(R.id.tv_TodayL);
        TextView tv_YesterdayL = findViewById(R.id.tv_YesterdayL);

        // Get current month name
        String[] nameOfMonths = new DateFormatSymbols(Locale.getDefault()).getMonths();
        String month = nameOfMonths[currentCalendar.get(Calendar.MONTH)];
        tv_CalMonth.setText(month);

        double cost = 0.00; //later set get data from the DB
        String price = String.valueOf(cost);
        tv_Cost.setText(price);


        // Bottom nav bar
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        long now = System.currentTimeMillis();
        dateNow = new Date(now);
        String getDate = simpleDate.format(dateNow); //get current time and change to yyyy-mm-dd format
        String urlToday = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate + "&toDate=" + getDate + "&total=true";

        requestWaterUsageTotal(tv_TodayL, urlToday);

        dateYesterday = new Date(now - 24 * 60 * 60 * 1000);
        String getDate_Y = simpleDate.format(dateYesterday); //get current time and change to yyyy-mm-dd format
        String urlYesterday = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + getDate_Y + "&toDate=" + getDate_Y + "&total=true";

        getCostofWaterUsage(dateNow);

        String[] date = new String[100];
        int[] value = new int[100];

        requestWaterUsageTotal(tv_YesterdayL, urlYesterday);

        //barchart

        chartData = new ArrayList<>();

        TextView tv_Daily = findViewById(R.id.tv_Daily);
        TextView tv_Weekly = findViewById(R.id.tv_Weekly);
        TextView tv_Monthly = findViewById(R.id.tv_Monthly);

        SharedPreferences waterUsage_value = getSharedPreferences("waterUsage_value", MODE_PRIVATE);

        tv_Daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_Daily.setVisibility(View.VISIBLE);

                dp_Daily.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                        int year_d = year;
                        int month_d = month + 1;
                        int day_d = day;
                        date_d = String.valueOf(year_d) + "-" + String.valueOf(month_d) + "-" + String.valueOf(day_d);
                        try {
                            date_daily = simpleDate.parse(date_d);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        btn_dailyConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rl_Daily.setVisibility(View.GONE);
                tv_showdaily.setText("Date end : " + date_d);

                chartData.clear();
                String[] date_chartDaily = new String[50];
                String[] day_chartDaily = new String[50];
                int[] daily_day = new int[50];
                int[] daily_intValue = new int[50];
                double[] testing_value = new double[50];
                double[] daily_value = new double[50];
                String[] url_daily = new String[50];
                barChartData[] barChartData = new barChartData[50];

                for (int i = 0; i < 30; i++) {


                    Calendar cal_daily = Calendar.getInstance();
                    cal_daily.setTime(date_daily);
                    cal_daily.add(Calendar.DATE, -29 + i);
                    date_chartDaily[i] = simpleDate.format(cal_daily.getTime());

                    day_chartDaily[i] = simpleDay.format(cal_daily.getTime()); // get day

                    daily_day[i] = Integer.valueOf(day_chartDaily[i]);

                    url_daily[i] = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + date_chartDaily[i] + "&toDate=" + date_chartDaily[i] + "&total=true";

                    int x = i;

                    JsonObjectRequest jsonObjectRequest_daily = new JsonObjectRequest(Request.Method.GET, url_daily[i], null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            daily_value[x] = response.optDouble("totalUsage");
                            chartData.add(new BarEntry(x, (float) daily_value[x]));

                            BarDataSet barDataSet = new BarDataSet(chartData, "waterUsage/L");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(15f);

                            BarData barData = new BarData(barDataSet);

                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.getDescription().setText("bar Chart");
                            barChart.animateY(2000);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Response error", error.getMessage());
                            Toast.makeText(WaterUsage.this,
                                            "Error_Daily.",
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
                    requestQueue.add(jsonObjectRequest_daily);

                }

            }
        });






        tv_Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartData.clear();
                tv_showdaily.setText("");

                String[] date_chartWeekly = new String[10];
                String[] date_chartWeekly_end = new String[10];

                int[] weekOfyear = new int[10];
                double[] weekly_value = new double[10];
                String[] url_weekly = new String[10];

                for (int i = 0; i < 8; i++) {

                    Calendar cal_weekly = Calendar.getInstance();
                    Calendar cal_end = Calendar.getInstance();
                    cal_weekly.setTime(dateNow);
                    cal_weekly.add(Calendar.WEEK_OF_YEAR, -7 + i);
                    date_chartWeekly[i] = simpleDate.format(cal_weekly.getTime());

                    try {
                        cal_end.setTime(simpleDate.parse(date_chartWeekly[i]));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    cal_end.add(Calendar.DATE, +7);
                    date_chartWeekly_end[i] = simpleDate.format(cal_end.getTime());

                    weekOfyear[i] = cal_weekly.get(Calendar.WEEK_OF_YEAR);

                    url_weekly[i] = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + date_chartWeekly[i] + "&toDate=" + date_chartWeekly_end[i] + "&total=true";

                    int x = i;

                    JsonObjectRequest jsonObjectRequest_weekly = new JsonObjectRequest(Request.Method.GET, url_weekly[i], null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            weekly_value[x] = response.optDouble("totalUsage");
                            chartData.add(new BarEntry(weekOfyear[x], (float) weekly_value[x]));

                            BarDataSet barDataSet = new BarDataSet(chartData, "waterUsage/L");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(15f);

                            BarData barData = new BarData(barDataSet);

                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.getDescription().setText("bar Chart");
                            barChart.animateY(2000);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Response error", error.getMessage());
                            Toast.makeText(WaterUsage.this,
                                            "Error_Daily.",
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
                    requestQueue.add(jsonObjectRequest_weekly);

                }
            }
        });

        tv_Monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chartData.clear();
                tv_showdaily.setText("");

                String[] date_chartMonthly = new String[15];
                String[] date_chartMonthly_end = new String[15];

                int[] date_Month = new int[15];
                int[] lastdayofMonth = new int[15];
                double[] montly_value = new double[15];
                String[] url_weekly = new String[15];

                Date[] dateofMonth = new Date[15];

                for (int i = 0; i < 12; i++) {

                    Calendar cal_Monthly = Calendar.getInstance();
                    Calendar cal_Monthly_end = Calendar.getInstance();
                    cal_Monthly.setTime(dateNow);
                    cal_Monthly.add(Calendar.MONTH, -11 + i);
                    date_chartMonthly[i] = simpleMonth_cal.format(cal_Monthly.getTime());

                    dateofMonth[i] = cal_Monthly.getTime();
                    cal_Monthly_end.setTime(dateofMonth[i]);

                    lastdayofMonth[i] = cal_Monthly.getActualMaximum(Calendar.DAY_OF_MONTH);


                    date_Month[i] = cal_Monthly.get(Calendar.MONTH);

                    url_weekly[i] = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + date_chartMonthly[i] + "-01&toDate=" + date_chartMonthly[i] + "-" + lastdayofMonth[i] + "&total=true";

                    int x = i;

                    JsonObjectRequest jsonObjectRequest_weekly = new JsonObjectRequest(Request.Method.GET, url_weekly[i], null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            montly_value[x] = response.optDouble("totalUsage");
                            chartData.add(new BarEntry(date_Month[x], (float) montly_value[x]));

                            BarDataSet barDataSet = new BarDataSet(chartData, "waterUsage/L");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(15f);

                            BarData barData = new BarData(barDataSet);

                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.getDescription().setText("L");
                            barChart.animateY(2000);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Response error", error.getMessage());
                            Toast.makeText(WaterUsage.this,
                                            "Error_Daily.",
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
                    requestQueue.add(jsonObjectRequest_weekly);

                }
            }
        });


        //History of the water Usage

        TextView et_startDate = findViewById(R.id.et_startDate);
        TextView et_endDate = findViewById(R.id.et_endDate);

        DatePicker datePicker_start = findViewById(R.id.dp_startDate);
        DatePicker datePicker_end = findViewById(R.id.dp_endDate);

        TextView tv_historyButton = findViewById(R.id.tv_historyButton);

        Button btn_startConfirm = findViewById(R.id.btn_startConfirm);
        Button btn_endConfirm = findViewById(R.id.btn_endConfirm);

        rl_start = (ConstraintLayout) findViewById(R.id.rl_start);
        rl_end = (ConstraintLayout) findViewById(R.id.rl_end);

        et_startDate.setText("Pick a date");
        et_endDate.setText("Pick a date");

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a Range of Date");
        final MaterialDatePicker materialDatePickerH = builder.build();

        et_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               rl_start.setVisibility(rl_start.VISIBLE);
            }
        });

        et_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_end.setVisibility(rl_end.VISIBLE);
            }
        });

        datePicker_start.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                year_s = year;
                month_s = month + 1;
                day_s = day;
                date_s = String.valueOf(year_s) + "-" + String.valueOf(month_s) + "-" + String.valueOf(day_s);
                try {
                    date_start = simpleDate.parse(date_s);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        datePicker_end.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                year_e = year;
                month_e = month + 1;
                day_e = day;
                date_e = String.valueOf(year_e) + "-" + String.valueOf(month_e) + "-" + String.valueOf(day_e);
                try {
                    date_end = simpleDate.parse(date_e);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });




        btn_startConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_startDate.setText(date_s);
                rl_start.setVisibility(rl_start.GONE);
            }
        });

        btn_endConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_endDate.setText(date_e);
                rl_end.setVisibility(rl_end.GONE);
            }
        });

        tv_historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String date_s = simpleDate.format(date_start);
                String date_e = simpleDate.format(date_end);

                requestHistoryWaterUsage(date_s, date_e);

            }
        });

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

    protected void costWaterusage(String year, String month, double usage) {

        TextView tv_Cost = (TextView) findViewById(R.id.tv_Cost);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        String url = getString(R.string.base_url) + "api/WaterRate/" + year + "/" + month;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int price = response.optInt("price");
                double totalCost = price * usage / 1000;
                String totalCost_s = "$" + String.format("%.2f", totalCost);
                tv_Cost.setText(totalCost_s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(WaterUsage.this,
                                "Error_1.",
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

    protected void getCostofWaterUsage(Date date) {

        Calendar cal_Monthly = Calendar.getInstance();
        Calendar cal_Monthly_end = Calendar.getInstance();
        cal_Monthly.setTime(date);
        cal_Monthly.add(Calendar.MONTH, +0);

        String date_s = simpleMonth_cal.format(cal_Monthly.getTime());
        String year = simpleYear.format(cal_Monthly.getTime());
        String month = simpleMonth.format(cal_Monthly.getTime());

        Date date_end = cal_Monthly.getTime();
        cal_Monthly_end.setTime(date_end);

        int day_e = cal_Monthly.getActualMaximum(Calendar.DAY_OF_MONTH);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        String url = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + date_s + "-01&toDate=" + date_s + "-" + day_e + "&total=true";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                double usage = response.optDouble("totalUsage");

                costWaterusage(year, month, usage);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(WaterUsage.this,
                                "Error_1.",
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

    protected void requestHistoryWaterUsage(String date_s, String date_e) {

        arrayList.clear();


        String url = getString(R.string.base_url) + "api/WaterUsage/MyInfo?fromDate=" + date_s + "&toDate=" + date_e + "&total=true";
        String dateTotal = date_s + "~" + date_e;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Double value = response.getDouble("totalUsage");
                    String valueTotal = String.valueOf(value);
                    waterUsageData = new waterUsageData(0, dateTotal, valueTotal);
                    arrayList.add(waterUsageData);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(WaterUsage.this,
                                "Error_1.",
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

    class compareX implements Comparator<waterUsageData> {
        @Override
        public int compare(waterUsageData t1, waterUsageData t2) {

            LocalDate d1 = LocalDate.parse(t1.getVl_waterUsageHistoryDate());
            LocalDate d2 = LocalDate.parse(t2.getVl_waterUsageHistoryDate());

            int result = d1.compareTo(d2);

            if (result < 0) {
                return 1;
            } else {
                return -1;
            }
        }

    }

}
