package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private String url = "http://10.0.2.2:5000/api/Customer/Login";
    private SharedPreferences Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_Login = (Button) findViewById(R.id.btn_Login);
        EditText et_ID = (EditText) findViewById(R.id.et_ID);
        EditText et_password = (EditText) findViewById(R.id.et_Password);
        TextView tv_Login = (TextView)findViewById(R.id.tv_LoginPage);

        final String[] test = new String[1];

        btn_Login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Token = getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = Token.edit();
                editor.clear();
                editor.commit();

                String username = et_ID.getText().toString();
                String password = et_password.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String jsonKey = response.getString("token");
                            test[0] = jsonKey;
                            Token = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = Token.edit();
                            editor.putString("token", jsonKey);
                            editor.putString("username", username);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(MainActivity.this, test[0], Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), homepage.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Invalid username or password" , Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);

            }
        });
    }

}
