package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePassword extends AppCompatActivity {

    private SharedPreferences Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_update_password);

        Button btn_savePassword = findViewById(R.id.btn_savePassword);
        EditText et_NewPassword = findViewById(R.id.et_NewPassword);
        EditText et_ConfirmPassword = findViewById(R.id.et_ConfirmPassword);
        Button btn_cancelPassword = (Button) findViewById(R.id.btn_cancelPassword);

        btn_savePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = et_NewPassword.getText().toString();
                String cfmPassword = et_ConfirmPassword.getText().toString();

                JSONObject jsonObject = new JSONObject();
                String url = getString(R.string.base_url) + "api/Customer/MyInfo";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            jsonObject.put("updatePassword", true);
                            //String jsonKey = response.getString("token");
                            String userId = response.getJSONObject("user").getString("userId");
                            String username = response.getJSONObject("user").getString("username");
                            String fullName = response.getJSONObject("user").getString("fullName");
                            String email = response.getJSONObject("user").getString("email");
                            String phone = response.getJSONObject("user").getString("phone");
                            String lastMaintenance = response.getJSONObject("user").getString("lastMaintenance");
                            String gender = response.getJSONObject("user").getString("gender");

                            Token = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = Token.edit();
                            //editor.putString("token", jsonKey);
                            editor.putString("username", username);
                            editor.putString("userId",userId);
                            editor.putString("password",password);
                            editor.putString("fullName",fullName);
                            editor.putString("email",email);
                            editor.putString("phone",phone);
                            editor.putString("gender",gender);
                            editor.putString("lastMaintenance",lastMaintenance);
                            editor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(UpdatePassword.this, "Password Changed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(UpdatePassword.this,
                                            "An error occurred. Network response code " + error.networkResponse.statusCode ,
                                            Toast.LENGTH_LONG)
                                    .show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(jsonObjectRequest);
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