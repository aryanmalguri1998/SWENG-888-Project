package com.example.sweng888;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// CommentAdapter class extends RecyclerView.Adapter and is used to bind comment data to the RecyclerView
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    // Context to access application-specific resources
    private Context context;

    // List of Comment objects to be displayed
    private List<Comment> commentList;

    // Constructor for initializing the adapter with context and comment list
    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    // Called when RecyclerView needs a new ViewHolder of the given type to represent an item
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for a single comment item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Get the comment object for the current position
        Comment comment = commentList.get(position);

        // Set the comment text in the TextView of the ViewHolder
        holder.commentText.setText(comment.getCommentText());
    }

    // Returns the total number of items in the data set held by the adapter
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    // ViewHolder class to hold references to the views for each item
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        // TextView to display the comment text
        TextView commentText;

        // Constructor for initializing the ViewHolder
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find the TextView for displaying the comment text
            commentText = itemView.findViewById(R.id.commentText);
        }
    }
}
