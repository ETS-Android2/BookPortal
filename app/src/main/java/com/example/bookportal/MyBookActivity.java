package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bookportal.adapter.ItemsRecyclerAdapter;
import com.example.bookportal.domain.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyBookActivity extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    private List<Items> mMyBookList;
    private RecyclerView myBookRecyclerView;
    private ItemsRecyclerAdapter myBookRecyclerAdapter ;
    private ProgressBar mProgressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);


        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        //mSearchText = findViewById(R.id.search_text);

        mProgressCircle = findViewById(R.id.progress_circle);

        final GlobalData globalData = (GlobalData) getApplication();



        mMyBookList = new ArrayList<>();
        myBookRecyclerView = findViewById(R.id.my_book_recycler);
        //itemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        myBookRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myBookRecyclerAdapter = new ItemsRecyclerAdapter(this, mMyBookList,false);
        myBookRecyclerView.setAdapter(myBookRecyclerAdapter);


        getMyBookData();

    }

    private void getMyBookData() {
        String collegePath , combinationPath , userID;
        final GlobalData globalData = (GlobalData) getApplication();


        collegePath = globalData.getCollegePath();
        combinationPath = globalData.getCombinationPath();
        userID = mAuth.getCurrentUser().getUid();

        mStore.collection("College").document(collegePath).collection("Combination")
                .document(combinationPath)
                .collection("BookData")
                .whereEqualTo("userID",userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for (DocumentSnapshot doc:task.getResult().getDocuments()){
                                Items items = doc.toObject(Items.class);
                                mMyBookList.add(items);
                                myBookRecyclerAdapter.notifyDataSetChanged();
                                //mProgressCircle.setVisibility(View.INVISIBLE);
                            }
                            mProgressCircle.setVisibility(View.INVISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(globalData, ""+e, Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(MyBookActivity.this, MainActivity.class));
        finish();
    }

}