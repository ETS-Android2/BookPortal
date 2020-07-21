package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {

    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mPhone;
    private EditText mUSN;
    private Button mRegBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    private ProgressBar mProgressCircle;


    private Spinner combinationSpinner;
    private Spinner collegeSpinner;


    ArrayAdapter<String> adapter, adapter1;
    ArrayList<String> spinnerCombiData, spinnerColleData;

    CollectionReference subjectsRef;

    String college, combination, name, email, password, phone , usn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setContentView(R.layout.activity_register);
        mName = findViewById(R.id.reg_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mPhone = findViewById(R.id.reg_mobile);
        mRegBtn = findViewById(R.id.reg_btn);
        mUSN = findViewById(R.id.reg_usn);

        mProgressCircle = findViewById(R.id.progress_circle);
        mProgressCircle.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();


        mStore = FirebaseFirestore.getInstance();
        subjectsRef = mStore.collection("College");


        combinationSpinner = findViewById(R.id.combinationSpinner);
        collegeSpinner = findViewById(R.id.collegespinner);

        spinnerCombiData = new ArrayList<>();
        adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerCombiData);
        combinationSpinner.setAdapter(adapter);

        getSpinnerData();


        // spinner for combination
        combinationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                combination = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), combination + " +", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerColleData = new ArrayList<>();
        adapter1 = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerColleData);
        collegeSpinner.setAdapter(adapter1);

        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                college = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), college, Toast.LENGTH_SHORT).show();
                updateNext();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = mName.getText().toString();
                email = mEmail.getText().toString();
                password = mPassword.getText().toString();
                phone = mPhone.getText().toString();
                usn = mUSN.getText().toString();
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !phone.isEmpty()&& !usn.isEmpty()) {
                    mProgressCircle.setVisibility(View.VISIBLE);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        userDataUpdate();


                                    } else {
                                        Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressCircle.setVisibility(View.INVISIBLE);
                            Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show();


                        }
                    });// we can put error listener too

                } else {
                    Toast.makeText(RegisterActivity.this, "Please fill empty field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void userDataUpdate() {

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, String> mMap = new HashMap<>();
        mMap.put("college", college);
        mMap.put("combination", combination);
        mMap.put("name", name);
        mMap.put("email", email);
        mMap.put("phone", phone);
        mMap.put("usn", usn);
        mMap.put("userID", userId);


        //important stuff
        // mAuth.getCurrentUser().getUid() (the current user id)
        mStore.collection("User")
                .document(userId)
                .collection("Path")
                .document(userId)
                .set(mMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                        mProgressCircle.setVisibility(View.INVISIBLE);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });




    }

    public void getSpinnerData() {

        subjectsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    spinnerColleData.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("name");
                        spinnerColleData.add(subject);
                    }
                    adapter1.notifyDataSetChanged();
                }
            }
        });


    }

    public void signIn(View view) {

        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void updateNext() {

        subjectsRef.document(college).collection("Combination").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if (task.isSuccessful()) {
                    spinnerCombiData.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("department");
                        spinnerCombiData.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });


    }


}