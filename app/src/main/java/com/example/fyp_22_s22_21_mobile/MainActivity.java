package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_Login = findViewById(R.id.btn_Login);
        EditText et_ID = findViewById(R.id.et_ID);
        EditText et_password = findViewById(R.id.et_Password);

        btn_Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = et_ID.getText().toString();
                String password = et_password.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url = getString(R.string.base_url) + "api/Customer/Login";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jsonKey = response.getString("token");
                            String userId = response.getJSONObject("user").getString("userId");

                            Token = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = Token.edit();
                            editor.putString("token", jsonKey);
                            editor.putString("username", username);
                            editor.putString("userId",userId);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 401) // unauthorized
                            Toast.makeText(MainActivity.this, "Invalid username or password" , Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,
                                    "An error occurred. Network response code " + error.networkResponse.statusCode ,
                                    Toast.LENGTH_LONG)
                                    .show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
            }
        });
    }

}
