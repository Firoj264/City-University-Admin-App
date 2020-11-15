package com.example.city_university_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class UploadImage extends AppCompatActivity {
    private Spinner imagecategoty;
    private CardView selectImage;
    private Button uploadimagebtn;
    private ImageView galleryimage;
    private String category;
    private final int REQ = 1;
    private Bitmap bitmap;
    private ProgressDialog pd;
    private DatabaseReference databaseReference,dbRef;
    private StorageReference storageReference;
    String downloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("gallery");
        storageReference = FirebaseStorage.getInstance().getReference().child("gallery");

        selectImage=findViewById(R.id.addgalleryimage);
        imagecategoty=findViewById(R.id.image_category);
        uploadimagebtn=findViewById(R.id.upload_gallery_button);
        galleryimage=findViewById(R.id.gallery_image_view);
        pd=new ProgressDialog(this);

        String[] items=new String[]{"Select Category","Nobin boron","Others Event"};
        imagecategoty.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items));
        imagecategoty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category=imagecategoty.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
            }
        });
        uploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap==null){
                    Toast.makeText(UploadImage.this, "Please upload Image", Toast.LENGTH_SHORT);
                }else if (category.equals("Select Category")){
                    Toast.makeText(UploadImage.this, "Please Select Image category", Toast.LENGTH_SHORT);

                }else {
                    pd.setMessage("Uploading.....");
                    pd.show();
                    uploadImages();
                }
            }
        });
    }

    private void uploadImages() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = storageReference.child(finalimg + "jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UploadImage.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    Toast.makeText(UploadImage.this, "Successfull", Toast.LENGTH_SHORT);
                                    downloadurl = String.valueOf(uri);
                                    uploadData();

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(UploadImage.this, "Something Went Wrong", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void uploadData() {
        dbRef=databaseReference.child(category);
        final String uniquekey = dbRef.push().getKey();
        databaseReference.child(uniquekey).setValue(downloadurl).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(UploadImage.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadImage.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

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
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            galleryimage.setImageBitmap(bitmap);
        }
    }
}