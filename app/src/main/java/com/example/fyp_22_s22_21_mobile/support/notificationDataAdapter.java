package com.example.fyp_22_s22_21_mobile.support;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.fyp_22_s22_21_mobile.Alert;
import com.example.fyp_22_s22_21_mobile.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class notificationDataAdapter extends RecyclerView.Adapter<notificationDataAdapter.CustomViewHolder> {

    SharedPreferences Token;

    private ArrayList<notificationData> arrayList;
    private Context context;
    private String[] vl_notificationId = new String[1000];
    private String[] vl_recipientId = new String[1000];
    private String[] vl_date = new String[1000];


    public notificationDataAdapter(ArrayList<notificationData> arrayList, Context context) {

        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public notificationDataAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notificationviewlist, parent, false);
        notificationDataAdapter.CustomViewHolder holder = new notificationDataAdapter.CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull notificationDataAdapter.CustomViewHolder holder, int position) {

        holder.vl_notificationContent.setText(arrayList.get(position).getVl_notificationContent());
        vl_notificationId[position] = arrayList.get(position).getVl_notificationId();
        vl_recipientId[position] = arrayList.get(position).getvl_recipientId();
        vl_date[position] = arrayList.get(position).getVl_date();

        holder.vl_notificationContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewNotification(vl_notificationId[position]);

            }
        });

        holder.iv_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestRead(vl_notificationId[position]);
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification(vl_notificationId[position]);
            }
        });

    }
    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView vl_notificationContent;
        protected ImageView iv_mark;
        protected ImageView iv_delete;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.vl_notificationContent = (TextView) itemView.findViewById(R.id.vl_notificationContent);
            this.iv_mark = (ImageView) itemView.findViewById((R.id.iv_mark));
            this.iv_delete = (ImageView) itemView.findViewById((R.id.iv_delete));
        }
    }

    protected void requestRead(String notificationId) {

        Token = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));

        String url = context.getString(R.string.base_url) + "api/Notification/" + notificationId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean isRead = response.optBoolean("isRead");
                requestReadChange(notificationId, isRead);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    protected void requestReadChange (String notificationId, Boolean isRead) {
        Token = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String read;

        String read_test = isRead.toString();

        if(read_test.contains("ue")) {
            read = "false";
        } else {
            read = "true";
        }

        String url = context.getString(R.string.base_url) + "api/Notification/" + notificationId + "/" + read;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            int a = 9;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    protected void deleteNotification(String notificationId) {

        Token = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));

        String url = context.getString(R.string.base_url) + "api/Notification/" + notificationId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Boolean success = response.optBoolean("success");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    protected void viewNotification(String notificationId) {

        Token = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));

        String url = context.getString(R.string.base_url) + "api/Notification/" + notificationId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String notification_detail = response.optString("content");
                AlertDialog.Builder dlg = new AlertDialog.Builder(context);
                dlg.setTitle("Notification");
                dlg.setMessage(notification_detail);
                dlg.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestReadChange(notificationId, false);
                    }
                });
                dlg.show();

                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }

}
