package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.bookportal.adapter.PdfRecyclerAdapter;
import com.example.bookportal.adapter.SchemeRecyclerAdapter;
import com.example.bookportal.domain.PdfItems;
import com.example.bookportal.domain.Scheme;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class SchemeActivity extends AppCompatActivity {


    private List<Scheme> mSchemeList;
    private RecyclerView schemeRecycler;
    private SchemeRecyclerAdapter schemeRecyclerAdapter ;

    ProgressBar progressBar1;

    private FirebaseFirestore mStore;

    private StorageReference mStorageRef;
    private  String collegePath, combinationPath, phone ,  sem , sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme);

        progressBar1 = findViewById(R.id.main_progress_circle_10);

        final GlobalData globalData = (GlobalData) getApplication();
        collegePath = globalData.getCollegePath();
        combinationPath = globalData.getCombinationPath();

        mStore = FirebaseFirestore.getInstance();


        mSchemeList= new ArrayList<>();
        schemeRecycler = findViewById(R.id.SchemeRecycler);
        schemeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        schemeRecyclerAdapter = new SchemeRecyclerAdapter(this, mSchemeList);
        schemeRecycler.setAdapter(schemeRecyclerAdapter);


        //getRecyclerData();




        progressBar1.setVisibility(View.VISIBLE);


        mStore.collection("College")
                .document(collegePath)
                .collection("Combination")
                .document(combinationPath)
                .collection("scheme")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    mSchemeList.clear();
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Scheme items = doc.toObject(Scheme.class);
                        mSchemeList.add(items);
                    }
                    schemeRecyclerAdapter.notifyDataSetChanged();
                    progressBar1.setVisibility(View.INVISIBLE);

                }


            }
        });






    }


    private void getRecyclerData() {

    }


    public void goBAK(View view) {
    }
}