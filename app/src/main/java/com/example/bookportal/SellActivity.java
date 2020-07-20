package com.example.bookportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class SellActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageButton mButtonChooseImage;
    private Button mButtonUpload;
    //private TextView mBookName;
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


    String collegePath, combinationPath, phone;


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
        //userRef = mStore.collection("User");


        mStore.collection("User").document(mAuth.getCurrentUser().getUid())
                .collection("Path").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        collegePath = document.getString("college");
                        combinationPath = document.getString("combination");
                        phone = document.getString("phone");
                        //viewQ.setText(collegePath + "  " + combinationPath);


                    }
                }
            }
        });


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
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

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            offView.setVisibility(View.INVISIBLE);
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
            //displaying the image (before uploading )
        }
    }

    private String getFileExtension(Uri uri) {
        //when a image file is passed it returns image extension
        ContentResolver cR = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return mine.getExtensionFromMimeType(cR.getType(uri));
    }

    public void uploadData() {

        if (mImageUri != null) {
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


                            Toast.makeText(SellActivity.this, "upload successful ", Toast.LENGTH_LONG).show();
                            Log.d("test", "before passing" + mBookName.getText().toString());
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //mMap.put("title",);


                                    String url = uri.toString();
                                    String bookName = mBookName.getText().toString();
                                    String description = mDescription.getText().toString();
                                    String authorName = mAuthorName.getText().toString();
                                    sendTextFile(url, bookName, authorName, description);


                                    Log.i("test", "onSuccess: " + url);
//                                   Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),url);
//                                   String uploadID = mDatabaseRef.push().getKey();
//                                   mDatabaseRef.child(uploadID).setValue(upload);
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
            btnClicked = true;
            Toast.makeText(this, "no file seletecd", Toast.LENGTH_SHORT).show();
        }


    }

    public void sendTextFile(String imgUrl, String bookName, String authorName, String description) {


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
        Log.i("np", "sendTextFile: " + docID);


        mStore.collection("College").document(collegePath).collection("Combination")
                .document(combinationPath).collection("BookData").document(docID).set(mMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mProgressCircle.setVisibility(View.INVISIBLE);
                btnClicked = false;
                Toast.makeText(SellActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnClicked = false;

                Toast.makeText(SellActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);

            }
        });

//        mStore.collection("College").document(collegePath).collection("Combination")
//            .document(combinationPath).collection("BookData").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//           @Override
//           public void onComplete(@NonNull Task<DocumentReference> task) {
//               mProgressCircle.setVisibility(View.INVISIBLE);
//
//           }
//       });

    }
}