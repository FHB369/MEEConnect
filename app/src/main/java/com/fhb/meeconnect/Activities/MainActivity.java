package com.fhb.meeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.fhb.meeconnect.Adapters.HomeRecyclerAdapter;
import com.fhb.meeconnect.DataElements.Catagory;
import com.fhb.meeconnect.DataElements.Faculty;
import com.fhb.meeconnect.R;
import com.fhb.meeconnect.DataElements.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    public static ArrayList<Catagory> catagories;
    private DatabaseReference myRef;
    private Context context;
    private ImageView settings, search;
    private Toolbar toolbar;
    private CardView card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = findViewById(R.id.home_settings);
        search = findViewById(R.id.home_search);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission( Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1000);
            }
        }else{
            card = findViewById(R.id.home_card);
            toolbar = findViewById(R.id.toolbar);

            card.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);

            settings = findViewById(R.id.toolbar_settings);
            search = findViewById(R.id.toolbar_search);
        }

        context=this;



        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right_exit, R.anim.left_to_right);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_to_left, R.anim.right_to_left_exit);
            }
        });

        recyclerView = findViewById(R.id.home_recycler);
        refreshLayout = findViewById(R.id.home_refresh);
        refreshLayout.setRefreshing(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        catagories = new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.keepSynced(true);

        getDataAndSetAdapter();

        refreshLayout.setProgressViewOffset(false,
                150, 220);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                catagories.clear();
                getDataAndSetAdapter();
            }
        });
    }

    private void getDataAndSetAdapter() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot catagory : dataSnapshot.getChildren()){
                    String catagoryName = catagory.getKey();
                    String coverImageURL = (String) catagory.child("coverImage").getValue();
                    ArrayList<Student> students = null;
                    ArrayList<Faculty> faculties = null;

                    if(Objects.equals(catagory.getKey(), "Teacher") ||
                            Objects.equals(catagory.getKey(), "Staff")){
                        faculties = new ArrayList<>();

                        for (DataSnapshot faculty : catagory.child("peoples").getChildren()) {
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
                            faculties.add(member);
                        }
                    }else {
                        students = new ArrayList<>();

                        for (DataSnapshot student : catagory.child("peoples").getChildren()) {
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
                            students.add(member);
                        }
                    }

                    Catagory userCatagory = new Catagory(catagoryName, coverImageURL, faculties, students);
                    catagories.add(userCatagory);
                }

                Log.d("BAPPY", String.valueOf(catagories.size()));

                Collections.reverse(catagories);

                HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(context, catagories);
                recyclerView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),
                        "Can't load data. Please check your internet connection.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
