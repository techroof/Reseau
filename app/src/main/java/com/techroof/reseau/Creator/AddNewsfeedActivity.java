package com.techroof.reseau.Creator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.techroof.reseau.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddNewsfeedActivity extends AppCompatActivity {

    private ImageView selectedImg, addImgBtn,backBtn;
    private EditText newsfeedEt;
    private Button postBtn;
    private TextView toolbarTitle;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private ProgressDialog pd;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private String newsfeedImgUrl=null,creatorName,creatorImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_newsfeed);

        backBtn=findViewById(R.id.back_btn);
        addImgBtn=findViewById(R.id.add_newsfeed_img);
        selectedImg=findViewById(R.id.newsfeed_selected_img);
        newsfeedEt=findViewById(R.id.newsfeed_et);
        postBtn=findViewById(R.id.post_newsfeed_btn);
        toolbarTitle=findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Add Newsfeed");

        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("newsfeedImages");

        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newsfeedText=newsfeedEt.getText().toString();

                if (TextUtils.isEmpty(newsfeedText)){
                    newsfeedEt.setError("Please write something");

                }else{
                    addNewsFeed(newsfeedText);
                }

            }
        });

        firestore.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            creatorName=task.getResult().get("name").toString();
                            creatorImg=task.getResult().get("image").toString();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(AddNewsfeedActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void addNewsFeed(String newsfeedText) {

        String newsfeedId=firestore.collection("newsfeeds").document().getId();

        List<String> supportsArrayList=new ArrayList<>();

        Map<String,Object> newsfeedMap=new HashMap<>();
        newsfeedMap.put("newsfeedId",newsfeedId);
        newsfeedMap.put("creatorId",mAuth.getCurrentUser().getUid());
        newsfeedMap.put("supports",supportsArrayList);
        newsfeedMap.put("timestamp", System.currentTimeMillis());
        newsfeedMap.put("newsfeedText",newsfeedText);
        newsfeedMap.put("newsfeedImg",newsfeedImgUrl);
        newsfeedMap.put("creatorImg",creatorImg);
        newsfeedMap.put("creatorName",creatorName);

        firestore.collection("newsfeeds")
                .document(newsfeedId)
                .set(newsfeedMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Intent newsfeed=new Intent(AddNewsfeedActivity.this,CreatorHomeActivity.class);
                            startActivity(newsfeed);

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNewsfeedActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            selectedImg.setImageURI(mImageUri);
            selectedImg.setVisibility(View.VISIBLE);
            uploadFile();

        }
    }

    private void uploadFile() {

        if (mImageUri != null) {

            final StorageReference fileReference = mStorageRef.child(mImageUri.getLastPathSegment()
                    + "_" + System.currentTimeMillis());

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                            if (task.isSuccessful()) {

                                Task<Uri> uriTask =
                                        mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                            @Override
                                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                if (task.isSuccessful()) {
                                                    pd.dismiss();
                                                    return fileReference.getDownloadUrl();

                                                } else {
                                                    pd.dismiss();
                                                    throw task.getException();
                                                }
                                            }
                                        }).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                pd.dismiss();
                                                newsfeedImgUrl = task.getResult().toString();
                                            }
                                        });

                            } else {
                                Toast.makeText(AddNewsfeedActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(AddNewsfeedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pd.setMessage((int) progress + "% Uploaded");
                            pd.show();

                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}