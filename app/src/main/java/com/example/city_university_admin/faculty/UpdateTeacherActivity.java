package com.example.city_university_admin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.city_university_admin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateTeacherActivity extends AppCompatActivity {
    private EditText updateTeacherName,updateTeacherEmail,updateTeacherPost;
    private Button updateButton,deleteButton;
    private ImageView updateTeacherImage;

    private String name1,email1,post1,image1, downloadurl= "";
    private final int REQ = 1;
    private Bitmap bitmap;

    private DatabaseReference reference;
    private StorageReference storageReference;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_teacher);

        name1=getIntent().getStringExtra("name");
        email1=getIntent().getStringExtra("email");
        post1=getIntent().getStringExtra("post");
        image1=getIntent().getStringExtra("image");



        updateTeacherEmail=findViewById(R.id.updateTeacherEmail);
        updateTeacherName=findViewById(R.id.updateTeacherName);
        updateTeacherPost=findViewById(R.id.updateTeacherPost);
        updateButton=findViewById(R.id.updateTeacherButton);
        deleteButton=findViewById(R.id.deleteTeacherButton);
        updateTeacherImage=findViewById(R.id.updateTeacherImage);


        reference= FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            Picasso.get().load(image1).into(updateTeacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }


        updateTeacherName.setText(name1);
        updateTeacherEmail.setText(email1);
        updateTeacherPost.setText(post1);


        updateTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name1=updateTeacherName.getText().toString();
                email1=updateTeacherEmail.getText().toString();
                post1=updateTeacherPost.getText().toString();
                checkValidation();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });


    }

    private void deleteData() {
        String uniquekey=getIntent().getStringExtra("key");
        String category=getIntent().getStringExtra("category");
        reference.child(category).child(uniquekey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateTeacherActivity.this, "Teacher deleted Succesfully", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();


            }
        });

    }
    private void checkValidation() {
        if (name1.isEmpty()){
            updateTeacherName.setError("Empty");
            updateTeacherName.requestFocus();
        } else if (post1.isEmpty()){
            updateTeacherPost.setError("Empty");
            updateTeacherPost.requestFocus();
        }else if (email1.isEmpty()){
            updateTeacherEmail.setError("Empty");
            updateTeacherEmail.requestFocus();
        }else if (bitmap == null){
            updateData(image1);

        }else{
            uploadImage();

        }

    }

    private void updateData(String s) {
        HashMap hp = new HashMap();
        hp.put("name",name1);
        hp.put("email",email1);
        hp.put("post",post1);
        hp.put("image",s);
       String uniquekey=getIntent().getStringExtra("key");
       String category=getIntent().getStringExtra("category");

        reference.child(category).child(uniquekey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateTeacherActivity.this, "Teacher Updated Succesfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(UpdateTeacherActivity.this,UpdateFaculty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void uploadImage() {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] finalimg = baos.toByteArray();
            final StorageReference filepath;
            filepath = storageReference.child("Teacher").child(finalimg + "jpg");
            final UploadTask uploadTask = filepath.putBytes(finalimg);
            uploadTask.addOnCompleteListener(UpdateTeacherActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        pd.dismiss();
                                        Toast.makeText(UpdateTeacherActivity.this, "Successfull", Toast.LENGTH_SHORT);
                                        downloadurl = String.valueOf(uri);
                                        updateData(downloadurl);

                                    }
                                });
                            }
                        });
                    } else {
                        pd.dismiss();
                        Toast.makeText(UpdateTeacherActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT);
                    }
                }
            });

    }

    private void opengallery() {
        Intent pic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pic, REQ);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateTeacherImage.setImageBitmap(bitmap);
        }
    }
}