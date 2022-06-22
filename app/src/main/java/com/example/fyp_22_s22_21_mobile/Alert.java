package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Alert extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

    TextView tv_Detail_1 = (TextView)findViewById(R.id.tv_Detail_1);
        Button btn_Return = (Button) findViewById(R.id.btn_Return);

    tv_Detail_1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), AlertDetail.class);
            startActivity(intent);
        }
    });

    btn_Return.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), homepage.class);
            startActivity(intent);
        }
    });
    }
}