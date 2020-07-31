package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.bookportal.adapter.ItemsRecyclerAdapter;
import com.example.bookportal.domain.Items;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookStoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //Drawer Menu
    DrawerLayout drawerlayout;
    NavigationView navvigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    static final float END_SCALE = 0.7f;


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressCircleBookSore;

    private List<Items> mItemList;
    private RecyclerView itemRecyclerView;
    private ItemsRecyclerAdapter itemsRecyclerAdapter , searchRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);



        mProgressCircleBookSore=findViewById(R.id.main_progress_circle_1);
        //navigation
        contentView = findViewById(R.id.content);

        //menu
        menuIcon = findViewById(R.id.nav_menu);

        //Menu
        drawerlayout = findViewById(R.id.drawer_layoutBookStore);
        navvigationView = findViewById(R.id.navview);

        //navigation methods(menuicon)
        navigationDrawer();








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

    private void navigationDrawer() {

//        //Navigation Drawer
        navvigationView.bringToFront();
        navvigationView.setNavigationItemSelectedListener(this);
        navvigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerlayout.isDrawerVisible(GravityCompat.START)) {
                    drawerlayout.closeDrawer(GravityCompat.START);
                } else
                    drawerlayout.openDrawer(GravityCompat.START);
            }
        });

       animateNavbar();



    }

    private void animateNavbar() {

        drawerlayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                //scale the view on slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //Translate teh view for scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslate = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslate);
            }


        });

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
                    mProgressCircleBookSore.setVisibility(View.INVISIBLE);
                }

            }
        });



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_shop:
                startActivity(new Intent(BookStoreActivity.this, BookStoreActivity.class));
            case R.id.nav_pdf:
                break;
            case R.id.nav_home:
                startActivity(new Intent(BookStoreActivity.this, MainActivity.class));
                break;
            case R.id.nav_myacc:
                startActivity(new Intent(BookStoreActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_search:
                startActivity(new Intent(BookStoreActivity.this, SearchActivity.class));
                break;
            case R.id.nav_sell_books:
                startActivity(new Intent(BookStoreActivity.this, SellActivity.class));
                break;
            case R.id.nav_mybooks:
                startActivity(new Intent(BookStoreActivity.this, MyBookActivity.class));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BookStoreActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed(){
        if (drawerlayout.isDrawerVisible(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        } else{
            startActivity(new Intent(BookStoreActivity.this, MainActivity.class));
            finish();
        }

    }
}






