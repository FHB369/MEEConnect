package com.fhb.meeconnect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import com.fhb.meeconnect.Adapters.ContributorsRecyclerAdapter;
import com.fhb.meeconnect.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Contributors extends AppCompatActivity {

    private ImageView backButton;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference myRef;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contributors);

        backButton = findViewById(R.id.contributors_back);
        recyclerView = findViewById(R.id.contributors_recycler);
        refreshLayout = findViewById(R.id.contributors_refresh);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Contributors");
        myRef.keepSynced(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        refreshContributors();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContributors();
            }
        });
    }

    private void refreshContributors() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refreshLayout.setRefreshing(true);
                ArrayList<Pair<String, String>> temp = new ArrayList<>();

                for(DataSnapshot contributor : dataSnapshot.getChildren()){
                    String name = (String) contributor.child("Name").getValue();
                    String batch = (String) contributor.child("Batch").getValue();

                    Pair<String, String> pair = new Pair<>(name, batch);
                    temp.add(pair);
                }

                ContributorsRecyclerAdapter adapter = new ContributorsRecyclerAdapter(temp);
                System.out.println("SIZE: "+temp.size());
                recyclerView.setAdapter(adapter);
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
    }
}
