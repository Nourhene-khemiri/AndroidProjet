package com.example.stockin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stockin.R;
import com.example.stockin.database.AppDatabase;
import com.example.stockin.model.Forum;

public class ForumDetailActivity extends AppCompatActivity {

    private ImageView forumImageView;
    private ImageView forumUserImageView;
    private TextView forumTitleTv;
    private TextView forumDescTv;
    private TextView forumUserTv;
    private Button deleteBtn;
    private Button editBtn;
    private Forum forum;
    @SuppressLint("MissingInflatedId")
    private Button btnLike, btnDislike;
    private TextView likeCount, dislikeCount;
    private int likeCounter = 0;
    private int dislikeCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);

        // Initialisation des vues
        btnLike = findViewById(R.id.btnLike);
        btnDislike = findViewById(R.id.btnDislike);
        likeCount = findViewById(R.id.likeCount);
        dislikeCount = findViewById(R.id.dislikeCount);

        forumTitleTv = findViewById(R.id.forumT);
        forumDescTv = findViewById(R.id.forumDesc);
        forumUserTv = findViewById(R.id.forumUser);
        forumImageView = findViewById(R.id.forumImg);
        forumUserImageView = findViewById(R.id.forumUserImage);
        deleteBtn = findViewById(R.id.deleteBtn);
        editBtn = findViewById(R.id.editBtn);

        // Initialisation des compteurs
        likeCounter = 0;
        dislikeCounter = 0;

        Intent intent = getIntent();

        // Vérification de l'intent pour récupérer les données du forum
        if (intent != null && intent.hasExtra("forum")) {
            forum = (Forum) intent.getSerializableExtra("forum");
            Log.d("database", "ID: " + forum.getBid());
            Log.d("database", "Title: " + forum.getForumTitle());
            Log.d("database", "Description: " + forum.getForumDescription());

            // Affichage des données du forum
            forumTitleTv.setText(forum.getForumTitle());
            forumDescTv.setText(forum.getForumDescription());
            forumUserTv.setText(forum.getForumUserName());
            forumImageView.setImageResource(R.drawable.blog_img);
            forumUserImageView.setImageResource(R.drawable.user_img);
        }

        // Gestion des clics sur le bouton "Like"
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeCounter++;
                likeCount.setText(String.valueOf(likeCounter));
                btnLike.setEnabled(false);
                btnDislike.setEnabled(true);
            }
        });

        // Gestion des clics sur le bouton "Dislike"
        btnDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dislikeCounter++;
                dislikeCount.setText(String.valueOf(dislikeCounter));
                btnDislike.setEnabled(false);
                btnLike.setEnabled(true);
            }
        });

        // Gestion du clic sur le bouton "Delete"
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.getAppDatabase(v.getContext()).forumDao().delete(forum);
                Intent intent = new Intent(v.getContext(), ForumsActivity.class);
                v.getContext().startActivity(intent);
            }
        });

        // Gestion du clic sur le bouton "Edit"
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("database", "ID: " + forum.getBid());
                Intent intent = new Intent(v.getContext(), UpdateForumActivity.class);
                intent.putExtra("forum", forum);
                v.getContext().startActivity(intent);
            }
        });
    }
}
