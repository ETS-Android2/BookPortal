package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SellActivity extends AppCompatActivity {


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    CollectionReference userRef;

    TextView viewQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        userRef = mStore.collection("User");

        viewQ = findViewById(R.id.View);


        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Path").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                 for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("college");
                        String subject1 = document.getString("combination");
                         viewQ.setText(subject+"  "+subject1);

                    }

                }

            }
        });





    }
}