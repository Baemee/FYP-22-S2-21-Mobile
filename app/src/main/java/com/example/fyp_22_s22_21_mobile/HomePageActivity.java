package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        ImageView btn_Alerts = findViewById(R.id.img_alert);
        Button btn_Bills = (Button) findViewById(R.id.btn_Bills);
        Button btn_Report = (Button) findViewById(R.id.btn_Report);
        ImageView btn_Logout = findViewById(R.id.img_logout);
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

        btn_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReportPage.class);
                startActivity(intent);

            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        // Bottom nav bar
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Home);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Home:
                        return true;
                    case R.id.Profile:
                        startActivity(new Intent(getApplicationContext(),ProfilePage.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}
