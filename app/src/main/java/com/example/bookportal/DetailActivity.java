package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bookportal.domain.Items;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class  DetailActivity extends AppCompatActivity {
    private ImageView bookImage;
    private TextView bookName;
    private TextView authorName;
    private TextView bookDes;
    private TextView ownerName;
    private ImageButton delBtn;
    String number, docID;
    Boolean correctUser = false;


    private FirebaseStorage mStorage;
    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    private List<Items> mUploads;

    Items items;
//    SearchItems items;


    private ProgressBar mProgressCircle;

    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bookImage = findViewById(R.id.book_img_det);
        bookName = findViewById(R.id.book_name_det);
        authorName = findViewById(R.id.author_name_det);
        bookDes = findViewById(R.id.book_des_det);
        delBtn = findViewById(R.id.delBtn);
        ownerName =findViewById(R.id.owner_name);

        delBtn.setVisibility(INVISIBLE);


        mProgressCircle = findViewById(R.id.progress_circle_detail);
        mProgressCircle.setVisibility(INVISIBLE);

        items = (Items) getIntent().getSerializableExtra("detail");
//        final Object obj = getIntent().getSerializableExtra("detail");
//        if(obj instanceof Items){
//
//            items = (Items) obj;
//            //items = (Items) obj;
//        }

        //Items items = new  Items();
//
        Glide.with(getApplicationContext()).load(items.getImg_url()).into(bookImage);
        bookName.setText(items.getBook());
        authorName.setText(items.getAuthor());
        bookDes.setText(items.getDescription());
        ownerName.setText(items.getOwerName());

        mStorage = FirebaseStorage.getInstance();
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        final String userID = mAuth.getCurrentUser().getUid();
        final String authConfirm = items.getUserID();

        if (userID.equals(authConfirm)) {
            delBtn.setVisibility(VISIBLE);
            correctUser = true;
        }


        number = items.getPhoneNo();
        docID = items.getDocID();

        // String n = item;


        if (isPermissionGranted()) {
        }
    }

    public void callOperation(View view) {


        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));
        if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }

    public void sendWhatsApp(View view) {

        //String phoneNumberWithCountryCode = "+918762623837";

        if (!number.toString().contains("+91")) {
            number = "+91" + number;
        }


        String message = "Hey i would like to buy " + items.getBook();

        startActivity(
                new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                String.format("https://api.whatsapp.com/send?phone=%s&text=%s", number, message)
                        )
                )
        );


    }


    public void deleteAction(View view) {
        mProgressCircle.setVisibility(VISIBLE);
        final GlobalData path = (GlobalData) getApplication();
        final String collegePath = path.getCollegePath();
        final String combinationPath = path.getCombinationPath();

        //Log.i("sh", "getData: +" +f + " " +g);


        if (correctUser) {
            StorageReference photoRef = mStorage.getReferenceFromUrl(items.getImg_url());
            photoRef.delete().addOnSuccessListener( new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mFireStore.collection("College").document(collegePath).collection("Combination")
                            .document(combinationPath)
                            .collection("BookData")
                            .document(docID).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressCircle.setVisibility(INVISIBLE);
                                    Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressCircle.setVisibility(INVISIBLE);
                                    Toast.makeText(path, "failed" + e, Toast.LENGTH_SHORT).show();

                                }
                            });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressCircle.setVisibility(INVISIBLE);
                    Toast.makeText(path, "failed" + e, Toast.LENGTH_SHORT).show();

                }
            });

        } else {
            mProgressCircle.setVisibility(INVISIBLE);
            Toast.makeText(this, "Only owner can delete", Toast.LENGTH_SHORT).show();
        }


        String n = String.valueOf(System.currentTimeMillis());
        Log.i("yes", "deleteAction: " + n);


    }


    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(DetailActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void mainscreen(View view) {
        startActivity(new Intent(DetailActivity.this, MainActivity.class));
        finish();
    }
}