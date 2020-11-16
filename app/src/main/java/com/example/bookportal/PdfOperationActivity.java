package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PdfOperationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerlayout;
    NavigationView navvigationView;
    ImageView menuIcon;
    LinearLayout contentView;
    static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pdf_operation);






        //mProgressCircleBookSore=findViewById(R.id.main_progress_circle_1);
        //navigation
        contentView = findViewById(R.id.content_PDF);

        //menu
        menuIcon = findViewById(R.id.nav_menu_PDF);

        //Menu
        drawerlayout = findViewById(R.id.drawer_layout_PDF);
        navvigationView = findViewById(R.id.navviewPDF);

        //navigation methods(menuicon)
        navigationDrawer();







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





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_pdf:
                break;
            case R.id.nav_home:
                startActivity(new Intent(PdfOperationActivity.this, MainActivity.class));
                break;
            case R.id.nav_myacc:
                startActivity(new Intent(PdfOperationActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_search:
                startActivity(new Intent(PdfOperationActivity.this, SearchActivity.class));
                break;
            case R.id.nav_sell_books:
                startActivity(new Intent(PdfOperationActivity.this, SellActivity.class));
                break;
            case R.id.nav_mybooks:
                startActivity(new Intent(PdfOperationActivity.this, MyBookActivity.class));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PdfOperationActivity.this, LoginActivity.class));
                break;
        }
        return true;
    }


    @Override
    public void onBackPressed(){
        if (drawerlayout.isDrawerVisible(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        } else{
            startActivity(new Intent(PdfOperationActivity.this, MainActivity.class));
            finish();
        }

    }








    public void download(View view) {

        startActivity(new Intent(PdfOperationActivity.this, PdfActivity.class));
        finish();
    }

    public void upload(View view) {
        startActivity(new Intent(PdfOperationActivity.this, PdfUploadActivity.class));
        finish();
    }

    public void back(View view) {
        startActivity(new Intent(PdfOperationActivity.this, MainActivity.class));
        finish();
    }



}