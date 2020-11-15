package com.example.city_university_admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

import com.example.city_university_admin.faculty.UpdateFaculty;
import com.example.city_university_admin.notice.DeleteNoticeActivity;
import com.example.city_university_admin.notice.Upload_Notice;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   private CardView Uploadnotice,addGallery,addEbook,addFaculty111,addDelete,addResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      Uploadnotice=findViewById(R.id.addNotice);
      addGallery=findViewById(R.id.addGallery);
      addEbook=findViewById(R.id.addEbook);
      addFaculty111=findViewById(R.id.addFaculty111);
        addDelete=findViewById(R.id.addDelete);
        addResult=findViewById(R.id.addResult);

       Uploadnotice.setOnClickListener(this);
       addGallery.setOnClickListener(this);
       addEbook.setOnClickListener(this);
        addFaculty111.setOnClickListener(this);
        addDelete.setOnClickListener(this);
        addResult.setOnClickListener(this);
    }

    @Override
   public void onClick(View v) {
        Intent intent;
        switch (v.getId()){

                case R.id.addNotice:
                 intent=new Intent(MainActivity.this, Upload_Notice.class);
                startActivity(intent);
                break;
                case R.id.addGallery:
                intent=new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent);
                break;
                case R.id.addEbook:
                intent=new Intent(MainActivity.this,UploadPdfActivity.class);
                startActivity(intent);
                break;
                case R.id.addFaculty111:
                intent=new Intent(MainActivity.this,UpdateFaculty.class);
                startActivity(intent);
                break;
                case R.id.addDelete:
                intent=new Intent(MainActivity.this, DeleteNoticeActivity.class);
                startActivity(intent);
                break;
                case R.id.addResult:
                intent=new Intent(MainActivity.this, UploadResultActivity.class);
                startActivity(intent);
                break;
}
    }
}