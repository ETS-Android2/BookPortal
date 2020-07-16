package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
//public class BuyActivity extends AppCompatActivity implements ItemsRecyclerAdapter.OnItemClickListener
public class BuyActivity extends AppCompatActivity  {

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    List<Items> mItemList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter;

    private ProgressBar mProgressCircle;
    private Toolbar mToolBar;

    String collegePath, combinationPath;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        mToolBar = findViewById(R.id.home_toolba);
        setSupportActionBar(mToolBar);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        mProgressCircle = findViewById(R.id.progress_circle);

         final GobalData gobalData =   (GobalData)getApplication();


        mItemList = new ArrayList<>();
        itemRecyclerView =findViewById(R.id.buy_recycler);
        //itemRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        itemRecyclerView.setLayoutManager(new GridLayoutManager(this ,2));
        itemsRecyclerAdapter = new ItemsRecyclerAdapter(this,mItemList);
        itemRecyclerView.setAdapter(itemsRecyclerAdapter);

        //itemsRecyclerAdapter.SetOnItemClickListener(BuyActivity.this);




        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Path").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collegePath = document.getString("college");
                        combinationPath = document.getString("combination");
                        gobalData.setCollegePath(collegePath);
                        gobalData.setCombinationPath(combinationPath);
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
                    }
                    mProgressCircle.setVisibility(View.INVISIBLE);
                    itemsRecyclerAdapter.notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BuyActivity.this, OperationActivity.class));
        finish();
    }


    // for toolBar logout option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout_btn){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(BuyActivity.this,MainActivity.class));
            finish();

        }
        return true;
    }


//    @Override
//    public void onItemClick(int position) {
//
//    }
//
//    @Override
//    public void onWhatEverClick(int position) {
//
//    }
//
//    @Override
//    public void onDeleteClick(int position) {
//
//        Toast.makeText(this, "gege", Toast.LENGTH_SHORT).show();
//
//    }
}