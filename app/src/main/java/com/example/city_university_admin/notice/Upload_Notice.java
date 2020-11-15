package com.example.city_university_admin.notice;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Upload_Notice extends AppCompatActivity {
   private CardView addimage;
    private final int REQ = 1;
    private Bitmap bitmap;
    private ImageView noticeImageView;
    private TextView noticetitle1;
    private Button button1;
    private DatabaseReference databaseReference,dbRef;
    private StorageReference mStorageRef;
    String downloadurl = "";
    private ProgressDialog pd;

    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__notice);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);
        addimage = findViewById(R.id.addNoticeimage);
        noticeImageView = findViewById(R.id.notice_image_view);
        noticetitle1 = findViewById(R.id.notice_title);
        button1 = findViewById(R.id.upload_notice_button);
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticetitle1.getText().toString().isEmpty()) {
                    noticetitle1.setError("Empty Title");
                    noticetitle1.requestFocus();
                } else if (bitmap == null) {
                    uploadData();
                } else {
                    uploadImages();
                }

            }


        });
    }

    private void uploadImages() {
        pd.setMessage("Uploading.....");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filepath;
        filepath = mStorageRef.child("Notice").child(finalimg + "jpg");
        final UploadTask uploadTask = filepath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(Upload_Notice.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                                    Toast.makeText(Upload_Notice.this, "Upload Successfully", Toast.LENGTH_SHORT);
                                    downloadurl = String.valueOf(uri);
                                    uploadData();


                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(Upload_Notice.this, "Something Went Wrong", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void uploadData() {
        dbRef = databaseReference.child("Notice");
        final String uniquekey = dbRef.push().getKey();
        String title = noticetitle1.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        String date = simpleDateFormat.format(calendar.getTime());

        Calendar calfortime = Calendar.getInstance();
        SimpleDateFormat currenttime = new SimpleDateFormat("hh:mm a");
        String time = currenttime.format(calfortime.getTime());

        Notice_Data noticeData = new Notice_Data(title, downloadurl, date, time, uniquekey);
        dbRef.child(uniquekey).setValue(noticeData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(Upload_Notice.this, "Notice Uploaded Successfully", Toast.LENGTH_SHORT).show();
                downloadurl=null;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(Upload_Notice.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void openGallery() {
        Intent pic = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pic, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            noticeImageView.setImageBitmap(bitmap);
        }
    }
}
