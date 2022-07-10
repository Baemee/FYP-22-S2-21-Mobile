package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class UpdatePassword extends AppCompatActivity {

    String key;
    String username;
    String userId;
    String password;
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
        EditText et_NewPassword = findViewById(R.id.et_NewPassword);
        EditText et_ConfirmPassword = findViewById(R.id.et_ConfirmPassword);
        Button btn_cancelPassword = (Button) findViewById(R.id.btn_cancelPassword);
        key = "Bearer " + Token.getString("token", String.valueOf(1));
        username = Token.getString("username", String.valueOf(1));
        userId = Token.getString("userId", String.valueOf(1));
        fullName = Token.getString("fullName", String.valueOf(1));
        gender = Token.getString("gender", String.valueOf(1));
        email = Token.getString("email", String.valueOf(1));
        phone = Token.getString("phone", String.valueOf(1));

        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password1 = et_NewPassword.getText().toString();
                String cfmPassword = et_ConfirmPassword.getText().toString();
                Log.i("test", password1.length()+"");
                Log.i("test", cfmPassword.length()+"");
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
                            Toast.makeText(UpdatePassword.this,
                                            "An error occurred. Network response code " + error.networkResponse.statusCode,
                                            Toast.LENGTH_LONG)
                                    .show();
                            NetworkResponse networkResponse = error.networkResponse;
                            String errorString = new String(networkResponse.data);
                            Log.i("Test", errorString);

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

        btn_cancelPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });

    }


}