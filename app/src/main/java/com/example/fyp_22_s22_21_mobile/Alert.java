package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class Alert extends AppCompatActivity {

    SharedPreferences Token;

    private ArrayList<alertData> arrayList;
    private alertDataAdapter adapter;
    public RecyclerView.LayoutManager layoutManager;

    SearchView searchView;

    String url;
    String sgtDate;
    String testing;

    String search;
    String regex = ".*[a-zA-Z].*";

    int x = 1;
    int y = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        ImageView img_back = findViewById(R.id.img_back);
        RecyclerView rv_alert = findViewById(R.id.rv_alert);
        rv_alert.setHasFixedSize(true);
        TextView tv_test = (TextView) findViewById(R.id.tv_test);

        layoutManager = new LinearLayoutManager(this);
        rv_alert.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        adapter = new alertDataAdapter(arrayList, this);
        rv_alert.setAdapter(adapter);

        searchView = findViewById(R.id.searchView);

        //Shared Preference + Json
        requestAlert(1);

        //Search

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                arrayList.clear();
                searchAlert(s, 1);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                arrayList.clear();
                requestAlert(1);
                return false;
            }
        });

        //
        rv_alert.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!rv_alert.canScrollVertically(1)) {
                    search = searchView.getQuery().toString();
                    if (!search.contains(regex)) {
                        x++;
                        requestAlert(x);
                    } else {
                        y++;
                        searchAlert(search, y);

                    }

                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });

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
    }

    protected void requestAlert(int a) {
        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));

        String[] alertId = new String[100];
        String[] alertTitle = new String[100];
        String[] alertDescription = new String[100];
        String[] createdAt = new String[100];
        alertData[] alertData = new alertData[100];

        alertId[0] = "";
        alertTitle[0] = "";
        alertDescription[0] = "";
        createdAt[0] = "";
        url = "http://10.0.2.2:5000/api/BroadcastAlert?page=" + a + "&pageSize=" + 32;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject[] jsonObject = new JSONObject[100];
                JSONArray jsonArray = new JSONArray();
                jsonArray = response.optJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject[i] = jsonArray.optJSONObject(i);

                    alertId[i] = jsonObject[i].optString("alertId");
                    alertTitle[i] = jsonObject[i].optString("alertTitle");
                    alertDescription[i] = jsonObject[i].optString("alertDescription");
                    createdAt[i] = jsonObject[i].optString("createdAt");

                    sgtDate = createdAt[i];
                    try {
                        //Convert UTC to SGT
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date utcDate = utcFormat.parse(sgtDate);

                        DateFormat sgtFormat = new SimpleDateFormat("dd-MMM-yyyy");
                        sgtFormat.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));
                        sgtDate = sgtFormat.format(utcDate);
                        createdAt[i] = sgtDate;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    alertData[i] = new alertData(createdAt[i], alertTitle[i], alertDescription[i], alertId[i]);
                    arrayList.add(alertData[i]);

                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Alert.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    protected void searchAlert(String search, int a) {
        arrayList.clear();

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));

        String[] alertId = new String[100];
        String[] alertTitle = new String[100];
        String[] alertDescription = new String[100];
        String[] createdAt = new String[100];
        alertData[] alertData = new alertData[100];

        alertId[0] = "";
        alertTitle[0] = "";
        alertDescription[0] = "";
        createdAt[0] = "";
        url = getString(R.string.base_url) + "api/BroadcastAlert/Search?keyword=" + search + "&page=" + a + "&pageSize=" + 32;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                arrayList.clear();
                JSONObject[] jsonObject = new JSONObject[100];
                JSONArray jsonArray = new JSONArray();
                jsonArray = response.optJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {

                    jsonObject[i] = jsonArray.optJSONObject(i);

                    alertId[i] = jsonObject[i].optString("alertId");
                    alertTitle[i] = jsonObject[i].optString("alertTitle");
                    alertDescription[i] = jsonObject[i].optString("alertDescription");
                    createdAt[i] = jsonObject[i].optString("createdAt");

                    alertData[i] = new alertData(createdAt[i], alertTitle[i], alertDescription[i], alertId[i]);
                    arrayList.add(alertData[i]);
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Alert.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }
}
