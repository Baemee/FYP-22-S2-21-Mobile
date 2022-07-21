package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Report extends AppCompatActivity {

    SharedPreferences Token;
    String key;
    String url = "http://10.0.2.2:5000/" + "api/ReportTicket/MyInfo";
    String title;
    String date;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        key = "Bearer " + Token.getString("token", String.valueOf(1));

        Button btn_Resolve = (Button)findViewById(R.id.btn_Resolve);
        Button btn_Return = (Button)findViewById(R.id.btn_Return);
/*
        btn_Resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });
        //requestReportDetails(url);
    }

    protected void requestReportDetails(String url)
    {
        TextView tv_reportTitle = findViewById(R.id.tv_ReportTitle);
        TextView tv_reportDate = findViewById(R.id.tv_reportDate);
        TextView tv_reportDescription = findViewById(R.id.tv_resolve);
    }
}