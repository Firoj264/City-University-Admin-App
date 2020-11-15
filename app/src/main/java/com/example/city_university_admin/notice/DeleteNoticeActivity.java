package com.example.city_university_admin.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.city_university_admin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeleteNoticeActivity extends AppCompatActivity {
    private ProgressBar progressbar;
    private RecyclerView deleteNoticerecyclerView;
    private NoticeAdapter adapter;
    private ArrayList<Notice_Data> list;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice);

        deleteNoticerecyclerView=(RecyclerView) this.findViewById(R.id.deleteNoticerecyclerView);
        progressbar=findViewById(R.id.progressbar);

        reference= FirebaseDatabase.getInstance().getReference().child("Notice");

        deleteNoticerecyclerView.setLayoutManager(new LinearLayoutManager(this));
        deleteNoticerecyclerView.setHasFixedSize(true);


        
        getNotice();

    }

    private void getNotice() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list=new ArrayList<>();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {

                    Notice_Data data = snapshot1.getValue(Notice_Data.class);
                    list.add(data);
                }
                adapter=new NoticeAdapter(DeleteNoticeActivity.this,list);
                adapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);


                deleteNoticerecyclerView.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressbar.setVisibility(View.GONE);

                Toast.makeText(DeleteNoticeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}