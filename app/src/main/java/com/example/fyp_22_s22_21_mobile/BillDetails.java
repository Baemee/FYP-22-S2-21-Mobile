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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class BillDetails extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url = "http://10.0.2.2:5000/" + "api/Bill/MyInfo";
    String billId;
    int mth;
    int yr;
    double usage;
    double rate;
    double amount;
    String deadline;
    String payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        requestBillsDetails(url);
    }

    protected void requestBillsDetails(String url)
    {
        TextView tv_billId = findViewById(R.id.tv_getBillId);
        TextView tv_billMth = findViewById(R.id.tv_getbillMth);
        TextView tv_billYr = findViewById(R.id.tv_getBillYr);
        TextView tv_billUsage = findViewById(R.id.tv_getWaterUsage);
        TextView tv_rate = findViewById(R.id.getRate);
        TextView tv_billAmount = findViewById(R.id.tv_getBillAmount);
        TextView tv_deadline = findViewById(R.id.tv_getDateline);
        TextView tv_payment = findViewById(R.id.tv_getPayment);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int length = response.length();

                            for(int i = 0; i<length; i++)
                            {
                                billId = response.getJSONObject(i).getString("billId");
                                mth = response.getJSONObject(i).getInt("month");
                                yr = response.getJSONObject(i).getInt("year");
                                rate = response.getJSONObject(i).getDouble("rate");
                                amount = response.getJSONObject(i).getDouble("amount");
                                usage = response.getJSONObject(i).getDouble("totalUsage");
                                deadline = response.getJSONObject(i).getString("deadline");
                                payment = response.getJSONObject(i).getString("payment");
                                if (payment.equals("null"))
                                    payment="Unpaid";
                                else
                                    payment="Paid";

                                tv_billId.append(billId+"\n");
                                tv_billMth.append(mth+"\n");
                                tv_billYr.append(yr+"\n");
                                tv_rate.append(String.format("%.2f",rate)+"\n");
                                tv_billAmount.append(String.format("%.2f",amount)+"\n");
                                tv_billUsage.append(String.format("%.2f",usage)+"\n");
                                tv_deadline.append(deadline+"\n");
                                tv_payment.append(payment+"\n");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(BillDetails.this,
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
