package com.example.city_university_admin.faculty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.city_university_admin.MainActivity;
import com.example.city_university_admin.R;
import com.example.city_university_admin.notice.Upload_Notice;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddTeacher extends AppCompatActivity {
    private ImageView addTeacherImage;
    private EditText addteacherName,addTeacherEmail,addTeacherPost;
    private Spinner addTeacherCategory;
    private Button addTeacherButton;
    private final int REQ = 1;
    private Bitmap bitmap=null;
    private String category;
    private String name,email,post,downloadurl;
    private ProgressDialog pd;
    private DatabaseReference reference,dbref;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        addTeacherImage=findViewById(R.id.addTeacherImage);
        addteacherName=findViewById(R.id.addTeacherName);
        addTeacherEmail=findViewById(R.id.addTeacherEmail);
        addTeacherPost=findViewById(R.id.addTeacherPost);
        addTeacherCategory=findViewById(R.id.addTeacherCategory);
        addTeacherButton=findViewById(R.id.addTeacherbutton);
        pd = new ProgressDialog(this);
        firebaseDatabase=FirebaseDatabase.getInstance();



        reference= FirebaseDatabase.getInstance().getReference().child("Teacher");
        storageReference = FirebaseStorage.getInstance().getReference();




        String[] items=new String[]{"Select Category","Computer Science","EEE","BBA","TEXTILE"};
        addTeacherCategory.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));

        addTeacherCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=addTeacherCategory.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });

        addTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chechkValuedation();
            }
        });
    }

    private void chechkValuedation() {
        name=addteacherName.getText().toString();
        email=addTeacherEmail.getText().toString();
        post=addTeacherPost.getText().toString();
        if (name.isEmpty()){
            addteacherName.setError("Empty");
            addteacherName.requestFocus();
        } else if (email.isEmpty()){
            addTeacherEmail.setError("Empty");
            addTeacherEmail.requestFocus();
        }else if (post.isEmpty()){
            addTeacherPost.setError("Empty");
            addTeacherPost.requestFocus();
        }else if (category.equals("Select Category")){
            Toast.makeText(this, "Please provide teacher Category", Toast.LENGTH_SHORT).show();
        }else if (bitmap==null){
            pd.setMessage("Uploading.....");
            pd.show();
            insertData();
            Intent intent=new Intent(AddTeacher.this, UpdateFaculty.class);
            startActivity(intent);

        }else{
            pd.setMessage("Uploading.....");
            pd.show();
            uploadImages();
            Intent intent=new Intent(AddTeacher.this, UpdateFaculty.class);
            startActivity(intent);
        }
    }

    private void uploadImages() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child("Teacher").child(finalimg+"jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddTeacher.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    Toast.makeText(AddTeacher.this, "Successfull", Toast.LENGTH_SHORT).show();
                                    downloadurl = String.valueOf(uri);
                                    insertData();

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(AddTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void insertData() {
        dbref = reference.child(category);
        final String uniquekey = dbref.push().getKey();

        TeacherClass teacherData = new TeacherClass(name, email, post, downloadurl, uniquekey);
        dbref.child(uniquekey).setValue(teacherData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Teacher added Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddTeacher.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

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
            addTeacherImage.setImageBitmap(bitmap);
        }
    }
}