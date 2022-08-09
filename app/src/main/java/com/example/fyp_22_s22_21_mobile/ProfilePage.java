package com.example.fyp_22_s22_21_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfilePage extends AppCompatActivity {

    SharedPreferences Token;
    String username, address, userId, createdAt, fullName, gender, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Button btn_pwd = findViewById(R.id.btn_changePwd);
        ImageView img_back = findViewById(R.id.img_back);
        TextView tv_userId = findViewById(R.id.tv_userId_user);
        TextView tv_userName = findViewById(R.id.tv_userName_user);
        TextView tv_fullName = findViewById(R.id.tv_fullName_user);
        TextView tv_address = findViewById(R.id.tv_address_user);
        TextView tv_dateCreate = findViewById(R.id.tv_dateCreate_user);
        TextView tv_Gender = findViewById(R.id.tv_Gender_user);
        TextView tv_email = findViewById(R.id.tv_email_user);
        TextView tv_phone = findViewById(R.id.tv_phone_user);

        Token = getSharedPreferences("user", MODE_PRIVATE);
        username = Token.getString("username", String.valueOf(1));
        address = Token.getString("address", String.valueOf(1));
        userId = Token.getString("userId", String.valueOf(1));
        createdAt = Token.getString("createdAt", String.valueOf(1));
        fullName = Token.getString("fullName", String.valueOf(1));
        gender = Token.getString("gender", String.valueOf("null"));
        email = Token.getString("email", String.valueOf("null"));
        phone = Token.getString("phones", String.valueOf("null"));

        tv_userId.setText(userId);
        tv_userName.setText(username);
        tv_fullName.setText(fullName);
        tv_address.setText(address);
        tv_dateCreate.setText(createdAt);
        tv_Gender.setText(gender);
        tv_email.setText(email);
        tv_phone.setText(phone);

        btn_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdatePassword.class);
                startActivity(intent);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(intent);
            }
        });

        // Bottom nav bar
        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.Profile);

        // Perform item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.Profile:
                        return true;
                    case R.id.Home:
                        startActivity(new Intent(getApplicationContext(),HomePageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}