package com.example.bookportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class OperationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
    }

    public void goToBuy(View view) {
        startActivity(new Intent(OperationActivity.this, BuyActivity.class));
        finish();
    }

    public void goToSell(View view) {


        startActivity(new Intent(OperationActivity.this, SellActivity.class));
        finish();

    }


}