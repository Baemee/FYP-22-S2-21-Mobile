package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

public class CreateReport extends AppCompatActivity
{
    String key;
    String custID;
    int priority;
    String status;
    String timeStamp;

    private SharedPreferences Token;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        Button btn_Submit = findViewById(R.id.btn_Submit);
        ImageView btn_Cancel = findViewById(R.id.img_back);
        EditText et_title = findViewById(R.id.et_title);
        EditText et_ReportDescription = findViewById(R.id.et_ReportDescription);

        custID = Token.getString("userId", String.valueOf(1));

        btn_Submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String title = et_title.getText().toString();
                String description = et_ReportDescription.getText().toString();
                timeStamp = Instant.now().toString();

                if (title.equals(""))
                {
                    Toast.makeText(CreateReport.this, "Title cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if (description.equals(""))
                {
                    Toast.makeText(CreateReport.this, "Description cannot be empty", Toast.LENGTH_LONG).show();
                }
                else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("title", title);
                        jsonObject.put("description", description);
                        jsonObject.put("customerId", custID);
                        jsonObject.put("createdAt",timeStamp);
                        jsonObject.put("status", "Active");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = getString(R.string.base_url) + "api/ReportTicket";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            SharedPreferences.Editor editor = Token.edit();
                            editor.putString("title", title);
                            editor.putString("description", description);
                            editor.apply();

                            Toast.makeText(CreateReport.this, "Report Ticket created", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), ReportMain.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(CreateReport.this,
                                            "An error occurred. Network response code " + error.networkResponse.statusCode,
                                            Toast.LENGTH_LONG)
                                    .show();
                            NetworkResponse networkResponse = error.networkResponse;
                            String errorString = new String(networkResponse.data);
                            Log.i("Test", "Error" + errorString);
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
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportMain.class);
                startActivity(intent);
            }
        });
    }
}