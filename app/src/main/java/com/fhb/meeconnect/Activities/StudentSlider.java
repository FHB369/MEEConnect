package com.fhb.meeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fhb.meeconnect.Adapters.StudentSliderAdapter;
import com.fhb.meeconnect.R;
import com.fhb.meeconnect.DataElements.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentSlider extends AppCompatActivity {

    private ViewPager slider;
    private ImageView back;
    private ArrayList<Student> students;
    private int position;
    private int catagoryIndex;
    private String catagoryName;
    private DatabaseReference myRef;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_slider);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        context = this;

        slider = findViewById(R.id.student_slider);
        back = findViewById(R.id.student_slider_back);

        position = getIntent().getIntExtra("position", 0);
        catagoryName = getIntent().getStringExtra("catagoryName");
        catagoryIndex = getIntent().getIntExtra("catagoryIndex", 0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(catagoryName).child("peoples");
        myRef.keepSynced(true);

        loadDataiInAdapter();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadDataiInAdapter() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Student> tempStudent = new ArrayList<>();
                for(DataSnapshot student : dataSnapshot.getChildren()){
                    String registrationNo = student.getKey();
                    String bloodGroup = (String) student.child("Blood").getValue();
                    String birthday = (String) student.child("DOB").getValue();
                    String nickname = (String) student.child("Nickname").getValue();
                    String messengerID = (String) student.child("MSG_Id").getValue();
                    String name = (String) student.child("Name").getValue();
                    String phone = (String) student.child("Phone").getValue();
                    String photoURL = (String) student.child("Photo").getValue();

                    Student member = new Student(registrationNo,
                            bloodGroup,
                            birthday,
                            messengerID,
                            name,
                            nickname,
                            phone,
                            photoURL);
                    tempStudent.add(member);
                }
                StudentSliderAdapter adapter = new StudentSliderAdapter(context, tempStudent);
                slider.setAdapter(adapter);
                slider.setCurrentItem(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
