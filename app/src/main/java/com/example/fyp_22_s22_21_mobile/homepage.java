package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Button btn_WaterUsage = (Button) findViewById(R.id.btn_WaterUsage);
        Button btn_Alerts = (Button) findViewById(R.id.btn_Alerts);
        Button btn_Bills = (Button) findViewById(R.id.btn_Bills);
        Button btn_UpdatePassword = (Button) findViewById(R.id.btn_UpdatePassword);
        Button btn_Report = (Button) findViewById(R.id.btn_Report);
        Button btn_Logout = (Button) findViewById(R.id.btn_Logout);


    }
}