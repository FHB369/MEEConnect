package com.fhb.meeconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Birthday extends AppCompatActivity {

    private ImageView back;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private DatabaseReference myRef;
    private ArrayList<Student> allPeoples;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        back = findViewById(R.id.birthday_back);
        recyclerView = findViewById(R.id.birthday_recycler);
        refreshLayout = findViewById(R.id.birthday_refresh);

        allPeoples = new ArrayList<>();
        context = this;

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.keepSynced(true);

        initPeoples();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPeoples();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("d MMMM");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    private void initPeoples() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refreshLayout.setRefreshing(true);
                allPeoples =  new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals("Teacher") || snapshot.getKey().equals("Staff")){
                        for(DataSnapshot faculty : snapshot.child("peoples").getChildren()){
                            String registrationNo = (String) faculty.child("Email").getValue();
                            String bloodGroup = (String) faculty.child("Blood").getValue();
                            String birthday = (String) faculty.child("DOB").getValue();
                            String nickname = (String) faculty.child("Name").getValue();
                            String messengerID = (String) faculty.child("MSG_Id").getValue();
                            String name = (String) faculty.child("Designation").getValue();
                            String phone = (String) faculty.child("Phone").getValue();
                            String photoURL = (String) faculty.child("Photo").getValue();

                            if(birthday.equals(getCurrentDate())) {
                                Student member = new Student(registrationNo,
                                        bloodGroup,
                                        birthday,
                                        messengerID,
                                        name,
                                        nickname,
                                        phone,
                                        photoURL);
                                allPeoples.add(member);
                            }
                        }
                    }else{
                        for(DataSnapshot student : snapshot.child("peoples").getChildren()){
                            String registrationNo = student.getKey();
                            String bloodGroup = (String) student.child("Blood").getValue();
                            String birthday = (String) student.child("DOB").getValue();
                            String nickname = (String) student.child("Nickname").getValue();
                            String messengerID = (String) student.child("MSG_Id").getValue();
                            String name = (String) student.child("Name").getValue();
                            String phone = (String) student.child("Phone").getValue();
                            String photoURL = (String) student.child("Photo").getValue();

                            if(birthday.equals(getCurrentDate())) {
                                Student member = new Student(registrationNo,
                                        bloodGroup,
                                        birthday,
                                        messengerID,
                                        name,
                                        nickname,
                                        phone,
                                        photoURL);
                                allPeoples.add(member);
                            }
                        }
                    }
                }
                BirthdayRecyclerAdapter adapter = new BirthdayRecyclerAdapter(allPeoples, context);
                recyclerView.setAdapter(adapter);
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Collections.reverse(allPeoples);
    }
}
