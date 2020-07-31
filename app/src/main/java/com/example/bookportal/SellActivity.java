package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SellActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton mButtonChooseImage;
    private Button mButtonUpload;
    private EditText mBookName;
    private EditText mAuthorName;
    private EditText mDescription;
    private ProgressBar mProgressBar;
    private ProgressBar mProgressCircle;
    private ImageView mImageView;
    private TextView offView;
    private Uri mImageUri;

    private StorageReference mStorageRef;
    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;
    Boolean btnClicked = false;
     String bookName ,description ,authorName;
    String collegePath, combinationPath, phone , owerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progressBar);
        mProgressCircle = findViewById(R.id.progress_circle);
        mBookName = findViewById(R.id.book_name);
        mAuthorName = findViewById(R.id.author_name);
        mDescription = findViewById(R.id.description);
        offView = findViewById(R.id.textOff);

        mProgressCircle.setVisibility(View.INVISIBLE);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        final GlobalData globalData = (GlobalData) getApplication();
        collegePath = globalData.getCollegePath();
        combinationPath = globalData.getCombinationPath();
        owerName = globalData.getName();
        phone = globalData.getPhone();




        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btnClicked){
                    btnClicked = true;
                    uploadData();
                    // uploadFile();
                }else{
                    Toast.makeText(SellActivity.this, "Please Wait...its uploading", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SellActivity.this, MainActivity.class));
        finish();
    }


    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(SellActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                         Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        ActivityCompat.requestPermissions(SellActivity.this, new String[]{Manifest.permission.CAMERA}, 12);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null ){
                            startActivityForResult(takePictureIntent, 10);

                        }


                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("yes", "onActivityResult: "+requestCode);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null) {
            Log.i("yes", "onActivityResult: fgff"+requestCode);

            offView.setVisibility(View.INVISIBLE);
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
            //displaying the image (before uploading )
        }


        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
             Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageUri = getImageUri(this, imageBitmap);
            offView.setVisibility(View.INVISIBLE);
            mImageView.setImageURI(mImageUri);
        }


    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private String getFileExtension(Uri uri) {
        //when a image file is passed it returns image extension
        ContentResolver cR = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadData() {

         bookName  = mBookName.getText().toString();
         description = mDescription.getText().toString();
         authorName = mAuthorName.getText().toString();

        if (mImageUri != null && !TextUtils.isEmpty(bookName) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(authorName) ) {
            mProgressCircle.setVisibility(View.VISIBLE);

            // Here we are naming the file with system time (to make sure that it wont over ride with same name)
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            // we can change the location here too
            //StorageReference fileReference = mStorageRef.child("uploads/"+System.currentTimeMillis()+"."+getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // this long shit is to get delay in the code
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 2000);

                            Log.d("test", "before passing" + mBookName.getText().toString());
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //mMap.put("title",);


                                    String url = uri.toString();
                                    sendTextFile(url);


                                    Log.i("test", "onSuccess: " + url);

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SellActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            mProgressCircle.setVisibility(View.INVISIBLE);

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d("test", "before passing" + taskSnapshot.getBytesTransferred());
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                            mProgressBar.setProgress((int) progress);

                        }
                    });

        } else {
            btnClicked = false;
            Toast.makeText(this, "Please fill all fields ", Toast.LENGTH_SHORT).show();
        }


    }

    public void sendTextFile(String imgUrl) {


        //Random no for the doc ID
        String docID = String.valueOf(System.currentTimeMillis());

        String userId = mAuth.getCurrentUser().getUid();
        Map<String, String> mMap = new HashMap<>();
        mMap.put("img_url", imgUrl);
        mMap.put("book", bookName);
        mMap.put("author", authorName);
        mMap.put("description", description);
        mMap.put("userID", userId);
        mMap.put("phoneNo", phone);
        mMap.put("docID", docID);
        mMap.put("owerName", owerName);
        Log.i("np", "sendTextFile: " + docID);


        mStore.collection("College")
                .document(collegePath)
                .collection("Combination")
                .document(combinationPath)
                .collection("BookData")
                .document(docID)
                .set(mMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressCircle.setVisibility(View.INVISIBLE);
                btnClicked = false;
                Toast.makeText(SellActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SellActivity.this, MainActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnClicked = false;

                Toast.makeText(SellActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
                startActivity(new Intent(SellActivity.this, MainActivity.class));
                finish();

            }
        });



    }

    public void mainscreen(View view) {
        startActivity(new Intent(SellActivity.this, MainActivity.class));
        finish();
    }
}