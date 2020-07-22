package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bookportal.adapter.ItemsRecyclerAdapter;
import com.example.bookportal.domain.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookStoreActivity extends AppCompatActivity {


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    private List<Items> mItemList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter , searchRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);



        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        //mSearchText = findViewById(R.id.search_text);

       // mProgressCircle = findViewById(R.id.progress_circle);

        final GlobalData globalData = (GlobalData) getApplication();



        mItemList = new ArrayList<>();
        itemRecyclerView = findViewById(R.id.bookstore_recycler);
        //itemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this, mItemList,false);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);


        getMyBookData();
    }

    private void getMyBookData() {

        String collegePath , combinationPath , userID;
        final GlobalData globalData = (GlobalData) getApplication();


        collegePath = globalData.getCollegePath();
        combinationPath = globalData.getCombinationPath();
        userID = mAuth.getCurrentUser().getUid();


        mStore.collection("College")
                .document(collegePath)
                .collection("Combination")
                .document(combinationPath)
                .collection("BookData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                        Items items = doc.toObject(Items.class);
                        mItemList.add(items);
                    }
                    //mProgressCircle.setVisibility(View.INVISIBLE);
                    itemsRecyclerAdapter.notifyDataSetChanged();
                }

            }
        });



    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(BookStoreActivity.this, MainActivity.class));
        finish();
    }
}