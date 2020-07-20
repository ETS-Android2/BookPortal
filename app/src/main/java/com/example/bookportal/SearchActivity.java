package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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

public class SearchActivity extends AppCompatActivity {


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    private List<Items> mItemList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter , searchRecyclerAdapter;

    private EditText mSearchText;


    private ProgressBar mProgressCircle;
    private Toolbar mToolBar;
    String collegePath, combinationPath;

    //SEARCH
    private List<Items> mItemSearchList;
    //private List<SearchItems> mItemSearchList;
    private RecyclerView mItemSearchRecyclerView;
    //private SearchRecyclerAdapter searchRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        mToolBar = findViewById(R.id.home_toolba);
        setSupportActionBar(mToolBar);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mSearchText = findViewById(R.id.search_text);



        mItemSearchList = new ArrayList<>();
        mItemSearchRecyclerView = findViewById(R.id.search_recycler);
        mItemSearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerAdapter = new ItemsRecyclerAdapter(this, mItemSearchList,true);
        mItemSearchRecyclerView.setAdapter(searchRecyclerAdapter);


        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mItemSearchList.clear();
                searchRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String nothing = null;
                //||s.toString().contains(" ")||s.toString().equals(nothing)


                if(s.toString().isEmpty()||s.toString().equals("")) {
                    mItemSearchList.clear();
                    searchRecyclerAdapter.notifyDataSetChanged();
                    Log.i("TAG", "searchItem: NOT FOUND  11"+s.toString());

                }
                else{
                    Log.i("TAG", "searchItem: FOUND 9090900");

                    searchItem(s.toString());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().isEmpty()){
                    mItemSearchList.clear();
                    searchRecyclerAdapter.notifyDataSetChanged();
                    Log.i("TAG", "searchItem: NOT FOUND  22");

                }

            }
        });




    }

    private void searchItem(String text) {
        mItemSearchList.clear();
        searchRecyclerAdapter.notifyDataSetChanged();
        final GobalData gobalData = (GobalData) getApplication();


        collegePath = gobalData.getCollegePath();
        combinationPath = gobalData.getCombinationPath();


        if(!text.isEmpty()){
            mStore.collection("College").document(collegePath).collection("Combination")
                    .document(combinationPath)
                    .collection("BookData")
                    .whereGreaterThanOrEqualTo("book",text).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()&& task.getResult()!=null){
                                for (DocumentSnapshot doc:task.getResult().getDocuments()){
                                    Log.i("mi", "onComplete: "+ doc.getData());
                                    Items items = doc.toObject(Items.class);
                                    mItemSearchList.add(items);
                                    searchRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

        }
        else{
            Log.i("TAG", "searchItem: NOT FOUND  3333");
            mItemSearchList.clear();
            searchRecyclerAdapter.notifyDataSetChanged();
        }
    }

    public void clearBtn(View view) {
        mItemSearchList.clear();
        searchRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SearchActivity.this, MainActivity.class));
        finish();
    }

}