package com.fhb.meeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.fhb.meeconnect.Adapters.SearchRecyclerAdapter;
import com.fhb.meeconnect.R;
import com.fhb.meeconnect.DataElements.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Search extends AppCompatActivity {

    private EditText searchBox;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DatabaseReference myRef;
    private ArrayList<Student> allPeoples;
    private Context context;
    private Toolbar toolbar;
    private CardView card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        searchBox = findViewById(R.id.search_bar_edittext);
        recyclerView = findViewById(R.id.search_recycler);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            card = findViewById(R.id.search_card);
            toolbar = findViewById(R.id.search_toolbar);

            card.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);

            searchBox = findViewById(R.id.toolbar_search_box);
        }

        allPeoples = new ArrayList<>();


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.keepSynced(true);

        initPeoples();

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                refreshPeoples(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void refreshPeoples(CharSequence charSequence) {
        String query = charSequence.toString().toLowerCase();
        ArrayList<Student> searchResult = new ArrayList<>();
        if(query.trim().length()!=0) {
            for (Student student : allPeoples) {
                if (student.getNickname().toLowerCase().startsWith(query)) {
                    searchResult.add(student);
                } else if (student.getName().toLowerCase().startsWith(query)) {
                    searchResult.add(student);
                }else if (student.getNickname().toLowerCase().contains(query)) {
                    searchResult.add(student);
                } else if (student.getName().toLowerCase().contains(query)) {
                    searchResult.add(student);
                } else if (student.getPhone().toLowerCase().startsWith(query)) {
                    searchResult.add(student);
                } else if (student.getRegistrationNo().toLowerCase().startsWith(query)) {
                    searchResult.add(student);
                } else if (student.getBloodGroup().toLowerCase().startsWith(query)) {
                    searchResult.add(student);
                }
            }
        }
        Collections.reverse(searchResult);
        SearchRecyclerAdapter adapter = new SearchRecyclerAdapter(searchResult, context);
        recyclerView.setAdapter(adapter);
    }

    private void initPeoples() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Collections.reverse(allPeoples);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right_exit, R.anim.left_to_right);
    }
}
