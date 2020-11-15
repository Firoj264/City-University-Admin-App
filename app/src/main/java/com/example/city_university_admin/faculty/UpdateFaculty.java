package com.example.city_university_admin.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.city_university_admin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView cse_department,eee_department,bba_department,tex_department;
    private LinearLayout csNoData,eeNoData,texNoaDta,bbaNoData;
    private List<TeacherClass> list1,list2,list3,list4;
    private DatabaseReference reference, dbref;
    private TeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        fab=findViewById(R.id.fab);
        cse_department=this.findViewById(R.id.cse_department);
        eee_department=this.findViewById(R.id.eee_department);
        bba_department=this.findViewById(R.id.bba_department);
        tex_department=this.findViewById(R.id.tex_department);

        csNoData=findViewById(R.id.csNoData);
        eeNoData=findViewById(R.id.eeNoData);
        texNoaDta=findViewById(R.id.texNoData);
        bbaNoData=findViewById(R.id.bbaNoData);

        reference= FirebaseDatabase.getInstance().getReference().child("Teacher");

        csDepartment();
        eeDepartment();
        bbaDepartment();
        texDepartment();



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateFaculty.this,AddTeacher.class));

            }
        });
    }


    private void csDepartment() {
        dbref=reference.child("Computer Science");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list1=new ArrayList<>();

                if (!snapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    cse_department.setVisibility(View.GONE);
                }else {
                    csNoData.setVisibility(View.GONE);
                    cse_department.setVisibility(View.VISIBLE);
                    for (DataSnapshot datasnapshot:snapshot.getChildren()){
                        TeacherClass data=datasnapshot.getValue(TeacherClass.class);
                        list1.add(data);
                    }
                    cse_department.setHasFixedSize(true);
                    cse_department.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list1,UpdateFaculty.this, "Computer Science");
                    cse_department.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void eeDepartment() {
        dbref=reference.child("EEE");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2=new ArrayList<>();

                if (!snapshot.exists()){
                    eeNoData.setVisibility(View.VISIBLE);
                    eee_department.setVisibility(View.GONE);
                }else {
                    eeNoData.setVisibility(View.GONE);
                    eee_department.setVisibility(View.VISIBLE);
                    for (DataSnapshot datasnapshot:snapshot.getChildren()){
                        TeacherClass data=datasnapshot.getValue(TeacherClass.class);
                        list2.add(data);
                    }
                    eee_department.setHasFixedSize(true);
                    eee_department.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list2,UpdateFaculty.this,"EEE");
                    eee_department.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void bbaDepartment() {
        dbref=reference.child("BBA");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list3=new ArrayList<>();

                if (!snapshot.exists()){
                    bbaNoData.setVisibility(View.VISIBLE);
                    bba_department.setVisibility(View.GONE);
                }else {
                    bbaNoData.setVisibility(View.GONE);
                    bba_department.setVisibility(View.VISIBLE);
                    for (DataSnapshot datasnapshot:snapshot.getChildren()){
                        TeacherClass data=datasnapshot.getValue(TeacherClass.class);
                        list3.add(data);
                    }
                    bba_department.setHasFixedSize(true);
                    bba_department.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list3,UpdateFaculty.this,"BBA");
                    bba_department.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void texDepartment() {
        dbref=reference.child("TEXTILE");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list4=new ArrayList<>();

                if (!snapshot.exists()){
                    texNoaDta.setVisibility(View.VISIBLE);
                    tex_department.setVisibility(View.GONE);
                }else {
                    texNoaDta.setVisibility(View.GONE);
                    tex_department.setVisibility(View.VISIBLE);
                    for (DataSnapshot datasnapshot:snapshot.getChildren()){
                        TeacherClass data=datasnapshot.getValue(TeacherClass.class);
                        list4.add(data);
                    }
                    tex_department.setHasFixedSize(true);
                    tex_department.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter=new TeacherAdapter(list4,UpdateFaculty.this,"TEXTILE");
                    tex_department.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateFaculty.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



}