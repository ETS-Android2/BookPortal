package com.example.bookportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PdfOperationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_operation);
    }

    public void download(View view) {

        startActivity(new Intent(PdfOperationActivity.this, PdfActivity.class));
        finish();
    }

    public void upload(View view) {
        startActivity(new Intent(PdfOperationActivity.this, PdfUploadActivity.class));
        finish();
    }

    public void back(View view) {
        startActivity(new Intent(PdfOperationActivity.this, MainActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(PdfOperationActivity.this, MainActivity.class));
        finish();
    }


}