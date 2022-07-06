package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    SharedPreferences Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        String key = Token.getString("token", String.valueOf(1));
        String username = Token.getString("username", String.valueOf(1));

        Button btn_WaterUsage = (Button) findViewById(R.id.btn_WaterUsage);
        Button btn_Alerts = (Button) findViewById(R.id.btn_Alerts);
        Button btn_Bills = (Button) findViewById(R.id.btn_Bills);
        Button btn_UpdatePassword = (Button) findViewById(R.id.btn_UpdatePassword);
        Button btn_Report = (Button) findViewById(R.id.btn_Report);
        Button btn_Logout = (Button) findViewById(R.id.btn_Logout);
        TextView tv_userID = (TextView) findViewById(R.id.tv_userID);
        tv_userID.setText(username);


        btn_WaterUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WaterUsage.class);
                startActivity(intent);
            }
        });

        btn_Alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);

            }
        });

        btn_Bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bills.class);
                startActivity(intent);

            }
        });

        btn_UpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdatePassword.class);
                startActivity(intent);

            }
        });

        btn_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Report.class);
                startActivity(intent);

            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


}
    }