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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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

    private Button mButtonChooseImage;
    private Button mButtonUpload;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;
    private ProgressBar mProgressBar;
    private ImageView mImageView;

    private Uri mImageUri;

    private StorageReference mStorageRef;


    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;


    String collegePath, combinationPath;

    TextView viewQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);



        mButtonChooseImage = findViewById(R.id.button_choose_image);
        mButtonUpload = findViewById(R.id.button_upload);

        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);



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
                        //viewQ.setText(collegePath + "  " + combinationPath);


                        // call later after getting the image


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
                uploadData();
               // uploadFile();

            }
        });




    }



    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            mImageUri = data.getData();

           //Picasso.with(this).load(mImageUri).into(mImageView);
             mImageView.setImageURI(mImageUri);
            //displaying the image (before uploading )
        }
    }

    private  String getFileExtension(Uri uri){
        //when a image file is passed it returns image extension
        ContentResolver cR = getContentResolver();
        MimeTypeMap mine = MimeTypeMap.getSingleton();
        return  mine.getExtensionFromMimeType(cR.getType(uri));

    }

   public void uploadData(){
        final String img_url = "null";

       if (mImageUri != null){

           // Here we are naming the file with system time (to make sure that it wont over ride with same name)
           final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

           // we can change the loction here too
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
                           },2000);



                           Toast.makeText(SellActivity.this,"upload successful ",Toast.LENGTH_LONG).show();
                           Log.d("test","before passing"+mEditTextFileName.getText().toString());
                           fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {



                                   //mMap.put("title",);


                                   String url = uri.toString();
                                   String title = mEditTextFileName.getText().toString();

                                   sendTextFile(url,title);




                                   Log.i("test", "onSuccess: "+ url);
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
                           Toast.makeText(SellActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                       }
                   })
                   .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           double progress = (100.0 *taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());

                           mProgressBar.setProgress((int)progress);

                       }
                   });

       }else{
           Toast.makeText(this,"no file seletecd",Toast.LENGTH_SHORT).show();
       }




    }

    public void sendTextFile (String imgUrl,String title ){


        Map<String,String> mMap = new HashMap<>();
        mMap.put("img_url",imgUrl);
        mMap.put("title",title);



        mStore.collection("College").document(collegePath).collection("Combination")
            .document(combinationPath).collection("BookData").add(mMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
           @Override
           public void onComplete(@NonNull Task<DocumentReference> task) {

           }
       });

    }
}