package com.example.sweng888;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

// ViewHolder class to hold references to each view in the item layout
public class MyViewHolder extends RecyclerView.ViewHolder {

    // Declaring UI components
    ImageView recImage, likeIcon, commentIcon;
    TextView recTitle, recDesc, recLang, likeCount, commentCount;
    CardView recCard;

    // Constructor to initialize the UI components
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initializing ImageView for the main image in the card
        recImage = itemView.findViewById(R.id.recImage);

        // Initializing CardView to handle the card layout
        recCard = itemView.findViewById(R.id.recCard);

        // Initializing TextView for the description
        recDesc = itemView.findViewById(R.id.recDesc);

        // Initializing TextView for the language or priority
        recLang = itemView.findViewById(R.id.recPriority);

        // Initializing TextView for the title
        recTitle = itemView.findViewById(R.id.recTitle);

        // Initializing ImageView for the like icon
        likeIcon = itemView.findViewById(R.id.likeIcon);

        // Initializing ImageView for the comment icon
        commentIcon = itemView.findViewById(R.id.commentIcon);

        // Initializing TextView for displaying the count of likes
        likeCount = itemView.findViewById(R.id.likeCount);

        // Initializing TextView for displaying the count of comments
        commentCount = itemView.findViewById(R.id.commentCount);
    }
}
