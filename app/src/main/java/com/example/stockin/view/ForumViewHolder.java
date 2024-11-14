package com.example.stockin.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockin.R;
import com.example.stockin.model.Forum;

public class ForumViewHolder extends RecyclerView.ViewHolder {
    private TextView forumTitle;
    private TextView forumDescription;
    private TextView forumUserName;
    private ImageView forumImage;
    private ImageView forumUserImg;
    private LinearLayout forumDetail;
    //private BlogClickListener clickListener;
    private Button detailsBtn;
    private Forum forum;
    public ForumViewHolder(@NonNull View itemView) {
        super(itemView);

        forumTitle = itemView.findViewById(R.id.forumTitle);
        forumDescription = itemView.findViewById(R.id.forumDecription);
        forumUserName = itemView.findViewById(R.id.forumUserName);
        forumImage = itemView.findViewById(R.id.forumImage);
        forumUserImg = itemView.findViewById(R.id.forumUserImg);
        forumDetail = itemView.findViewById(R.id.forumDetails);


        forumDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(itemView.getContext(), ForumDetailActivity.class);
                intent.putExtra("forum",forum);

                itemView.getContext().startActivity(intent);
            }
        });


    }

    public void bind(Forum forum) {
        this.forum = forum;
        forumTitle.setText(forum.getForumTitle());
        forumDescription.setText(forum.getForumDescription());
            forumUserName.setText(forum.getForumUserName());
        forumImage.setImageResource(R.drawable.blog_img);
        forumUserImg.setImageResource(R.drawable.blog_img);
    }


}