package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PdfUploadActivity extends AppCompatActivity {

    private Button choosePdf;
    final static int PICK_PDF_CODE = 12;
    private EditText pdfname;
    ProgressBar progressBarPDF;

    private Uri pdfData;

    private Spinner semSpinner;
    private Spinner subjectSpinner;

    ArrayAdapter<String> semAdapter, subAdapter;
    ArrayList<String> semList, subList;
    private  String sem , sub;



    private StorageReference mStorageRef;


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    CollectionReference subjectsRef;
    String collegePath, combinationPath, phone;

    Boolean btnClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_upload);

        choosePdf = findViewById(R.id.choose_pdf);



        final GlobalData globalData = (GlobalData) getApplication();
        collegePath = globalData.getCollegePath();
        combinationPath = globalData.getCombinationPath();
        phone = globalData.getPhone();


        mStorageRef = FirebaseStorage.getInstance().getReference("PDFuploads");
        mStore = FirebaseFirestore.getInstance();
       // subjectsRef=mStore.collection("College");
        subjectsRef = mStore.collection("College")
                .document(collegePath)
                .collection("Combination")
                .document(combinationPath)
                .collection("PDFData");

        mAuth = FirebaseAuth.getInstance();

        pdfname = findViewById(R.id.pdfName);
        progressBarPDF = findViewById(R.id.progressBarpdf);

        semSpinner = findViewById(R.id.semSpinner);
        subjectSpinner = findViewById(R.id.subSpinner);


        getSpinnerData();



        semList = new ArrayList<>();
        semAdapter =new ArrayAdapter<String>(PdfUploadActivity.this, android.R.layout.simple_spinner_dropdown_item, semList);
        semSpinner.setAdapter(semAdapter);

        subList = new ArrayList<>();
        subAdapter =new ArrayAdapter<String>(PdfUploadActivity.this, android.R.layout.simple_spinner_dropdown_item, subList);
        subjectSpinner.setAdapter(subAdapter);



        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sem = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), sem, Toast.LENGTH_SHORT).show();
                getSpinnerSubData(sem);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), sub, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        choosePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnClicked){
                    getPDF();

                }else{
                    Toast.makeText(PdfUploadActivity.this, "Please wait uploading....", Toast.LENGTH_SHORT).show();
                }
                
            }
        });


    }

    private void getSpinnerSubData(String sem) {


        subjectsRef.document(sem).collection("subjects")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    subList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("name");
                        subList.add(subject);
                    }
                    subAdapter.notifyDataSetChanged();
                }

            }
        });



    }

    private void getSpinnerData() {


        subjectsRef
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    semList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String subject = document.getString("sem");
                        semList.add(subject);
                    }
                    semAdapter.notifyDataSetChanged();
                }
            }
        });


    }

    private void getPDF() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                    Uri.parse("package:" + getPackageName()));
//            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
//                uploadFile(data.getData());
                pdfData = data.getData();
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void uploadTextdata(String downloadURL, String pdfName) {

        String docID = String.valueOf(System.currentTimeMillis());

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, String> mMap = new HashMap<>();
        mMap.clear();
        mMap.put("pdf_url", downloadURL);
        mMap.put("pdf_name", pdfName);
        mMap.put("docID", docID);
        mMap.put("userID", userId);



        mStore.collection("College")
                .document(collegePath)
                .collection("Combination")
                .document(combinationPath)
                .collection("PDFData")
                .document(sem)
                .collection("subjects")
                .document(sub)
                .collection("pdfData")
                .document(docID)
                .set(mMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressBarPDF.setVisibility(View.INVISIBLE);

                        btnClicked = false;
                        Toast.makeText(PdfUploadActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnClicked = false;
                progressBarPDF.setVisibility(View.INVISIBLE);
                Toast.makeText(PdfUploadActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PdfUploadActivity.this, PdfOperationActivity.class));
        finish();
    }


    public void UploadPdf(View view) {

        btnClicked = true;

        progressBarPDF.setVisibility(View.VISIBLE);

        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + ".pdf");

        fileReference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadURL = uri.toString();
                                String pdfName = pdfname.getText().toString();

                                uploadTextdata(downloadURL,pdfName);
                            }
                        });




                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnClicked = false;
                        progressBarPDF.setVisibility(View.INVISIBLE);
                        Toast.makeText(PdfUploadActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
    }

    public void goBAK(View view) {
        startActivity(new Intent(PdfUploadActivity.this, PdfOperationActivity.class));
        finish();
    }
}