package com.example.fyp_22_s22_21_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

public class PaymentPage extends AppCompatActivity {

    final String API_GET_TOKEN = "http://10.0.2.2/braintree/main.php";
    final String API_CHECK_OUT = "http://10.0.2.2/braintree/checkout.php";

    String token, amount;
    HashMap<String, String> paramsHash;

    Button btn_pay;
    TextView tv_amount;
    LinearLayout group_waiting, group_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        ImageView img_back = findViewById(R.id.img_back);
        Button btn_pay = (Button) findViewById(R.id.btn_submitPayment);
        TextView tv_amount = (TextView) findViewById(R.id.tv_ExpirtionDate); // need to chage later

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Bills.class);
                startActivity(intent);
            }
        });

    }
}