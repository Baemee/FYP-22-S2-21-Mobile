package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
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
import com.braintreepayments.api.DropInClient;
import com.braintreepayments.api.DropInListener;
import com.braintreepayments.api.DropInPaymentMethod;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.DropInResult;
import com.braintreepayments.api.GooglePayRequest;
import com.braintreepayments.api.PayPalRequest;
import com.braintreepayments.api.PaymentMethodNonce;
import com.example.fyp_22_s22_21_mobile.support.alertData;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class BillDetails extends AppCompatActivity implements DropInListener {

    //    private static final String SANDBOX_KEY = "sandbox_jy9bjhfx_pctg926vzs477j7t";
    private static final String SANDBOX_KEY = "sandbox_d5y6j6pb_cfmsx5n4w9rxwxw9";
    private static final String MERCHANT_SERVER_URL = "https://sdk-sample-merchant-server.herokuapp.com";


    SharedPreferences Token;
    String key;
    String url;
    String checkoutURL = "http://10.0.2.2:5000/" + "api/Payment";
    String billId, Nonce;
    int mth;
    int yr;
    double usage;
    double rate;
    double amount;
    String deadline;
    String payment;

    private DropInClient dropInClient;

    JSONObject jsonObject = new JSONObject();


    private void createPaymentModule() {
        DropInRequest dropInRequest = new DropInRequest();
        dropInRequest.setMaskCardNumber(false);

        dropInClient = new DropInClient(this, dropInRequest, SANDBOX_KEY);
        dropInClient.setListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);



        Intent getIntent = getIntent();
        String billId = getIntent.getStringExtra("billId");

        url = "http://10.0.2.2:5000/" + "api/Bill/MyInfo/" + billId;

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        requestBillsDetails(url);

        Button btn_payment = findViewById(R.id.btn_payment);
        Button btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bills.class);
                startActivity(intent);
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dropInClient.launchDropInForResult(BillDetails.this, 1111);
            }
        });
        createPaymentModule();

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

    protected void requestBillsDetails(String url) {
        TextView tv_billId = findViewById(R.id.tv_getBillId);
        TextView tv_billMth = findViewById(R.id.tv_getbillMth);
        TextView tv_billYr = findViewById(R.id.tv_getBillYr);
        TextView tv_billUsage = findViewById(R.id.tv_getWaterUsage);
        TextView tv_rate = findViewById(R.id.getRate);
        TextView tv_billAmount = findViewById(R.id.tv_getBillAmount);
        TextView tv_deadline = findViewById(R.id.tv_getDateline);
        TextView tv_payment = findViewById(R.id.tv_getPayment);

        Button btn_payment = findViewById(R.id.btn_payment);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    rate = response.getDouble("rate");
                    billId = response.getString("billId");
                    mth = response.getInt("month");
                    yr = response.getInt("year");
                    amount = response.getDouble("amount");
                    usage = response.getDouble("totalUsage");
                    deadline = response.getString("deadline");
                    payment = response.getString("payment");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (payment=="null") {
                    payment = "Unpaid";
                }
                else {
                    payment = "Paid";
                    btn_payment.setVisibility(View.GONE);
                }

                tv_billId.setText(billId );
                tv_billMth.setText(String.valueOf(mth));
                tv_billYr.setText(String.valueOf(yr));
                tv_rate.setText("$" + String.format("%.2f", rate));
                tv_billAmount.setText("$" + String.format("%.2f", amount));
                tv_billUsage.setText(String.format("%.2f", usage) + "L");
                tv_deadline.setText(deadline);
                tv_payment.setText(payment);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BillDetails.this, "Error", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);



    }

    protected void requestPayment(JSONObject jsonObject, String checkoutURL) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, checkoutURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String,String>();
                headers.put("Authorization", key);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onDropInSuccess(@NonNull DropInResult dropInResult) {

        TextView tv_getbillId = (TextView)findViewById(R.id.tv_getBillId);

        PaymentMethodNonce paymentNonce = dropInResult.getPaymentMethodNonce();

        DropInPaymentMethod paymentMethodType = dropInResult.getPaymentMethodType();
        if (paymentMethodType != null) {
            Nonce = paymentNonce.getString();
            String billId_s = tv_getbillId.getText().toString();

            try {
                jsonObject.put("billId", billId_s);
                jsonObject.put("paymentNonce", Nonce);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            requestPayment(jsonObject, checkoutURL);
            Intent intent = new Intent(getApplicationContext(), Bills.class);
            startActivity(intent);
        }

    }

    @Override
    public void onDropInFailure(@NonNull Exception e) {
        Toast.makeText(BillDetails.this, "Failed to make a purchase", Toast.LENGTH_LONG).show();
    }




}