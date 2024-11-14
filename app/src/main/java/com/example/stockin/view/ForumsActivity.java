package com.example.stockin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockin.R;
import com.example.stockin.database.AppDatabase;
import com.example.stockin.model.Forum;

import java.util.ArrayList;
import java.util.List;

public class ForumsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ForumAdapter forumAdapter;
    private Button forumFrom;
    private AppDatabase database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);
        List<Forum> forumPosts = new ArrayList<>();

        // Ajoutez d'autres articles
        database = AppDatabase.getAppDatabase(this);
        forumPosts = database.forumDao().getAll();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        forumAdapter = new ForumAdapter(forumPosts);
        recyclerView.setAdapter(forumAdapter);
        forumFrom=findViewById(R.id.forumForm);
        forumFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ForumFormActivity.class);

                v.getContext().startActivity(intent);
            }
        });



    }


}