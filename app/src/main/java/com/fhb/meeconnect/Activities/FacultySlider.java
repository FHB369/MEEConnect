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

import com.fhb.meeconnect.Adapters.FacultySliderAdapter;
import com.fhb.meeconnect.DataElements.Faculty;
import com.fhb.meeconnect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FacultySlider extends AppCompatActivity {

    private ViewPager slider;
    private ImageView back;
    private ArrayList<Faculty> students;
    private int position;
    private int catagoryIndex;
    private String catagoryName;
    private DatabaseReference myRef;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_slider);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        context = this;

        slider = findViewById(R.id.faculty_slider);
        back = findViewById(R.id.faculty_slider_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        position = getIntent().getIntExtra("position", 0);
        catagoryName = getIntent().getStringExtra("catagoryName");
        catagoryIndex = getIntent().getIntExtra("catagoryIndex", 0);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(catagoryName).child("peoples");
        myRef.keepSynced(true);

        loadDataiInAdapter();
    }

    private void loadDataiInAdapter() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Faculty> tempFaculty = new ArrayList<>();
                for(DataSnapshot faculty : dataSnapshot.getChildren()){
                    String id = faculty.getKey();
                    String bloodGroup = (String) faculty.child("Blood").getValue();
                    String birthday = (String) faculty.child("DOB").getValue();
                    String designation = (String) faculty.child("Designation").getValue();
                    String email = (String) faculty.child("Email").getValue();
                    String messengerID = (String) faculty.child("MSG_Id").getValue();
                    String name = (String) faculty.child("Name").getValue();
                    String phone = (String) faculty.child("Phone").getValue();
                    String photoURL = (String) faculty.child("Photo").getValue();

                    Faculty member = new Faculty(id,
                            bloodGroup,
                            birthday,
                            designation,
                            email,
                            messengerID,
                            name,
                            phone,
                            photoURL);
                    tempFaculty.add(member);
                }
                FacultySliderAdapter adapter = new FacultySliderAdapter(context, tempFaculty);
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
