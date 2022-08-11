package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePassword extends AppCompatActivity {

    String key;
    String username;
    String userId;
    String fullName;
    String gender;
    String email;
    String phone;

    private SharedPreferences Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_password);

        Token = getSharedPreferences("user", MODE_PRIVATE);

        Button btn_savePassword = findViewById(R.id.btn_savePassword);
        ImageView img_back = findViewById(R.id.img_back);
        TextView tv_userID = findViewById(R.id.tv_userID);
        EditText et_NewPassword = findViewById(R.id.et_NewPassword);
        EditText et_ConfirmPassword = findViewById(R.id.et_ConfirmPassword);

        key = "Bearer " + Token.getString("token", String.valueOf(1));
        username = Token.getString("username", String.valueOf(1));
        userId = Token.getString("userId", String.valueOf(1));
        fullName = Token.getString("fullName", String.valueOf(1));
        gender = Token.getString("gender", String.valueOf(1));
        email = Token.getString("email", String.valueOf(1));
        phone = Token.getString("phone", String.valueOf(1));
        tv_userID.setText(username);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
            }
        });

        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = et_NewPassword.getText().toString();
                String cfmPassword = et_ConfirmPassword.getText().toString();

                if (!password1.equals(cfmPassword)) {
                    Toast.makeText(UpdatePassword.this, "Password and Confirm password is different", Toast.LENGTH_LONG).show();

                }
                else if ((password1.equals("") || (cfmPassword.equals("")))) {
                    Toast.makeText(UpdatePassword.this, "Please enter a password", Toast.LENGTH_LONG).show();
                }
                else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("updatePassword", true);
                        jsonObject.put("password", password1);
                        jsonObject.put("username", username);
                        jsonObject.put("userId", userId);
                        jsonObject.put("fullName", fullName);
                        jsonObject.put("gender", gender);
                        jsonObject.put("email", email);
                        jsonObject.put("phone", phone);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String url = getString(R.string.base_url) + "api/Customer/MyInfo";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            SharedPreferences.Editor editor = Token.edit();
                            editor.putString("password", password1);
                            editor.apply();

                            Toast.makeText(UpdatePassword.this, "Password Changed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error.networkResponse.statusCode == 400) {
                                Toast.makeText(UpdatePassword.this, "Password must contain at least on capital, small letter and special character", Toast.LENGTH_LONG).show();
                                et_NewPassword.setText("");
                                et_ConfirmPassword.setText("");

                            } else {
                                Toast.makeText(UpdatePassword.this,
                                                "An error occurred. Network response code " + error.networkResponse.statusCode,
                                                Toast.LENGTH_LONG)
                                        .show();
                                NetworkResponse networkResponse = error.networkResponse;
                                String errorString = new String(networkResponse.data);
                                Log.i("Test", errorString);
                            }
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
}