package com.example.bookportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PdfActivity extends AppCompatActivity {

    private Button goToSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        goToSell = findViewById(R.id.goToSellActivity);

        goToSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PdfActivity.this,PdfUploadActivity.class);
                startActivity(intent);
            }
        });
    }
}