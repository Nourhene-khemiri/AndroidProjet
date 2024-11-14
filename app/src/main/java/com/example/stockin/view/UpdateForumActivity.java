package com.example.stockin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockin.R;
import com.example.stockin.database.AppDatabase;
import com.example.stockin.model.Forum;

public class UpdateForumActivity extends AppCompatActivity {
    private AppDatabase database;
    private EditText etTitle;
    private EditText etDesc;
    private EditText etUsername;
    private Button saveBtn;
    private Forum forum;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_forum);
        etTitle = findViewById(R.id.etForumTitle);
        etDesc = findViewById(R.id.etForumDesc);
        etUsername = findViewById(R.id.etUserName);
        saveBtn = findViewById(R.id.btnSaveForum);

        database = AppDatabase.getAppDatabase(this); // Initialize the database instance
        Intent intent = getIntent();
        forum = (Forum) intent.getSerializableExtra("forum");
        Log.d("database", "ID: " + forum.getBid());
        if(intent != null) {
            if(intent.hasExtra("forum")) {
                forum = (Forum) intent.getSerializableExtra("forum");
                Log.d("database", "ID: " + forum.getBid());
                Log.d("database", "Title: " + forum.getForumTitle());
                Log.d("database", "Description: " + forum.getForumDescription());

                etTitle.setText(forum.getForumTitle());
                etDesc.setText(forum.getForumDescription());
                etUsername.setText(forum.getForumUserName());

            }
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forum.setForumTitle(etTitle.getText().toString());
                forum.setForumDescription(etDesc.getText().toString());
                forum.setForumUserName(etUsername.getText().toString());
                forum.setForumImage("forum_img");
                forum.setForumUserImg("user_img");
                if (database != null) {
                    database.forumDao().updateOne(forum);
                    Log.d("database", "ID:update " );
                    Log.d("database", "ID: " + forum.getBid());
                    Log.d("database", "Title: " + forum.getForumTitle());
                    Log.d("database", "Description: " + forum.getForumDescription());
                    Intent intent = new Intent(v.getContext(), ForumsActivity.class);
                    v.getContext().startActivity(intent);
                } else {
                    Log.e("database", "Database is null");
                }
            }
        });
    }
}