package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.fyp_22_s22_21_mobile.support.waterUsageData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class billPrice extends AppCompatActivity {

    SharedPreferences Token;
    String url;

    String dateStart, dateEnd;
    String title, date;
    int year, month, rate;
    double totalUsage, amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_price);

        TextView tv_billTitle = (TextView) findViewById(R.id.tv_billTitle);
        TextView tv_BillDate_cal = (TextView) findViewById(R.id.tv_BillDate_cal);
        TextView tv_billRate_cal = (TextView) findViewById(R.id.tv_billRate_cal);
        TextView tv_billTotalUsage_cal = (TextView) findViewById(R.id.tv_billTotalUsage_cal);
        TextView tv_billCost_cal = (TextView) findViewById(R.id.tv_billCost_cal);

        Button btn_billCheckOut = (Button) findViewById(R.id.btn_billCheckOut);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));
        String userId = Token.getString("userId", String.valueOf(1));

        dateStart = "2022-06-01";
        dateEnd = "2022-06-30";

        url = getString(R.string.base_url) + "api/Bill/MyInfo?fromDate=" + dateStart + "&toDate=" + dateEnd;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    JSONObject data = response.getJSONObject(0);

                    title = data.optString("title");
                    year = data.optInt("year");
                    month = data.optInt("month");
                    rate = data.optInt("rate");
                    totalUsage = data.optLong("totalUsage");
                    amount = data.optDouble("amount");

                    date = String.valueOf(year) + "/" + String.valueOf(month);

                    tv_billTitle.setText(title);
                    tv_BillDate_cal.setText(date);
                    tv_billRate_cal.setText(String.valueOf(rate));
                    tv_billTotalUsage_cal.setText(String.valueOf(totalUsage) + "L");
                    tv_billCost_cal.setText("$" + String.valueOf(amount));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(billPrice.this, "Error", Toast.LENGTH_LONG).show();
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
        requestQueue.add(jsonArrayRequest);



        btn_billCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PaymentPage.class);
                intent.putExtra("amount", amount);
                startActivity(intent);
            }
        });

    }


}