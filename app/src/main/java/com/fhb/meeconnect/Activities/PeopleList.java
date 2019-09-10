package com.fhb.meeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fhb.meeconnect.Adapters.PeopleRecyclerAdapter;
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

public class PeopleList extends AppCompatActivity {

    private TextView title;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private String catagoryName;
    public static ArrayList<Student> students;
    public static ArrayList<Faculty> faculties;
    public ArrayList<Catagory> catagories;
    private int catagoryIndex;
    private DatabaseReference myRef;
    private Context context;
    public int currentVisiblePosition;
    private LinearLayoutManager layoutManager;

    public static int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        listPosition = 1;

        students = null;
        faculties = null;
        context = this;

        title = findViewById(R.id.people_title);
        backButton = findViewById(R.id.people_back);
        recyclerView = findViewById(R.id.people_recycler);
        refreshLayout = findViewById(R.id.people_refresh);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        catagoryName = getIntent().getStringExtra("Catagory Name");
        catagoryIndex = getIntent().getIntExtra("Catagory Index",0);

        this.catagories = MainActivity.catagories;

//        students = this.catagories.get(catagoryIndex).getStudents();
//        faculties = this.catagories.get(catagoryIndex).getFaculties();
//
//        PeopleRecyclerAdapter adapter = new PeopleRecyclerAdapter(catagoryName, students, faculties, this);
//        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(catagoryName).child("peoples");
        myRef.keepSynced(true);

        refreshPeoples();

        if(catagoryName.equals("Teacher") || catagoryName.equals("Staff")) {
            title.setText(catagoryName+"s");
        }else{
            title.setText(catagoryName);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPeoples();
            }
        });
    }

    private void refreshPeoples() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refreshLayout.setRefreshing(true);
                if(catagoryName.equals("Teacher") || catagoryName.equals("Staff")){
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
                    PeopleRecyclerAdapter adapter = new PeopleRecyclerAdapter(catagoryIndex, catagoryName, null, tempFaculty, context);
                    recyclerView.setAdapter(adapter);
                }else{
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
                    PeopleRecyclerAdapter adapter = new PeopleRecyclerAdapter(catagoryIndex, catagoryName, tempStudent, null, context);
                    recyclerView.setAdapter(adapter);
                }
                refreshLayout.setRefreshing(false);
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
        currentVisiblePosition= 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentVisiblePosition = 0; // this variable should be static in class
        currentVisiblePosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        refreshPeoples();
        ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPosition(currentVisiblePosition);
        currentVisiblePosition = 0;
    }
}
