package com.example.city_university_admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;

public class UploadPdfActivity extends AppCompatActivity {
    private CardView addpdf;
    private final int REQ = 1;
    private Bitmap bitmap;
    private TextView pdftitle;
    private Button pdf_button;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private Uri addPdfUri;
    private ProgressDialog pd;
    private TextView pdftextview;
    private String pdfName,title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(this);
        addpdf = findViewById(R.id.addpdf);
        pdftitle= findViewById(R.id.pdf_title);
       pdf_button = findViewById(R.id.upload_Ebook_button);
        addpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        pdftextview=findViewById(R.id.pdfTextView);
        pdf_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=pdftitle.getText().toString();
                if(title.isEmpty()){
                    pdftitle.setError("Empty");
                    pdftitle.requestFocus();
                }else if (addPdfUri==null){
                    Toast.makeText(UploadPdfActivity.this, "Please Upload Pdf File", Toast.LENGTH_SHORT).show();

                }else {
                    uploadpdf();
                }
            }
        });


    }

    private void uploadpdf() {
        pd.setTitle("Please Wait.....");
        pd.setMessage("Uploading.....");
        pd.show();
        StorageReference reference=mStorageRef.child("pdf/"+pdfName+""+System.currentTimeMillis()+".pdf");
        reference.putFile(addPdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isCanceled());
                Uri uri=uriTask.getResult();
                uploadData(String.valueOf(uri));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadData(String downloadurl) {
        String uniquekey=databaseReference.child("pdf").push().getKey();
        HashMap data=new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",downloadurl);
        databaseReference.child("pdf").child("uniqekey").setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Pdf Uploaded Successfully", Toast.LENGTH_SHORT).show();
                pdftitle.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(UploadPdfActivity.this, "Failed to upload Pdf", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("pdf/docs/ppt");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Pdf File"),REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            addPdfUri =data.getData();
            if (addPdfUri.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor=UploadPdfActivity.this.getContentResolver().query(addPdfUri,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if(addPdfUri.toString().startsWith("file://")){
                pdfName=new File(addPdfUri.toString()).getName();

            }
            pdftextview.setText(pdfName);

        }
    }
}