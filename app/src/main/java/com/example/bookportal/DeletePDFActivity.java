package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bookportal.domain.Items;
import com.example.bookportal.domain.PdfItems;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.view.View.VISIBLE;

public class DeletePDFActivity extends AppCompatActivity {

    PdfItems pdfItems;

    Boolean correctUser = false;


    private FirebaseStorage mStorageRef;


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    CollectionReference subjectsRef;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_p_d_f);


        final GlobalData globalData = (GlobalData) getApplication();
        pdfItems = (PdfItems) getIntent().getSerializableExtra("detail");

        final String userID = globalData.getUserID();
        final String authConfirm = pdfItems.getUserID();


        final String collegePath = globalData.getCollegePath();
        final String combinationPath = globalData.getCombinationPath();



        mStorageRef = FirebaseStorage.getInstance();
        mStore = FirebaseFirestore.getInstance();
        // subjectsRef=mStore.collection("College");
        subjectsRef = mStore.collection("College")
                .document(collegePath)
                .collection("Combination")
                .document(combinationPath)
                .collection("PDFData")
                .document(globalData.getSem())
                .collection("subjects")
                .document(globalData.getSubject())
                .collection("pdfData");



        if (userID.equals(authConfirm)) {
            //delBtn.setVisibility(VISIBLE);
            correctUser = true;
        }
        if(correctUser){


            final StorageReference pdfDelPath = mStorageRef
                    .getReferenceFromUrl(pdfItems.getPdf_url());


            pdfDelPath.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    subjectsRef.document(pdfItems.getDocID())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DeletePDFActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DeletePDFActivity.this,PdfActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });








                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DeletePDFActivity.this, "failed"+e.getMessage(), Toast.LENGTH_SHORT).show();


                    Intent intent = new Intent(DeletePDFActivity.this,PdfActivity.class);
                    startActivity(intent);
                    finish();
                }
            });



        }
        else{
            Toast.makeText(DeletePDFActivity.this, "Only user can delete", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(DeletePDFActivity.this,PdfActivity.class);
            startActivity(intent);
            finish();
        }


    }
}