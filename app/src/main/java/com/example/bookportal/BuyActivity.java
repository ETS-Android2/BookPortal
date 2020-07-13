package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;

import com.example.bookportal.adapter.ItemsRecyclerAdapter;
import com.example.bookportal.domain.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuyActivity extends AppCompatActivity {

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    List<Items> mItemList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;

    String collegePath, combinationPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();


        mItemList = new ArrayList<>();
        itemRecyclerView =findViewById(R.id.buy_recycler);
        //new GridLayoutManager(this ,2))
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(getApplicationContext(),mItemList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);




        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Path").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collegePath = document.getString("college");
                        combinationPath = document.getString("combination");
                        getData();

                    }

                }

            }
        });




    }

    public void getData(){


        mStore.collection("College").document(collegePath).collection("Combination")
                .document(combinationPath)
                .collection("BookData")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult().getDocuments()){
                        Items items =doc.toObject(Items.class);
                        mItemList.add(items);
                        itemsRecyclerAdapter.notifyDataSetChanged();
                    }
                }

            }
        });


    }
}