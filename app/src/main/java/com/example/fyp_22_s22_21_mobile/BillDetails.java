package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
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
import com.braintreepayments.api.DropInClient;
import com.braintreepayments.api.DropInListener;
import com.braintreepayments.api.DropInPaymentMethod;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.DropInResult;
import com.braintreepayments.api.GooglePayRequest;
import com.braintreepayments.api.PaymentMethodNonce;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    String url = "http://10.0.2.2:5000/" + "api/Bill/MyInfo";
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
        dropInRequest.setGooglePayRequest(getGooglePayRequest());
        dropInRequest.setMaskCardNumber(false);


        dropInClient = new DropInClient(this, dropInRequest, SANDBOX_KEY);
        dropInClient.setListener(this);
    }

    private GooglePayRequest getGooglePayRequest() {
        GooglePayRequest googlePayRequest = new GooglePayRequest();
        googlePayRequest.setTransactionInfo(TransactionInfo.newBuilder()
                .setTotalPrice("1.00")
                .setCurrencyCode("USD")
                .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                .build());
        googlePayRequest.setEmailRequired(true);
        return googlePayRequest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

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
//                Intent intent = new Intent(getApplicationContext(), PaymentPage.class);
//                startActivity(intent);
                dropInClient.launchDropInForResult(BillDetails.this, 1111);
            }
        });

        requestBillsDetails(url);
        createPaymentModule();

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

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            int length = response.length();

                            for (int i = 0; i < length; i++) {
                                billId = response.getJSONObject(i).getString("billId");
                                mth = response.getJSONObject(i).getInt("month");
                                yr = response.getJSONObject(i).getInt("year");
                                rate = response.getJSONObject(i).getDouble("rate");
                                amount = response.getJSONObject(i).getDouble("amount");
                                usage = response.getJSONObject(i).getDouble("totalUsage");
                                deadline = response.getJSONObject(i).getString("deadline");
                                payment = response.getJSONObject(i).getString("payment");

                                if (payment.equals("null"))
                                    payment = "Unpaid";
                                else
                                    payment = "Paid";

                                tv_billId.append(billId + "\n");
                                tv_billMth.append(mth + "\n");
                                tv_billYr.append(yr + "\n");
                                tv_rate.append(String.format("%.2f", rate) + "\n");
                                tv_billAmount.append(String.format("%.2f", amount) + "\n");
                                tv_billUsage.append(String.format("%.2f", usage) + "\n");
                                tv_deadline.append(deadline + "\n");
                                tv_payment.append(payment + "\n");

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

    protected void requestPayment(JSONObject jsonObject, String checkoutURL) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, checkoutURL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Boolean result = response.getBoolean("success");
                    if (result = true) {
                        Toast.makeText(BillDetails.this, "Payment has bee successfully paid", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(BillDetails.this, "Failed to make a purchase_2", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BillDetails.this, "Failed to make a purchase_1", Toast.LENGTH_LONG).show();
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

        PaymentMethodNonce paymentNonce = dropInResult.getPaymentMethodNonce();

        DropInPaymentMethod paymentMethodType = dropInResult.getPaymentMethodType();
        if (paymentMethodType != null) {
            Toast.makeText(BillDetails.this, paymentNonce.getString(), Toast.LENGTH_LONG).show();
            Nonce = paymentNonce.getString();

            try {
                jsonObject.put("billId", billId);
                jsonObject.put("paymentNonce", Nonce);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            requestPayment(jsonObject, checkoutURL);
        }

    }

    @Override
    public void onDropInFailure(@NonNull Exception e) {
        Toast.makeText(BillDetails.this, "Failed to make a purchase", Toast.LENGTH_LONG).show();
    }




}