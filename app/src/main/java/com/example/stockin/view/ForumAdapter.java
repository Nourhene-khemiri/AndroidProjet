package com.example.stockin.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stockin.model.Forum;

import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumViewHolder> {
    public interface ForumClickListener {
        void onBlogClicked(int position);
    }
    private List<Forum> forumPosts;
    private ForumAdapter.ForumClickListener clickListener;
    public ForumAdapter(List<Forum> forumPosts) {
        this.forumPosts = forumPosts;
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(com.example.stockin.R.layout.forums_single_item, parent, false);
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        Forum forumPost = forumPosts.get(position);
        holder.bind(forumPost);
    }

    @Override
    public int getItemCount() {
        return forumPosts.size();
    }
}
