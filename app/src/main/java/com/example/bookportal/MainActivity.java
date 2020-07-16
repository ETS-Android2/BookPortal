package com.example.bookportal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
    }

    public void goToLogin(View view) {
        Intent intent =new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void goToSignUp(View view) {
        Intent intent =new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check whether the use exist or not
        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(MainActivity.this,OperationActivity.class));
            finish();
        }
    }

    public void testO(View view) {



//        Uri uri = Uri.parse("smsto:" + "+918762623837");
//        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hai Good Morning");
//        sendIntent.setPackage("com.whatsapp");
//        startActivity(sendIntent);

//        String number= "9483645664";
//
//        Uri uri = Uri.parse("smsto:" + number);
//        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//        i.setPackage("com.whatsapp");
//        startActivity(Intent.createChooser(i, "GCCCC"));
    }
}