package com.example.fyp_22_s22_21_mobile.API;

import android.app.Application;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp_22_s22_21_mobile.Model;
import com.example.fyp_22_s22_21_mobile.User;

import org.json.JSONException;
import org.json.JSONObject;

public class WebAPI implements API{

    public static final String BASE_URL = "http://10.0.2.2:5000/";

    private RequestQueue mRequestQueue;
    private final Application mApplication;
    private final Model mModel;

    public WebAPI(Application application) {
        mApplication = application;
        mRequestQueue= Volley.newRequestQueue((application));
        mModel = Model.getInstance(mApplication);
    }

    public void login(String username, String password) {
        String url = BASE_URL + "api/Customer/Login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);

            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(mApplication, "Successful", Toast.LENGTH_LONG);
                    try {
                        User user = User.getUser(response);
                        mModel.setUser(user);
                        Toast.makeText(mApplication, "Successful get User", Toast.LENGTH_LONG);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(mApplication, "Error get user", Toast.LENGTH_LONG);
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mApplication, "Error", Toast.LENGTH_LONG);
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
            mRequestQueue.add(request);

        } catch (JSONException e) {
            Toast.makeText(mApplication, "JsonException", Toast.LENGTH_LONG);
        }



    }

}
