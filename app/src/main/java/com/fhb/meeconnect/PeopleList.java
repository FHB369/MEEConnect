package com.fhb.meeconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PeopleList extends AppCompatActivity {

    private TextView title;
    private ImageView backButton;
    private RecyclerView recyclerView;
    private String catagoryName;
    public static ArrayList<Student> students;
    public static ArrayList<Faculty> faculties;
    private int catagoryIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);

        students = null;
        faculties = null;

        title = findViewById(R.id.people_title);
        backButton = findViewById(R.id.people_back);
        recyclerView = findViewById(R.id.people_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        catagoryName = getIntent().getStringExtra("Catagory Name");
        catagoryIndex = getIntent().getIntExtra("Catagory Index",0);

        students = MainActivity.catagories.get(catagoryIndex).getStudents();
        faculties = MainActivity.catagories.get(catagoryIndex).getFaculties();

        PeopleRecyclerAdapter adapter = new PeopleRecyclerAdapter(catagoryName, students, faculties, this);
        recyclerView.setAdapter(adapter);

        title.setText(catagoryName);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
