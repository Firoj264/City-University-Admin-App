package com.example.city_university_admin.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.city_university_admin.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter> {

    private List<TeacherClass> list;
    private Context context;
    private String category;

    public TeacherAdapter(List<TeacherClass> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.faculty_item_layout,parent,false);
        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewAdapter holder, final int position) {
        final TeacherClass item=list.get(position);

        holder.teacherName.setText(item.getName());
        holder.teacherEmail.setText(item.getEmail());
        holder.teacherPost.setText(item.getPost());


        try {
            Picasso.get().load(item.getImage()).into(holder.teacherImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UpdateTeacherActivity.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("email",item.getEmail());
                intent.putExtra("post",item.getPost());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
               intent.putExtra("category",category);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
    int a ;

        if( list!= null && !list.isEmpty()) {

            a = list.size();
        }
        else {

            a = 0;

        }

        return a;
    }

    public class TeacherViewAdapter extends RecyclerView.ViewHolder {
        private TextView teacherName, teacherPost, teacherEmail;
        private ImageView teacherImage;
        private Button update;

        public TeacherViewAdapter(@NonNull View itemView) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacherName);
            teacherPost = itemView.findViewById(R.id.teacherPost);
            teacherEmail = itemView.findViewById(R.id.teacherEmail);
            teacherImage = itemView.findViewById(R.id.teacherImage);

           update=itemView.findViewById(R.id.teacherUpdate_btn);


        }
    }
}
