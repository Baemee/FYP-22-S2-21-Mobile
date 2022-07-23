package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class Bills extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url = "http://10.0.2.2:5000/" + "api/Bill/MyInfo";
    String date;
    double amount;
    double usage;
    //String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));
        Button btn_back = (Button) findViewById(R.id.btn_back);
        TextView tv_billDate = findViewById(R.id.tv_billDate);

        requestBills(url);

        tv_billDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BillDetails.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void requestBills(String url)
    {
        TextView tv_billDate = findViewById(R.id.tv_billDate);
        TextView tv_billAmount = findViewById(R.id.tv_billAmount);
        TextView tv_billUsage = findViewById(R.id.tv_billUsage);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int length = response.length();

                            for(int i = 0; i<length; i++)
                            {
                                date = response.getJSONObject(i).getString("title");
                                amount = response.getJSONObject(i).getDouble("amount");
                                usage = response.getJSONObject(i).getDouble("totalUsage");
                                //status = response.getJSONObject(i).getString("Payment");

                                tv_billDate.append(date+"\n");
                                tv_billAmount.append(String.format("%.2f",amount)+"\n");
                                tv_billUsage.append(String.format("%.2f",usage)+"\n");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(Bills.this,
                                "An error occurred.",
                                Toast.LENGTH_LONG)
                        .show();
            }
        })
        {
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