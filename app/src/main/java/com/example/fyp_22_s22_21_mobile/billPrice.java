package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.example.fyp_22_s22_21_mobile.support.alertData;
import com.example.fyp_22_s22_21_mobile.support.waterUsageData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class billPrice extends AppCompatActivity {

    private static final int REQUEST_CODE = 1234;

    Button btn_pay;
    TextView tv_amount;
    TextView tv_totalPrice;

    SharedPreferences Token;
    String url;

    String dateStart, dateEnd;
    String title, date;
    int year, month, rate;
    double totalUsage, amount;

    final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    final String API_CHECK_OUT = "http://10.0.2.2/braintree/checkout.php";

    String token_pay, amount_pay;
    HashMap<String, String> paramsHash;
    Button btn_billCheckOut;
    TextView tv_billCost_cal;
    LinearLayout group_payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_price);

        TextView tv_billTitle = (TextView) findViewById(R.id.tv_billTitle);
        TextView tv_BillDate_cal = (TextView) findViewById(R.id.tv_BillDate_cal);
        TextView tv_billRate_cal = (TextView) findViewById(R.id.tv_billRate_cal);
        TextView tv_billTotalUsage_cal = (TextView) findViewById(R.id.tv_billTotalUsage_cal);

        tv_billCost_cal = (TextView) findViewById(R.id.tv_billCost_cal);
        btn_billCheckOut = (Button) findViewById(R.id.btn_billCheckOut);

        btn_pay = (Button)findViewById(R.id.btn_pay);

        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);

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

        new getToken().execute();

        btn_billCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPayment();
            }
        });




    }

    private class getToken extends AsyncTask {
        ProgressDialog mDialog;

        @Override
        protected Object doInBackground(Object[] objects) {
            HttpClient client=new HttpClient();
            client.get(API_GET_TOKEN, new HttpResponseCallback() {
                @Override
                public void success(final String responseBody) {
                    mDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            token_pay=responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception) {
                    mDialog.dismiss();
                    Log.d("Err",exception.toString());
                }
            });
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog=new ProgressDialog(billPrice.this,android.R.style.Theme_DeviceDefault_Light_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Loading Wallet, Please Wait");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
        }
    }

    private void submitPayment() {
        String payValue=tv_billCost_cal.getText().toString();
        if(!payValue.isEmpty())
        {
            DropInRequest dropInRequest=new DropInRequest().clientToken(token_pay);
            startActivityForResult(dropInRequest.getIntent(this),REQUEST_CODE);
        }
        else
            Toast.makeText(this, "Error getting an price", Toast.LENGTH_SHORT).show();
    }

    private void sendPayments(){
        RequestQueue queue= Volley.newRequestQueue(billPrice.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().contains("Successful")){
                            Toast.makeText(billPrice.this, "Payment Success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(billPrice.this, "Payment Failed", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Response",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Err",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(paramsHash==null)
                    return null;
                Map<String,String> params=new HashMap<>();
                for(String key:paramsHash.keySet())
                {
                    params.put(key,paramsHash.get(key));
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Content-type","application/x-www-form-urlencoded");
                return params;
            }
        };
        RetryPolicy mRetryPolicy=new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(mRetryPolicy);
        queue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNounce = nonce.getNonce();
                if (!tv_totalPrice.getText().toString().isEmpty()) {
                    amount_pay = tv_totalPrice.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount", amount_pay);
                    paramsHash.put("nonce", strNounce);

                    sendPayments();
                } else {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "User canceled", Toast.LENGTH_SHORT).show();
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("Err", error.toString());
            }
        }
    }

}