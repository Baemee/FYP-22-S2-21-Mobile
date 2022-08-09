package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class updateProfile extends AppCompatActivity {

    SharedPreferences Token;
    String username, address, userId,  fullName, gender, email, phone, password;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        setProf();

        ImageView img_back = findViewById(R.id.img_back);
        Button btn_UpdateProfile = findViewById(R.id.btn_UpdateProfile);

        btn_UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProf();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
            }
        });

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


    protected void setProf() {

        EditText et_fullName = findViewById(R.id.et_fullName);
        EditText et_address = findViewById(R.id.et_address);
        EditText et_email = findViewById(R.id.et_email);
        EditText et_Phone = findViewById(R.id.et_Phone);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        fullName = Token.getString("fullName", String.valueOf(1));
        address = Token.getString("address", String.valueOf(1));
        email = Token.getString("email", String.valueOf("null"));
        phone = Token.getString("phone", String.valueOf("null"));

        et_fullName.setText(fullName);
        et_address.setText(address);
        et_email.setText(email);
        et_Phone.setText(phone);

    }

    protected void updateProf() {

        EditText et_fullName = findViewById(R.id.et_fullName);
        EditText et_address = findViewById(R.id.et_address);
        EditText et_email = findViewById(R.id.et_email);
        EditText et_phone = findViewById(R.id.et_Phone);

        Token = getSharedPreferences("user", MODE_PRIVATE);

        key = "Bearer " + Token.getString("token", String.valueOf(1));
        userId = Token.getString("userId", String.valueOf(1));
        gender = Token.getString("gender", String.valueOf(1));
        password = Token.getString("password", String.valueOf(1));
        username = Token.getString("username", String.valueOf(1));

        //changed value
        fullName = String.valueOf(et_fullName.getText());
        address = String.valueOf(et_address.getText());
        email = String.valueOf(et_email.getText());
        phone = String.valueOf(et_phone.getText());

        String url = getString(R.string.base_url) + "api/Customer/MyInfo";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("updatePassword", false);
            jsonObject.put("password", password);
            jsonObject.put("username", username);
            jsonObject.put("userId", userId);
            jsonObject.put("fullName", fullName);
            jsonObject.put("gender", gender);
            jsonObject.put("email", email);
            jsonObject.put("phone", phone);
            jsonObject.put("address", address);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                SharedPreferences.Editor editor = Token.edit();
                editor.putString("fullName", fullName);
                editor.putString("email", email);
                editor.putString("phone", phone);
                editor.putString("address", address);
                editor.apply();

                Toast.makeText(updateProfile.this, "Profile has been successfully updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response error", error.getMessage());
                Toast.makeText(updateProfile.this,
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
        requestQueue.add(jsonObjectRequest);

    }


}